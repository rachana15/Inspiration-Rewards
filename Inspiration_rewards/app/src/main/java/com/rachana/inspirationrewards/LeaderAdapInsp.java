package com.rachana.inspirationrewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LeaderAdapInsp extends RecyclerView.Adapter<GetSetLeaderViewHolder> {
    private static final String TAG = "LeaderAdapInsp";
    private List<GetSetLeaderBoardInspiration> inspLeaderArrayList;
    private LeaderBoardActivityInspiration LeaderBoardActivityInspiration;

    public LeaderAdapInsp(LeaderBoardActivityInspiration LeaderBoardActivityInspiration, List<GetSetLeaderBoardInspiration> inspLeaderArrayList) {
        this.inspLeaderArrayList = inspLeaderArrayList;
        this.LeaderBoardActivityInspiration = LeaderBoardActivityInspiration;
    }

    @NonNull
    @Override
    public GetSetLeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaderboard_entry, viewGroup, false);
        itemView.setOnClickListener(LeaderBoardActivityInspiration);
        itemView.setOnLongClickListener(LeaderBoardActivityInspiration);
        return new GetSetLeaderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GetSetLeaderViewHolder GetSetLeaderViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        GetSetLeaderBoardInspiration getSetLeaderBoardInspiration = inspLeaderArrayList.get(position);
        GetSetLeaderViewHolder.inspLeadName.setText(getSetLeaderBoardInspiration.getLastName() + ", " + getSetLeaderBoardInspiration.getFirstName());
        GetSetLeaderViewHolder.inspLeadPosDept.setText(getSetLeaderBoardInspiration.getPosition() + ", " + getSetLeaderBoardInspiration.getDepartment());
        GetSetLeaderViewHolder.inspLeadPoints.setText(""+getSetLeaderBoardInspiration.rewardPtAward);
        String imgString = getSetLeaderBoardInspiration.getImageBytes();
        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        GetSetLeaderViewHolder.inspLeadImge.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return inspLeaderArrayList.size();
    }
}
