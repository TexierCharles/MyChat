package com.arthurbochard.cctexier.mychatapplication.UI.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arthurbochard.cctexier.mychatapplication.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;

public class RegisterActivity extends Activity {


    private static final String API_BASE_URL_V2 = "http://training.loicortola.com/chat-rest/2.0";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText username;
    private EditText password;
    private Button resetBtn;
    private Button submitBtn;
    private ProgressBar progressBar;
    private RegisterTask registerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        resetBtn = (Button) findViewById(R.id.reset_button);
        submitBtn = (Button) findViewById(R.id.submit_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset username text to empty
                username.setText("");
                password.setText("");
                Toast message = Toast.makeText(RegisterActivity.this, R.string.form_reset, LENGTH_LONG);
                message.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                // Cancel previous task if it is still running
                if (registerTask != null && registerTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    registerTask.cancel(true);
                }
                // Launch Login Task
                registerTask = new RegisterTask();
                registerTask.execute(usernameStr, passwordStr);
            }
        });
    }

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {

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

            // using StringBuilder to avoid a lot of string creation
            StringBuilder jsonToSend = new StringBuilder().append("{\"login\":\"").append(username).append("\",\"password\":\"").append(password).append("\"}");
            OkHttpClient client = new OkHttpClient();

            String url = new StringBuilder(API_BASE_URL_V2 + "/register").toString();
            RequestBody body = RequestBody.create(JSON, jsonToSend.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.code() == 200;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // Here, hide progress bar and proceed to login if OK.
            progressBar.setVisibility(View.GONE);

            // Wrong login entered
            if (!success) {
                Toast.makeText(RegisterActivity.this, R.string.register_error, LENGTH_LONG).show();
                return;
            }

            // Everything good!
            Toast.makeText(RegisterActivity.this, R.string.register_success, LENGTH_LONG).show();
            finish();
        }
    }

}
