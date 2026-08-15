package com.scbs.model;

public class ClassSection {
    private String id;
    private String name; // e.g. CSE-3A
    private String semester; // e.g. 5th Semester
    private int totalStudents;

    public ClassSection() {}

    public ClassSection(String id, String name, String semester, int totalStudents) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.totalStudents = totalStudents;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
}
