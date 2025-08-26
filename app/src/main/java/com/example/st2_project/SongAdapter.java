package com.example.st2_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> songNames;
    private OnItemClickListener listener;

    public SongAdapter(Context context, ArrayList<String> songNames, OnItemClickListener listener) {
        this.context = context;
        this.songNames = songNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // هنا نستخدم الملف item_song.xml بدل simple_list_item_1
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        holder.songTitle.setText(songNames.get(position));
        // ممكن تغير الأيقونة حسب الحالة، دلوقتي ثابتة
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return songNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        ImageView songIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            songIcon = itemView.findViewById(R.id.songIcon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int index);
    }
}
