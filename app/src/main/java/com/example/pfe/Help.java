package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pfe.databinding.FragmentCreateProjectBinding;
import com.example.pfe.databinding.FragmentHelpBinding;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Help#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Help extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    private FragmentHelpBinding binding;
    public Help() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Help newInstance() {
        Help fragment = new Help();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentHelpBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.desc.getText().toString().equals("")){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Something went wrong!")
                            .setContentText("Description field is empty")
                            .show();
                }
                else{
                    addFeedback();
                    }
            }
        });
        return view;
    }
    private void addFeedback() {
        String URL="http://192.168.1.16:8080/rest/webapi/feedback/create";
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest objectRequest=new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("feedback added")
                                .setContentText("thanks, we will follow up on your feedback as soon as possible")
                                .show();
                        binding.desc.setText(null);
                        binding.Comment.toggle();

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
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                int id=binding.radioGroup.getCheckedRadioButtonId();
                RadioButton radio=binding.radioGroup.findViewById(id);
                params.put("desc",binding.desc.getText().toString());
                params.put("type",radio.getText().toString());
                params.put("ID_USER", ((CIN) getActivity().getApplication()).getCIN());
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}