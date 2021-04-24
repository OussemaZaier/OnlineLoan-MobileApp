package com.example.pfe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectsAdapter extends RecyclerView.Adapter <ProjectsAdapter.MyProjectsHolder>{
    String data1[], data2[];
    Context context;
    public ProjectsAdapter(Context ct, String s1[], String s2[]) {
        context=ct;
        data1=s1;
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
        holder.ProjectTitle.setText(data1[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyProjectsHolder extends RecyclerView.ViewHolder{
        TextView ProjectTitle;
        public MyProjectsHolder(@NonNull View itemView) {
            super(itemView);
            ProjectTitle=itemView.findViewById(R.id.ProjectName);
        }
    }
}
