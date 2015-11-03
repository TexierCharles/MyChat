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
import com.google.gson.Gson;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.UUID;

public class NewMessageActivity extends Activity {

    Button button;
    private static final String API_BASE_URL_V2 = "http://training.loicortola.com/chat-rest/2.0";
    private static final String TAG = NewMessageActivity.class.getSimpleName();

    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    public static final String FROM = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    private SendMessage sendMessageTask;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        // Retrieve login extra passed from previous activity
        String login = "";
        String password = "";

        if(getIntent().getStringExtra(("caller")).equals("list"))
        {
            login = getIntent().getStringExtra(("login"));
            password = getIntent().getStringExtra(("password"));

        }
        else {
            login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);

        }
        String from = getString(R.string.from, login);




        TextView welcomeText = (TextView) findViewById(R.id.from);
        welcomeText.setText(from);

        message = (EditText) findViewById(R.id.newMessage1);
        addListenerOnButton(message);
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
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
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

                if(getIntent().getStringExtra(("caller")).equals("list"))
                {
                    sendMessageTask.execute(getIntent().getStringExtra(("login")), getIntent().getStringExtra("password"), messageStr);

                }
                else {
                    sendMessageTask.execute(getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN), getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD), messageStr);
                }
            }
        });

    }

    private class SendMessage extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String message = params[2];

            OkHttpClient client = new OkHttpClient();

            UUID uuid = UUID.randomUUID();
            Message msg = new Message(uuid.toString(), message, username);

            // Gson converti un objet Java dans sa représentation JSON et vice versa.
            Gson gson = new Gson();
            String json = gson.toJson(msg);

            String url = new StringBuilder(API_BASE_URL_V2 + "/messages/").toString();

            String credential = Credentials.basic(username, password);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", credential)
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
            Toast.makeText(getApplicationContext(), "Message sent successfully !", Toast.LENGTH_LONG).show();
            if(success){
                finish();
            }
        }
    }
}