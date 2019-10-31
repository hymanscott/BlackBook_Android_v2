package com.lynxstudy.lynx;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;

import java.util.List;

public class LynxBadgesRecyclerViewAdapter extends RecyclerView.Adapter<LynxBadgesRecyclerViewAdapter.BadgeViewHolder> {
    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeImage;
        TextView rowBadgeName;
        TextView rowBadgeDescription;
        TextView rowBadgeEarnedTimes;

        BadgeViewHolder(View itemView) {
            super(itemView);

            badgeImage = (ImageView)itemView.findViewById(R.id.badgeImage);
            rowBadgeName = (TextView)itemView.findViewById(R.id.rowBadgeName);
            rowBadgeDescription = (TextView)itemView.findViewById(R.id.rowBadgeDescription);
            rowBadgeEarnedTimes = (TextView)itemView.findViewById(R.id.rowBadgeEarnedTimes);
        }
    }

    Context context;
    DatabaseHelper db;
    List<BadgesMaster> badgesMasters;

    LynxBadgesRecyclerViewAdapter(Context context, List<BadgesMaster> badgesMasters){
        this.badgesMasters = badgesMasters;
        this.context = context;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public int getItemCount() {
        return badgesMasters.size();
    }

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_lynx_badges_item, viewGroup, false);
        BadgeViewHolder pvh = new BadgeViewHolder(v);

        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder badgeViewHolder, int i) {
        final BadgesMaster badgesMaster = badgesMasters.get(i);

        int noOfCount = db.getUserBadgesCountByBadgeID(badgesMaster.getBadge_id());
        int imageDrawableId = context.getResources().getIdentifier(badgesMaster.getBadge_icon(), "drawable", context.getPackageName());

        badgeViewHolder.badgeImage.setImageDrawable(context.getResources().getDrawable(imageDrawableId));
        badgeViewHolder.rowBadgeName.setText(badgesMaster.getBadge_name());
        badgeViewHolder.rowBadgeDescription.setText(badgesMaster.getBadge_description());
        badgeViewHolder.rowBadgeEarnedTimes.setText("Earned " + noOfCount +" time(s)");

        if(badgesMaster.getBadge_name().equals("PrEP")){
            badgeViewHolder.rowBadgeName.setText("PrEP'd");
        }else if(badgesMaster.getBadge_name().equals("I Love Anal")){
            badgeViewHolder.rowBadgeName.setText(Html.fromHtml("I &#9829; Anal"));
        }

        if(noOfCount > 0){
            View v = badgeViewHolder.itemView;

            v.setClickable(true);
            v.setFocusable(true);
            v.setId(badgesMaster.getBadge_id());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent badgeScreen = new Intent(context, BadgeScreenActivity.class);

                    badgeScreen.putExtra("badge_id", badgesMaster.getBadge_id());
                    badgeScreen.putExtra("isAlert", "No");

                    context.startActivity(badgeScreen);
                }
            });
        }
    }
}
