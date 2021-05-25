package com.example.gproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.example.gproject.Adapters.TransferOpertaionAdapter;
import com.example.gproject.Models.TransferOperationModel;
import com.example.gproject.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperationFragment extends Fragment {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ArrayList<TransferOperationModel> transferOperationModelArrayList;
    private TransferOpertaionAdapter transferOpertaionAdapter;
    private RecyclerView transferOperationRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    // creating a variable for our page and liTransferOperationModelmit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    int page = 0, limit = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_op, container, false);
        // creating a new array list.
        transferOperationModelArrayList = new ArrayList<>();
//////////////////////////////
        System.out.println("onCreateView");
////////////////
        // initializing our views.
        transferOperationRV = view.findViewById(R.id.idRVTransferOperation);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // calling a method to load our api.
        getDataFromAPI(page, limit);
        // adding on scroll change listener method for our nested scroll view.
//        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // on scroll change we are checking when users scroll as bottom.
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    page++;
//                    loadingPB.setVisibility(View.VISIBLE);
//                    getDataFromAPI(page, limit);
//
//                    //     Toast.makeText(getContext(),"onViewCreated"+ transferOperationModelArrayList.get(0).toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
    }

    private void getDataFromAPI(int page, int limit) {

        // creating a string variable for url .
        String url = TransferOperationModel.URL_TRANSFER_OPERATION;

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                        System.out.println("response:::::::::::::::::::::::::::::::;" + response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String success = jObject.getString("success");
                            JSONArray jsonArray = jObject.getJSONArray("transferOperations");
                            if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting product object from json array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                boolean status = jsonObject.getString("status").equals("1");
                                // on below line we are extracting data from our json object.
                                transferOperationModelArrayList.add(new TransferOperationModel(
                                        jsonObject.getString("mobile_number"),
                                        jsonObject.getString("category"),
                                        status,
                                        jsonObject.getString("sim_type"),
                                        jsonObject.getString("date")));

                                System.out.println("jsonObject" + jsonObject.toString());

                            }

                        }
                            // passing array list to our adapter class.
                            transferOpertaionAdapter = new TransferOpertaionAdapter(transferOperationModelArrayList, getContext());

                            // setting layout manager to our recycler view.
                            transferOperationRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            transferOperationRV.setAdapter(transferOpertaionAdapter);
                            loadingPB.setVisibility(View.GONE);

                        } catch (JSONException e) {

                            loadingPB.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()

                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("error1:::" + e.toString()

                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);

                // handling on error listener method.
                Toast.makeText(getContext(), "Fail to get data.." + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("error2" + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", "1");
                return parameters;
            }
        };
        //adding our stringrequest to queue
        queue.add(stringRequest);
    }

}
