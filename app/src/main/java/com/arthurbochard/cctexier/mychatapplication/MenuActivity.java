package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Retrieve views
        TextView welcomeText = (TextView) findViewById(R.id.welcome_text);

        // Retrieve login extra passed from previous activity
        String login = getIntent().getStringExtra(MainActivity.EXTRA_LOGIN);

        // Retrieve string from resources
        String welcomeMsg = getString(R.string.welcome_msg, login);

        // Set welcome text into text view
        welcomeText.setText(welcomeMsg);
    }
}
