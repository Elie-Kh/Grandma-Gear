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
    public static final String TAG = "_FirebaseHelper";
    public static final String userDB = "userDB";
    public static final String deviceDB = "deviceDB";
    public static final String eventDB = "eventDB";
    public static FirebaseAuth firebaseAuth;
    public static FirebaseFirestore firebaseFirestore;

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
        user.put(FirebaseObjects.Account_Type, newUser.accountType);
        user.put(FirebaseObjects.Age, newUser.age);
        user.put(FirebaseObjects.Height, newUser.height);
        user.put(FirebaseObjects.Weight, newUser.weight);
        user.put(FirebaseObjects.GPS_Follow, false);
        user.put(FirebaseObjects.Devices_Followed, newUser.devicesFollowed);
        user.put(FirebaseObjects.Events, newUser.events);
        user.put(FirebaseObjects.Notifications, newUser.notifications);


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
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(firebaseAuth.getCurrentUser().getUid());

        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                if(newVar instanceof ArrayList<?>){
                    //Map<String, String> map = new HashMap<>();
//                    for(int i = 0; i < ((ArrayList) newVar).size(); i++){
//                        map.put(String.valueOf(i), ((ArrayList) newVar).get(i).toString());
//                    }
                    userDBO.setDevicesFollowed((ArrayList<String>) newVar);
                    transaction.update(documentReference, field, userDBO.getDevicesFollowed());
                }
                else {
                    transaction.update(documentReference, field, newVar);
                }

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
    public interface Callback_DeviceFollowed {
        void onCallback(boolean checker);
    }

    public void addDeviceFollowed(final FirebaseObjects.UserDBO userDBO, final String deviceID){

        firebaseFirestore.collection(deviceDB).whereEqualTo(FirebaseObjects.Username, userDBO.username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<String> devices = new ArrayList<String>();
                    devices = userDBO.getDevicesFollowed();
                    devices.add(deviceID);
                    userDBO.setDevicesFollowed(devices);
                    editUser(userDBO, FirebaseObjects.Devices_Followed, devices);
                }
            }
        });
    }

    public void addNotification(final FirebaseObjects.UserDBO userDBO,
                                final FirebaseObjects.Notifications notification){
       firebaseFirestore.collection(userDB).whereEqualTo(FirebaseObjects.Username, firebaseAuth.getCurrentUser().getUid())
               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   ArrayList<FirebaseObjects.Notifications> notifications = new ArrayList<FirebaseObjects.Notifications>();
                   notifications = userDBO.getNotifications();
                   notifications.add(notification);
                   userDBO.setNotifications(notifications);
                   editUser(userDBO, FirebaseObjects.Notifications, notifications);
               }
           }
       });
    }

    public void addDevice(final FirebaseObjects.DevicesDBO device){

        //device.deviceID = firebaseAuth.getUid();
        final DocumentReference documentReference = firebaseFirestore.collection(deviceDB).document(device.id);
        Map<String,Object> devices = new HashMap<>();
        devices.put(FirebaseObjects.ID, firebaseAuth.getUid());
        devices.put(FirebaseObjects.Longitude, device.longitude);
        devices.put(FirebaseObjects.Latitude, device.latitude);
        devices.put(FirebaseObjects.Heartrate, device.heartrate);
        devices.put(FirebaseObjects.PhoneBattery, device.phoneBattery);
        devices.put(FirebaseObjects.DeviceBattery, device.deviceBattery);
        devices.put(FirebaseObjects.Notifications, device.notifications);

        documentReference.set(devices).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Device profile is created for " + device.id);
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
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(device.id);

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
        final DocumentReference documentReference = firebaseFirestore.collection(userDB).document(device.id);

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

    public interface Callback_AddPatient {
        void onCallback(FirebaseObjects.UserDBO user);
    }

    public interface Callback_Type {
        void onCallback(boolean checker);
    }

    public void addingPatient(final Callback_AddPatient callback,final FirebaseObjects.UserDBO user,
                           final String patientDevice){
        firebaseFirestore
                .collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID,patientDevice)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //firebaseHelper.addDevice(device);
                            Log.d("__ADDPATIENT__", user.email);
                            addDeviceFollowed(user, patientDevice);
                            callback.onCallback(user);
                        }else{

                        }
                    }
                });
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
                        if((Boolean) document.get(FirebaseObjects.Account_Type)){
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


    public interface Callback_Device {
        void onCallback(FirebaseObjects.DevicesDBO device);
    }

    public void getDevice(final Callback_Device callback, String deviceID) {
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
//                                Log.d("__GettingType", (String) Objects.requireNonNull(document.get("deviceID")));

                                returnable[0] = new FirebaseObjects.DevicesDBO(
                                        (String)document.get(FirebaseObjects.ID),
                                        (Integer) Math.round((Long) document.get(FirebaseObjects.Heartrate)),
                                        (Integer) Math.round((Long) document.get(FirebaseObjects.PhoneBattery)),
                                        (Integer) Math.round((Long) document.get(FirebaseObjects.DeviceBattery)));
                                callback.onCallback(returnable[0]);
                                // callback_notifications.onCallback(notifications);
                            }
                        }
                    }
                });
    }

    public interface Callback_Notifications {
        void onCallback(ArrayList<HashMap<String, Object>> notifications);
    }


    public void getNotifications_follower(final Callback_Notifications callback_notifications){
        //get notifications of all devices followed by the follower.
        Log.d(TAG, "inside get notif follow" + getCurrentUserID());
        firebaseFirestore.collection(userDB).whereEqualTo(FirebaseObjects.Username, getCurrentUserID())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        ArrayList<HashMap<String, Object>> notifications =
                                (ArrayList<HashMap<String, Object>>) documentSnapshot.get(FirebaseObjects.Notifications);
                        callback_notifications.onCallback(notifications);
                    }
                }
            }
        });
    }

    public interface Callback_getUser{
        void onCallback(FirebaseObjects.UserDBO user);
    }

    public void getUser(final Callback_getUser callback, String email, final boolean type){
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
                                            (Boolean) document.get(FirebaseObjects.Account_Type),
                                            (Boolean) document.get(FirebaseObjects.GPS_Follow));
                                } else {
                                    Log.d("__TESTERS__", (String) Objects.requireNonNull(document.get(FirebaseObjects.Email)));
                                    returnable[0] = new FirebaseObjects.UserDBO(
                                            (String) document.get(FirebaseObjects.Email),
                                            (String) document.get(FirebaseObjects.First_Name),
                                            (String) document.get(FirebaseObjects.Last_Name),
                                            (String) document.get(FirebaseObjects.Password),
                                            (Boolean) document.get(FirebaseObjects.Account_Type),
                                            (Boolean) document.get(FirebaseObjects.GPS_Follow),
                                            (Integer) Math.round((Long) document.get(FirebaseObjects.Age)),
                                            (Integer) Math.round((Long) document.get(FirebaseObjects.Weight)),
                                            (Integer) Math.round((Long) document.get(FirebaseObjects.Height))
                                            );
                                }
                                callback.onCallback(returnable[0]);
                            }
                        }
                    }
                });
    }

    public interface Callback_getUserFollowers {
        void onCallback(ArrayList<String> followers);
    }


    public void getUser_followers(final Callback_getUserFollowers callback_getUserFollowers,
                                          final ArrayList<String> followers,
                                          final FirebaseObjects.UserDBO user){
        //get notifications of all devices followed by the follower.
        firebaseFirestore.collection(userDB).whereEqualTo(FirebaseObjects.Email, user.email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                ArrayList<String> vals = new ArrayList<String>
                                        ((ArrayList<String>)document.get(FirebaseObjects.Devices_Followed));
                                callback_getUserFollowers.onCallback(vals);
                            }
                        }
                    }

                });

    }

    public String getCurrentUserID(){
        return firebaseAuth.getCurrentUser().getUid();
    }

}
