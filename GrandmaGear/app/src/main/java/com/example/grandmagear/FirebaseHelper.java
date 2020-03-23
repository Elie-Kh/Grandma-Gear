package com.example.grandmagear;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/** Functions implemented in this helper class:
 * 1- Add User -- Adds user to Firestore DB
 * 2- Edit User -- Edit a field of a user in Firestore DB
 * 3- Add Device Followed -- Adds a new device to the array of devices in Firestore TODO: FINISH THIS
 * 4- Add Notification -- Adds notification TODO: FINISH THIS
 * 5- Add Device -- adds device to Firestore DB
 * 6- Edit Device -- Edit a field of a device in Firestore DB
 * 7- Edit Location -- Edit the longitude and latitude of a device in Firestore DB
 * 8- get Type -- Gets the type of a user (Patient or Client)
 * 9- Get Device -- Gets device information from Firestore DB
 * 10- Get Notifications Follower -- Gets notifications of all followed devices
 * 11- Get User -- Gets user info from the Firestore DB
 * 12- ??
 * **/

public class FirebaseHelper {
    protected static final String userDB = "userDB";
    protected static final String deviceDB = "deviceDB";
    protected static final String eventDB = "eventDB";
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
        user.put(FirebaseObjects.Username, newUser.username);
        user.put(FirebaseObjects.Email, newUser.email);
        user.put(FirebaseObjects.First_Name, newUser.firstName);
        user.put(FirebaseObjects.Last_Name, newUser.lastName);
        user.put(FirebaseObjects.Password, newUser.password);
        user.put(FirebaseObjects.Account_Type, newUser.acc_type);
        user.put(FirebaseObjects.Age, newUser.age);
        user.put(FirebaseObjects.Height, newUser.height);
        user.put(FirebaseObjects.Weight, newUser.weight);
        user.put(FirebaseObjects.GPS_Follow, false);
        user.put(FirebaseObjects.Devices_Followed, newUser.device_ids);
        user.put(FirebaseObjects.Events, newUser.events);


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
        firebaseFirestore.collection(deviceDB).whereEqualTo(FirebaseObjects.Username, userDBO.username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });
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

    public interface Callback_Type {
        void onCallback(boolean checker);
    }

    public void getType(final Callback_Type callback, final String email){
        Log.d("__GettingType", email);
        firebaseFirestore.collection(userDB)
        .whereEqualTo("Email",email)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("__GettingType", "Success");
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("__GettingType", (String) Objects.requireNonNull(document.get("Email")));
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
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    //false
                    callback.onCallback(false);
                }

            }
        });
    }

    public interface Callback_Notifications {
        void onCallback(ArrayList<FirebaseObjects.Notifications> notifications);
    }

    public interface Callback_Device {
        void onCallback(String device);
    }

    public FirebaseObjects.DevicesDBO getDevice(String deviceID) {
        final FirebaseObjects.DevicesDBO[] returnable = {null};
        firebaseFirestore
                .collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID, deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("__GettingType", (String) Objects.requireNonNull(document.get("deviceID")));

                                returnable[0] = new FirebaseObjects.DevicesDBO(
                                        (String)document.get(FirebaseObjects.ID),
                                        (Integer)document.get(FirebaseObjects.Heartrate),
                                        (Integer)document.get(FirebaseObjects.PhoneBattery),
                                        (Integer)document.get(FirebaseObjects.DeviceBattery));
                                // callback_notifications.onCallback(notifications);
                            }
                        }
                    }
                });
        return returnable[0];
    }

    public void getNotifications_follower(final Callback_Notifications callback_notifications,
                                          final ArrayList<FirebaseObjects.Notifications> notifications,
                                          final FirebaseObjects.DevicesDBO device){
        //get notifications of all devices followed by the follower.
        firebaseFirestore.collection(deviceDB).whereEqualTo(FirebaseObjects.ID, device.deviceID)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("__GettingType", (String) Objects.requireNonNull(document.get("Email")));

                          Object valsObj = document.getData();
                          String vals = new Gson().toJson(valsObj);
                        try {
                            JSONObject infoObj=new JSONObject(vals).getJSONObject(FirebaseObjects.Notifications);
                            Iterator<String> iterator = infoObj.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                JSONObject objArray=infoObj.getJSONObject(key);
                                FirebaseObjects.Notifications notificationTemp
                                        = new FirebaseObjects.Notifications(
                                                objArray.getString("notificationsType"),
                                                objArray.getString("notificationsInfo"),
                                                objArray.getInt("time"));
                                notifications.add(notificationTemp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback_notifications.onCallback(notifications);
                        }
                    }
                }

        });

    }

    public FirebaseObjects.UserDBO getUser(String email, final boolean type){
        final FirebaseObjects.UserDBO[] returnable = {null};
        firebaseFirestore
                .collection(FirebaseHelper.userDB)
                .whereEqualTo(FirebaseObjects.Email, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("__GettingType", (String) Objects.requireNonNull(document.get("email")));
                                if(type) {
                                    returnable[0] = new FirebaseObjects.UserDBO(
                                            (String) document.get(FirebaseObjects.Email),
                                            (String) document.get(FirebaseObjects.First_Name),
                                            (String) document.get(FirebaseObjects.Last_Name),
                                            (String) document.get(FirebaseObjects.Password),
                                            (Boolean) document.get(FirebaseObjects.Account_Type));
                                } else {
                                    Log.d("__TESTERS__", (String) Objects.requireNonNull(document.get(FirebaseObjects.Email)));
                                    returnable[0] = new FirebaseObjects.UserDBO(
                                            (String) document.get(FirebaseObjects.Email),
                                            (String) document.get(FirebaseObjects.First_Name),
                                            (String) document.get(FirebaseObjects.Last_Name),
                                            (String) document.get(FirebaseObjects.Password),
                                            (Boolean) document.get(FirebaseObjects.Account_Type)
                                            //(Integer) document.get(FirebaseObjects.Age),
                                            //(Integer) document.get(FirebaseObjects.Weight),
                                            //(Integer) document.get(FirebaseObjects.Height)
                                            );
                                }
                                // callback_notifications.onCallback(notifications);
                            }
                        }
                    }
                });
        return returnable[0];
    }

}
