package persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ASynchronisation.Löschen;
import ASynchronisation.Eingabe;
import ASynchronisation.Bearbeiten;
import models.Note;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context)

    {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note)

    {
        new Eingabe(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNoteTask(Note note){
        new Bearbeiten(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNoteTask(Note note)
            
    {
        new Löschen(mNoteDatabase.getNoteDao()).execute(note);
    }
}
