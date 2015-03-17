package com.example.hackathon.hackathonriot;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class SelectPersonActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_person);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        getThings gety = new getThings();
        gety.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_person, menu);
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 1:break;
                case 2:break;
                case 3:break;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */



    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public RelativeLayout layout;
        public static int myNumber;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            myNumber = sectionNumber;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        Button buttonEstadistica;
        Button buttonJugar1;
        Button buttonJugar2;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_select_person, container, false);
            layout = (RelativeLayout)rootView.findViewById(R.id.select_id);

            getActivity().setTitle("Tipo");
            switch(myNumber){
                case 1:
                    layout.setBackgroundResource(R.drawable.usehumna);
                    break;
                case 2:
                    layout.setBackgroundResource(R.drawable.userzombie1);
                    break;
                case 3:
                    layout.setBackgroundResource(R.drawable.userpaladin);
                    break;
            }
            buttonEstadistica = (Button)rootView.findViewById(R.id.buttonEstadistica);
            buttonJugar1 = (Button)rootView.findViewById(R.id.buttonJugar1);
            buttonJugar2 = (Button)rootView.findViewById(R.id.buttonJugar2);

            buttonEstadistica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newInt = new Intent(rootView.getContext(),codeRoadActivity.class);
                    startActivity(newInt);
                }
            });

            buttonJugar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newInt = new Intent(rootView.getContext(),codeRoadActivity.class);
                    startActivity(newInt);
                }
            });

            buttonJugar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newInt = new Intent(rootView.getContext(),MapsActivity.class);
                    startActivity(newInt);
                }
            });


            return rootView;
        }




    }
    public class getThings extends AsyncTask<String, Void, String> {
        //  private final ProgressDialog dialog = new ProgressDialog(getActivity().getApplicationContext());
        private final String LOG_TAG = getThings.class.getSimpleName();
        public InputStream is = null;

        @Override
        protected void onPreExecute() {
            //   dialog.setMessage("Ingresando");
            //    dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                final java.lang.String FORECAST_BASE_URL = "http://one.hackiot.com:8080/riot-core-services/api/thing/";
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
                    Log.v("REQUEST_THJINNGSS",sb.toString());
                    return (String )sb.toString();
                }else{
                    System.out.println(urlConnection.getResponseMessage());
                    Log.v("REQUEST_THJINNGSS","No se pudo conectar REQUEST_THJINNGSS");
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
            //  if (dialog != null && dialog.isShowing())
            //      dialog.dismiss();
            System.out.println("result " +result);
            try {
                getThigns(result);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No se sspudo obtener Datos", Toast.LENGTH_LONG).show();
            }
        }

        public THING[] getThigns(String json) throws JSONException{
            JSONObject myJ = new JSONObject(json);
            JSONArray myArray = myJ.getJSONArray("results");
            THING[] things = new THING[myArray.length()];
            for(int k =0;k<myArray.length();k++){
                JSONObject aux = myArray.getJSONObject(k);
                Log.v("IDS_THING",aux.getString("id"));
                things[k] = new THING(aux.getString("id"),aux.getString("activated"),aux.getString("name"),aux.getString("serial"));
            }
            GLOBALS.listTHINGS = things;
            return things;
        }
    }
}
