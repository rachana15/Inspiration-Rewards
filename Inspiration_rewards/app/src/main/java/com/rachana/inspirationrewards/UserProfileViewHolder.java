package com.rachana.inspirationrewards;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileViewHolder extends RecyclerView.ViewHolder {
    public TextView userProfileDate;
    public TextView userProfileName;
    public TextView userProfilePoints;
    public TextView userProfileText;
    public ImageView userProfileImage;

    public UserProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        userProfileDate = itemView.findViewById(R.id.rewardHistoyDate_view);
        userProfileName = itemView.findViewById(R.id.rewardHistoyName_view);
        userProfilePoints = itemView.findViewById(R.id.rewardHistoyPts_view);
        userProfileText = itemView.findViewById(R.id.rewardHistoyTxt_view);
        userProfileImage = itemView.findViewById(R.id.rewardHistoyImg_view);
    }
}