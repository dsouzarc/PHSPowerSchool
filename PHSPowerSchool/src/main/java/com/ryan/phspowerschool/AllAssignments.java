package com.ryan.phspowerschool;

import java.util.ArrayList;

/**
 * Created by Ryan on 1/21/14.
 */
public class AllAssignments {
    private final ArrayList<Assignment> allAssignments = new ArrayList<Assignment>();

    public AllAssignments(String HTML) {
        String theAnswer = HTML.substring(HTML.indexOf("00/00/0000") + 50);

        String[] what = theAnswer.split("<tr bgcolor");

        for (int i = 1; i < what.length; i++) {
            String[] datesHwGreat = what[i].split("<td>");

            String tDates = datesHwGreat[1].replace("</td", "").replace(">", "");

            String tTypeOfAssignment = datesHwGreat[2].replace("</td>", "").
                    replace(" ", "").replace(">", "");

            String tAssignments = "";
            if (datesHwGreat[3].charAt(0) == '<' || datesHwGreat[3].charAt(1) == '<') {
                try {
                    tAssignments = datesHwGreat[3].substring(datesHwGreat[3].indexOf(">"),
                            datesHwGreat[3].indexOf("</")).replace("</", "").replace(">", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    tAssignments = datesHwGreat[3].substring(0,
                            datesHwGreat[3].indexOf("</td")).replace("<", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String tNumberGrade = datesHwGreat[3].substring(datesHwGreat[3].indexOf("bold-underline"),
                    datesHwGreat[3].indexOf("</span>")).replace("bold-underline\">", "");

            String forLetter = datesHwGreat[3].split("center")[3];

            String tLetterGrade = "";
            if (forLetter.contains("&nbsp"))
                tLetterGrade = "N/A";
            else
                tLetterGrade = forLetter.substring(forLetter.indexOf(">"),
                        forLetter.indexOf("<")).replace("<", "").replace(">", "");

            allAssignments.add(new Assignment(tDates, tTypeOfAssignment,
                    tAssignments, tNumberGrade, tLetterGrade));
        }
    }

    public ArrayList<Assignment> getAllAssignments() {
        return allAssignments;
    }
}
