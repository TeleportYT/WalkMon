package com.vik.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class StatsAdpter extends ArrayAdapter<GameStats> {

    public StatsAdpter(Context context, List<GameStats> resource) {
        super(context, 0,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the data item for this position
        GameStats stat = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stats_layout, parent, false);
        }

        // look up the views and populate them with the data
        TextView textViewName1 = convertView.findViewById(R.id.state);
        textViewName1.setText(stat.getState() == GameState.Win ? "Winned" : "Lost");
        textViewName1.setBackgroundColor(stat.getState() == GameState.Win ? Color.GREEN : Color.RED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewName1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        TextView textViewName3 = convertView.findViewById(R.id.score);
        textViewName3.setText(""+stat.getPlayerScore()+"  ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewName3.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        TextView textViewName4 = convertView.findViewById(R.id.time);
        textViewName4.setText(stat.getTime()+"  ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewName4.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        TextView textViewName5 = convertView.findViewById(R.id.killed);
        textViewName5.setText(""+stat.getEnemiesKilled()+"  ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewName5.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        TextView textViewName6 = convertView.findViewById(R.id.hp);
        textViewName6.setText(""+stat.getPlayerHealth()+"  ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewName6.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


        Resources res = this.getContext().getResources();
        for (int i =0 ; i<4;i++){
            int id = res.getIdentifier("txt"+(i+1), "id", getContext().getPackageName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((TextView)convertView.findViewById(id)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
            ((TextView)convertView.findViewById(id)).setLines(1);
        }

        // return the completed view to render on screen
        return convertView;
    }
}
