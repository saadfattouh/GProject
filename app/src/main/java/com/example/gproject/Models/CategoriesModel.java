package com.example.gproject.Models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gproject.Utils.URLConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesModel {
    public int category_id;
    public String amount;
    public int price;
    private Context context;
    static boolean initiateStatus;

    private ArrayList<CategoriesModel> categoriesList;

       public static String URL_CATEGORIES =
            URLConnector.BASE_URL + "/" +
                    URLConnector.BASE_API_FOLDER + "/" +
                    URLConnector.CATEGORIES_URL;

    public CategoriesModel(Context context) {
        this.context = context;
        categoriesList = new ArrayList<CategoriesModel>();

    }

    public CategoriesModel(int category_id, String amount, int price) {
        this.category_id = category_id;
        this.amount = amount;
        this.price = price;

        categoriesList = new ArrayList<CategoriesModel>();

    }
    public CategoriesModel(){}

    public int getCategory_id() {
        return category_id;
    }

    public String getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void initiateCategories() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                        System.out.println("Rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //converting the string to json array object
                          //  JSONArray array = new JSONArray(response);
                            JSONArray array = jsonObject.getJSONArray("categories");
                            System.out.println(array);

                            if (success.equals("1")) {
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
                                System.out.println("before :::::::;"+initiateStatus);

                                initiateStatus = true;

                                System.out.println("after ::::::::::::: "+initiateStatus);
                            }else if  (success.equals("0")){
                                initiateStatus = false;
                                System.out.println("else ::::::::::::: "+initiateStatus);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(context, "LogIn Error2! " + error.toString()
                                    + "\nStatus Code " + "\nCause " + error.getCause()
                                    + "\nmessage" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        //adding our stringrequest to queue
   //     Volley.newRequestQueue(context).add(stringRequest);

    }

    public ArrayList<CategoriesModel> getCategoriesList(){
        System.out.println("status    ::::::::::  "+initiateStatus);
        if(initiateStatus){
            return categoriesList;

        }
        return null;
    }
}

