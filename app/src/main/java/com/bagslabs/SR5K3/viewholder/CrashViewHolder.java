package com.bagslabs.SR5K3.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bagslabs.SR5K3.R;
import com.bagslabs.SR5K3.models.AddCrash;

public class CrashViewHolder extends RecyclerView.ViewHolder {

    public TextView areaView;
    public TextView problemView;
    public TextView causeView;
    public TextView actionView;

    public CrashViewHolder(View itemView) {
        super(itemView);

        areaView = itemView.findViewById(R.id.post_area);
        problemView = itemView.findViewById(R.id.post_problem);
        causeView = itemView.findViewById(R.id.post_cause);
        actionView = itemView.findViewById(R.id.post_action);
    }

    public void bindToPost(AddCrash addCrash, View.OnClickListener starClickListener) {
        areaView.setText(addCrash.area);
        problemView.setText(addCrash.problem);
        causeView.setText(addCrash.cause);
        actionView.setText(addCrash.action);
    }
}
