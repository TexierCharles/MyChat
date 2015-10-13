package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_BASE_URL = "http://formation-android-esaip.herokuapp.com";
    private static final String API_BASE_URL_V1 = "http://training.loicortola.com/chat-rest/1.0";
    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    public static final String FROM = "MainActivity";

    private EditText username;
    private EditText password;
    private Button resetBtn;
    private Button submitBtn;
    private ProgressBar progressBar;

    private LoginTask loginTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Retrieve views from XML
        resetBtn = (Button) findViewById(R.id.reset_button);
        submitBtn = (Button) findViewById(R.id.submit_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Set on click listener on reset button
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset username text to empty
                username.setText("");
                password.setText("");
                // Create Toast message (context, string resource, length)
                Toast message = Toast.makeText(MainActivity.this, R.string.form_reset, LENGTH_LONG);
                // Show Toast message
                message.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                // Cancel previous task if it is still running
                if (loginTask != null && loginTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    loginTask.cancel(true);
                }
                // Launch Login Task
                loginTask = new LoginTask();
                loginTask.execute(usernameStr, passwordStr);
            }
        });
    }

    @Override
    protected void onPause() {
        if (loginTask != null) {
            loginTask.cancel(true);
        }
        super.onPause();
    }

    /**
     * LoginTask: AsyncTask to process authentication.
     * FYI: AsyncTask takes three generic types: Params, Progress, Result
     */
    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // Here, show progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * @param params [login, password]
         * @return true if login succeeded, false otherwise
         */
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            // Here, call the login webservice
            HttpClient client = new DefaultHttpClient();

            // Webservice URL
            String url = new StringBuilder(API_BASE_URL_V1 + "/connect/")
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

            Log.i("url",url);

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
            // Here, hide progress bar and proceed to login if OK.
            progressBar.setVisibility(View.GONE);

            // Wrong login entered
            if (!success) {
                Toast.makeText(MainActivity.this, R.string.login_error, LENGTH_LONG).show();
                return;
            }

            // Everything good!
            Toast.makeText(MainActivity.this, R.string.login_success, LENGTH_LONG).show();

            // Declare activity switch intent
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra(EXTRA_LOGIN, username.getText().toString());
            intent.putExtra(EXTRA_PASSWORD, password.getText().toString());
            intent.putExtra(FROM, "MainActivity");


            // Start activity
            startActivity(intent);
            // If you don't want the current activity to be in the backstack,
            // uncomment the following line:
            // finish();
        }
    }

}
