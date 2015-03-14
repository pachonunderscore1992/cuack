package com.example.hackathon.hackathonriot;

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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

           postLogin pos = new postLogin();
            pos.execute();

            return rootView;
        }

        public class postLogin extends AsyncTask<String,Void,String[]> {
            private final String LOG_TAG = postLogin.class.getSimpleName();

            private  String convertInputStreamToString(InputStream inputStream) throws IOException{
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine()) != null)
                    result += line;

                inputStream.close();
                return result;

            }

            public InputStream is = null;
            public JSONObject jObj = null;
            public String json = "";
            @Override
            protected String[] doInBackground(String... params) {
                /*Client client = ClientBuilder.newClient();
                Entity payload = Entity.json("{  'username': 'team11',  'password': 'patilla'}");
                Response response = client.target("http://one.hackiot.com:8080/riot-core-services/api/user/login")
                        .path("/user/login")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header("Accept", "application/json")
                        .post(payload);

                System.out.println("AAAXXstatus: " + response.getStatus());
                System.out.println("AAAXXheaders: " + response.getHeaders());
                System.out.println("AAAXXbody:" + response.readEntity(String.class));*/


                // Making HTTP request
                try {
                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://one.hackiot.com:8080/riot-core-services/api/user/login");
                    httpPost.setHeader ("Accept","application/json");
                    httpPost.setEntity(new HttpEntity() {
                        @Override
                        public boolean isRepeatable() {
                            return false;
                        }

                        @Override
                        public boolean isChunked() {
                            return false;
                        }

                        @Override
                        public long getContentLength() {
                            return 0;
                        }

                        @Override
                        public Header getContentType() {
                            return null;
                        }

                        @Override
                        public Header getContentEncoding() {
                            return null;
                        }

                        @Override
                        public InputStream getContent() throws IOException, IllegalStateException {
                            return null;
                        }

                        @Override
                        public void writeTo(OutputStream outputStream) throws IOException {

                        }

                        @Override
                        public boolean isStreaming() {
                            return false;
                        }

                        @Override
                        public void consumeContent() throws IOException {

                        }
                    });
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "n");
                    }
                    is.close();
                    json = sb.toString();
                    Log.v("EMILIOO",json);
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                // return JSON String
                Log.v("EYYYVIND", jObj.toString());
               // return jObj;

                return new String[3];
            }

            @Override
            protected void onPostExecute(String[] strings) {
                if(strings != null){

                }
                super.onPostExecute(strings);
            }
        }
    }
}
