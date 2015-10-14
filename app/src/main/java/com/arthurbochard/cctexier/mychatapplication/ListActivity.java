package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListActivity extends android.app.ListActivity {

    private Menu optionsMenu;
    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String API_BASE_URL_V2 = "http://training.loicortola.com/chat-rest/2.0";
    private List<Message> listMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
        String password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

        new GetMessagesFromServer().execute(login, password);

    }

    public void onReloadAdapter(List<Message> values) {

        MessageAdapter messageAdapter = new MessageAdapter(this, values);

        setListAdapter(messageAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.messages_menuRefresh:

                String login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
                String password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

                setRefreshActionButtonState(true);
                new GetMessagesFromServer().execute(login, password);
                //setRefreshActionButtonState(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetMessagesFromServer extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getApplicationContext(), "Loading messages", Toast.LENGTH_LONG).show();
        }

       /* @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar
           mProgressBar.setProgress(values[0]);
        }*/

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            // Here, call the login webservice

            OkHttpClient client = new OkHttpClient();

            String url = new StringBuilder(API_BASE_URL_V2 + "/messages?&limit=100&offset=0").toString();

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



            String responseStrNew = null;
            try {
                responseStrNew = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Type listType = new TypeToken<ArrayList<Message>>() {
            }.getType();

            Gson gson = new Gson();
            listMessages = gson.fromJson(responseStrNew, listType);

            //messages les plus recents en haut de la liste
            listMessages = Lists.reverse(listMessages);

            if (status == 200) {
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            onReloadAdapter(listMessages);
            //Toast.makeText(getApplicationContext(), "Messages loaded", Toast.LENGTH_LONG).show();
            setRefreshActionButtonState(false);
        }
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.messages_menuRefresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


}
