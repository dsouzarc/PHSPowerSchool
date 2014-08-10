package com.ryan.phspowerschool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LoggingIntoPowerSchool extends Activity
{

    private static final String prefName = "PSCHOOL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Context theContext = this;
        final Button problemButton = (Button) findViewById(R.id.problemButton);
        final TextView powerSchoolButton = (TextView) findViewById(com.ryan.phspowerschool.R.id.powerSchoolTV);
        final TextView loadingTV = (TextView) findViewById(R.id.loggingInTV);

        //If username/password is already saved, show it
        final String[] userPass = new String[2];
        final Intent getUserNamePassword = new Intent(LoggingIntoPowerSchool.this, UsernamePassword.class);

        /**Gets stored Username and Password */
        try {
            SharedPreferences settings = getSharedPreferences(prefName, 0);
            String username = settings.getString("username", "");
            String password = settings.getString("password", "");

            if (username.length() <= 3 || username == null) {
                getUserNamePassword.putExtra("error", "No previously saved username");
                startActivity(getUserNamePassword);
            } else if (password.length() <= 3 || password == null) {
                getUserNamePassword.putExtra("error", "No previously saved password");
                startActivity(getUserNamePassword);
            } else {
                userPass[0] = username;
                userPass[1] = password;
            }
        } catch (Exception e) {
            userPass[0] = "";
            userPass[1] = "";
            Intent i = new Intent(LoggingIntoPowerSchool.this, UsernamePassword.class);
            i.putExtra("error", "No saved Username or Password" + e.toString());
            startActivity(i);
        }

        try {
            if (userPass[0] == null || userPass[1] == null) {
                startActivity(getUserNamePassword.putExtra("error", "Invalid Username or Password"));
                finish();
            }
        } catch (Exception e) {
            startActivity(getUserNamePassword.putExtra("error", "Invalid Username or Password"));
            finish();
        }

        final String userName = userPass[0];
        final String userPassword = userPass[1];

        final boolean[] firstTime = {false};

        if((userPass[0].length() >= 4) && (userPass[1].length() >= 4))
        {
            //For the WebPage
            final WebView web = new WebView(this);
            web.clearHistory();
            web.clearCache(true);
            web.clearFormData();
            web.getSettings().setSaveFormData(false);
            web.getSettings().setSavePassword(false);
            web.setSaveEnabled(false);
            web.getSettings().setJavaScriptEnabled(true);
            web.setWebChromeClient(new WebChromeClient()
            {
                @Override
                public boolean onConsoleMessage(ConsoleMessage cmsg)
                {
                    final String theHTML = cmsg.message();
                    ArrayList<Subject> thePage;

                    try
                    {
                        thePage = HTMLParser.getFrontPageGrades(theHTML);
                        if(thePage.size() > 3)
                            toMoveOn(theHTML);
                    }
                    catch (Exception e) { e.printStackTrace(); }
                    // check secret prefix
                    if (theHTML.startsWith("MAGIC"))
                    {
                        thePage = HTMLParser.getFrontPageGrades(theHTML);
                        try
                        {
                            if(theHTML.contains("Current Year Schedule"))
                            {
                                toMoveOn(theHTML);
                                loadingTV.setText("All Done!");
                                startActivity(new Intent(LoggingIntoPowerSchool.this, ViewAllGrades.class).putExtra("html", theHTML));
                            }
                        }

                        catch (Exception e)
                        {
                            loadingTV.setText("CMG CATCH: Loading...: " + e.toString());
                            if(theHTML.contains("Current Year Schedule"))
                                toMoveOn(theHTML);
                        }

                        if(thePage.size() > 3)
                            toMoveOn(theHTML);
                        return true;
                    }
                    return false;
                }

                public void onProgressChanged(WebView view, int progress) {
                    loadingTV.setTextColor(Color.BLUE);
                    if(!firstTime[0])
                        loadingTV.setText("Going to PowerSchool " + progress + "%");
                    if(firstTime[0])
                        loadingTV.setText("Logging into PowerSchool " + progress + "%");
                    if(firstTime[0] && progress == 10)
                        loadingTV.setText("Entering username and password " + progress + "%");
                    if(firstTime[0] && progress > 85 && progress < 90)
                        loadingTV.setText("Don't Worry. We're not frozen. " + progress + "%");
                    if(progress == 100)
                        firstTime[0] = true;
                    if(progress == 100 && firstTime[0])
                        loadingTV.setText("Finished!");
                }
            });

            // inject the JavaScript on page load
            web.setWebViewClient(new android.webkit.WebViewClient() {
                public void onPageFinished(WebView view, String address)
                {
                    view.loadUrl("javascript:(function(){document.getElementById('fieldAccount').value='" + userName + "';})()");
                    view.loadUrl("javascript:(function(){document.getElementsByName('pw')[0].value='" + userPassword + "';})()");
                    view.loadUrl("javascript:(function(){document.getElementById('btn-enter').click();})()");
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                }
            });

            web.loadUrl("https://pschool.princetonk12.org/public/home.html");

            powerSchoolButton.setOnLongClickListener(new android.view.View.OnLongClickListener() {
                @Override
                public boolean onLongClick(android.view.View view) {
                    web.reload();

                    return false;
                }
            });
        }

        loadingTV.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View view) {
                loadingTV.setTextColor(Color.RED);
                loadingTV.setText("If this page persists for a long time and your Internet Connection is good, " +
                    "the username/password entered might be wrong. You can always press the \"Click to Open In Browser\" button below " +
                    " or long tap the blue PowerSchool text to refresh this process");

                return false;
            }
        });

        final TextView theCreators = (TextView) findViewById(R.id.creatorRyan);
        theCreators.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ImageView image = new ImageView(theContext);
                image.setImageResource(R.drawable.ryan_dsouza_picture);

                final String message = "Ryan is a Junior at Princeton High School " +
                        "and is interested in Computer Science & Economics. " +
                        "He is currently working on his Eagle Scout Project and blogs at dsouzarc.wordpress.com . " +
                        "You can find him in Mrs. Elia's room at school.";


                final android.app.AlertDialog.Builder builder =
                        new android.app.AlertDialog.Builder(theContext).
                                setTitle("About the Coders").
                                setView(image).setMessage(message).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            }
        });

        final TextView andrew = (TextView) findViewById(R.id.creatorAndrew);
        andrew.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ImageView image = new ImageView(theContext);
                image.setImageResource(com.ryan.phspowerschool.R.drawable.andrew_barry_picture);
                final String message = "Andrew is also a Junior at Princeton HS " +
                        "who is interested in Computer science, but not Economics. " +
                        "He is not an Eagle Scout, and he does not run a WordPress blog." +
                        "However, you can still find him at Mrs. Elia's Room at school.";


                final android.app.AlertDialog.Builder builder =
                        new android.app.AlertDialog.Builder(theContext).
                                setTitle("About the Coders").
                                setView(image).setMessage(message).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            }
        });

        final TextView ben = (TextView) findViewById(com.ryan.phspowerschool.R.id.creatorBen);
        ben.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ImageView image = new ImageView(theContext);
                image.setImageResource(com.ryan.phspowerschool.R.drawable.ben_grass_picture);

                final String message = "Ben is a Junior at Princeton High School and is interested in Computer Science. " +
                        "He enjoys playing baseball, reading about the performance and ordeals of his " +
                        "favorite Toronto based sports teams (Blue Jays and Raptors), and loves tinkering " + "" +
                        "with often pointless computer programs. He can be found aimlessly wandering the halls of " +
                        "PHS or being yelled at by the name \"Vikram Patel\"";

                final android.app.AlertDialog.Builder builder =
                        new android.app.AlertDialog.Builder(theContext).
                                setTitle("About the Coders").
                                setView(image).setMessage(message).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            }
        });

        problemButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent problem = new Intent(LoggingIntoPowerSchool.this, OpenInBrowser.class);
                startActivity(problem);
            }
        });
    }

    private void toMoveOn(final String HTML)
    {
        ArrayList<Subject> theClasses = HTMLParser.getFrontPageGrades(HTML);

        if(theClasses.size() > 0)
        {
            Intent theIntent = new Intent(LoggingIntoPowerSchool.this, ViewAllGrades.class);
            theIntent.putExtra("html", HTML);
            startActivity(theIntent);
            finish();
        }
    }

    public static String capitalizeString(String string) {
        boolean found = false;

        string = string.replace("Iii", "III");
        string = string.replace("Ii", "II");
        string = string.replace("iii", "III");
        string = string.replace("ii", "II");

        char[] chars = string.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;

            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public void copyToClipboard(final String message) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard =
                    (android.text.ClipboardManager) getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            clipboard.setText(message);
        }
        else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            android.content.ClipData oldStuff;

            try { oldStuff = clipboard.getPrimaryClip(); }
            catch (Exception e) { oldStuff = android.content.ClipData.newPlainText("text label", ""); }

            if (oldStuff == null)
                oldStuff = android.content.ClipData.newPlainText("text label", "");

            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", message);

            clipboard.setPrimaryClip(clip);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
