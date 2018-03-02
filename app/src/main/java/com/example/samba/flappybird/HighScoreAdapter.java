package com.example.samba.flappybird;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by samba on 07/11/2017.
 */

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {
    private LayoutInflater inflador;
    private  Vector<String> list;
    private Context context;
    protected View.OnClickListener onClickListener;

    public HighScoreAdapter (Context context, Vector<String> list) {
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
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

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.high_score_element, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position));
        holder.icon.setImageResource(R.drawable.flappy_1);


    }

    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
