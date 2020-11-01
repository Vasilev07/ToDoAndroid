package com.example.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    public static final String NOTE_INFO = "NOTE_INFO";
    private NoteInfo note;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteText;
    private EditText textNoteTitle;
    private int notePosition;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalText;
    private String originalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapterCourses);
        
        readDisplayStateValues();
        saveOriginalStateValues();

        textNoteText = findViewById(R.id.text_note_text);
        textNoteTitle = findViewById(R.id.text_note_title);

        if (!isNewNote) {
            displayNote(spinnerCourses, textNoteTitle, textNoteText);
        }

    }

    private void saveOriginalStateValues() {
        if (isNewNote) {
            return;
        }

        originalNoteCourseId = note.getCourse().getCourseId();
        originalText = note.getText();
        originalTitle = note.getTitle();
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(note.getCourse());
        spinnerCourses.setSelection(courseIndex);

        textNoteText.setText(note.getText());
        textNoteTitle.setText(note.getTitle());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;
        if (isNewNote) {
            createNewNote();
        } else {
            note = DataManager.getInstance().getNotes().get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //5 Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isCancelling) {
            if (isNewNote) {
                DataManager.getInstance().removeNote(notePosition);
            } else {
                 storePreviousNoteValues();
            }
        }

        saveNote();
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        note.setCourse(course);
        note.setText(originalText);
        note.setTitle(originalTitle);
    }

    public void saveNote() {
        note.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        note.setText(textNoteText.getText().toString());
        note.setTitle(textNoteTitle.getText().toString());
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();
        note = dm.getNotes().get(notePosition);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = course.getTitle() + "\"\n" + textNoteText.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}