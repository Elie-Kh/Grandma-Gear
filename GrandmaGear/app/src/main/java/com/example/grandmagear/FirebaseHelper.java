package com.example.grandmagear;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseHelper {
    private static final String userDB = "userDB";
    private static final String deviceDB = "deviceDB";
    private static final String eventDB = "eventDB";
    protected static FirebaseAuth firebaseAuth;
    protected static FirebaseFirestore firebaseFirestore;

    public FirebaseHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void AddUser(final FirebaseObjects.UserDBO newUser) {
        newUser.username = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection(userDB).document(newUser.username);
        Map<String,Object> user = new HashMap<>();
        user.put("Username", newUser.username);
        user.put("Email", newUser.email);
        user.put("First Name", newUser.firstName);
        user.put("Last Name", newUser.lastName);
        user.put("Password", newUser.password);
        user.put("Account Type", newUser.acc_type);
        user.put("Age", newUser.age);
        user.put("Height", newUser.height);
        user.put("Weight", newUser.weight);
        user.put("Devices Followed", newUser.device_ids);
        user.put("Events", newUser.events);


        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: user Profile is created for " + newUser.username);
            }
        });
        documentReference.set(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void editUser(final FirebaseObjects.UserDBO userDBO, final String field, final Object newVar){
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(userDBO.username);

        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(documentReference, field, newVar);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Edit success!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Edit failure.", e);
            }
        });
    }

    public void addDeviceFollowed(final FirebaseObjects.UserDBO userDBO, String deviceID){
        ArrayList<String> devices = userDBO.getDevice_ids();
        devices.add(deviceID);
    }

    public void addNotification(final FirebaseObjects.UserDBO userDBO,
                                FirebaseObjects.Notifications notifications){
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(userDBO.username);
        ArrayList<FirebaseObjects.EventsDBO> events = userDBO.getEvents();
        events.add(new FirebaseObjects.EventsDBO(events.size()+1, notifications));
    }

    public void addDevice(final FirebaseObjects.DevicesDBO device){

        device.deviceID = firebaseAuth.getUid();
        final DocumentReference documentReference = firebaseFirestore.collection(deviceDB).document(device.deviceID);
        Map<String,Object> devices = new HashMap<>();
        devices.put(FirebaseObjects.ID, device.deviceID);
        devices.put(FirebaseObjects.Longitude, device.lonGPS);
        devices.put(FirebaseObjects.Latitude, device.latGPS);
        devices.put(FirebaseObjects.Heartrate, device.bpm);
        devices.put(FirebaseObjects.PhoneBattery, device.phoneBattery);
        devices.put(FirebaseObjects.DeviceBattery, device.deviceBattery);
        devices.put(FirebaseObjects.Notifications, device.notifications);
        documentReference.set(devices).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Device profile is created for " + device.deviceID);
            }
        });
        documentReference.set(devices).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void editDevice(FirebaseObjects.DevicesDBO device, final String field, final Object newVal){
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(device.deviceID);

        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(documentReference, field, newVal);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Edit success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Edit failure.", e);
                    }
                });
    }

    public void editLocation(FirebaseObjects.DevicesDBO device, final Object newLongitude,
                             final Object newLatitude){
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(device.deviceID);

        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(documentReference, FirebaseObjects.Longitude , newLongitude);
                transaction.update(documentReference, FirebaseObjects.Latitude , newLatitude);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Edit success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Edit failure.", e);
                    }
                });
    }

    public interface Callback {
        void onCallback(boolean checker);
    }

    public void getType(final Callback callback, final String email){
        Log.d("__GettingType", email);
        firebaseFirestore.collection("userDB")
            .whereEqualTo("Email",email)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d("__GettingType", "Success");
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Log.d("__GettingType", (String) Objects.requireNonNull(document.get("Email")));
                            if(((String) document.get("Email")).equals(email)){
                                if((Boolean) document.get("Account Type")){
                                    ///true
                                    callback.onCallback(true);
                                    break;
                                }
                                else {
                                    //false
                                    callback.onCallback(false);
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        //false
                        callback.onCallback(false);
                    }

                }
            });

    }

}
