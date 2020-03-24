package com.example.grandmagear;

import android.bluetooth.BluetoothClass;

import java.util.ArrayList;

public class FirebaseObjects {

    //variable names for user
    public static final String Username = "Username";
    public static final String Email = "Email";
    public static final String First_Name = "First Name";
    public static final String Last_Name = "Last Name";
    public static final String Password = "Password";
    public static final String Account_Type = "Account Type";
    public static final String Age = "Age";
    public static final String Height = "Height";
    public static final String Weight = "Weight";
    public static final String GPS_Follow = "GPS Follow";
    public static final String Devices_Followed = "Devices Followed";
    public static final String Events = "Events";

    //variable names for device
    public static final String ID = "ID";
    public static final String Longitude = "Longitude";
    public static final String Latitude = "Latitude";
    public static final String Heartrate = "Heartrate";
    public static final String Notifications = "Notifications";
    public static final String DeviceBattery = "Device Battery";
    public static final String PhoneBattery = "Phone Battery";

    public static class UserDBO{
        protected String username;
        protected String email;
        protected String firstName;
        protected String lastName;
        protected String password;
        protected Boolean acc_type;
        protected Boolean gps_follow = false;
        protected int age = 0;
        protected int weight = 0;
        protected int height = 0;
        protected ArrayList<String> device_ids;
        protected ArrayList<EventsDBO> events;

        /*the following constructor is strictly for testing purposes*/

        public UserDBO(String email, String firstName, String lastName) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public UserDBO(/*String username,*/ String email, String firstName, String lastName, String password, Boolean acc_type) {
            //this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.acc_type = acc_type;
            this.device_ids = new ArrayList<String>();
            this.events = new ArrayList<EventsDBO>();
        }

        public UserDBO(/*String username,*/ String email, String firstName, String lastName, String password, Boolean acc_type, int age, int weight, int height) {
            //this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.acc_type = acc_type;
            this.age = age;
            this.weight = weight;
            this.height = height;
            this.device_ids = new ArrayList<String>();
            this.events = new ArrayList<EventsDBO>();
        }

        public Boolean getGps_follow() {
            return gps_follow;
        }

        public void setGps_follow(Boolean gps_follow) {
            this.gps_follow = gps_follow;
        }

        public void setDevice_ids(ArrayList<String> device_ids) {
            this.device_ids = device_ids;
        }

        public void setEvents(ArrayList<EventsDBO> events) {
            this.events = events;
        }

        public ArrayList<String> getDevice_ids() {
            return device_ids;
        }

        public ArrayList<EventsDBO> getEvents() {
            return events;
        }
    }

    public static class DevicesDBO{
        protected String deviceID;
        protected int bpm;
        protected long latGPS;    //latitude
        protected long lonGPS;    //longitude
        protected int deviceBattery;
        protected int phoneBattery;
        protected ArrayList<Notifications> notifications; //notifications linked to that device only

        public DevicesDBO(String deviceID) {
            this.deviceID = deviceID;
        }

        public DevicesDBO(String deviceID, int bpm, int deviceBattery, int phoneBattery) {
            this.deviceID = deviceID;
            this.bpm = bpm;
            this.deviceBattery = deviceBattery;
            this.phoneBattery = phoneBattery;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public int getBpm() {
            return bpm;
        }

        public long getLatGPS() {
            return latGPS;
        }

        public long getLonGPS() {
            return lonGPS;
        }

        public int getDeviceBattery() {
            return deviceBattery;
        }

        public int getPhoneBattery() {
            return phoneBattery;
        }

        public ArrayList<FirebaseObjects.Notifications> getNotifications() {
            return notifications;
        }
    }

    public static class EventsDBO{
        protected int eventNum;
        protected ArrayList<Notifications> notification; //all notifications of followed devices
        //this list will be displayed in the follower notifications list, descending order.


        public EventsDBO(int eventNum, Notifications notifications) {
            this.eventNum = eventNum;
            this.notification.add(notifications);
        }

        public int getEventNum() {
            return eventNum;
        }
        public void setNotification(ArrayList<Notifications> notification) {
            this.notification = notification;
        }
    }

    public static class Notifications{

        protected String notificationType;
        protected String notificationInfo;
        protected int time;

        public Notifications(String notificationType, String notificationInfo, int time) {
            this.notificationType = notificationType;
            this.notificationInfo = notificationInfo;
            this.time = time;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public String getNotificationInfo() {
            return notificationInfo;
        }
    }
}
