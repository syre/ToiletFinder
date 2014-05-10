package com.example.toiletfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.toiletfinder.data.ToiletController;
import com.example.toiletfinder.data.ToiletUtils;
import com.example.toiletfinder.fragments.ToiletMapFragment;
import com.example.toiletfinder.fragments.ToiletPagerAdapter;
import com.example.toiletfinder.model.Toilet;
import com.example.toiletfinder.model.ToiletGroup;
import com.example.toiletfinder.service.MqttServiceTwo;
import com.example.toiletfinder.service.RESTPullService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements ToiletMapFragment.ToiletMapInterface {

    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    ServiceState serviceState = ServiceState.REST_5_SECONDS;

    enum ServiceState {
        RealTime, REST_10_SECONDS, REST_5_SECONDS;
    }

    MQTTMessageReceiver messageIntentReceiver = new MQTTMessageReceiver();
    RESTPollMessageReceiver restPollMessageReceiver = new RESTPollMessageReceiver();

    public class RESTPollMessageReceiver extends BroadcastReceiver {

        Timer t = null;
        TimerTask timerTask = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle notificationData = intent.getExtras();
            String newTopic = notificationData.getString(RESTPullService.REST_MSG_RECEIVED_TOPIC);
            String newData = notificationData.getString(RESTPullService.REST_MSG_RECEIVED_MSG);
            handleReceivedJson(newData);

        }

        public void startPollingInterval(int interval, final Context context) {
            t = new Timer();
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    try {
                        Intent mServiceIntent = new Intent(context, RESTPullService.class);
                        context.startService(mServiceIntent);
                    } catch (NullPointerException e) {
                        throw new RuntimeException("");
                    }
                }
            };
            t.schedule(timerTask, 100, interval);

        }

        public void stopPolling() {
            if (t != null) {
                t.cancel();
            }
        }


    }

    public class MQTTMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle notificationData = intent.getExtras();
            String newTopic = notificationData.getString(MqttServiceTwo.MQTT_MSG_RECEIVED_TOPIC);
            String newData = notificationData.getString(MqttServiceTwo.MQTT_MSG_RECEIVED_MSG);
            handleReceivedSingleToiletJson(newData);

        }
    }

    private void handleReceivedJson(String receivedText) {
        if (receivedText != null) {
            ToiletGroup toilet = ToiletUtils.parseToiletGroupJSon(receivedText);
            if (toilet != null) {
                ToiletController.updateToilets(toilet);
                updateMakers();
            }
        }
    }

    private void handleReceivedSingleToiletJson(String receivedText) {
        if (receivedText != null) {
            Toilet toilet = ToiletUtils.parseToiletJSon(receivedText);
            if (toilet != null) {
                ToiletController.updateToilets(toilet);
                updateMap();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();
        adapter = new ToiletPagerAdapter(fm);
        pager.setAdapter(adapter);

        registerReceivers();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_real_time:
                serviceState = ServiceState.RealTime;
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_refresh was selected
            case R.id.action_refresh_10:
                serviceState = ServiceState.REST_10_SECONDS;
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_refresh_5:
                serviceState = ServiceState.REST_5_SECONDS;
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        startServices();

        return true;
    }

    public void stopAllServices() {
        final Intent restServiceIntent = new Intent(this, RESTPullService.class);
        this.stopService(restServiceIntent);
        final Intent mqttServiceIntent = new Intent(this, MqttServiceTwo.class);
        this.stopService(mqttServiceIntent);
    }

    public void registerReceivers() {
        messageIntentReceiver = new MQTTMessageReceiver();
        final IntentFilter intentCFilterMqtt = new IntentFilter(MqttServiceTwo.MQTT_MSG_RECEIVED_INTENT);
        registerReceiver(messageIntentReceiver, intentCFilterMqtt);

        restPollMessageReceiver = new RESTPollMessageReceiver();
        final IntentFilter intentCFilterREST = new IntentFilter(RESTPullService.REST_MSG_RECEIVED_INTENT);
        registerReceiver(restPollMessageReceiver, intentCFilterREST);
    }

    public void startServices() {
        restPollMessageReceiver.stopPolling();
        stopAllServices();

        switch (serviceState) {
            // action with ID action_refresh was selected
            case RealTime:
                SharedPreferences settings = getSharedPreferences(VARS.APP_ID, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(VARS.MQTT_BROKER_URL, "192.168.43.18");
                editor.putString(VARS.MQTT_TOPICS, "client4,client6,client1");
                editor.commit();
                Intent mServiceIntent = new Intent(this, MqttServiceTwo.class);
                this.startService(mServiceIntent);
                break;
            // action with ID action_settings was selected
            case REST_10_SECONDS:
                restPollMessageReceiver.startPollingInterval(10000, this);
                break;
            case REST_5_SECONDS:
                restPollMessageReceiver.startPollingInterval(5000, this);
                break;
            default:
                Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMap map = supportMapFragment.getMap();
        map.getUiSettings().setRotateGesturesEnabled(false);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(56, 10), 6);
        map.moveCamera(update);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Integer id = Integer.parseInt(marker.getTitle());
                return true;
            }
        });
    }

    public void updateMakers() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMap map = supportMapFragment.getMap();
        if (map != null) {
            map.getUiSettings().setRotateGesturesEnabled(false);
            while (!ToiletController.getConcurrentLinkedQueue().isEmpty()) {
                Toilet toilet = ToiletController.getConcurrentLinkedQueue().poll();
                ToiletUtils.setMarker(map, toilet);
            }
        }
    }

    public void updateMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMap map = supportMapFragment.getMap();
        if (map != null) {
            map.getUiSettings().setRotateGesturesEnabled(false);
            for (ToiletGroup tg : ToiletController.getToiletGroups().values()) {
                ToiletUtils.setMarker(map, tg);
                for (Toilet toilet : tg.getToilets().values()) {
                    ToiletUtils.setMarker(map, toilet);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(messageIntentReceiver);
        unregisterReceiver(restPollMessageReceiver);
        stopAllServices();
    }

}

