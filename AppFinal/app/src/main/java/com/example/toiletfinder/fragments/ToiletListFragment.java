package com.example.toiletfinder.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.toiletfinder.MainActivity;
import com.example.toiletfinder.R;
import com.example.toiletfinder.data.ToiletController;
import com.example.toiletfinder.model.ToiletGroup;

import java.util.ArrayList;

public class ToiletListFragment extends android.support.v4.app.ListFragment {
    private ArrayAdapter<ToiletGroup> adapter;
    //    private MQConsumer consumer;
    private Button fetchToiletsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ToiletListAdapter(getActivity(), new ArrayList<ToiletGroup>(ToiletController.getToiletGroups().values()));
        setListAdapter(adapter);
        return inflater.inflate(R.layout.toiletlistfragment, null);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        fetchToiletsButton = (Button) getView().findViewById(R.id.fetchtoiletsbutton);
        fetchToiletsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFetchButtonClick(view);
            }
        });
    }

    @Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        ToiletGroup item = ToiletController.getToiletGroups().get(position);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps?q=loc:" + item.getLatLng().first + "," + item.getLatLng().second));
        startActivity(intent);
    }

    public void onFetchButtonClick(View v) {
        ToiletController.initToilets();
        Toast.makeText(getActivity(), "connecting to broker!", Toast.LENGTH_SHORT).show();
        Log.d("ToiletFinder", "Connecting to broker");
        ((MainActivity) getActivity()).initMap();
        ((MainActivity) getActivity()).startServices();

//        consumer = new MQConsumer(RESTService.SERVERHOSTNAME, "logs", "fanout");
//        consumer.connectToBroker();
//        consumer.setOnReceiveMessageHandler(new MQConsumer.onReceiveMessageHandler()
//        {
//            public void onReceiveMessage(byte[] message)
//            {
//                String text = "";
//                try {
//                    text = new String(message, "UTF8");
//                } catch (Exception e) {
//                    System.out.print("Exception caught in onReceiveMessage: "+e);
//                }
//
//                Log.d("ToiletFinder", text);
//                ToiletController.updateToilet(text);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (consumer != null)
//            consumer.dispose();
    }
}
