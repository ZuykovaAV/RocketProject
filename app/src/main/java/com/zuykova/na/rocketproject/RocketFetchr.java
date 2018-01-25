package com.zuykova.na.rocketproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RocketFetchr {
    private static final String TAG = "RocketFetchr";
    private static final String API_URL = "https://api.spacexdata.com/v2/launches?launch_year=2017";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Rocket> fetchItems(){
        List<Rocket> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(API_URL);
            Log.i(TAG, jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            parseItems(items, jsonArray);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

    private void parseItems(List<Rocket> items, JSONArray jsonArray)
                                                    throws IOException, JSONException {
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Rocket item = new Rocket();
            item.setRocketTime(jsonObject.getLong("launch_date_unix")); //время запуска
            item.setRocketDesc(jsonObject.getString("details")); //описание

            JSONObject rocketJsonObject = jsonObject.getJSONObject("rocket");
            item.setRocketName(rocketJsonObject.getString("rocket_name")); //название ракеты

            JSONObject linksJsonObject = jsonObject.getJSONObject("links");
            item.setRocketIcon(linksJsonObject.getString("mission_patch")); //иконка
            item.setArticleLink(linksJsonObject.getString("article_link")); //ссылка на статью

            items.add(item);

        }


    }
}
