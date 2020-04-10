package com.example.grandmagear;

import java.util.ArrayList;

import javax.xml.transform.Source;

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
    public static final String Image = "image";

    //variable names for device
    public static final String ID = "id";
    public static final String Longitude = "longitude";
    public static final String Latitude = "latitude";
    public static final String Heartrate = "heartrate";
    public static final String Notifications = "notifications";
    public static final String DeviceBattery = "deviceBattery";
    public static final String PhoneBattery = "phoneBattery";
    public static final String Fall = "fall";
    public static final String DeviceOn = "deviceOn";
    public static final String Help = "helpRequested";

    public static class UserDBO{
        public String username;
        public String email;
        public String firstName;
        public String lastName;
        public String password;
        public Boolean accountType;
        public Boolean gpsFollow = false;
        public Integer age = 0;
        public Integer weight = 0;
        public Integer height = 0;
        public ArrayList<String> devicesFollowed;
        public ArrayList<EventsDBO> events;
        public ArrayList<Notifications> notifications;

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
            this.notifications = new ArrayList<Notifications>();
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
            this.notifications = new ArrayList<Notifications>();
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

        public ArrayList<Notifications> getNotifications(){
            return notifications;
        }

        public void setNotifications(ArrayList<Notifications> notifications){
            this.notifications = notifications;
        }
    }

    public static class DevicesDBO{
        protected String id;
        protected int heartrate;
        protected long latitude;    //latitude
        protected long longitude;    //longitude
        protected int deviceBattery;
        protected int phoneBattery;
        protected String fall = "good";
        protected String deviceOn = "off";
        protected long timeStamp;
        protected String helpRequested = "no";
        protected ArrayList<Notifications> notifications; //notifications linked to that device only

        public DevicesDBO() {
            //constructor for firestore snapshot
        }

        public DevicesDBO(String id) {
            this.id = id;
        }

        public DevicesDBO(String id, int heartrate, int deviceBattery, int phoneBattery, long timeStamp) {
            this.id = id;
            this.heartrate = heartrate;
            this.deviceBattery = deviceBattery;
            this.phoneBattery = phoneBattery;
            this.timeStamp = timeStamp;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getId() {
            return id;
        }

        public int getHeartrate() {
            return heartrate;
        }

        public long getLatitude() {
            return latitude;
        }

        public long getLongitude() {
            return longitude;
        }

        public int getDeviceBattery() {
            return deviceBattery;
        }

        public int getPhoneBattery() {
            return phoneBattery;
        }

        public String getFall() {
            return fall;
        }

        public String getDeviceOn() {
            return deviceOn;
        }

        public ArrayList<FirebaseObjects.Notifications> getNotifications() {
            return notifications;
        }

        public void setNotifications(ArrayList<FirebaseObjects.Notifications> notifications){
            this.notifications = notifications;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setHeartrate(int heartrate) {
            this.heartrate = heartrate;
        }

        public void setLatitude(long latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(long longitude) {
            this.longitude = longitude;
        }

        public void setDeviceBattery(int deviceBattery) {
            this.deviceBattery = deviceBattery;
        }

        public void setPhoneBattery(int phoneBattery) {
            this.phoneBattery = phoneBattery;
        }

        public void setFall(String fall) {
            this.fall = fall;
        }

        public void setDeviceOn(String deviceOn) {
            this.deviceOn = deviceOn;
        }

        public String getHelpRequested() {
            return helpRequested;
        }

        public void setHelpRequested(String helpRequested) {
            this.helpRequested = helpRequested;
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

        protected String notificationTitle;
        protected String notificationText;
        protected String notificationTime;

        public Notifications() {
            //constructor for snapshot
        }

        public Notifications(String notificationTitle, String notificationText, String notificationTime) {
            this.notificationTitle = notificationTitle;
            this.notificationText = notificationText;
            this.notificationTime = notificationTime;
        }

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public String getNotificationText() {
            return notificationText;
        }

        public String getNotificationTime(){
            return notificationTime;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public void setNotificationText(String notificationText) {
            this.notificationText = notificationText;
        }

        public void setNotificationTime(String notificationTime) {
            this.notificationTime = notificationTime;
        }
    }
}
