package com.cultureshock.app;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> festival;


    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView full_name;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.name_text);
        }
    }
    public SearchAdapter(Context context, ArrayList<String> festival) {
        this.context = context;
        this.festival = festival;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        holder.full_name.setText(festival.get(position));
        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int k = holder.getAdapterPosition();
                String s = festival.get(k);
                Intent intent = new Intent(context, FestivalScreen.class);
                intent.putExtra("fest_name", s);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return festival.size();
    }
}
