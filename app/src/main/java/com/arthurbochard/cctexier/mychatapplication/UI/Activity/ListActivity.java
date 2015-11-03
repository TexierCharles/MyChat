package com.arthurbochard.cctexier.mychatapplication.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.arthurbochard.cctexier.mychatapplication.UI.Adapter.MessageAdapter;
import com.arthurbochard.cctexier.mychatapplication.Model.Message;
import com.arthurbochard.cctexier.mychatapplication.R;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends android.app.ListActivity {

    private Menu optionsMenu;
    private static final String API_BASE_URL_V2 = "http://training.loicortola.com/chat-rest/2.0";
    private List<Message> listMessages = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;

    public static String login="";
    public static String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                String loginX = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
                String passwordX = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

                setRefreshActionButtonState(true);
                new GetMessagesFromServer().execute(loginX, passwordX);
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        login = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
        password = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

        new GetMessagesFromServer().execute(login, password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        new GetMessagesFromServer().execute(login, password);

    }

    public void onReloadAdapter(List<Message> values) {

        MessageAdapter messageAdapter = new MessageAdapter(this, values, login);
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

                String loginX = getIntent().getStringExtra(MenuActivity.EXTRA_LOGIN);
                String passwordX = getIntent().getStringExtra(MenuActivity.EXTRA_PASSWORD);

                setRefreshActionButtonState(true);
                new GetMessagesFromServer().execute(loginX, passwordX);
                //setRefreshActionButtonState(false);
                return true;

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_new:
                final Context context = this;

                Intent intent = new Intent(context, NewMessageActivity.class);
                intent.putExtra("login", login);
                intent.putExtra("password", password);
                intent.putExtra("caller","list");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMessagesFromServer extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            // call the login webservice
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
