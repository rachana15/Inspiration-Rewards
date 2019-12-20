package com.rachana.inspirationrewards;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GetSetLeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView inspLeadName;
    public TextView inspLeadPoints;
    public TextView inspLeadPosDept;
    public ImageView inspLeadImge;

    public GetSetLeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        inspLeadName = itemView.findViewById(R.id.inspLeadName);
        inspLeadPosDept = itemView.findViewById(R.id.inspLeadPosDept);
        inspLeadPoints = itemView.findViewById(R.id.inspLeadPoints);
        inspLeadImge = itemView.findViewById(R.id.inspLeadImge);
    }
}
