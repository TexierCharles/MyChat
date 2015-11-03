package com.arthurbochard.cctexier.mychatapplication.UI.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arthurbochard.cctexier.mychatapplication.R;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity {

    private static final String API_BASE_URL_V2 = "http://training.loicortola.com/chat-rest/2.0";
    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    public static final String FROM = "MainActivity";

    private EditText username;
    private EditText password;
    private Button resetBtn;
    private Button submitBtn;
    private ProgressBar progressBar;
    private LoginTask loginTask;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        resetBtn = (Button) findViewById(R.id.reset_button);
        submitBtn = (Button) findViewById(R.id.submit_button);
        register = (Button) findViewById(R.id.register_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
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

            OkHttpClient client = new OkHttpClient();
            // Webservice URL
            String url = new StringBuilder(API_BASE_URL_V2 + "/connect/").toString();

            String credential = Credentials.basic(username, password);
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", credential)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            int status = response.code();

            if (status == 200) {
                return true;
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

            // Declare activity switch intent
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra(EXTRA_LOGIN, username.getText().toString());
            intent.putExtra(EXTRA_PASSWORD, password.getText().toString());
            intent.putExtra(FROM, "MainActivity");

            startActivity(intent);
        }
    }

}
