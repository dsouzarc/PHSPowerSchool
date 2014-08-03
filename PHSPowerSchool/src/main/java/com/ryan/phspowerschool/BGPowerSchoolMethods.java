package com.ryan.phspowerschool;

import java.util.ArrayList;

public class BGPowerSchoolMethods {
    
    /*
     public static String[][] to2DArray(String[] q1, String[] q2, String[] midterm, String[] q3,String[] q4, String[] fin) {
     String[][] grades = new String[q1.length][6];
     for(int x = 0; x < grades.length; x++) grades[x] = new String[]{q1[x], q2[x], midterm[x], q3[x], q4[x], fin[x]};
     return grades;
     }
     */

    public static double getGPA(ArrayList<Subject> classes, boolean weighted) {
        int N = classes.size();
        String[][] classGrades = new String[N][7];
        String[] classNames = new String[N];
        for (int x = 0; x < N; x++) {
            classGrades[x] = classes.get(x).getGrades();
            classNames[x] = classes.get(x).getClassName();
        }
        if (weighted) return weightedGPA(classNames, classGrades);
        else return unweightedGPA(classNames, classGrades);
    }

    private static double weightedGPA(String[] classNames, String[][] classGrades) {
        double totalValue = 0;
        double totalCount = 0;
        for (int x = 0; x < classGrades.length; x++) {
            double changeFactor = 0;
            if ((classNames[x].indexOf("Advanced") != -1) || (classNames[x].indexOf("AP") != -1) || (classNames[x].indexOf("Accelerated") != -1) || (classNames[x].equals("Organic Chemistry")))
                changeFactor = 1;
            for (int y = 0; y < 6; y++) {
                double credits = 1;
                if ((classNames[x].indexOf("Biology") != -1) || (classNames[x].indexOf("Chemistry") != -1) || (classNames[x].indexOf("Physics") != -1)
                        || (classNames[x].indexOf("Genetics") != -1) || (classNames[x].indexOf("Astronomy") != -1)
                        || (classNames[x].indexOf("Oceanography") != -1) || (classNames[x].indexOf("Anatomy") != -1) || (classNames[x].indexOf("Horticulture") != -1)
                        || (classNames[x].indexOf("Environmental") != -1) || (classNames[x].indexOf("Forensics") != -1) || (classNames[x].indexOf("Anthropology") != -1))
                    credits = 1.28;
                double average = changeFactor;
                String grade = classGrades[x][y];
                if (y == 2 || y == 5) credits /= 2;
                if (grade.equals("A")) average += (4);
                else if (grade.equals("A-")) average += (4 - (1 / 3));
                else if (grade.equals("B+")) average += (3 + (1 / 3));
                else if (grade.equals("B")) average += (3);
                else if (grade.equals("B-")) average += (3 - (1 / 3));
                else if (grade.equals("C+")) average += (2 + (1 / 3));
                else if (grade.equals("C")) average += (2);
                else if (grade.equals("C-")) average += (2 - (1 / 3));
                else if (grade.equals("D+")) average += (1 + (1 / 3));
                else if (grade.equals("D")) average += (1);
                else if (grade.equals("D-")) average += (1 - (1 / 3));
                else if (grade.equals("F")) average += (0);
                else { // do not include this reading
                    average = 0;
                    credits = 0;
                }

                totalValue += average * credits;
                totalCount += credits;

            }
        }
        double returnValue = totalValue / totalCount;
        return (double) Math.round(returnValue * 1000) / 1000;
    }

    private static double unweightedGPA(String[] classNames, String[][] classGrades) {
        double totalValue = 0;
        double totalCount = 0;
        for (int x = 0; x < classGrades.length; x++) {
            for (int y = 0; y < 6; y++) {
                double credits = 1;
                if ((classNames[x].indexOf("Biology") != -1) || (classNames[x].indexOf("Chemistry") != -1) || (classNames[x].indexOf("Physics") != -1)
                        || (classNames[x].indexOf("Genetics") != -1) || (classNames[x].indexOf("Astronomy") != -1)
                        || (classNames[x].indexOf("Oceanography") != -1) || (classNames[x].indexOf("Anatomy") != -1) || (classNames[x].indexOf("Horticulture") != -1)
                        || (classNames[x].indexOf("Environmental") != -1) || (classNames[x].indexOf("Forensics") != -1) || (classNames[x].indexOf("Anthropology") != -1))
                    credits = 1.28;
                double average = 0;
                String grade = classGrades[x][y];
                if (y == 2 || y == 5) credits /= 2;
                if (grade.equals("A")) average += (4);
                else if (grade.equals("A-")) average += (4 - (1 / 3));
                else if (grade.equals("B+")) average += (3 + (1 / 3));
                else if (grade.equals("B")) average += (3);
                else if (grade.equals("B-")) average += (3 - (1 / 3));
                else if (grade.equals("C+")) average += (2 + (1 / 3));
                else if (grade.equals("C")) average += (2);
                else if (grade.equals("C-")) average += (2 - (1 / 3));
                else if (grade.equals("D+")) average += (1 + (1 / 3));
                else if (grade.equals("D")) average += (1);
                else if (grade.equals("D-")) average += (1 - (1 / 3));
                else if (grade.equals("F")) average += (0);
                else { // do not include this reading
                    average = 0;
                    credits = 0;
                }

                totalValue += average * credits;
                totalCount += credits;

            }
        }
        double returnValue = totalValue / totalCount;
        return (double) Math.round(returnValue * 1000) / 1000;
    }


}