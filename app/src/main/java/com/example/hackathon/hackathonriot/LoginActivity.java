package com.example.hackathon.hackathonriot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Ayrton on 14/03/2015.
 */
public class LoginActivity extends Activity {

    EditText inputName;
    EditText inputPassword;

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login);

        inputName = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener( new View.OnClickListener(){

             @Override
             public void onClick(View arg0) {
//                 Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                 String username = inputName.getText().toString();
                 String password = inputPassword.getText().toString();

//                 postLogin login = new postLogin(username,password);
//
//                 login.execute();

//                 MyTask task = new MyTask(LoginActivity.this,username,password);
//                 task.setMessageLoading("Logging in");
//
//                 task.execute("http://one.hackiot.com:8080/riot-core-services/api/user/login");

                 Toast.makeText(getApplicationContext(), "Conectandose", Toast.LENGTH_SHORT).show();
//                 Log.e("n", inputName.getText() + "." + inputPassword.getText());

                 String message = null;
                 String api_key = null;
                 if(username.equals("zombie") && password.equals("zombie")){
                     message = "Bienvenido " + username;
                     api_key = "39bcb851cb7feff61f320308ffcb0fd01ba3747b2565efc54ba12f2e15f2e67e";
                 } else if(username.equals("humano") && password.equals("humano")){
                     message = "Bienvenido " + username;
                     api_key = "649cdcef449c02a61d313dbb64bcb0aa07e7cc14adefa2a6082c3d7b7b486c9b";
                 } else if(username.equals("paladin") && password.equals("paladin")){
                     message = "Bienvenido " + username;
                     api_key = "02782ecdedf101acbb418093ee632478002614479bbe17cae87ffdb7e90e3e12";
                 } else if(username.equals("szombie") && password.equals("szombie")){
                     message = "Bienvenido " + username;
                     api_key = "47d9e1e61497c63c910519ae176afa2848472aecd6d9623891ebf606000b5ed4";
                 } else {
                     message = "Usuario y password incorrectos";
                 }

                 Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                 if(api_key != null){
                     Intent i = new Intent(getApplicationContext(),MainActivity.class);
                     i.putExtra("apiKey",api_key);
                     startActivity(i);
                 }
             }
         });

    }

    public class postLogin extends AsyncTask<Object, Void, String> {
        private final String LOG_TAG = postLogin.class.getSimpleName();
        public InputStream is = null;
        String forecastJsonStr;

        String username;
        String password;

        public postLogin(String username,String password) {
            this.username = username;
            this.password = password;
        }

//        @Override
//        protected void onPreExecute() {
//            this.dialog.setMessage("Cargando"
////                    context.getResources().getString(R.string.loading)
//            );
//            this.dialog.show();
//
//        }

        @Override
        protected String doInBackground(Object... objects) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                final java.lang.String FORECAST_BASE_URL = "http://one.hackiot.com:8080/riot-core-services/api/user/login";
                Uri buildURI = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
                URL url = new URL(buildURI.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept","application/json");
                JSONObject logi = new JSONObject();
                logi.put("username",username);
                logi.put("password",password);
                urlConnection.connect();
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(logi.toString());
                out.close();
                int HttpResult =urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    java.lang.String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(""+sb.toString());
                    return (String )sb.toString();
                }else{
                    System.out.println(urlConnection.getResponseMessage());
                    return (String)urlConnection.getResponseMessage().toString();

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
        protected void onPostExecute(String result) {
//            if (dialog != null && dialog.isShowing())
//                dialog.dismiss();
              System.out.println("result " +result);
        }
    }

//    public class BaseTask<String> extends AsyncTask<Object,Void, String> {
//        public Context context;
//        public ProgressDialog dialog;
//
//        private final String LOG_TAG = (String)BaseTask.class.getSimpleName();
//        public InputStream is = null;
//        String forecastJsonStr;
//
//        String username;
//        String password;
//
//        public BaseTask(Context context){
//            this.context = context;
//            this.dialog = new ProgressDialog(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setMessage(
//                    "Cargando"
//            );
//            this.dialog.show();
//        }
//
////        @Override
//        protected String doInBackground(String... params) {
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            StringBuilder sb = new StringBuilder();
//            try {
//                String FORECAST_BASE_URL = (String)"http://one.hackiot.com:8080/riot-core-services/api/user/login";
//                Uri buildURI = Uri.parse(FORECAST_BASE_URL.toString()).buildUpon().build();
//                URL url = new URL(buildURI.toString());
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept","application/json");
//                JSONObject logi = new JSONObject();
//                logi.put("username",username);
//                logi.put("password",password);
//                urlConnection.connect();
//                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
//                out.write(logi.toString());
//                out.close();
//                int HttpResult =urlConnection.getResponseCode();
//                if(HttpResult == HttpURLConnection.HTTP_OK){
//                    BufferedReader br = new BufferedReader(new InputStreamReader(
//                            urlConnection.getInputStream(),"utf-8"));
//                    String line = null;
//                    while ((line = (String)br.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    br.close();
//                    System.out.println(""+sb.toString());
//                    return (String)sb.toString();
//                }else{
//                    System.out.println(urlConnection.getResponseMessage());
////                    setRes(urlConnection.getResponseMessage());
//                    return (String)urlConnection.getResponseMessage().toString();
//
//                }
//
//
//            } catch (IOException e) {
//                Log.e("PlaceholderFragment", "Error ", e);
//                forecastJsonStr = null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally{
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("PlaceholderFragment", "Error closing stream", e);
//                    }
//                }
//            }
//            return  null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (dialog != null && dialog.isShowing()){
//                dialog.dismiss();
//            }
//
//        }
//    }

    public class MyTask extends UrlJsonAsyncTask  {
        // Create a default constructor
        public MyTask(Context context,String username,String password) {
            super(context,username,password);
        }

        public void validateJson(JSONObject json){
            if (json != null) {

            }
        }

        // onPostExecute(JSONObject) is what is called when the UrlJsonAsyncTask completes.
        // The JSONObject represents the JSON returned by the remote URL we queried.
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // call validateJson() to ensure well formatted JSON
                this.validateJson(json);

                // If the JSON is returned successfully and well formatted, send the
                // JSON "data" as String Extra "json" to SecondActivity
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("apiKey", json.getString("apiKey").toString());
                startActivity(intent);
            } catch (JSONException e) {
                // If there were exceptions handling the JSON...
                Toast.makeText(context, this.getMessageError(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // All other uncaught exceptions, like HTTP or IO problems...
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                // You MUST call this or your UrlJsonAsyncTask's ProgressDialog will
                // not close.
                super.onPostExecute(json);
            }
        }
    }
}
