package com.example.gproject.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gproject.Activities.MainActivity;
import com.example.gproject.Models.CategoriesModel;
import com.example.gproject.Models.TransferOperationModel;
import com.example.gproject.R;
import com.example.gproject.SpinnerCustom.CustomSpinnerAdapter;
import com.example.gproject.SpinnerCustom.CustomSpinnerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    EditText phoneBox, passBox;
    Button sendOperationRequestBtn;
    Spinner customSpinner;
    String spinnerSelectedItem;

    String selectedProvider;


    private ArrayList<CustomSpinnerItem> mCustomSpinnerItems;
    private CustomSpinnerAdapter mCustomSpinnerAdapter;

    Spinner spinner;
    ArrayList<CategoriesModel> categoriesList;
    ArrayAdapter<String> spinnerAdapter;
    CategoriesModel categoriesModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        phoneBox = view.findViewById(R.id.phoneBox);
        passBox = view.findViewById(R.id.pass_box);
        sendOperationRequestBtn = view.findViewById(R.id.send_operation_request_btn);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        customSpinner = (Spinner) view.findViewById(R.id.custom_spinner);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        // Categories Spinner ////////////////////////////////////////////////////////////////////////////
        //To do :: check internet connection

        categoriesModel = new CategoriesModel();
//        categoriesModel.initiateCategories();
//        categoriesList = categoriesModel.getCategoriesList();
//        categoriesList = initiateCategories();


//       else{
//
//            final String[] spinnerCategories = getResources().getStringArray(R.array.categories);
//            final ArrayList<String> categoriesListAlternative = new ArrayList<>(Arrays.asList(spinnerCategories));
//            // Creating adapter for spinner
//            spinnerAdapter =
//                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoriesListAlternative);
//        }

