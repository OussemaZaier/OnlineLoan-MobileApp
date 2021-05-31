package com.example.pfe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubmitAdapter extends RecyclerView.Adapter <SubmitAdapter.MyViewHolder>{
    ///////////
    private List<Project> demands=new ArrayList<>();
    private RecyclerViewClickListener listener;

    public SubmitAdapter(List<Project> demands, RecyclerViewClickListener listener){
        this.demands=demands;
        this.listener=listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView status;
        private TextView StatusEtat;
        public MyViewHolder(final View view){
            super(view);
            name=view.findViewById(R.id.ProjectName);
            status=view.findViewById(R.id.Status);
            StatusEtat=view.findViewById(R.id.Etat);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SubmitAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.projectrow,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitAdapter.MyViewHolder holder, int position) {
        holder.name.setText(demands.get(position).getName());
        holder.status.setText("Status :");
        holder.StatusEtat.setText(demands.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return demands.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
