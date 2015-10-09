package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ListActivity extends android.app.ListActivity {


    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String API_BASE_URL = "http://formation-android-esaip.herokuapp.com";
    private String[] mStrings = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
        String password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

        new GetMessagesFromServer().execute(login, password);

    }


    public void onReloadAdapter(String[] mStrings)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);

        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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


    private class GetMessagesFromServer extends AsyncTask<String, Void, Boolean>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }

       /* @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar
           mProgressBar.setProgress(values[0]);
        }*/

        @Override
        protected Boolean doInBackground(String... params)
        {
            String username = params[0];
            String password = params[1];

            // Here, call the login webservice
            HttpClient client = new DefaultHttpClient();

            // Webservice URL
            String url = new StringBuilder(API_BASE_URL + "/messages/")
                    .append(username)
                    .append("/")
                    .append(password)
                    .toString();

            // Request
            try {
                // FIXME to be removed. Simulates heavy network workload
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            HttpGet loginRequest = new HttpGet(url);
            try {
                HttpResponse response = client.execute(loginRequest);

                String responseStr = EntityUtils.toString(response.getEntity());
                Log.e("Byche en String",responseStr);

                mStrings=responseStr.split(";");


                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return true;
                }
            } catch (IOException e) {
                Log.w(TAG, "Exception occured while logging in: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            onReloadAdapter(mStrings);
            Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }
    }



}
