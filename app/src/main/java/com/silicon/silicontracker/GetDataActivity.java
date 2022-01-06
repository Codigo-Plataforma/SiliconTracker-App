package com.silicon.silicontracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DataAdapter db;

    int counter = 0;
    Button btnGetMore;


    ArrayList<String> longitude, latitude, city, pinCode, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        recyclerView = findViewById(R.id.rv_getLocation);

        btnGetMore = findViewById(R.id.btnGetMore);

        getLocationFromServer();

        btnGetMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromServer();
                counter = counter + 1;
            }
        });


        longitude = new ArrayList<>();
        latitude = new ArrayList<>();
        city = new ArrayList<>();
        pinCode = new ArrayList<>();
        address = new ArrayList<>();


        db = new DataAdapter(latitude, longitude, city, pinCode, address);




    }

    private void getLocationFromServer() {
        String url = "https://silicon-tracker-dev.herokuapp.com/api/location/"+counter;

        Log.d("urlSidd",url);

        RequestQueue requestQueue = Volley.newRequestQueue(GetDataActivity.this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.d("responseSidd",response.toString());

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObjectRequest = response.getJSONObject(i);

                        longitude.add(jsonObjectRequest.getString("longitude"));
                        latitude.add(jsonObjectRequest.getString("latitude"));
                        pinCode.add(jsonObjectRequest.getString("pinCode"));
                        city.add(jsonObjectRequest.getString("city"));
                    }
                    recyclerView.setAdapter(db);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(GetDataActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);


    }
}


