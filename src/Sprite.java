import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {

    final String type;
    final Image image;

    public Sprite(String type, Image image, double x, double y) {
        super(image);
        this.type = type;
        this.image = image;
        this.setPreserveRatio(true);
        setTranslateX(x);
        setTranslateY(y);
        setScaleX(1);
    }

    void turnLeft() {
        if (type.contains("Player") || type.contains("enemy")) {
            setScaleX(1);
        } else if (type.contains("fireball")) {
            setScaleX(-1);
        }
    }

    void turnRight() {
        if (type.contains("Player") || type.contains("enemy")) {
            setScaleX(-1);
        } else if (type.contains("fireball")) {
            setScaleX(1);
        }
    }
}
