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
    String data1[],data2[];
    List<Project> projects=new ArrayList<>();
    Context context;
    public ProjectsAdapter(Context ct, List<Project> Projects, String s2[]) {
        context=ct;
        projects=Projects;
        data2=s2;
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

    public class MyProjectsHolder extends RecyclerView.ViewHolder{
        TextView ProjectTitle;
        public MyProjectsHolder(@NonNull View itemView) {
            super(itemView);
            ProjectTitle=itemView.findViewById(R.id.ProjectName);
        }
    }
}
