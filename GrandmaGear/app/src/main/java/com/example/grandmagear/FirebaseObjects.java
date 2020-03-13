package com.example.grandmagear;

import android.bluetooth.BluetoothClass;

import java.util.ArrayList;

public class FirebaseObjects {
    public class UserDBO{
        protected String username;
        protected String email;
        protected String name;
        protected String password;
        protected Boolean acc_type;
        protected int age;
        protected short weight;
        protected short height;
        protected ArrayList<DevicesDBO> device_ids;
        protected ArrayList<EventsDBO> events;

    }

    public class DevicesDBO{
        protected int bpm;
        protected double latGPS;    //latitude
        protected double lonGPS;    //longitude
        protected int deviceBattery;
        protected int phoneBattery;
        protected ArrayList<Notifications> notifications; //notifications linked to that device only

    }

    public class EventsDBO{
        protected int eventNum;
        protected ArrayList<Notifications> notification; //all notifications of followed devices
        //this list will be displayed in the follower notifications list, descending order.
    }

    public class Notifications{
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
