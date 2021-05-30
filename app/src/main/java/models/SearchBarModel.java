package models;
import android.graphics.Bitmap;

public class SearchBarModel {
    private Bitmap image;
    private String text;

    public SearchBarModel(Bitmap Image, String Text) {
        this.image = Image;
        this.text = Text;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

}
