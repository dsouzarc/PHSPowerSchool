package com.ryan.phspowerschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAllGrades extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_grades);

        final Bundle stuff = getIntent().getExtras();
        final String HTMLCode = stuff.getString("html", "error in View All Grades");
        final GridLayout theGrid = (GridLayout) findViewById(R.id.allGradesGridLayout);
        final ArrayList<Subject> theSub = HTMLParser.getFrontPageGrades(HTMLCode);
        final Context theContext = this;
        copyToClipboard(HTMLCode, true);

        /*If there are no subjects, error message */
        AlertDialog theAlert = null;
        if (theSub.size() == 0) {
            theAlert = new android.app.AlertDialog.Builder(this)
                    .setTitle("Please Wait... PowerSchool is very slow")
                    .setMessage("Still loading... Please give us a few seconds " +
                            "This might happen if you accessed PowerSchool recently or are still logged in " +
                            "Or if you lost Internet connection")
                    .show();

            startActivity(new Intent(ViewAllGrades.this, LoggingIntoPowerSchool.class));
        }
        if (theAlert != null && theSub.size() > 0)
            theAlert.cancel();

        //To store data that will be used in Display
        String[] classes = new String[theSub.size()];
        String[] gradesQ1 = new String[theSub.size()];
        String[] gradesQ2 = new String[theSub.size()];
        String[] gradesQ3 = new String[theSub.size()];
        String[] gradesQ4 = new String[theSub.size()];
        String[] midterm = new String[theSub.size()];
        String[] finalExam = new String[theSub.size()];
        String[] finalGrade = new String[theSub.size()];
        String[] links = new String[theSub.size()];
        String[] q1Links = new String[theSub.size()];
        String[] q2Links = new String[theSub.size()];
        String[] q3Links = new String[theSub.size()];
        String[] q4Links = new String[theSub.size()];
        String[] midTermLinks = new String[theSub.size()];
        String[] finalExamLinks = new String[theSub.size()];
        String[] finalGradeLinks = new String[theSub.size()];

        /*Initializes String[] with the data */
        for (int i = 0; i < theSub.size(); i++) {
            //Get name of class with first letter of each word capitalized
            classes[i] = capitalizeString(theSub.get(i).getClassName()) + " " +
                    capitalizeString(theSub.get(i).getTeacherName());

            //Get grades. Try subtracting 1 //"A"; //
            gradesQ1[i] = theSub.get(i).getGrades()[0];
            gradesQ2[i] = theSub.get(i).getGrades()[1];
            midterm[i] = theSub.get(i).getGrades()[2];
            gradesQ3[i] = theSub.get(i).getGrades()[3].replace("&", "").replace("amp;", "");
            gradesQ4[i] = theSub.get(i).getGrades()[4];
            finalExam[i] = theSub.get(i).getGrades()[5];
            //finalGrade[i] = "A";
            try {finalGrade[i] = theSub.get(i).getGrades()[6]; } catch (Exception e) { finalGrade[i] = "--";}
            links[i] = theSub.get(i).getDetailedLink();

            //Get links
            q1Links[i] = theSub.get(i).getQ1Link();
            q2Links[i] = theSub.get(i).getQ2Link();
            q3Links[i] = theSub.get(i).getQ3Link();
            q4Links[i] = theSub.get(i).getQ4Link();
            midTermLinks[i] = theSub.get(i).getMidtermLink();
            finalExamLinks[i] = theSub.get(i).getFinalExamLink();
            finalGradeLinks[i] = theSub.get(i).getFinalExamLink();
        }

        /*Begin adding the grades to the display */
        theGrid.setColumnCount(8);
        //+ 1 for labels
        theGrid.setRowCount(classes.length + 1);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        final int classWidth = width / 2;
        final int gradeWidth = width / 10;
        int counter = 0;

        //Classes
        TextView classTV = new TextView(this);
        classTV.setText("Classes");
        classTV.setMinWidth(classWidth);
        theGrid.addView(classTV, counter);
        counter++;

        //Quarter titles
        theGrid.addView(getTVTitle("Q1", gradeWidth), counter);
        counter++;
        theGrid.addView(getTVTitle("Q2", gradeWidth), counter);
        counter++;
        theGrid.addView(getTVTitle("MidTerm", gradeWidth), counter);
        counter++;
        theGrid.addView(getTVTitle("Q3", gradeWidth), counter);
        counter++;
        theGrid.addView(getTVTitle("Q4", gradeWidth), counter);
        counter++;
        theGrid.addView(getTVTitle("Final Exam", gradeWidth + 50), counter);
        counter++;
        theGrid.addView(getTVTitle("Final Grade", gradeWidth + 50), counter);
        counter++;

        //Vertical difference between assignments (textviews)
        final int dVertical = 30;

        //Horizontal difference between textviews
        final int dHorizontal = 20;

        //textView.padding(left, top, right, bottom)

        /*Add quarter grades and class titles */
        for (int i = 0; i < classes.length; i++) {
            //Name of class
            TextView clasS = new TextView(this);
            clasS.setText(classes[i]);
            clasS.setMaxWidth(classWidth);
            clasS.setMinWidth(classWidth);
            clasS.setPadding(0, dVertical, 0, 0);
            if (i % 2 == 0)
                clasS.setTextColor(Color.BLUE);
            theGrid.addView(clasS, counter);
            counter++;

            //Quarter 1 grade
            TextView q1 = getTVGrade(gradesQ1[i], gradeWidth, q1Links[i], classes[i]);
            q1.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                q1.setTextColor(Color.BLUE);
            theGrid.addView(q1, counter);
            counter++;

            //Quarter 2 grade
            TextView q2 = getTVGrade(gradesQ2[i], gradeWidth, q2Links[i], classes[i]);
            q2.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                q2.setTextColor(Color.BLUE);
            theGrid.addView(q2, counter);
            counter++;

            //MidTerm grade
            TextView mTerm = getTVGrade(midterm[i], gradeWidth + 10, midTermLinks[i], classes[i]);
            mTerm.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                mTerm.setTextColor(Color.BLUE);
            theGrid.addView(mTerm, counter);
            counter++;

            //Quarter 3 Grade
            TextView q3 = getTVGrade(gradesQ3[i], gradeWidth, q3Links[i], classes[i]);
            q3.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                q3.setTextColor(Color.BLUE);
            theGrid.addView(q3, counter);
            counter++;

            //Quarter 4 grade
            TextView q4 = getTVGrade(gradesQ4[i], gradeWidth, q4Links[i], classes[i]);
            q4.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                q4.setTextColor(Color.BLUE);
            theGrid.addView(q4, counter);
            counter++;

            //Final Exam grade
            TextView fExam = getTVGrade(finalExam[i], gradeWidth + 10, finalExamLinks[i], classes[i]);
            fExam.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                fExam.setTextColor(Color.BLUE);
            theGrid.addView(fExam, counter);
            counter++;

            TextView fGrade = getTVGrade(finalGrade[i], gradeWidth + 10, finalGradeLinks[i], classes[i]);
            fGrade.setPadding(dHorizontal, dVertical, 0, 0);
            if (i % 2 == 0)
                fGrade.setTextColor(Color.BLUE);
            theGrid.addView(fGrade, counter);
            counter++;
        }

        /*Logout button. Switch to UserNamePassword.java while logging out in background */
        final Button logOut = (Button) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to UsernamePassword activity
                Intent backBack = new Intent(ViewAllGrades.this, UsernamePassword.class);
                backBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backBack);
                finish();


                //Open that current page in PowerSchool
                final WebBrowser theBrowser = new WebBrowser(theContext);
                theBrowser.getSettings().setJavaScriptEnabled(true);
                theBrowser.addSourceCodeListener(new SourceCodeListener() {
                    @Override
                    public void onSourceCodeAcquired(android.webkit.WebView webView, String html) {
                        //Press the log out button
                        theBrowser.clickButton("btnLogout");
                        finish();
                    }

                    @Override
                    public void onProgressChanged(android.webkit.WebView webView, int progress) {
                    }
                });
            }
        });

        /*GPA */
        final double weighted = BGPowerSchoolMethods.getGPA(theSub, true);
        final double unweighted = BGPowerSchoolMethods.getGPA(theSub, false);

        final String disclaimer = "DISCLAIMER\nThe GPA calculated in this app is not an exact " +
                "measure, but more of a somewhat accurate calculation. " +
                "Normal GPA is calculated cumulatively over all 4 years with only each year's final grades, " +
                "while this GPA is only applicable to the current year and is calculated using " +
                "quarter grades, which are incomplete data. " +
                "It also uses quarters that are not yet complete, so if, for example, " +
                "you do not do well on the first assignment of a quarter, your GPA may be lower than" +
                " it will actually be. We included this tool to give you a sense of what your performance" +
                " has been like this year, and we hope you find it useful. " +
                "Do not take the calculated GPA too seriously, because it is an imperfect and" +
                " imprecise measure of your performance, and only applies to the current year.\n" +
                "-- Ryan, Ben, and Andrew\n";

        //HERE
        final TextView weightedTV = getGPATV(weighted, disclaimer, true, this);
        final TextView unweightedTV = getGPATV(unweighted, disclaimer, false, this);

        //6 blank TextViews to get from one row to the next
        theGrid.addView(weightedTV);
        for (int i = 0; i < theGrid.getColumnCount()-1; i++)
            theGrid.addView(new TextView(this));

        theGrid.addView(unweightedTV);
    }

    /**
     * Gets TextView with GPA + ActionListener for Disclaimer alert
     */
    protected TextView getGPATV(double gpa, final String disclaimer, boolean isWeighted, final Context theContext) {
        TextView theView = new TextView(this);
        if (isWeighted)
            theView.setText("Weighted GPA: " + gpa);
        else
            theView.setText("Unweighted GPA: " + gpa);

        theView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                //Show the disclaimer
                final AlertDialog.Builder builder =
                        new android.app.AlertDialog.Builder(theContext).
                                setTitle("Approximate GPA Disclaimer")
                                .setMessage(disclaimer).setPositiveButton("Back", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

        return theView;
    }

    /**
     * Returns TextView with the grade for that quarter + ActionListener to go to that page
     */
    protected TextView getTVGrade(String grade, final int gradeWidth,
                               final String gradeLink, final String className) {
        TextView theView = new TextView(this);
        theView.setText(grade);
        theView.setMaxWidth(gradeWidth);
        theView.setMinWidth(gradeWidth);
        theView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                goToClassPage(gradeLink, className);
            }
        });

        return theView;
    }

    /**
     * Returns TextView with format for each quarter
     */
    protected TextView getTVTitle(String qTitle, final int maxWIDTH) {
        TextView theView = new TextView(this);
        theView.setText(qTitle);
        theView.setMaxWidth(maxWIDTH);
        theView.setMinWidth(maxWIDTH);
        return theView;
    }

    /**
     * Goes to the link of the quarter/Exam summoned
     */
    protected void goToClassPage(final String URL, final String className) {
        final WebBrowser theBrowser = new WebBrowser(this);

        final android.app.AlertDialog r = new android.app.AlertDialog.Builder(this)
                .setTitle("Loading " + className + " grades")
                .setMessage("Downloading " + 0)
                .show();

        theBrowser.getSettings().setJavaScriptEnabled(true);
        theBrowser.loadUrl(URL);
        theBrowser.addSourceCodeListener(new SourceCodeListener() {
            @Override
            public void onSourceCodeAcquired(WebView webView, String html) {
                Intent i = new Intent(ViewAllGrades.this, ClassQuarterGrades.class);
                i.putExtra("html", html);
                String classNameT = className;
                //For numeric percent grade
                /**HERE*/
                if (html.contains("% &nbsp;\""))
                    classNameT += ", " + html.substring(html.indexOf("% &nbsp;\"") - 5, html.indexOf("% &nbsp;\"")).replace("\"", "") + "%";
                            //+ " <-- Percent Grade for current quarter";
                i.putExtra("ClassName", classNameT);
                try {r.dismiss(); r.cancel(); } catch (Exception e) { e.printStackTrace(); }
                startActivity(i);
            }

            @Override
            public void onProgressChanged(android.webkit.WebView webView, int progress) {
                r.setMessage("Downloading " + progress);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ViewAllGrades.this, UsernamePassword.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.view_all_grades, menu);
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

    private static String capitalizeString(String string) {
        string = string.replace("Iii", "III");
        string = string.replace("Ii", "II");
        string = string.replace("iii", "III");
        string = string.replace("ii", "II");
        string = string.replace("ap ", "AP ");
        string = string.replace("Ap ", "AP ");
        string = string.replace("pe ", "PE ");
        string = string.replace("Pe ", "PE ");
        string = string.replace("bryan ashenfelter", "");
        char[] chars = string.toCharArray();//string.toLowerCase().toCharArray();
        boolean found = false;

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

    protected void copyToClipboard(final String message, final boolean toNotOverRide) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            @SuppressWarnings("deprecation") android.text.ClipboardManager clipboard =
                    (android.text.ClipboardManager) getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            clipboard.setText(message);
        }

        else
        {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(android.content.Context.CLIPBOARD_SERVICE);

            android.content.ClipData oldStuff;
            try { oldStuff = clipboard.getPrimaryClip(); }
            catch (Exception e) { oldStuff = android.content.ClipData.newPlainText("text label", ""); }

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
}
