//package com.example.phygen_java.ui;
//
//import android.util.Log;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class FirebaseService extends FirebaseMessagingService {
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        Log.d("FCM", "Token: " + token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage message) {
//        super.onMessageReceived(message);
//        Log.d("FCM", "Message: " + message.getNotification().getBody());
//        // TODO: show notification
//    }
//}
