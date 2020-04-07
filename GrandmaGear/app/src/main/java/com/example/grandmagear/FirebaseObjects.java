package com.example.grandmagear;

import java.util.ArrayList;

public class FirebaseObjects {

    //variable names for user
    public static final String Username = "username";
    public static final String Email = "email";
    public static final String First_Name = "firstName";
    public static final String Last_Name = "lastName";
    public static final String Password = "password";
    public static final String Account_Type = "accountType";
    public static final String Age = "age";
    public static final String Height = "height";
    public static final String Weight = "weight";
    public static final String GPS_Follow = "gpsFollow";
    public static final String Devices_Followed = "devicesFollowed";
    public static final String Events = "events";

    //variable names for device
    public static final String ID = "id";
    public static final String Longitude = "longitude";
    public static final String Latitude = "latitude";
    public static final String Heartrate = "heartrate";
    public static final String Notifications = "notifications";
    public static final String DeviceBattery = "deviceBattery";
    public static final String PhoneBattery = "phoneBattery";

    public static class UserDBO{
        //@PropertyName("Username")
        public String username;
        //@PropertyName("Email")
        public String email;
        //@PropertyName("First Name")
        public String firstName;
        //@PropertyName("Last Name")
        public String lastName;
        //@PropertyName("Password")
        public String password;
        //@PropertyName("Account Type")
        public Boolean accountType;
        //@PropertyName("GPS Follow")
        public Boolean gpsFollow = false;
        //@PropertyName("Age")
        public Integer age = 0;
        //@PropertyName("Weight")
        public Integer weight = 0;
        //@PropertyName("Height")
        public Integer height = 0;
        //@PropertyName("Devices Followed")
        public ArrayList<String> devicesFollowed;
        //@PropertyName("Events")
        public ArrayList<EventsDBO> events;

        /*the following constructor is strictly for testing purposes*/

        public UserDBO(String email, String firstName, String lastName) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        //@PropertyName("Email")
        public String getEmail() {
            return email;
        }

        public UserDBO() {
            //empty constructor for snapshot.
        }

        public UserDBO(/*String username,*/ String email, String firstName, String lastName, String password, Boolean AccountType, Boolean gpsFollow) {
            //this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.accountType = AccountType;
            this.gpsFollow = gpsFollow;
            this.devicesFollowed = new ArrayList<String>();
            this.events = new ArrayList<EventsDBO>();
        }

        public UserDBO(/*String username,*/ String email, String firstName, String lastName, String password, Boolean AccountType, Boolean gpsFollow, int age, int weight, int height) {
            //this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.accountType = AccountType;
            this.gpsFollow = gpsFollow;
            this.age = age;
            this.weight = weight;
            this.height = height;
            this.devicesFollowed = new ArrayList<String>();
            this.events = new ArrayList<EventsDBO>();
        }

        public UserDBO(String username, String email, String firstName, String lastName, String password, Boolean AccountType, Boolean gpsFollow, int age, int weight, int height, ArrayList<String> DevicesFollowed, ArrayList<EventsDBO> events) {
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.accountType = AccountType;
            this.gpsFollow = gpsFollow;
            this.age = age;
            this.weight = weight;
            this.height = height;
            this.devicesFollowed = DevicesFollowed;
            this.events = events;
        }

        //@PropertyName("First Name")
        public String getFirstName() {
            return firstName;
        }

        //@PropertyName("Last Name")
        public String getLastName() {
            return lastName;
        }

        //@PropertyName("Account Type")
        public Boolean getAccountType() {
            return accountType;
        }

        //@PropertyName("Age")
        public int getAge() {
            return age;
        }

        //@PropertyName("Weight")
        public int getWeight() {
            return weight;
        }

        //@PropertyName("Height")
        public int getHeight() {
            return height;
        }

        //@PropertyName("Username")
        public String getUsername() {
            return username;
        }
        //@PropertyName("Username")
        public void setUsername(String username) {
            this.username = username;
        }
        //@PropertyName("Email")
        public void setEmail(String email) {
            this.email = email;
        }

        //@PropertyName("First Name")
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        //@PropertyName("Last Name")
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        //@PropertyName("Password")
        public String getPassword() {
            return password;
        }
        //@PropertyName("Password")
        public void setPassword(String password) {
            this.password = password;
        }
        //@PropertyName("Account Type")
        public void setAccountType(Boolean AccountType) {
            this.accountType = AccountType;
        }
        //@PropertyName("Age")
        public void setAge(Integer age) {
            this.age = age;
        }
        //@PropertyName("Weight")
        public void setWeight(Integer weight) {
            this.weight = weight;
        }
        //@PropertyName("Height")
        public void setHeight(Integer height) {
            this.height = height;
        }

        //@PropertyName("GPS Follow")
        public Boolean getGpsFollow() {
            return gpsFollow;
        }
        //@PropertyName("GPS Follow")
        public void setGpsFollow(Boolean gpsFollow) {
            this.gpsFollow = gpsFollow;
        }
        //@PropertyName("Devices Followed")
        public void setDevicesFollowed(ArrayList<String> devicesFollowed) {
            this.devicesFollowed = devicesFollowed;
        }
        //@PropertyName("Events")
        public void setEvents(ArrayList<EventsDBO> events) {
            this.events = events;
        }

        //@PropertyName("Devices Followed")
        public ArrayList<String> getDevicesFollowed() {
            return devicesFollowed;
        }

        //@PropertyName("Events")
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
