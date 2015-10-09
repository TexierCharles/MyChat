package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class NewMessageActivity extends Activity {

    Button button;
    private static final String API_BASE_URL = "http://formation-android-esaip.herokuapp.com";
    private SendMessage sendMessageTask;
    private static final String TAG = NewMessageActivity.class.getSimpleName();

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        TextView welcomeText = (TextView) findViewById(R.id.from);

        // Retrieve login extra passed from previous activity
        String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN2);

        Log.e("tag",login);

        // Retrieve string from resources
        String from = getString(R.string.from, login);

        // Set welcome text into text view
        welcomeText.setText(from);

        addListenerOnButton();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_message, menu);
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

    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                switch (arg0.getId()) {
                    case R.id.button1:
                        sendMessageTask = new SendMessage();
                        sendMessageTask.execute("");
                        break;
                }

                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);

            }


        });

    }


    private class SendMessage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            // Here, call the login webservice
            HttpClient client = new DefaultHttpClient();
            String url = new StringBuilder(API_BASE_URL + "/connect/")
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
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return "true";
                }
            } catch (IOException e) {
                Log.w(TAG, "Exception occured while logging in: " + e.getMessage());
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.output);
            txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }






}
