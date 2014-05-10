package com.example.toiletfinder.data;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.util.Log;

public class RESTService {
    public static final String SERVERHOSTNAME = "192.168.43.18";
    private final static String port = "5000";
    private final static String protocol = "http://";

    public static String retrieveToiletIslandUpdate(String id) {

        String rest_url = (id != null ? protocol + SERVERHOSTNAME + ":" + port + "/toiletgroups/" + id :
                protocol + SERVERHOSTNAME + ":" + port + "/toiletgroups/");
        HttpURLConnection connection = null;
        String requeststring = null;
        try {
            final URL url = new URL(rest_url);
            connection = (HttpURLConnection) url.openConnection();
            InputStream input = new BufferedInputStream(connection.getInputStream());
            requeststring = IOUtils.toString(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        if (requeststring != null) {
            Log.d("ToiletFinder", requeststring);
        }
        return requeststring;
    }
}
