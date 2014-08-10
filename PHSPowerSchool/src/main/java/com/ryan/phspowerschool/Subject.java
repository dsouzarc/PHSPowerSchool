package com.ryan.phspowerschool;
// this class represents a class someone is taking
// Note: Individual assignments are not stored in this class, see: Assignment class
public class Subject {
    private final String className; // name of the class
    private final String teacher; // teacher of the class
    private final String email; // teahcer's email
    private final String[] grades; // quarter/exam/final grades
    private final String detailedLink; // link to a more detailed grade report
    private final String[] links;

    public Subject(String cn, String tn, String em, String[] gr, String[] links) {
        className = cn;
        teacher = tn;
        email = em;
        grades = gr;

        //CHANGE
        detailedLink = links[0];
        this.links = links;

        for (int x = 0; x < grades.length; x++) {
            if (grades[x] == null) {
                grades[x] = "--";
            } // replace grades that don't exist yet with --
        }
    }

    public String getClassName() {
        return className;
    }

    public String getTeacherName() {
        return teacher;
    }

    public String getEmail() {
        return email;
    }

    public String[] getGrades() {
        return grades;
    }

    public String getDetailedLink() {
        return detailedLink;
    }

    public String getQ1Link() {
        if (links[0] == null) return links[0];
        return links[0];
    }

    public String getQ2Link() {
        if (links[1] == null) return links[0];
        return links[1];
    }

    public String getMidtermLink() {
        if (links[3] == null) return links[0];
        return links[3];
    }

    public String getQ3Link() {
        if (links[4] == null) return links[0];
        return links[4];
    }

    public String getQ4Link() {
        if (links[5] == null) return links[0];
        return links[5];
    }

    public String getFinalGradeLink() {
        try { if(links[6] == null) return links[0]; } catch (Exception e) {}
        return links[6];
    }

    public String[] getAllLinks() {
        return links;
    }

    public String getFinalExamLink() {
        try {
            if (links[5] == null) return links[0];
            return links[5];
        } catch (Exception e) {
            return links[0];
        }
    }

    /**
     * CHANGE
     */
    public String[] getLinks() {
        return links;
    }

    public String toString() {
        String ret = this.getClassName() + ", taught by " + this.getTeacherName() + ". Contact at " + this.getEmail() + "\t Grades: ";
        for (int x = 0; x < this.getGrades().length; x++) {
            ret = ret + this.getGrades()[x] + " | ";
        }
        ret += "\t" + this.getDetailedLink() + " for more information.";
        return ret;
    }
}