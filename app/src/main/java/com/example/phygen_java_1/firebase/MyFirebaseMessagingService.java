//package com.example.phygen_java.firebase;
//
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        Log.d("FCM", "Token: " + token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getNotification() != null) {
//            Log.d("FCM", "Message received: " + remoteMessage.getNotification().getBody());
//        } else {
//            Log.d("FCM", "Message received with no notification body");
//        }
//        // TODO: Show notification to user
//    }
//}
