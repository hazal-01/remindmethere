package ASynchronisation;

import android.os.AsyncTask;

import models.Note;
import persistence.NoteDao;

public class Eingabe extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Eingabe(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }

}