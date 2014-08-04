package com.ryan.phspowerschool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class DownloadGradeHomePage extends Activity {
    /**
     * Written by Ryan D'souza
     * Downloads HTML Code of main page on PS
     * That contains all classes + all grades over all quarters
     * Goe to VIEWALLGRADES.JAVA
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Normal loading page
        setContentView(R.layout.activity_main);

        final TextView loading = (TextView) findViewById(R.id.loadingTV);
        final Button problemButton = (Button) findViewById(R.id.problemButton);
        final Context theContext = this;

        problemButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                startActivity(new Intent(DownloadGradeHomePage.this, OpenInBrowser.class).
                        putExtra("URL", "https://pschool.princetonk12.org/public/home.html"));
            }
        });

        final WebBrowser theWebBrowser = new WebBrowser(theContext);
        theWebBrowser.getSettings().setJavaScriptEnabled(true);
        theWebBrowser.getSettings().setSaveFormData(false);
        //noinspection deprecation
        theWebBrowser.getSettings().setSavePassword(false);
        theWebBrowser.setSaveEnabled(false);
        theWebBrowser.loadUrl("https://pschool.princetonk12.org/guardian/home.html");
        setContentView(theWebBrowser);

        theWebBrowser.addSourceCodeListener(new SourceCodeListener() {
            @Override
            public void onSourceCodeAcquired(android.webkit.WebView webView, final String html) {
                //copyToClipboard(" Just finished @DGHP Acq: " + html, true);

                if (html.contains("Invalid Username")) {
                    Intent invalid = new Intent(DownloadGradeHomePage.this, UsernamePassword.class);
                    invalid.putExtra("error", "Invalid Username or Password");
                    startActivity(invalid);
                    finish();
                }

                if (html.contains("Attendance By Class")) {
                    Intent r = new Intent(DownloadGradeHomePage.this, ViewAllGrades.class);
                    r.putExtra("html", html);
                    r.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loading.setText("Done!");
                    startActivity(r);
                    //copyToClipboard("DOWNLOAD GRADE HOME PAGE: " + html, true);
                    theWebBrowser.destroy();
                    webView.destroy();
                    finish();
                }

                //if(time/1000 >= 15000) startActivity(new Intent(DownloadGradeHomePage.this, UsernamePassword.class).putExtra("error", "Error getting grades"));

            }

            @Override
            public void onProgressChanged(android.webkit.WebView webView, int progress) {
                if (progress == 89)
                    loading.setText("Please be patient " + progress + " %. I'm not frozen");
                else if (progress > 85 && progress < 90 && progress != 89)
                    loading.setText("Please be patient " + progress + " %. PowerSchool is very slow & annoying");
                else if (progress > 50)
                    loading.setText("Still Downloading " + progress + " %");
                else
                    loading.setText("Downloading " + progress + " %");
                loading.setTextColor(Color.BLUE);

                if (progress == 100) {
                    final long startTime = System.currentTimeMillis();

                    loading.setText("Almost done. Hang on, we're loading the last page. " +
                            "Note: This page might appear longer then the rest, but that doesn't mean I'm frozen");

                    if (startTime / 1000 > 12) {
                        /*loading.setText("Still loading. We have reason to believe that " +
                                "PowerSchool is being slow, your Internet connection is weak, you logged on recently, or there is another issue.\n" +
                                "In any case, please wait a bit more or press the button below to open PowerSchool in the browser. " +
                                "Your grades will still pop up when the page finishes loading");*/
                        loading.setText("Still loading. Please hang on a bit longer");
                        loading.setTextSize(20);
                    }
                }
            }
        });


        loading.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                loading.setText("If this page appears for over 20 seconds and you have good Internet connection, " +
                        "an invalid username/password might have been entered, or the account might already be logged in. " +
                        "If either is the case, please long-tap this box to go back to the main page where you can " +
                        " press the open WebBrowser button to open PowerSchool in the WebBrowser. Sorry for the inconvenience");
                loading.setTextSize(20);
                loading.setTextColor(android.graphics.Color.RED);
            }
        });


        //If view long-held, open in WebBrowser
        loading.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View view) {
                Intent toWebBrowser = new Intent(DownloadGradeHomePage.this, UsernamePassword.class);
                startActivity(toWebBrowser);
                finish();
                return false;
            }
        });


        /*final TextView theCreators = (TextView) findViewById(R.id.creatorRyan);

        theCreators.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ImageView image = new ImageView(theContext);
                image.setImageResource(R.drawable.ryan_dsouza_picture);

                final String message = "Ryan is a Junior at Princeton High School " +
                        "and is interested in Computer Science & Economics. " +
                        "He is currently working on his Eagle Scout Project and blogs at dsouzarc.wordpress.com . " +
                        "You can find him in Mrs. Elia's room at school.";
                //theCreators.setText(message);
                theWebBrowser.reload();

                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(theContext).
                                setTitle("About the Coders").
                                setView(image).setMessage(message).setPositiveButton("Done", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                //theWebBrowser.reload();
                                dialogInterface.dismiss();
                            }
                        });
                //builder.create().show();
                builder.show();
            }
        });*/
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public void copyToClipboard(final String message, final boolean toNotOverRide) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            @SuppressWarnings("deprecation") android.text.ClipboardManager clipboard =
                    (android.text.ClipboardManager) getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            clipboard.setText(message);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(android.content.Context.CLIPBOARD_SERVICE);

            android.content.ClipData oldStuff;
            try {
                oldStuff = clipboard.getPrimaryClip();
            } catch (Exception e) {
                e.printStackTrace();
                oldStuff = android.content.ClipData.newPlainText("text label", "");
            }

            if (oldStuff == null)
                oldStuff = android.content.ClipData.newPlainText("text label", "");

            android.content.ClipData clip;
            if (toNotOverRide)
                clip = android.content.ClipData.newPlainText("text label", message + oldStuff.toString());
            else
                clip = android.content.ClipData.newPlainText("text label", message);

            clipboard.setPrimaryClip(clip);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_grades, menu);
        return true;
    }
}