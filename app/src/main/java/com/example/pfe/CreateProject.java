package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

    //dec
    String s1[], s2[];
    RecyclerView Projects;
    List<Project> projects;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_project, container, false);

        add=(FloatingActionButton) view.findViewById(R.id.Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , AddProject.class));
            }
        });

        projects = new ArrayList<>();
        getUserProjects();
        Projects = (RecyclerView) view.findViewById(R.id.Projects);
        s1 = getResources().getStringArray(R.array.testdesc);

        return view;
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
                        project.setName(projectObject.getString("name"));
                        projects.add(project);
                        s2 = getResources().getStringArray(R.array.testdesc);
                        ProjectsAdapter myAdapter = new ProjectsAdapter(getContext(), projects, s2);
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
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something went wrong!")
                        .setContentText(error.toString())
                        .show();
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
}