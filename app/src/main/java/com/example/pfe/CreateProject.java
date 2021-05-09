package com.example.pfe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pfe.databinding.FragmentCreateProjectBinding;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProject#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProject extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton add;

    FragmentCreateProjectBinding fragmentCreateProjectBinding;
    //dec
    private String s1[], s2[];
    private RecyclerView Projects;
    private List<Project> projects;
    private ProjectsAdapter.RecyclerViewListener listener;
    private SweetAlertDialog.OnSweetClickListener Modify;
    private SweetAlertDialog.OnSweetClickListener Delete;
    private SweetAlertDialog.OnSweetClickListener refresh;
    private SweetAlertDialog.OnSweetClickListener no;
    private String pos;
    public CreateProject() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateProject.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProject newInstance(String param1, String param2) {
        CreateProject fragment = new CreateProject();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCreateProjectBinding=FragmentCreateProjectBinding.inflate(inflater,container,false);
        View view=fragmentCreateProjectBinding.getRoot();
        fragmentCreateProjectBinding.empty.setVisibility(View.GONE);
        setOnClickListnerModify();
        setOnClickListnerDelete();
        setOnClickListnerRefresh();
        setOnClickListnerNo();
        projects = new ArrayList<>();
        Projects = (RecyclerView) view.findViewById(R.id.Projects);
        getUserProjects();
        add=(FloatingActionButton) view.findViewById(R.id.Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , AddProject.class));
            }
        });


        return view;
    }

    private void setOnClickListner(){
        listener=new ProjectsAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View v, int position) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(projects.get(position).getName())
                        .setContentText(projects.get(position).getDescription())
                        .setConfirmText("Modify")
                        .setCancelText("Delete")
                        .setCustomImage(R.drawable.ic_lightbulb)
                        .setConfirmClickListener(Modify)
                        .setCancelClickListener(Delete)
                        .show();
                pos=projects.get(position).getId();
            }
        };
    }

    private void setOnClickListnerModify(){
        Modify=new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                Intent intent=new Intent(getContext(),SelectModifyProject.class);
                intent.putExtra("id",pos);
                startActivity(intent);
            }
        };
    }
    private void setOnClickListnerDelete(){
        Delete=new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("are you sure you want to delete project")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setConfirmClickListener(refresh)
                        .setCancelClickListener(no)
                        .show();
            }
        };
    }
    private void setOnClickListnerRefresh(){
        refresh=new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                deleteProject(pos);

            }
        };
    }
    private void setOnClickListnerNo(){
        no=new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        };
    }

    private void getUserProjects() {
        String URL = "http://192.168.1.16:8080/rest/webapi/projects/get";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject projectObject = response.getJSONObject(i);
                        Project project = new Project();
                        project.setId(projectObject.getString("id"));
                        project.setName(projectObject.getString("name"));
                        project.setDescription(projectObject.getString("description"));
                        projects.add(project);
                        s2 = getResources().getStringArray(R.array.testdesc);
                        setOnClickListner();
                        ProjectsAdapter myAdapter = new ProjectsAdapter(getContext(), projects, s2,listener);
                        Projects.setAdapter(myAdapter);
                        Projects.setLayoutManager(new LinearLayoutManager(getContext()));
                    } catch (JSONException e) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(e.toString())
                                .show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentCreateProjectBinding.empty.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("CIN", ((CIN) getActivity().getApplication()).getCIN());

                return params;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }
    private void deleteProject(String pose) {
        String URL="http://192.168.1.16:8080/rest/webapi/projects/delete";
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        StringRequest objectRequest=new StringRequest(
                Request.Method.DELETE,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        projects.clear();
                        getUserProjects();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Something went wrong!")
                                .setContentText(error.toString())
                                .show();
                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("ID",pose);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}