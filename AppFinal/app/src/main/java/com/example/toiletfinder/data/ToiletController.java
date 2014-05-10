package com.example.toiletfinder.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.AsyncTask;

//import com.example.toiletfinder.model.Toilet;
import com.example.toiletfinder.model.Toilet;
import com.example.toiletfinder.model.ToiletGroup;

public class ToiletController {

    @SuppressWarnings("serial")
    private static Map<String, ToiletGroup> toiletGroups = new HashMap<String, ToiletGroup>();
    private static ConcurrentLinkedQueue<Toilet> concurrentLinkedQueue = new ConcurrentLinkedQueue<Toilet>();

    public static Map<String, ToiletGroup> getToiletGroups() {
        return toiletGroups;
    }

    public static void initToilets() {
        final AsyncTask<Integer, Void, String> result = new ToiletRetrieverTask().execute(1);
        try {
            final ToiletGroup toiletGroup = ToiletUtils.parseToiletGroupJSon(result.get());
            toiletGroups.put(toiletGroup.getId().toString(), toiletGroup);

        } catch (Exception e) {

        }

    }

    public static void updateToilets(ToiletGroup toiletGroup) {

    }

    public static void updateToilets(Toilet newToilet) {
        final ToiletGroup toiletGroup = toiletGroups.get(newToilet.getGroupId().toString());
        if (toiletGroup != null) {
            final Toilet toilet = toiletGroup.getToilets().get(newToilet.getId().toString());
            if (toilet != null) {
                toilet.setOccupied(newToilet.getOccupied());
                concurrentLinkedQueue.add(toilet);
            }
        }
    }

    public static class ToiletRetrieverTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
//            Integer id = params[0];
            String result = RESTService.retrieveToiletIslandUpdate("1");
            ToiletGroup toiletGroup = ToiletUtils.parseToiletGroupJSon(result);
            toiletGroups.clear();
            toiletGroups.put("1", toiletGroup);
            return "";
        }

    }

    public static ConcurrentLinkedQueue<Toilet> getConcurrentLinkedQueue() {
        return concurrentLinkedQueue;
    }
}
