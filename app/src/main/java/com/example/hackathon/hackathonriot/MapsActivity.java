package com.example.hackathon.hackathonriot;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

//int maxThings = GLOBALS.listTHINGS.length;
//    int currentThings = 0;
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(-68.088234,-16.541189)).title("Marker tese"));
        //getUsers getU = new getUsers();

        //    getU.execute("3832");
        for(int k = 0; k < GLOBALS.listTHINGS.length;k++){
               getUsers getU = new getUsers();
                getU.execute(GLOBALS.listTHINGS[k].id);
        }
    }

    public class getUsers extends AsyncTask<String, Void, String> {
        //  private final ProgressDialog dialog = new ProgressDialog(getActivity().getApplicationContext());
        private final String LOG_TAG = getUsers.class.getSimpleName();
        public InputStream is = null;

        @Override
        protected void onPreExecute() {
            //   dialog.setMessage("Ingresando");
            //    dialog.show();
        }
        public String id_thig = "";
        @Override
        protected String doInBackground(String... strings) {
            id_thig = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                final String FORECAST_BASE_URL = "http://one.hackiot.com:8080/riot-core-services/api/thing/"+strings[0];
                Uri buildURI = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
                URL url = new URL(buildURI.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("Api_key",GLOBALS.API_KEY);

                urlConnection.connect();

                int HttpResult =urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    java.lang.String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return (String )sb.toString();
                }else{
                    System.out.println(urlConnection.getResponseMessage());
                    return (String)urlConnection.getResponseMessage().toString();
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            Log.v("REQUEST_LOGIN","No se pudo conectar");
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("result " +result);
            try {
                getThigns(result);
            } catch (JSONException e) {
            }
        }

        public THING_USER getThigns(String json) throws JSONException{
            JSONObject myJ = new JSONObject(json);
            JSONArray myArray = myJ.getJSONArray("fields");
            THING_USER things = null;
            LatLng loc = null;
            for(int k = 0;k<myArray.length();k++){
                JSONObject aux = myArray.getJSONObject(k);
                things = new THING_USER(id_thig);
                String val = aux.getString("name");
                Log.v("VALUE<FIELD NAME >>= ",aux.toString());
                if(val.equals("locationXYZ")){
                    things.locationXYZ = aux.getString("value");
                }else if(val.equals("tiempo")){
                    things.tiempo = aux.getString("value");
                }else if(val.equals("location")){
                    things.location = aux.getString("value");
                    Log.v("LOCATION THINGS_IF -> ",aux.getString("value") );


                    try{
                        loc = new LatLng(
                                Float.parseFloat(aux.getString("value").split(";")[1])
                                ,
                                Float.parseFloat(aux.getString("value").split(";")[0])
                        );
                        Log.v("LOCAT THINGS Parse-> ","Suceess");
                    }catch (Exception e){
                        loc = new LatLng(
                                12
                                ,
                                12
                        );
                    }
                }else if(val.equals("Vida")){
                    things.Estado = aux.getString("value");
                }else if(val.equals("Estado")){
                    things.Estado = aux.getString("value");
                }/*else{
                    things.name = aux.getString("value");
                }*/
            }
            if(things!=null){
                things.name = myJ.getString("name");
                Log.v("LOCATION THINGS -> ",things.location);
            }



    if(loc!=null){
        mMap.addMarker(new MarkerOptions().position(loc).title(things.name));
    }
            return things;
        }
    }
}
