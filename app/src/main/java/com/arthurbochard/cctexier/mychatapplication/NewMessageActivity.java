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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewMessageActivity extends Activity {

    Button button;
    private static final String API_BASE_URL = "http://formation-android-esaip.herokuapp.com";
    private static final String API_BASE_URL_V1 = "http://training.loicortola.com/chat-rest/1.0";
    private SendMessage sendMessageTask;
    private static final String TAG = NewMessageActivity.class.getSimpleName();
    private EditText message;


    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    public static final String FROM = "";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");



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
            OkHttpClient client = new OkHttpClient();

            // Webservice URL
            String url = new StringBuilder(API_BASE_URL_V1 + "/messages/")
                    .append(username)
                    .append("/")
                    .append(password)
                    .toString();

            Log.e("byche en string leopard",url);

            UUID uuid = UUID.randomUUID();
            Log.i("uuid",uuid.toString());

            // Request
            try {
                // FIXME to be removed. Simulates heavy network workload
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }




            Message msg = new Message(uuid.toString(),message,username);

            Gson gson = new Gson();
            String json = gson.toJson(msg);

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.code()==200;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Toast.makeText(getApplicationContext(), "Message sent successfully !", Toast.LENGTH_LONG).show();
        }

    }






}
