package com.example.todo.notes;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    public static final String ORIGINAL_NOTE_COURSE_ID = "ORIGINAL_COURSE_NOTE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "ORIGINAL_COURSE_NOTE_ID";
    public static final String ORIGINAL_NOTE_TEXT = "ORIGINAL_COURSE_NOTE_ID";

    public String originalNoteCourseId;
    public String originalText;
    public String originalTitle;
    public boolean isNewlyCrated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TEXT, originalText);
        outState.putString(ORIGINAL_NOTE_TITLE, originalTitle);
    }

    public void restoreState(Bundle inState) {
        originalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalText = inState.getString(ORIGINAL_NOTE_TEXT);
        originalTitle = inState.getString(ORIGINAL_NOTE_TITLE);
    }
}
