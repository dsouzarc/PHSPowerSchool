package com.ryan.phspowerschool;// Andrew Barry
// This code parses powerschool, gets grades, and prints them in an orderly fashion.
//ses powerschool, gets grades, and prints them in an orderly fashion.


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
public class HTMLParser {
    // each object of Subject requires class name, teacher name, teacher email, array of quarter grades, array of links links to the quarter


    // Complete re-write of the HTML parser, much more organized (and it actually works!)
    public static ArrayList<Subject> getFrontPageGrades(String FrontPageHTML) {

        ArrayList<Subject> ret = new ArrayList<Subject>(); // the array list to be returned
        String[] Subjects = Arrays.copyOfRange(FrontPageHTML.split("td align=\"left\">"), 1, FrontPageHTML.split("td align=\"left\">").length); // split on td align lefts for class names (ignore leading garbage)

        for (String s : Subjects) { // for each subject
            String className = s.substring(0,s.indexOf("&")); // stop at &nbsp to get the class name
            String teacherName = s.split("mailto:")[1].substring(0, s.split("mailto:")[1].indexOf("@")).replace("_"," "); // derive teacher name from email
            String teacherEmail = teacherName.replace(" ","_") + "@princetonk12.org"; // manually recreate email address

            String[] gradesAndLinks = Arrays.copyOfRange(s.split("<td>"),1,s.split("<td>").length); // we will only deal with the first 7 td lines, the rest is garbage
            String[] grades = new String[7];
            String[] links = new String[7]; // neither of these are constant, filler will be placed where necessary
            int c = 0; // counter, only check the <td> lines we want
            while (c <= 6) {
                String line = gradesAndLinks[c];
                if (line.contains("scores.html")) { // this line has a grade and a link
                    String link = line.substring(line.indexOf("\"") + 1, line.indexOf(">") - 1);
                    link = "https://pschool.princetonk12.org/guardian/" + link.substring(0,link.length() - 6); // remove the &fg=XX to get a link we want to use
                    String grade = line.substring(line.indexOf(">") + 1, line.substring(1,line.length()).indexOf("<") + 1); // get the letter grade
                    grades[c] = grade;
                    links[c] = link;
                }
                else { // no grade, no link, add filler (&nbsp)
                    grades[c] = "--"; // display this
                    links[c] = null; // detect this
                }
                c++; // inc counter
            } // all info has been collected, create and add new Subject object
            ret.add(new Subject(className, teacherName, teacherEmail, grades, links));
        }

        return ret;
    }

    public static HashMap<String,Integer> getStudents(String theHTML) {
        String studentsList = theHTML.split("<ul id=\"students-list\">")[1];
        studentsList = studentsList.split("<input ")[0]; // split again
        String[] students = studentsList.split("href=\"javascript:switchStudent");
        int studentNumber = 0;
        String studentName = "";
        HashMap<String,Integer> fin = new HashMap<String,Integer>();
        for (int x = 1; x < students.length; x++) {
            studentNumber = Integer.parseInt(students[x].substring(1,students[x].indexOf(")")));
            studentName = students[x].substring(students[x].indexOf(">")+1,students[x].indexOf("<"));
            fin.put(studentName,studentNumber);
        }
        return fin;
    }














