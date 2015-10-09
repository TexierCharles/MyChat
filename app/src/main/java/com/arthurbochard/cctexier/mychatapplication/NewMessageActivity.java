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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class NewMessageActivity extends Activity {

    Button button;
    private static final String API_BASE_URL = "http://formation-android-esaip.herokuapp.com";
    private SendMessage sendMessageTask;
    private static final String TAG = NewMessageActivity.class.getSimpleName();
    private EditText message;
    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    public static final String FROM = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        TextView welcomeText = (TextView) findViewById(R.id.from);

        // Retrieve login extra passed from previous activity
        String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
        String password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);


        // Retrieve string from resources
        String from = getString(R.string.from, login);

        // Set welcome text into text view
        welcomeText.setText(from);

        message = (EditText) findViewById(R.id.newMessage1);

        addListenerOnButton(message);

    }

    @Override
    protected void onPause() {

        super.onPause();

        String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
        String password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

        Intent intent = new Intent(NewMessageActivity.this, MenuActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        intent.putExtra(EXTRA_PASSWORD, password);
        intent.putExtra(FROM, "NewMessageActivity");
        startActivity(intent);
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

    public void addListenerOnButton(EditText message) {

        final Context context = this;


        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                        EditText message = (EditText) findViewById(R.id.newMessage1);
                        String messageStr = message.getText().toString();
                        sendMessageTask = new SendMessage();
                        sendMessageTask.execute(getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN),getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD),messageStr);



                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);
        }



        });

    }


    private class SendMessage extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            String username = params[0];
            String password = params[1];
            String message = params[2];

            // Here, call the login webservice
            HttpClient client = new DefaultHttpClient();

            // Webservice URL
            String url = new StringBuilder(API_BASE_URL + "/message/")
                    .append(username)
                    .append("/")
                    .append(password)
                    .append("/")
                    .append(message)
                    .toString();

            Log.e("byche en string leopard",url);

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
                    return true;
                }
            } catch (IOException e) {
                Log.w(TAG, "Exception occured while logging in: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }

    }






}
