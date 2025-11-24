package com.uottawaseg.otams.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawaseg.otams.Requests.Availability;

import java.time.OffsetTime;
import java.util.HashMap;

public class StudentAvailabilityWriter {

    public interface CompletionCallback {
        void onComplete(boolean success, String errorMessage);
    }

    public static void bookAvailabilityForStudent(Availability availability, String studentFirst, String studentLast, String studentUsername, CompletionCallback callback) {
        if (availability.getTutorUsername()==null || availability.getTutorUsername().isEmpty()) {
            callback.onComplete(false, "Tutor username missing");
            return;
        }
        availability.setBooked(true);
        availability.setStudentCredentials(studentFirst, studentLast, studentUsername);
        writeToTutorAsync(availability, (success1, err1) -> {
            if (!success1) {
                callback.onComplete(false, err1);
                return;
            }
            writeToStudentAsync(studentUsername, "bookedSessions", availability, (success2, err2) -> {
                if (!success2){
                    callback.onComplete(false, err2);
                }
                else{
                    callback.onComplete(true, null);
                }
            });
        });
    }


    private static void writeToTutorAsync(Availability a, CompletionCallback callback) {
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(LoginManager.ACCOUNTS)
                .child(a.getTutorUsername())
                .child("availabilities");
        ref.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult()==null) {
                callback.onComplete(false, "Failed to read tutor availabilities");
                return;
            }
            var snapshot= task.getResult();
            boolean updated= false;
            for (var child : snapshot.getChildren()) {
                String dateStr= child.child("date").getValue(String.class);
                OffsetTime startTime= AvailabilityReader.readOffsetTime((HashMap) child.child("start").getValue());
                OffsetTime endTime= AvailabilityReader.readOffsetTime((HashMap) child.child("end").getValue());
                if (dateStr != null && dateStr.equals(a.getDate().toString()) && startTime.equals(a.getStart()) && endTime.equals(a.getEnd())) {
                    child.getRef().setValue(convertAvailability(a)).addOnCompleteListener(t -> {
                        if (!t.isSuccessful() && t.getException() != null){
                            callback.onComplete(false, t.getException().getMessage());
                        }
                        else{ callback.onComplete(true, null);
                        }
                    });
                    updated= true;
                    break;
                }
            }
            if (!updated) {
                ref.push().setValue(convertAvailability(a)).addOnCompleteListener(t -> {
                    if (!t.isSuccessful() && t.getException() != null){
                        callback.onComplete(false, t.getException().getMessage());
                    }
                    else{
                        callback.onComplete(true, null);
                    }
                });
            }
        });
    }

    private static void writeToStudentAsync(String username, String section, Availability a, CompletionCallback callback) {
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(LoginManager.ACCOUNTS)
                .child(username)
                .child(section);
        ref.push().setValue(convertAvailability(a)).addOnCompleteListener(task -> {
            if (!task.isSuccessful() && task.getException() != null){
                callback.onComplete(false, task.getException().getMessage());
            }
            else{
                callback.onComplete(true, null);
            }
        });
    }

    private static HashMap<String, Object> convertAvailability(Availability a) {
        HashMap<String, Object> map= new HashMap<>();
        map.put("autoApprove", a.getAutoApprove());
        map.put("booked", a.isBooked());
        map.put("date", a.getDate().toString());
        map.put("start", convertOffsetTime(a.getStart()));
        map.put("end", convertOffsetTime(a.getEnd()));
        map.put("tutorFirstName", a.getTutorFirstName());
        map.put("tutorLastName", a.getTutorLastName());
        map.put("tutorUsername", a.getTutorUsername());
        if (a.getStudentFirstName() != null) {
            map.put("studentFirstName", a.getStudentFirstName());
        }
        if (a.getStudentLastName() != null) {
            map.put("studentLastName", a.getStudentLastName());
        }
        if (a.getStudentUsername() != null) {
            map.put("studentUsername", a.getStudentUsername());
        }
        return map;
    }

    private static HashMap<String, Object> convertOffsetTime(OffsetTime t) {
        HashMap<String, Object> map= new HashMap<>();
        map.put("hour", t.getHour());
        map.put("minute", t.getMinute());
        HashMap<String, Object> offsetMap= new HashMap<>();
        offsetMap.put("id", t.getOffset().toString());
        map.put("offset", offsetMap);
        return map;
    }
}





