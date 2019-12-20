package com.rachana.inspirationrewards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class GetAllProfilesAPIAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetAllProfilesAPIAsyncTask";
    private LeaderBoardActivityInspiration LeaderBoardActivityInspiration;
    private final String baseurlAdress = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private final String loginURL = "/allprofiles";
    private List<GetSetLeaderBoardInspiration> leaderArrayList =new ArrayList<>() ;
    int connectionCode=-1;
    public GetAllProfilesAPIAsyncTask(LeaderBoardActivityInspiration LeaderBoardActivityInspiration) {
        this.LeaderBoardActivityInspiration = LeaderBoardActivityInspiration;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... strings) {
        String stuId = strings[0];
        String uName = strings[1];
        String pwd = strings[2];

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", stuId);
            jsonObject.put("username", uName);
            jsonObject.put("password", pwd);

            Log.d(TAG, "doInBackground: " );
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

            String urlString = baseurlAdress + loginURL;

            Uri uri = Uri.parse(urlString);
            URL url = new URL(uri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();
            if (responseCode == HTTP_OK) {
                connectionCode=responseCode;

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }


                return result.toString();

            } else {
                connectionCode=responseCode;
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
                    Log.e(TAG, "doInBackground: closing: " + e.getMessage());
                }
            }
        }
        return "error occurred";
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String connectionResult) {
        Log.d(TAG, "onPostExecute: " );
        GetSetLeaderBoardInspiration bean;
        if (connectionCode == HTTP_OK) {
            try {
                JSONArray jsonArray = new JSONArray(connectionResult);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String studentId = jsonObject.getString("studentId");
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    int pointsToAward = jsonObject.getInt("pointsToAward");
                    String department = jsonObject.getString("department");
                    String story = jsonObject.getString("story");
                    String position = jsonObject.getString("position");
                    boolean admin = jsonObject.getBoolean("admin");
                    String location = jsonObject.getString("location");
                    String imageBytes = jsonObject.getString("imageBytes");
                    String rewards1 = jsonObject.getString("rewards");

                    List<RewardRecords> rewardsList = new ArrayList<>();

                    if (rewards1 != "null") {
                        JSONArray rewards = new JSONArray(rewards1);
                        for (int j = 0; j < rewards.length(); j++) {
                            RewardRecords rewardRecordsBean = new RewardRecords();
                            JSONObject reward = rewards.getJSONObject(j);
                            String studentIdR = reward.getString("studentId");
                            String usernameR = reward.getString("username");
                            String nameR = reward.getString("name");
                            String notesR = reward.getString("notes");
                            String dateR = reward.getString("date");
                            int valueR = reward.getInt("value");
                            rewardRecordsBean.setStudentId(studentIdR);
                            rewardRecordsBean.setUsernameRecord(usernameR);
                            rewardRecordsBean.setName(nameR);
                            rewardRecordsBean.setDate(dateR);
                            rewardRecordsBean.setNotes(notesR);
                            rewardRecordsBean.setValue(valueR);
                            rewardsList.add(rewardRecordsBean);
                        }
                        bean = new GetSetLeaderBoardInspiration(studentId, username, password, firstName, lastName, pointsToAward, department, story, position, admin, location, imageBytes, rewardsList);
                    }
                    else
                        bean = new GetSetLeaderBoardInspiration(studentId, username, password, firstName, lastName, pointsToAward, department, story, position, admin, location, imageBytes, null);
                    leaderArrayList.add(bean);
                }
                LeaderBoardActivityInspiration.getAllProfilesAPIResp(leaderArrayList,"SUCCESS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (connectionCode == -1)
            LeaderBoardActivityInspiration.getAllProfilesAPIResp(null,"Error Occured");
        else
        {
            Log.d(TAG, "onPostExecute: Inside else loop ");
            try {
                JSONObject errorDetailsJson = new JSONObject(connectionResult);
                JSONObject errorJsonMsg = errorDetailsJson.getJSONObject("errordetails");
                String errorMsg = errorJsonMsg.getString("message");
                Log.d(TAG, "onPostExecute: Error " + errorMsg);
                LeaderBoardActivityInspiration.getAllProfilesAPIResp(null,errorMsg.split("\\{")[0].trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }
}