//        // Drop down layout style - list view with radio button
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(spinnerAdapter);

        initiateCategories();
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
    //Custom spinner //////////////////////////////////////////////////////////
        initCustomSpinnerList();
        mCustomSpinnerAdapter = new CustomSpinnerAdapter(getContext(), mCustomSpinnerItems);
        customSpinner.setAdapter(mCustomSpinnerAdapter);
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CustomSpinnerItem customSpinnerItem = (CustomSpinnerItem) parent.getItemAtPosition(position);
                if(position == 0){
                    selectedProvider = TransferOperationModel.MTN_STRING;
                }else if (position == 1){
                    selectedProvider = TransferOperationModel.SYRIATEL_STRING;
                }
                //  selectedProvider = customSpinnerItem.getProviderName();
           //     Toast.makeText(getContext(), selectedProvider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        passBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendOperationRequestBtn.performClick();
                    return true;
                }
                return false;
            }
        });


        sendOperationRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(phoneBox.getText().toString().isEmpty()){
                    phoneBox.setError("Please enter a mobile number");
                    return;
                }
                String number = phoneBox.getText().toString().trim();

                if (!checkPhoneNumber(number) ){
                    phoneBox.setError("please enter a valid mobile number");
                    Toast.makeText(getContext(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                    return;
                }
                String password = passBox.getText().toString().trim();
                if(password.isEmpty()){
                    passBox.setError("please enter your password");
                }
                TelecomManager telecomManager = (TelecomManager) getActivity().getSystemService(Context.TELECOM_SERVICE);

//                //To call from SIM 1
//                Uri uri = Uri.fromParts("tel",number, "");
//                Bundle extras = new Bundle();  extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,primaryPhoneAccountHandle);
//                telecomManager.placeCall(uri, extras);

                String code = "";
                if(selectedProvider.equals((TransferOperationModel.MTN_STRING))){
                    code = "*150*75169*"+ number + "*"+ spinnerSelectedItem + "#";
                }else{
                    code = "*150*1*74862*1*"+ spinnerSelectedItem + "*" + number + "*" + number + "#";
                }

//                String mtnCode = "*150*75169*"+ number + "*"+ spinnerSelectedItem + "#";
//                String syriatelCode = "*150*1*74862*1*"+ spinnerSelectedItem + "*" + number + "*" + number + "#";
                Toast.makeText(getContext(), "code" + code+
                        "proider" + selectedProvider
                        +"category" + spinnerSelectedItem, Toast.LENGTH_LONG).show();

                //To call from SIM 2
                Uri uri1 = Uri.parse("tel:" + Uri.encode( code ));
                final int REQUEST_PHONE_CALL = 1;
                Bundle extras = new Bundle();
                /**
                 * ApI > = 26
                 *
                 *        TelephonyManager telMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                 *        int simState = telMgr.getSimState(1);
                 */
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, getSimHandler(telecomManager,  1) );

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    telecomManager.placeCall(uri1, extras);
                }
            }
        });
    }

    //Initializing of custom spinner
    private void initCustomSpinnerList() {
        mCustomSpinnerItems = new ArrayList<>();
        mCustomSpinnerItems.add(new CustomSpinnerItem("MTN", R.drawable.ic_mtn ));
        mCustomSpinnerItems.add(new CustomSpinnerItem("", R.drawable.ic_syriatel_logo ));

    }

    //spinner function
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        spinnerSelectedItem = parent.getItemAtPosition(position).toString().trim();
        // Showing selected spinner item
         Toast.makeText(parent.getContext(), "Selected: " + spinnerSelectedItem, Toast.LENGTH_SHORT).show();

    }

    //spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @TelecomManager Object
     * @int value represent  0 for main sim card , any number greater than 0 for second sim card
     * if value is negative it will use main sim
     *
      */
    private PhoneAccountHandle getSimHandler(TelecomManager telecomManager, int simNumber) {
        int mDefaultSimNum = 0;
        if(simNumber <= 0){
            simNumber = mDefaultSimNum;
        }

        //To find SIM ID
        String primarySimId = "", secondarySimId = "";
        SubscriptionManager subscriptionManager = (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 2);

        }
        List<SubscriptionInfo> subList = subscriptionManager.getActiveSubscriptionInfoList();
        int index = -1;
        for (SubscriptionInfo subscriptionInfo : subList) {
            index++;
            if (index == 0) {
                primarySimId = subscriptionInfo.getIccId();
            } else {
                secondarySimId = subscriptionInfo.getIccId();
            }
        }
        // TO CREATE PhoneAccountHandle FROM SIM ID
        List<PhoneAccountHandle> list = telecomManager.getCallCapablePhoneAccounts();
        PhoneAccountHandle primaryPhoneAccountHandle = null, secondaryPhoneAccountHandle = null;
        for (PhoneAccountHandle phoneAccountHandle : list) {
            if (phoneAccountHandle.getId().contains(primarySimId)) {
                primaryPhoneAccountHandle = phoneAccountHandle;
            }
            if (phoneAccountHandle.getId().contains(secondarySimId)) {
                secondaryPhoneAccountHandle = phoneAccountHandle;
            }
        }
        if(simNumber == 0) {
            return primaryPhoneAccountHandle;
        }else {
            return secondaryPhoneAccountHandle;
        }
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
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

    public void initiateCategories() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, CategoriesModel.URL_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                        System.out.println("Rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            JSONArray array = jsonObject.getJSONArray("categories");
                            System.out.println(array);

                            if (success.equals("1")) {
                                Toast.makeText(getContext(), "succes!!!", Toast.LENGTH_SHORT).show();
                                categoriesList = new ArrayList<>();
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject object = array.getJSONObject(i);

                                    System.out.println("JsonObject"+ object.toString());
                                    //adding the product to product list
                                    categoriesList.add(new CategoriesModel(
                                            object.getInt("category_id"),
                                            object.getString("amount"),
                                            object.getInt("price")

                                    ));
                                }
                                ArrayList<String> spinnerCategories = new ArrayList<>();
                                for(int i = 0; i< categoriesList.size(); i++ ){
                                    spinnerCategories.add(categoriesList.get(i).getAmount().trim());

                                }
                                // Creating adapter for spinner
                                spinnerAdapter =
                                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerCategories);

                                // Drop down layout style - list view with radio button
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinner.setAdapter(spinnerAdapter);



                            }else if (success.equals("0")){


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(getContext(), "LogIn Error2! " + error.toString()
                                    + "\nStatus Code " + "\nCause " + error.getCause()
                                    + "\nmessage" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

//        adding our stringrequest to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }
}