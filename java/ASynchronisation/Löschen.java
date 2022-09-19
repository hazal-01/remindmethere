package ASynchronisation;


import android.os.AsyncTask;

import models.Note;
import persistence.NoteDao;

public class Löschen extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Löschen(NoteDao dao)
    {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.delete(notes);
        return null;
    }
}