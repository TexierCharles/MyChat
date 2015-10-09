package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity {

    Button button1;
    Button button2;


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

        addListenerOnButton1();
        addListenerOnButton2();



    }

        public void addListenerOnButton1() {

            final Context context = this;

            button1 = (Button) findViewById(R.id.button1);

            button1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(context, NewMessageActivity.class);
                    startActivity(intent);

                }

            });

        }



    public void addListenerOnButton2() {

        final Context context = this;

        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ListActivity.class);
                startActivity(intent);

            }

        });

    }
}
