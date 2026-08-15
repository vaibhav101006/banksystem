package com.scbs.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimetableSlot {
    private String id;
    private String classId;
    private String dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    private LocalTime startTime;
    private LocalTime endTime;
    private String subjectName;
    private String facultyName;
    private String roomNumber;

    public TimetableSlot() {}

    public TimetableSlot(String id, String classId, String dayOfWeek, LocalTime startTime, 
                         LocalTime endTime, String subjectName, String facultyName, String roomNumber) {
        this.id = id;
        this.classId = classId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectName = subjectName;
        this.facultyName = facultyName;
        this.roomNumber = roomNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getFormattedTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm a");
        if (startTime != null && endTime != null) {
            return startTime.format(fmt) + " - " + endTime.format(fmt);
        }
        return "";
    }
}
