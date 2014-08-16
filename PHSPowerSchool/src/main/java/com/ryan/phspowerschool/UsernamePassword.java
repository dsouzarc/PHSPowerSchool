package com.ryan.phspowerschool;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Written by Ryan D'souza
 * Updates/Gets stored Username and Password
 */

public class UsernamePassword extends Activity {

    private static final String prefName = "PSCHOOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_username_password);
        setContentView(R.layout.username_password);

        //Buttons
        final Button save = (Button) findViewById(R.id.saveButton);
        final Button deleteButton = (Button) findViewById(R.id.deleteButton);
        final Button problem = (Button) findViewById(R.id.problemButton);

        //If username/password is already saved, show it
        try {
            SharedPreferences settings = getSharedPreferences(prefName, 0);
            String username = settings.getString("username", "");
            String password = settings.getString("password", "");

            EditText theText;
            theText = (EditText) findViewById(R.id.userNameED);
            theText.setText(username);
            theText = (EditText) findViewById(R.id.passwordED);
            theText.setText(password);
        }
        catch (Exception e) {}

        //If there is an error message like invalid username/password
        try {
            TextView errorMessage = (TextView) findViewById(R.id.errorMessageTV);
            Bundle extras = getIntent().getExtras();
            errorMessage.setText(extras.getString("error"));
            errorMessage.setVisibility(View.VISIBLE);
        }
        catch (Exception e) { }

        /**Delete & Clear stored data */
        deleteButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                SharedPreferences settings = getSharedPreferences(prefName, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", "");
                editor.putString("password", "");
                //editor.putBoolean("isStudent", false);
                // Commit the edits!
                editor.commit();
                Intent i = new Intent(UsernamePassword.this, UsernamePassword.class);
                startActivity(i);
            }
        });

        /**Save & Log in */
        save.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Valid Username and Password --> LoggingIntoPowerSchool downloader
                final Intent i = new Intent(UsernamePassword.this, LoggingIntoPowerSchool.class);

                //Invalid Username and Password --> UsernamePassowrd activity + error message
                final Intent invalid = new Intent(UsernamePassword.this, UsernamePassword.class).
                        putExtra("error", "Invalid Username or Password");

                //The input fields
                final EditText usernameET = (EditText) findViewById(R.id.userNameED);
                final EditText passwordET = (EditText) findViewById(R.id.passwordED);

                //Username
                String username = "";
                try {
                    username = usernameET.getText().toString();

                    if(username.equalsIgnoreCase("Bonn grades"))
                    {
                        startActivity(invalid.putExtra("error", "Doesn't work for you"));
                        finish();
                    }
                } catch (Exception e) {
                    startActivity(invalid);
                    finish();
                }

                //Password
                String password = "";
                try {
                    password = passwordET.getText().toString();
                } catch (Exception e) {
                    startActivity(invalid);
                    finish();
                }

                if (username.length() <= 2 || password.length() <= 2) {
                    startActivity(invalid);
                    finish();
                }

                final SharedPreferences settings = getSharedPreferences(prefName, 0);
                final SharedPreferences.Editor editor = settings.edit();

                //If different username, clear data in next Activity
                if (!settings.getString("username", "").equals(username))
                    i.putExtra("FromUsernamePassword", true);
                else
                    i.putExtra("FromUsernamePassword", false);

                //Save the username and password
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                editor.apply();

                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish();
            }
        });

        /**To open PS in Browser */
        problem.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent theIntent = new Intent(UsernamePassword.this, OpenInBrowser.class);
                theIntent.putExtra("URL", "https://pschool.princetonk12.org/public/home.html");
                startActivity(theIntent);
            }
        });

        /**Switch password to plain text */
        TextView passwordTV = (TextView) findViewById(R.id.passwordTV);
        passwordTV.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View view) {
                EditText thePass = (EditText) findViewById(R.id.passwordED);
                thePass.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.username_password, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
