package com.example.grandmagear;

import android.bluetooth.BluetoothClass;

import java.util.ArrayList;

public class FirebaseObjects {
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
        protected String name;
        protected String password;
        protected Boolean acc_type;
        protected int age = 0;
        protected int weight = 0;
        protected int height = 0;
        protected ArrayList<String> device_ids;
        protected ArrayList<EventsDBO> events;

        /*the following constructor is strictly for testing purposes*/

        public UserDBO(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public UserDBO(String username, String email, String name, String password, Boolean acc_type) {
            this.username = username;
            this.email = email;
            this.name = name;
            this.password = password;
            this.acc_type = acc_type;
        }

        public UserDBO(String username, String email, String name, String password, Boolean acc_type, int age, short weight, short height) {
            this.username = username;
            this.email = email;
            this.name = name;
            this.password = password;
            this.acc_type = acc_type;
            this.age = age;
            this.weight = weight;
            this.height = height;
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

        public String getNotificationType() {
            return notificationType;
        }

        public String getNotificationInfo() {
            return notificationInfo;
        }
    }
}
