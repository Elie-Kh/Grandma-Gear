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
import java.util.Collection;
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
    protected static final String TAG = "_FirebaseHelper";
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
                    userDBO.setDevice_ids((ArrayList<String>) newVar);
                    transaction.update(documentReference, field, userDBO.getDevice_ids());
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
                    devices = userDBO.getDevice_ids();
                    devices.add(deviceID);
                    userDBO.setDevice_ids(devices);
                    editUser(userDBO, FirebaseObjects.Devices_Followed, devices);
                }
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

        //device.deviceID = firebaseAuth.getUid();
        final DocumentReference documentReference = firebaseFirestore.collection(deviceDB).document(device.deviceID);
        Map<String,Object> devices = new HashMap<>();
        devices.put(FirebaseObjects.ID, firebaseAuth.getUid());
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
        void onCallback(ArrayList<FirebaseObjects.Notifications> notifications);
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
                                //Log.d("__GettingType", (String) Objects.requireNonNull(document.get("Email")));

//                                Object valsObj = document.getData();
//                                String vals = new Gson().toJson(valsObj);
//                                try {
//                                    JSONObject infoObj=new JSONObject(vals).getJSONObject(FirebaseObjects.Devices_Followed);
//                                    Iterator<String> iterator = infoObj.keys();
//                                    while (iterator.hasNext()) {
//                                        String key = iterator.next();
//                                        JSONObject objArray=infoObj.getJSONObject(key);
//                                        String temp = objArray.getString(FirebaseObjects.Devices_Followed);
//                                        followers.add(temp);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                HashMap<String, String> temp =
//                                        (HashMap<String, String>) document.get(FirebaseObjects.Devices_Followed);
//                                Collection<String> values = temp.values();

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
