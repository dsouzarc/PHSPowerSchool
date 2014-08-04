package com.ryan.phspowerschool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassQuarterGrades extends Activity {

    /**
     * Written by Ryan D'souza
     * Parses HTML code of a particular class
     * Displays the assignments and info about class
     */
    private ArrayList<Assignment> theAssignmentAL;
    private final String[] toCopy = {" "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarter_grades);

        //The HTML code for that CLASS
        final String HTML = getIntent().getExtras().getString("html", "Error yo");

        //Writes name of class and percentage grade in that class
        TextView forClassGrade = (TextView) findViewById(R.id.classGradePercentTV);
        final String className = getIntent().getExtras().getString("ClassName", "N/A");
        forClassGrade.setText(className);

        //Initializes ArrayList of all assignments with the assignments
        theAssignmentAL = new AllAssignments(HTML).getAllAssignments();

        //Search through the Array, if any are invalid, remove them
        for (int i = 0; i < theAssignmentAL.size(); i++) {
            if (theAssignmentAL.get(i).getName().contains("nbsp") || theAssignmentAL.get(i).getName().contains("00"))
                theAssignmentAL.remove(i);
            else if (theAssignmentAL.get(i).getDate().contains("00"))
                theAssignmentAL.remove(i);
        }

        if(theAssignmentAL.size() == 0)
        {
            //Alert Dialog, no Internet connection
        }

        String[] theAssignments = new String[theAssignmentAL.size()];
        for (int i = 0; i < theAssignments.length; i++)
            theAssignments[i] = theAssignmentAL.get(i).getName();

        final GridLayout theGrid = (GridLayout) findViewById(R.id.allAssignmentGradesGridLayout);

        //Number of assignments
        theGrid.setRowCount(theAssignmentAL.size() + 15);
        theGrid.setColumnCount(5);

        //Width of screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int lgSIZE = 80; //LetterGrade
        final int ngSIZE = 300; //NumberGrade
        final int asSIZE = getLargestValue(theAssignments) * 10 + 30; //Assignment Title
        final int dSIZE = 300; //Date
        final int tSIZE = 300; //Type of assignment

        int counter = 0;

        //Adds everything to the screen in chronlogical order
        //for(int i = 1; i < theAssignmentAL.size(); i++)
        for (int i = theAssignmentAL.size() - 1; i >= 0; i--) {
            TextView letterGrade = getView(theAssignmentAL.get(i).getLetterGrade(), lgSIZE, i);
            letterGrade.setPadding(0, 50, 0, 0);

            TextView numberGrade = getView(theAssignmentAL.get(i).getGrade(), ngSIZE, i);
            numberGrade.setPadding(0, 50, 0, 0);

            TextView assignment = getView(theAssignmentAL.get(i).getName(), asSIZE, i);
            assignment.setPadding(0, 50, 0, 0);

            TextView date = getView(theAssignmentAL.get(i).getDate(), dSIZE, i);
            date.setPadding(15, 50, 0, 0);

            TextView type = getView(theAssignmentAL.get(i).getType(), tSIZE, i);
            type.setPadding(0, 30, 0, 0);

            if (i % 2 == 0) {
                letterGrade.setTextColor(Color.BLUE);
                numberGrade.setTextColor(Color.BLUE);
                assignment.setTextColor(Color.BLUE);
                date.setTextColor(Color.BLUE);
                type.setTextColor(Color.BLUE);
            }

            theGrid.addView(letterGrade, counter);
            counter++;
            theGrid.addView(numberGrade, counter);
            counter++;
            theGrid.addView(assignment, counter);
            counter++;
            theGrid.addView(date, counter);
            counter++;
            theGrid.addView(type, counter);
            counter++;
        }
        //Explains what N/A means
        TextView explanation = new TextView(this);
        explanation.setText("Note: N/A means that the grade has not yet been published");
        explanation.setMaxWidth(asSIZE);
        explanation.setTextColor(Color.RED);
        theGrid.addView(new TextView(this)); //Two blank spaces
        theGrid.addView(new TextView(this)); //Two blank spaces
        theGrid.addView(explanation);//, counter);

        TextView forCopy = (TextView) findViewById(R.id.forCopyTV);
        final Context theC = this;
        forCopy.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    @SuppressWarnings("deprecation") android.text.ClipboardManager clipboard =
                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(toCopy[0]);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip =
                            android.content.ClipData.newPlainText("text label", toCopy[0]);
                    clipboard.setPrimaryClip(clip);
                }
                final android.app.AlertDialog r = new android.app.AlertDialog.Builder(theC)
                        .setTitle("Successful!")
                        .setMessage("Successfully Copied Contents to Clipboard ").show();
            }
        });
    }

    private static int getLargestValue(String[] theArray1) {
        int max = 0;
        for (int i = 0; i < theArray1.length; i++) {
            try {
                if (theArray1[i].length() > max)
                    max = theArray1[i].length();
            } catch (Exception e) {
                e.printStackTrace();
                max = theArray1.length;
            }
        }

        return max;
    }

    protected TextView getView(String content, int maxSize, int num) {
        TextView theView = new TextView(this);

        if (content.contains("href") || content.contains("javascript")) {
            try {
                String partial = content.substring(content.indexOf(")"), content.length());
                String almost = partial.substring(partial.indexOf(">"), content.length());
                theView.setText(almost.replace("</a>", ""));
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    String partial = content.substring(content.indexOf(">"));
                    partial = partial.replace(">", "").replace("</a>", "");
                    theView.setText(partial);
                } catch (Exception ei) {
                    e.printStackTrace();
                }
                theView.setText(content.replace("ahref", ""));
            }
        }
        if (content.contains("clswrk"))
            theView.setText("Classwork");
        else
            theView.setText(content);

        theView.setMinWidth(maxSize);
        theView.setMaxWidth(maxSize);
        theView.setClickable(true);

        if (content.contains("00/00/0000")) {
            theView.setText("");
            return theView;
        }

        if (content.replace(" ", "").equals("N/A--")) {
            theView.setText("");
            return theView;
        }

        final Assignment theAs = theAssignmentAL.get(num);
        theView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                toCopy[0] += theAs.getGrade() + " for " + theAs.getName() + " on " +
                        theAs.getDate() + " ";
                TextView forCopy = (TextView) findViewById(R.id.forCopyTV);
                forCopy.setText(toCopy[0]);
            }
        });
        return theView;
    }

    public static String getSubstring(int num, String theString) {
        if (theString.length() >= num)
            return theString.substring(0, num);
        String answer = "";
        for (int i = 0; i < num; i++) {
            try {
                answer += theString.charAt(i);
            } catch (Exception e) {
                theString += " ";
            }
        }
        return answer;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.class_quarter_grades, menu);
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
