package com.example.toiletfinder.service;

import android.app.IntentService;
import android.content.Intent;
import com.example.toiletfinder.VARS;
import com.example.toiletfinder.data.RESTService;

public class RESTPullService extends IntentService {

    public static final String APP_ID = VARS.APP_ID;

    // constants used to notify the Activity UI of received messages
    public static final String REST_MSG_RECEIVED_INTENT = APP_ID + ".MSGRECVD";
    public static final String REST_MSG_RECEIVED_TOPIC = APP_ID + ".MSGRECVD_TOPIC";
    public static final String REST_MSG_RECEIVED_MSG = APP_ID + ".MSGRECVD_MSGBODY";

    public RESTPullService() {
        super("RESTPullService");
    }

    private void broadcastReceivedMessage(String topic, String message) {
        // pass a message received from the MQTT server on to the Activity UI
        //   (for times when it i¨s running / active) so that it can be displayed
        //   in the app GUI
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(REST_MSG_RECEIVED_INTENT);
        broadcastIntent.putExtra(REST_MSG_RECEIVED_TOPIC, topic);
        broadcastIntent.putExtra(REST_MSG_RECEIVED_MSG, message);
        sendBroadcast(broadcastIntent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {

//        SharedPreferences settings = getSharedPreferences(VARS.APP_ID, 0);
        String topic = "1";
        String restResult = RESTService.retrieveToiletIslandUpdate(topic);
        try {
            broadcastReceivedMessage(topic, restResult);
        } catch (NullPointerException e) {
            throw new RuntimeException("");
        }

    }
}
