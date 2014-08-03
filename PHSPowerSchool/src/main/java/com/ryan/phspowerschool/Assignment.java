package com.ryan.phspowerschool;// Andrew Barry

// this class represents an assignment
public class Assignment {
    private final String date;
    private final String type;
    private final String name;
    private final String grade;
    private final String letterGrade;

    public Assignment(String date, String assignmentType, String assignmentName,
                      String assignmentGrade, String assignmentLetterGrade) {
        this.date = date;
        this.type = assignmentType;
        this.name = assignmentName;
        this.letterGrade = assignmentLetterGrade;
        this.grade = assignmentGrade;
    }

    public String toString() {
        return name + " (" + type + ") Assigned on " + date + " - " +
                grade + " (" + letterGrade + ")";

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getGrade() {
        return grade;
    }

    public String getLetterGrade() {
        return letterGrade;
    }
}