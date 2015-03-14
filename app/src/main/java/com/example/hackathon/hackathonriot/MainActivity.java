package com.example.hackathon.hackathonriot;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//import android.webkit.JavascriptInterface;

public class MainActivity extends ActionBarActivity {
    public static int ACTION_VAR_HEIGHT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ACTION_VAR_HEIGHT =  getSupportActionBar().getHeight();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements SensorEventListener{

        private LocationManager locManager;
        public float degreeRotate = 0;
        public PlaceholderFragment() {
        }
        SensorManager mySensorManager;
        Sensor sensorOrientacion;
        Sensor sensorAcelerometro;
        FrameLayout myFrame;
        MapTag myMapa;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //getActivity().getActionBar().hide();
            //getActivity().getS
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            myFrame = (FrameLayout)rootView.findViewById(R.id.myFrameLayout);
            myMapa = new MapTag(rootView.getContext());
            Log.v("TAMANOS DEL FRAME: ","x = "+getActivity().getWindowManager().getDefaultDisplay().getWidth() +"  y = "+getActivity().getWindowManager().getDefaultDisplay().getHeight());
            //myMapa.setMaxis((float)(getActivity().getWindowManager().getDefaultDisplay().getWidth()),(float)(getActivity().getWindowManager().getDefaultDisplay().getHeight()-MainActivity.ACTION_VAR_HEIGHT-30));
            float height = (float)getActivity().getWindowManager().getDefaultDisplay().getHeight()-MainActivity.ACTION_VAR_HEIGHT;
            float width = (float) getActivity().getWindowManager().getDefaultDisplay().getWidth();
            float maxSize = height > width ? height:width;
            myMapa.setMaxis(width,height);

            myFrame.addView(myMapa);
            mySensorManager=(SensorManager)getActivity().getSystemService(SENSOR_SERVICE);
            sensorOrientacion = mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorAcelerometro = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            escucharSensores(rootView);
            registrerLocation();
            long[] pattern = { 0, 500,50,500,50,500,50,500};
            //Creamos la clase Vibrator
           /// Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
           // v.vibrate(pattern, -1);
            postLogin log = new postLogin();
            log.execute();
            return rootView;
        }
        float currentDegree = 0;
        public void startAnimation(){
            //float degree =(float)Globals.finalDegree;
            float degree =degreeRotate;
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(210);
            ra.setFillAfter(true);
            myFrame.startAnimation(ra);
            currentDegree = degree ;
            ra=null;
        }
        public void escucharSensores(View rootV){
            if(sensorOrientacion!=null){
                mySensorManager.registerListener(this,sensorOrientacion, 20000);
                Toast.makeText(rootV.getContext(),"Hay TYPE_ORIENTATION",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootV.getContext(),"No hay TYPE_ORIENTATION",Toast.LENGTH_LONG).show();
            }

            if(sensorAcelerometro!=null){
                mySensorManager.registerListener(this,sensorAcelerometro, 20000);
                Toast.makeText(rootV.getContext(),"Hay TYPE_ACELEROMETRO",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootV.getContext(),"No hay TYPE_ACELEROMETRO",Toast.LENGTH_LONG).show();
            }
        }
        public void noescucharSensores(View rootV){
            if(sensorOrientacion!=null){
                //mySensorManager.unregisterListener(sensorOrientacion);
            }

        }
        public class Personas{
            public String name;
            public float px,py;
            public int tipo;
            public Personas(String nam,float x,float y,int tipo){
                name = nam;
                px = x;
                py = y;
                this.tipo = tipo;
            }
        }


