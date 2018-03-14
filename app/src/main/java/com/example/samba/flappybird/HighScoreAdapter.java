package com.example.samba.flappybird;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Vector;

/**
 * Created by samba on 07/11/2017.
 */

public class HighScoreAdapter extends FirebaseRecyclerAdapter<Score, HighScoreAdapter.ViewHolder> {
    private LayoutInflater inflador;
    private List<Score> scoresList;
    private Context context;

    public HighScoreAdapter (Context context, DatabaseReference databaseReference) {
        super(Score.class, R.layout.high_score_element, HighScoreAdapter.ViewHolder.class, databaseReference);
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, othertext;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            othertext = itemView.findViewById(R.id.othertext);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    public HighScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.high_score_element,null);
        return new ViewHolder(v);
    }
    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Score score, int position) {
        viewHolder.title.setText(score.toString());
        viewHolder.icon.setImageResource(R.drawable.player_right);
    }
}
