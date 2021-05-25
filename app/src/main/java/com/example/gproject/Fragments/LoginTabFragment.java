package com.example.gproject.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gproject.Activities.MainActivity;
import com.example.gproject.Utils.SharedPrefs;
import com.example.gproject.Utils.URLConnector;
import com.example.gproject.Models.UserModel;
import com.example.gproject.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginTabFragment extends Fragment {

    private EditText loginMobileNumberEditText;
    private ProgressBar loginProgressBar;
    private Button loginButton;
    private TextView loginRegisterText;
    private UserModel userModel;
    static boolean requestStatus;

    //key for storing user's id
    private String LOGGED_IN_KEY = "first_ime";



    private static String URL_LOGIN =
            URLConnector.BASE_URL + "/" +
            URLConnector.BASE_API_FOLDER + "/" +
            URLConnector.LOGIN_URL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_login, container, false);
        loginMobileNumberEditText = view.findViewById(R.id.login_mobile_phone);
        loginProgressBar = view.findViewById(R.id.login_progressbar);
        loginButton = view.findViewById(R.id.login_btn);
        loginRegisterText = view.findViewById(R.id.login_register_link);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = loginMobileNumberEditText.getText().toString().trim();

                if (!mobileNumber.isEmpty()) {
                    if(checkPhoneNumber(mobileNumber)) {
                        logIn(mobileNumber);
                        if(requestStatus){

                        }else{
                            loginProgressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    loginMobileNumberEditText.setError("Please enter a mobile number");


                }
            }
        });
    }

    private void logIn(String mobileNumber) {
        loginProgressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        userModel = new UserModel();
        userModel.setMobileNumber(loginMobileNumberEditText.getText().toString().trim());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    userModel.setUser_id(object.getInt("user_id"));
                                    userModel.setFirstName(object.getString("first_name").trim());
                                    userModel.setMiddleName(object.getString("middle_name").trim());
                                    userModel.setLastName(object.getString("last_name").trim());
                                    userModel.setMobileNumber(object.getString("mobile_number").trim());
                                    userModel.setPhoneNumber(object.getString("phone_number").trim());
                                    userModel.setNationalNumber(object.getString("national_number").trim());
                                    userModel.setAddress(object.getString("address").trim());
                                    userModel.setPassword(object.getString("password").trim());

                                    Toast.makeText(getContext(), "LogIn Success \n" +
                                            "Your Name:" + userModel.getFirstName() + " "
                                            + userModel.getLastName(), Toast.LENGTH_LONG).show();
                                    loginButton.setVisibility(View.VISIBLE);
                                    loginProgressBar.setVisibility(View.GONE);

                                    //for the first login
                                    saveUsersLogIn(userModel.getUser_id());
                                    // todo send user credentials for retrieving user's related data
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            } else if (success.equals("0")) {
                                requestStatus = false;
                                Toast.makeText(getContext(), "This Number is not registered ", Toast.LENGTH_SHORT).show();
                                loginButton.setVisibility(View.VISIBLE);
                                loginProgressBar.setVisibility(View.GONE);
                            }
                            requestStatus = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            loginButton.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.GONE);
                            requestStatus = false;

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null) {
                            Toast.makeText(getContext(), "LogIn Error2! " + error.toString()
                                    + "\nCause " + error.getCause()
                                    + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                            loginButton.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.GONE);
                            requestStatus = false;
                        }

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
            //    if (!noNumberFound) {
                    params.put("mobileNumber", userModel.getMobileNumber());

                    return params;
//                } else {
//                    return checkParams(params);
//                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void saveUsersLogIn(int user_id) {
        SharedPrefs.save(getContext(), LOGGED_IN_KEY, user_id);
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    private boolean checkPhoneNumber(String phoneNumber){

        //check length of number and if it starts with "09"
        if (phoneNumber.length() != 10 || phoneNumber.charAt(0)!= '0' || phoneNumber.charAt(1) != '9') {
            return false;
        }
        //matcher.find() will return true if there were any non numeric characters
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneNumber);
        return !matcher.find();

    }

}
