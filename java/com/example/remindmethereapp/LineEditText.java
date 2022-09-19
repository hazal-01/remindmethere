package com.example.remindmethereapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;


// für die Text Eingabe von activity_note
public class LineEditText extends AppCompatEditText {

    private static final String TAG = "LinedEditText";

    private Rect mRect;
    private Paint mPaint;


    // Konstruktor für den LayoutInflater
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect(); //Objekt für ein leeres Rechteck
        mPaint = new Paint(); // Objekt für die Farbe, Stil
        mPaint.setStyle(Paint.Style.STROKE); //Der Stil gibt an,dass das Paper gestrichen ist
        mPaint.setStrokeWidth(2);// Breite
        mPaint.setColor(0xFF000000); // Farbe von den Linien

    }

    //Methode onDraw ist ein Canvas Objekt, mit welches man eine View selbt zeichnen kann
    @Override
    protected void onDraw(Canvas canvas) {

        // die Höhe von der View
        int height = ((View)this.getParent()).getHeight();

        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        //getLineBounds gibt die Linie zurück
        int baseline = getLineBounds(0, r);

        for (int i = 0; i < numberOfLines; i++) {

            // Linie beginnt links und endet rechts
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);

            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }

}