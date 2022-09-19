package util;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//ItemDecoration ist für die Trennlinien:
// Dadurch wird am unteren Rand jeder Ansicht eine Linie gezeichnet, die als letzte als Trennzeichen zwischen Elementen fungiert.
public class VerticalSpacingItemDecoretor extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    public VerticalSpacingItemDecoretor(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    //Generate eine Methode: outRect bestimmt auf jeder Seite die Menge der Polsterung
    @Override
    public void getItemOffsets(@NonNull Rect outRect,@NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        outRect.bottom = verticalSpaceHeight;
    }
}