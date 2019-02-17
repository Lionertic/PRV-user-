package com.example.lionertic.main.AsyncTask;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.CarMove.WorkerResultReceiver;
import com.example.lionertic.main.CarMove.job;
import com.example.lionertic.main.Fragments.Maps;
import com.example.lionertic.main.MainActivity;
import com.example.lionertic.main.R;
import com.example.lionertic.main.RequestHandler;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Routes extends AsyncTask<String, Void, String> implements WorkerResultReceiver.Receiver {

    Context context;
    static LatLng prev[] = new LatLng[2];
    WorkerResultReceiver mWorkerResultReceiver;
    static double time = 0;
    SharedPreferences sd;

    public Routes(Context cnt) {
        context=cnt;
        sd=context.getSharedPreferences("route",Context.MODE_PRIVATE);
    }

    private void animateCarMove(final Marker marker, final LatLng beginLatLng, final LatLng endLatLng, final long duration ) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();

        final Interpolator interpolator = new LinearInterpolator();

        // set car bearing for current part of path
        float angleDeg = (float)(180 * getAngle(beginLatLng, endLatLng) / Math.PI);
        Matrix matrix = new Matrix();
        matrix.postRotate(angleDeg);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_car));

        handler.post(new Runnable() {
            @Override
            public void run() {
                // calculate phase of animation
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                // calculate new position for marker
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;

                marker.setPosition(new LatLng(lat, lng));

                // if not end of line segment of path
                if (t < 1.0) {
                    // call next marker position
                    handler.postDelayed(this, 16);
                }
                    // call turn animation
            }
        });
    }
    private double getAngle(LatLng beginLatLng, LatLng endLatLng) {
        double f1 = Math.PI * beginLatLng.latitude / 180;
        double f2 = Math.PI * endLatLng.latitude / 180;
        double dl = Math.PI * (endLatLng.longitude - beginLatLng.longitude) / 180;
        return Math.atan2(Math.sin(dl) * Math.cos(f2) , Math.cos(f1) * Math.sin(f2) - Math.sin(f1) * Math.cos(f2) * Math.cos(dl));
    }
    private void route(JSONObject jObject, int c, int dr,LatLng o,LatLng d) {
        if(o!=null){
            List<LatLng> l = new ArrayList<>();
            l.add(o);
            l.add(d);
            job.enqueueWork(context, mWorkerResultReceiver,l,dr);
        }
        JSONArray jRoutes, jLegs;
        ArrayList points = new ArrayList();
        LatLng latLng;
        try {
            jRoutes = jObject.getJSONArray("paths");
            jObject = jRoutes.getJSONObject(0);
            jObject = jObject.getJSONObject("points");
            jRoutes = jObject.getJSONArray("coordinates");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = jRoutes.getJSONArray(i);
                latLng = new LatLng(jLegs.getDouble(1), jLegs.getDouble(0));
                points.add(latLng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        PolylineOptions lineOptions =  new PolylineOptions();

        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(c);
        lineOptions.geodesic(true);
        if(Maps.dri[dr]!=null)
            Maps.dri[dr].remove();
        Maps.dri[dr]=Maps.mMap.addPolyline(lineOptions);
        Maps.dr[dr] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_car);
    }

    @Override
    protected String doInBackground(final String... urls) {
        // Create URL object
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null ;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.ROUTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            MainActivity.progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            for(int i=0;i<jsonObject.getInt("no");i++){
                                LatLng lo = new LatLng(jsonObject.getJSONArray(i + "co").getDouble(0), jsonObject.getJSONArray(i + "co").getDouble(1));

                                if (sd.getInt(Integer.toString(i),0)==0) {
                                    Log.e("qwertyuiop","hi");
                                    Maps.drv[i] = Maps.mMap.addMarker(new MarkerOptions().position(lo).title("TEST"));
                                    route(jsonObject.getJSONObject(Integer.toString(i)), Color.GREEN, i, prev[i], lo);
                                    prev[i] = new LatLng(lo.latitude, lo.longitude);
                                    sd.edit().putInt(Integer.toString(i),1).apply();
                                }
                                route(jsonObject.getJSONObject(Integer.toString(i)), Color.GREEN, i, prev[i], lo);
                                prev[i] = new LatLng(lo.latitude, lo.longitude);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("asdfghjkl","asdfghjkl"+error.toString()+urls[0]);

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", urls[0]);
                params.put("lat", Double.toString(Maps.mCurrentLocation.getLatitude()));
                params.put("lon", Double.toString(Maps.mCurrentLocation.getLongitude()));

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(String earthquake) {

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case job.SHOW_RESULT:
                if (resultData != null) {
                    List<LatLng> l = (List<LatLng>) resultData.getSerializable("loca");
                    int no = resultData.getInt("num");
                    animateCarMove(Maps.drv[no],l.get(0),l.get(1),2000);
                }
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        mWorkerResultReceiver = new WorkerResultReceiver(new Handler());
        mWorkerResultReceiver.setReceiver(this);
    }
}