     /* The old and busted method, broken because it assumes things about grades/links formatting that are very untrue
     public static ArrayList<Subject> getFrontPageGrades(String FrontPageHTML) {
          String[] Subjects = Arrays.copyOfRange(FrontPageHTML.split("td align=\"left\">"), 1, FrontPageHTML.split("td align=\"left\">").length); // the first thing we need from this is class name, which comes right after td align=left
          ArrayList<Subject> ret = new ArrayList<Subject>(); // the array list to be returned
          for (String s : Subjects) {
               String className = s.substring(0, s.indexOf("&")); // get class name
               String teacherEmail = s.split("mailto:")[1].substring(0, s.split("mailto:")[1].indexOf("@")) + "@princetonk12.org"; // get email
               String teacherName = teacherEmail.replace("_", " ").replace("@princetonk12.org", ""); // turn the email into the name of the teacher
               teacherName = ("" + teacherName.charAt(0)).toUpperCase() + teacherName.substring(1); // capitalize first name
               teacherName = teacherName.substring(0, teacherName.indexOf(" ") + 1) + ("" + teacherName.charAt(teacherName.indexOf(" ") + 1)).toUpperCase() + teacherName.substring(teacherName.indexOf(" ") + 2); // capitalize last name
               String[] gradesRaw = Arrays.copyOfRange(s.split("scores.html"), 1, s.split("scores.html").length); // split existing class string based on each grade
               String[] grades = new String[7]; // four quarters, two exams, final grade
               String[] links = new String[7]; // generic, Q1/2/X1/3/X2 (no final grade)
               int i = 0; // 'i' is our counter, the expected final value is
               for (String s2 : gradesRaw) {
                    if (i == 0) { // we will use this to create our detailed link
                         links[0] = "http://pschool.princetonk12.org/guardian/scores.html" + s2.substring(s2.indexOf("?"), s2.indexOf("&")); // general link
                    } else {
                         links[i] = "http://pschool.princetonk12.org/guardian/scores.html" + s2.substring(s2.indexOf("?"), s2.substring(s2.indexOf("f") + 1).indexOf("f") + 1); // quarter link
                    }


                    grades[i] = s2.substring(s2.indexOf(">") + 1, s2.indexOf("<")); // get the grade, set it
                    i++;
               }
               if (i != 6 && i != 7) { // blank boxes (gym, free, etc.) grades[] and links[] are therefore wrong and need (5-i) leading nulls
                    String[] newGrades = new String[7];
                    for (int x = 0; x < 6 - i; x++) {
                         newGrades[x] = "--";
                         //newLinks[x] = "--";
                    } // now merge this array with the old one
                    for (int x = 6 - i; x < 7; x++) {
                         newGrades[x] = grades[x - (6 - i)];
                         //newLinks[x] = links[x - (6-i)];
                    }
                    grades = newGrades.clone();
                    //links = newLinks.clone();

               }
               if (teacherName.equals("Bryan Ashenfelter")) { // who is this guy
                    teacherName = "Staff"; // that's better
               }
               ret.add(new Subject(className, teacherName, teacherEmail, grades, links)); // all info has been gathered, create Subject and add to arraylist
          }

          return ret; // done
     }
     */
     /*public static double getNumericAverage(String HTML) {
      String line = HTML.split("document.write(\"")[2]; // there are two instances of document.write, we want the second one, and we need to ignore the garbage in front
      String grade = line.substring(0,line.indexOf("%")); // trim off the fat
      return Double.parseDouble(grade); // parse to a double, return it
      }*/


    public static void main(String[] args) throws IOException {

        final String fileName = "main.txt";
        String theContents = "";
        try
        {
            BufferedReader theReader = new BufferedReader(new FileReader(fileName));

            String line;
            while((line = theReader.readLine()) != null)
                theContents += line + ", ";
            theReader.close();
        }

        catch(Exception e) { e.printStackTrace(); }

        ArrayList<Subject> classes = HTMLParser.getFrontPageGrades(theContents);

        String[] allLinks = classes.get(6).getAllLinks();

        //GETS ALL GRADES FROM FIRST CLASS
        String[] class1 = classes.get(6).getGrades();

        System.out.println(classes.get(6).getClassName());
        System.out.println(Arrays.toString(class1));

        //System.out.println(Arrays.toString(allLinks));

        System.out.println("Q1:\t" + classes.get(6).getQ1Link());
        System.out.println("Q2:\t" + classes.get(6).getQ2Link());
        System.out.println("MidTerm:\t" + classes.get(6).getMidtermLink());
        System.out.println("Q3:\t" + classes.get(6).getQ3Link());
        System.out.println("Q4:\t" + classes.get(6).getQ4Link());
        System.out.println("Final Exam:\t" + classes.get(6).getFinalExamLink());

    }
}