package com.rachana.inspirationrewards;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class UpdateProfileAPIAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "UpdateProfileAPIAsyncTask";
    private static final String baseUrl = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String loginEndPoint = "/profiles";
    private CreateProfile bean;
    private List<RewardRecords> rewardArrayList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private EditActivity editActivity;
    private JSONArray rewardsJsonArr;
    private int connectionCode = -1;

    public UpdateProfileAPIAsyncTask(EditActivity editActivity, CreateProfile bean, List<RewardRecords> rewardArrayList) {

        this.editActivity = editActivity;
        this.bean = bean;
        this.rewardArrayList = rewardArrayList;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(Void... voids) {
        JSONObject jsonObject = new JSONObject();
        String studentId = bean.studentId;
        String username = bean.username;
        String password = bean.password;
        String firstName = bean.firstName;
        String lastName = bean.lastName;
        int pointsToAward = bean.pointsToAward;
        String story = bean.story;
        String department = bean.department;
        String position = bean.position;
        boolean admin = bean.admin;
        String location = bean.location;
        String imageBytes = bean.imageBytes;
        rewardsJsonArr = new JSONArray();
        if (!rewardArrayList.isEmpty()) {
            for (int i = 0; i < rewardArrayList.size(); i++) {
                JSONObject rewJson = new JSONObject();
                RewardRecords r = rewardArrayList.get(i);
                String studentR = r.getStudentId();
                String usernmaeR = r.getUsernameRecord();
                String nameR = r.getName();
                String dateR = r.getDate();
                String notesR = r.getNotes();
                int valueR = r.getValue();
                rewardsJsonArr.put(rewJson);
            }
        }

        Log.d(TAG, "doInBackground: UpdateProfile" + studentId + username + password + firstName + lastName + pointsToAward + department + position + admin + location + imageBytes);
        try {
            jsonObject.put("studentId", studentId);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("pointsToAward", pointsToAward);
            jsonObject.put("department", department);
            jsonObject.put("story", story);
            jsonObject.put("position", position);
            jsonObject.put("admin", admin);
            jsonObject.put("location", location);
            jsonObject.put("imageBytes", imageBytes);
            if (rewardArrayList.size() == 0)
                jsonObject.put("rewardRecords", "[]");
            else
                jsonObject.put("rewardRecords", rewardsJsonArr);

            return doAPICall(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    private String doAPICall(JSONObject jsonObject) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = baseUrl + loginEndPoint;

            Uri uri = Uri.parse(urlString);
            URL url = new URL(uri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");


            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            // Write the JSON (as String) to the open connection
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();
            Log.d(TAG, "doAPICall: UpdateProfile" + responseCode);
            if (responseCode == HTTP_OK) {
                connectionCode = responseCode;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();

            } else {
                connectionCode = responseCode;
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            }

        } catch (Exception e) {
            Log.d(TAG, "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: error close " + e.getMessage());
                }
            }
        }
        return "error occurred";
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String connectionResult) {
        Log.d(TAG, "onPostExecute: UpdateProfile" + connectionResult);
        if (connectionCode == HTTP_OK)
            editActivity.getEditProfileAPIResp("SUCCESS");
        else if (connectionCode == -1)
            editActivity.getEditProfileAPIResp("error occured");
        else {
            Log.d(TAG, "onPostExecute: Inside else ");
            try {
                JSONObject errorDetailsJson = new JSONObject(connectionResult);
                JSONObject errorJsonMsg = errorDetailsJson.getJSONObject("errordetails");
                String errorMsg = errorJsonMsg.getString("message");
                Log.d(TAG, "onPostExecute: Error " + errorMsg);
                editActivity.getEditProfileAPIResp(errorMsg.split("\\{")[0].trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