        @Override
        public void onSensorChanged(SensorEvent event) {
            //txt=(TextView)findViewById(R.id.textView1);
            switch(event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                   // Log.v("ACELEROMETRO",event.values[0] + "  " + event.values[1] + "  " + event.values[2]);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    degreeRotate = -event.values[0];
                    Personas [] per = new Personas[4];

                    per[0] = new Personas("Paladin",134.0f,56f,3);
                    per[1] = new Personas("Humano",254f,96f,1);
                    per[2] = new Personas("Zombie",84f,156f,2);
                    per[3] = new Personas("Humano",194f,46f,1);

                    myMapa.SetPersonas(per);
                    startAnimation();
                    //Log.v("GYROSCOPE",event.values[0] + "  " + event.values[1] + "  " + event.values[2]);
                    break;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        private void initLocation(){
            locManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location loc=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println(String.valueOf(loc.getLatitude()));
        }
        private void getListproviders(){
            locManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            List<String> listaProviders=locManager.getAllProviders();
            LocationProvider provider =locManager.getProvider(listaProviders.get(0));
            System.out.println(provider.getAccuracy());
            System.out.println(provider.supportsAltitude());
            System.out.println(provider.getPowerRequirement());
        }

        private Criteria getBestCriteria(){
            Criteria req=new Criteria();
            req.setAccuracy(Criteria.ACCURACY_FINE);
            req.setAltitudeRequired(true);
            return req;
        }
        private boolean isGPSAviable(){
            locManager =(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                return false;
            else
                return true;
        }

        LocationListener myLocationListener;
        public String proveedor;
        private void registrerLocation(){
            locManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,0,new MyLocationListener());
            Criteria c = new Criteria();
            c.setAccuracy(Criteria.ACCURACY_FINE);
            proveedor = locManager.getBestProvider(c,true);
            Location localizacion = locManager.getLastKnownLocation(proveedor);
            muestraPosicion(localizacion);
            myLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    muestraPosicion(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,0,myLocationListener);
        }
        private void muestraPosicion(Location loc) {
            if(loc != null){
                ////lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
                //lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
                //lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
                Log.i("LocAndroidaaaaaaaaa", String.valueOf(
                        loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
                if(myMapa!=null){
                    myMapa.setPositionTag1(loc.getLatitude(),loc.getLatitude());
                }
            }
            else{
                Log.i("LocAndroidaaaaaaaaa","Locationis null");
            }
        }

        public class MapTag extends View{
            public float maxX=220;//200px
            public float maxY=220;//200px
            public float t1x=0;
            public float t1y=0;
            public float t2x=0;
            public float t2y=0;
            public float scal=4.0f;//+X00%
            private Paint mPaint =new Paint(Paint.DITHER_FLAG);
            Canvas canvas;
            Context context;
            Personas [] person = null;
            public MapTag(Context context) {
                super(context);
                this.context = context;
            }
            public void setMaxis(float mx,float my){
                maxX = mx;
                maxY = my;
            }
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                mPaint.setAlpha(200);
                mPaint.setColor(Color.BLACK);
                for(int k=0;k<=maxX;k+=13){
                    canvas.drawLine(k,0,k,maxY,mPaint);
                }

                for(int k=0;k<=maxY;k+=13){
                    canvas.drawLine(0,k,maxX,k,mPaint);
                }
                mPaint.setAlpha(255);
                mPaint.setColor(Color.MAGENTA);
                canvas.drawCircle(maxX / 2, maxY / 2, 9f, mPaint);
                mPaint.setColor(Color.BLUE);
                //canvas.rotate(degreeRotate);
                canvas.drawText("YO",maxX/2,maxY/2 - 10,mPaint);

                if(person!=null){
                    mPaint.setAlpha(255);
                    for(int i = 0; i<person.length;i++){
                        switch (person[i].tipo){
                            case 1: mPaint.setColor(Color.GREEN);break; //persona
                            case 2: mPaint.setColor(Color.RED);break;  //zombie
                            case 3: mPaint.setColor(Color.BLUE);break; //PALADIN
                        }


                        canvas.drawText(person[i].name,person[i].px,person[i].py-10,mPaint);
                        canvas.drawCircle(person[i].px,person[i].py, 6f, mPaint);
                    }
                }
            }
            public void setPositionTag1(double x,double y){
                t1x=((int)((float)x*10000))%100;
                t1y=((int)((float)y*10000))%100;
                this.invalidate();
            }

            public void SetPersonas(Personas[] per){
                person = new Personas[per.length];
                for(int i = 0; i<person.length;i++){
                    person[i] = new Personas(per[i].name,per[i].px,per[i].py,per[i].tipo);
                }
                this.invalidate();
            }
        }



        private class MyLocationListener implements LocationListener {
            final String LOG_TAG = "HACKATOHN "+MyLocationListener.class.getSimpleName();
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                System.out.println();
                Log.v(LOG_TAG, "La posicion a cambiado");
                Log.v(LOG_TAG,"La posicion a cambiado: Altitude = "+ location.getAltitude()+"  Latitud= "+location.getLatitude());
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                Log.v(LOG_TAG,"GPS Desabilitado");
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                Log.v(LOG_TAG,"GPS Habilitado");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Log.v(LOG_TAG,provider);
            }

        }



        public class postLogin extends AsyncTask<String,Void,String[]> {
            private final String LOG_TAG = postLogin.class.getSimpleName();
            public InputStream is = null;
            String forecastJsonStr;
            @Override
            protected String[] doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {
                    final String FORECAST_BASE_URL = "http://one.hackiot.com:8080/riot-core-services/api/user/login";
                    Uri buildURI = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
                    URL url = new URL(buildURI.toString());
                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept","application/json");
                    JSONObject logi = new JSONObject();
                    logi.put("username","team11");
                    logi.put("password","patilla");
                    urlConnection.connect();
                    OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
                    out.write(logi.toString());
                    out.close();
                    int HttpResult =urlConnection.getResponseCode();
                    if(HttpResult == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream(),"utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println(""+sb.toString());

                    }else{
                        System.out.println(urlConnection.getResponseMessage());
                    }


                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    forecastJsonStr = null;
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return  null;
            }

            @Override
            protected void onPostExecute(String[] strings) {
                if(strings != null){

                }
                super.onPostExecute(strings);
            }
        }

        public class WebAppInterface {
            Context mContext;

            /** Instantiate the interface and set the context */
            WebAppInterface(Context c) {
                mContext = c;
            }

            /** Show a toast from the web page */
            @JavascriptInterface
            public void showToast(String toast) {
                Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
            }
        }

    }
}
