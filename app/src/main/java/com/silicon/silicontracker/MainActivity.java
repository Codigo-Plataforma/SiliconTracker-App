package com.silicon.silicontracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button btnUpload,btnGetData;
    FusedLocationProviderClient fusedLocationProviderClient;

    String latitude,longitude,city,fullAddress,pinCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload=findViewById(R.id.btnUpload);

        btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,GetDataActivity.class);
                startActivity(i);
            }
        });





        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==
                        PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();

                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull  Task<Location> task) {
                Location location = task.getResult();
                if(location!=null)
                {
                    try{
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());


                        List<Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        Log.d("siddheswar", address.toString());

                        float lati = (float) address.get(0).getLatitude();
                        latitude= String.valueOf(lati);




                        float longi = (float) address.get(0).getLongitude();
                        longitude= String.valueOf(longi);

                        fullAddress=address.get(0).getLocality();
                        city=address.get(0).getSubAdminArea();
                        Log.d("CityResponse",city);



                        pinCode = address.get(0).getPostalCode();

                        setLocationToServer();


                    }catch (Exception e )
                    {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void setLocationToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://silicon-tracker-dev.herokuapp.com/api/location";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseFromServer",response);

                try {
                    JSONObject responseFromServer = new JSONObject(response);

                    String message = responseFromServer.getString("message");
                    if(message.equals("Location Updated"))
                    {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fullAddress",fullAddress);
                params.put("city",city);
                Log.d("cityParams",city);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("pinCode",pinCode);


                return params;



                /*
                 "fullAddress" : "sambalpur, odisha",
    "city" : "hfihsfgs",
    "pinCode" : 362649,
    "latitude" : "7350830753",
    "longitude" : "902382842"w
                 */
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put( "charset", "utf-8");
//                return headers;
//            }
        };

        requestQueue.add(stringRequest);
    }
}