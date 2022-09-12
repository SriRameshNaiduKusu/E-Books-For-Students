package in.cwt.ebooksforstudents;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Aplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("New_Books_Available");
        FirebaseMessaging.getInstance().subscribeToTopic("Book_Added");
        FirebaseMessaging.getInstance().subscribeToTopic("Exams_are_Coming");
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();

    }



}