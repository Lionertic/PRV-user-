package com.example.lionertic.main.AsyncTask;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.MainActivity;
import com.example.lionertic.main.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class SaveLoca extends AsyncTask<Location, Void, String> {

    Context context;

    public SaveLoca(Context cnt) {
        context=cnt;
    }

    @Override
    protected String doInBackground(final Location... urls) {
        // Create URL object

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("123context", response+"iiiii");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("123context", error.getMessage()+"rrrrrr");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Lat", Double.toString(urls[0].getLatitude()));
                params.put("Lon", Double.toString(urls[0].getLongitude()));
                params.put("key", MainActivity.KEY);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    return null;
    }

    @Override
    protected void onPostExecute(String earthquake) {

    }
}

