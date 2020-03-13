package com.example.grandmagear;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        user.put("Name", newUser.name);
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
        devices.put("deviceID", device.deviceID);
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
}
