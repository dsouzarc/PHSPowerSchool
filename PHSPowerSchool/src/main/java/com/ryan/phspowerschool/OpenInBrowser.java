package com.ryan.phspowerschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class OpenInBrowser extends Activity {
    private static final String prefName = "PSCHOOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_in_browser);

        try {
            android.content.SharedPreferences settings = getSharedPreferences(prefName, 0);
            String username = settings.getString("username", "");
            String password = settings.getString("password", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final android.app.AlertDialog r = new android.app.AlertDialog.Builder(this)
                .setTitle("Loading PowerSchool home page")
                .setMessage("Downloading " + 0)
                .show();

        WebView theWeb = (WebView) findViewById(R.id.someThingWrongWebView);
        theWeb.getSettings().setJavaScriptEnabled(true);
        theWeb.setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public void onProgressChanged(android.webkit.WebView webView, int progress) {
                r.setMessage("Downloading " + progress + "%");

                if(progress > 50)
                    try { r.cancel(); } catch (Exception e) { e.printStackTrace(); }
            }
        });
        theWeb.setWebViewClient(new android.webkit.WebViewClient()
        {
            public void onPageFinished(WebView view, String address)
            {
                r.dismiss();
            }
        });

        //If there is a URL, open it. Else, open main page anyway
        try {
            theWeb.loadUrl(getIntent().getExtras().getString("URL"));
        } catch (Exception e) {
            theWeb.loadUrl("https://pschool.princetonk12.org/public/home.html");
        }

        TextView theView = (TextView) findViewById(R.id.somethingWrongTV);
        theView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                final String[] emailList = {"dsouzarc@gmail.com"};
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);
                startActivity(Intent.createChooser(emailIntent, "Report glitch by email"));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_in_browser, menu);
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
