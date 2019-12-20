package com.rachana.inspirationrewards;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class UserProfileRewardHistoryAdapter extends RecyclerView.Adapter<UserProfileViewHolder> {
    private static final String TAG = "UserProfileRewardHistoryAdapter";
    List<RewardRecords> rewardsArrayList = new ArrayList<>();
    private UserProfileActivity userProfileActivity;

    public UserProfileRewardHistoryAdapter(UserProfileActivity userProfileActivity, List<RewardRecords> rewardsArrayList) {
        this.userProfileActivity = userProfileActivity;
        this.rewardsArrayList = rewardsArrayList;
    }

    @NonNull
    @Override
    public UserProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rewardhistory_entry, viewGroup, false);
        itemView.setOnClickListener(userProfileActivity);
        itemView.setOnLongClickListener(userProfileActivity);
        return new UserProfileViewHolder(itemView);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull UserProfileViewHolder userProfileViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        RewardRecords rewardRecords = rewardsArrayList.get(position);
        userProfileViewHolder.userProfileDate.setText(rewardRecords.getDate());
        userProfileViewHolder.userProfileName.setText(rewardRecords.getName());
        userProfileViewHolder.userProfileText.setText(rewardRecords.getNotes());
        userProfileViewHolder.userProfilePoints.setText("" + rewardRecords.getValue());
    }

    @Override
    public int getItemCount() {
        return rewardsArrayList.size();
    }
}
