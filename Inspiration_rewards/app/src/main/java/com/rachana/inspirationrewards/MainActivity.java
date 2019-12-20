package com.rachana.inspirationrewards;


import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.app.ActivityCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String sId = "A20445105";
    private static final int B_REQUEST_CODE = 1;
   // private static final int UP_REQUEST_CODE = 2;
    private CheckBox rememberMe;
    private ProgressBar progressBar;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private RewardsPreferences preferences;
    private EditText username;
    private EditText password;
    //private TextView newAccView;
    private LocationManager locationManager;
    //private Location currentLocation;
    private Criteria criteria;
   // private JSONObject jsonObj;
    //private static int MY_LOCATION_REQUEST_CODE = 329;
    //private static int MY_EXT_STORAGE_REQUEST_CODE = 330;
    MainActivity mainActivity = this;
   // private ArrayList<RewardRecords> rewardsArrList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon);
        setTitle("Rewards");

        if (isOnline())
            setFileLocationPermissions();
        else
            errorDialog("errorDialog: No Internet Connectivity!!", "No Internet Connection", "Application stopping");
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        progressBar=findViewById(R.id.progressBar);
        preferences = new RewardsPreferences(this);

        Log.d(TAG, "onCreate: " + preferences.getValue(getString(R.string.pref_user)));
        username.setText(preferences.getValue(getString(R.string.pref_user)));
        password.setText(preferences.getValue(getString(R.string.pref_pass)));
        rememberMe.setChecked(preferences.getBoolValue(getString(R.string.pref_check)));
    }

    public void setFileLocationPermissions() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        int permLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permExt = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permLoc != PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permExt != PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public void errorDialog(String logStmt, String title, String message) {
        int d = Log.d(TAG, logStmt);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onLoginBtnClick(View v) {

        if (rememberMe.isChecked()) {
            Log.d(TAG, "login button clicked ");
            preferences.save(getString(R.string.pref_user), username.getText().toString());
            preferences.save(getString(R.string.pref_pass), password.getText().toString());
            preferences.saveBool(getString(R.string.pref_check), rememberMe.isChecked());
        }
        String uName = username.getText().toString();
        String pwd = password.getText().toString();

        if (uName.equals("") || pwd.equals("")) {
            errorDialog("errorDialog: Incomplete User Input!", "Incomplete User Input ", "Please enter a valid UserName/Password");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            new LoginAPIAsyncTask(mainActivity).execute(sId, uName, pwd);

        }
    }

    public void onNewAccCreateClick(View v) {
        Log.d(TAG, "onNewAccCreateClick: Main");
        if (isOnline()) {
            makeCustomToast(this, "Creating new user Profile", Toast.LENGTH_SHORT);
            Intent intent = new Intent(this, ActivityCreate.class);
            startActivityForResult(intent, B_REQUEST_CODE);
        } else
            makeCustomToast(this, "No Internet Connection", Toast.LENGTH_SHORT);
    }

    public static void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, " " + message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(100, 50, 100, 50);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }

    public void getLoginAPIResp(CreateProfile respBean, List<RewardRecords> rewardsList, String connectionResult) {
        Log.d(TAG, "getLoginAPIResp: " + respBean);
        if (respBean == null) {
            makeCustomToast(this, connectionResult, Toast.LENGTH_SHORT);
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            Log.d(TAG, "getLoginAPIResp: " + respBean.getUsername() + respBean.getFirstName() + respBean.getLastName() + respBean.getLocation() + respBean.getDepartment() + respBean.getPassword() + respBean.getPosition() + respBean.getStory() + respBean.getPointsToAward());

            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("USERPROFILE", respBean);
            intent.putExtra("USERPROFILE_LIST", (Serializable) rewardsList);
            progressBar.setVisibility(View.GONE);
            startActivity(intent);

        }
    }
}
