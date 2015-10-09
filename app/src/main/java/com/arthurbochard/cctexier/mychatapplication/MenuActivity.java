package com.arthurbochard.cctexier.mychatapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity {

    Button button1;
    Button button2;
    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Retrieve views
        TextView welcomeText = (TextView) findViewById(R.id.welcome_text);

        String login = "";
        String password = "";

        // Retrieve login extra passed from previous activity
        if(getIntent().getStringExtra(MainActivity.FROM)!= null)
        {
            login = getIntent().getStringExtra(MainActivity.EXTRA_LOGIN);
            password = getIntent().getStringExtra(MainActivity.EXTRA_PASSWORD);
        }
        if(getIntent().getStringExtra(NewMessageActivity.FROM)!= null)
        {
            login = getIntent().getStringExtra(MainActivity.EXTRA_LOGIN);
            password = getIntent().getStringExtra(MainActivity.EXTRA_PASSWORD);
        }

        // Retrieve string from resources
        String welcomeMsg = getString(R.string.welcome_msg, login, password);

        // Set welcome text into text view
        welcomeText.setText(welcomeMsg);

        addListenerOnButton1(login,password);
        addListenerOnButton2(login,password);



    }

        public void addListenerOnButton1(final String login, final String psw) {

            final Context context = this;

            button1 = (Button) findViewById(R.id.button1);

            button1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(context, NewMessageActivity.class);
                    intent.putExtra(EXTRA_LOGIN, login);
                    intent.putExtra(EXTRA_PASSWORD, psw);
                    startActivity(intent);




                }

            });

        }



    public void addListenerOnButton2(final String login, final String psw) {

        final Context context = this;

        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra(EXTRA_LOGIN, login);
                intent.putExtra(EXTRA_PASSWORD, psw);
                startActivity(intent);

            }

        });

    }
}
