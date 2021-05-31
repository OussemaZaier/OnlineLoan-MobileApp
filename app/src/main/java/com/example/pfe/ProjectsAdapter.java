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

public class ProjectsAdapter extends RecyclerView.Adapter <ProjectsAdapter.MyProjectsHolder>{
    List<Project> projects=new ArrayList<>();
    Context context;
    private RecyclerViewListener listener;
    public ProjectsAdapter(Context ct, List<Project> Projects, RecyclerViewListener listener) {
        context=ct;
        projects=Projects;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.projectrow,parent,false);
        return new MyProjectsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectsHolder holder, int position) {
        holder.ProjectTitle.setText(projects.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
    public interface RecyclerViewListener{
        void onClick(View v,int position);
    }
    public class MyProjectsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView ProjectTitle;
        public MyProjectsHolder(@NonNull View itemView) {
            super(itemView);
            ProjectTitle=itemView.findViewById(R.id.ProjectName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());
        }
    }

}
