package ASynchronisation;

import android.os.AsyncTask;

import models.Note;
import persistence.NoteDao;

public class Bearbeiten extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Bearbeiten(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.updateNotes(notes);
        return null;
    }

}