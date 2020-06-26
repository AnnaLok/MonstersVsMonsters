import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Player extends Sprite {

    final Image fireballImg = new Image(Player.class.getResourceAsStream("./img/fireball2.gif"));
    final Image firePlayerImg = new Image(Player.class.getResourceAsStream("./img/fire.png"));
    final static Image heartImg = new Image(Player.class.getResourceAsStream("./img/heart.png"), 35, 35, false, false);

    Sprite firePlayer = new Sprite("firePlayer", firePlayerImg,(1280-firePlayerImg.getWidth())/2, 300);

    String fireSound = Player.class.getClassLoader().getResource("./sound/firecast.mp3").toString();
    AudioClip fireClip = new AudioClip(fireSound);

    boolean firing = false;

    public static ArrayList<Sprite> fireballs = new ArrayList<Sprite>();
    public static Pane fireballPane = new Pane();
    public static Pane livesPane = new Pane();


    public Player(String type, Image image, double x, double y) throws FileNotFoundException {
        super(type, image, x, y);
    }

    void fireUpdate() {
        for (int i = 0 ; i < fireballs.size(); i++) {
            if (fireballs.get(i).type.contains("R") && fireballs.get(i).getLayoutX() < 600 - MiniGame.level*100) {
                fireballs.get(i).relocate(fireballs.get(i).getLayoutX() + 10, fireballs.get(i).getLayoutY());
            } else if (fireballs.get(i).type.contains("L") && fireballs.get(i).getLayoutX() > -600 + MiniGame.level*100) {
                fireballs.get(i).relocate(fireballs.get(i).getLayoutX() - 10, fireballs.get(i).getLayoutY());
            } else {
                if (MiniGame.score > 0){
                    MiniGame.score--;
                }
                fireballs.remove(i);
                fireballPane.getChildren().remove(i);
            }
        }
    }

    void fire(KeyCode direction) {
        if (!firing) {
            Sprite newFireball;
            if (direction == KeyCode.RIGHT) {
                newFireball = new Sprite("fireballR", fireballImg, 660, 440);
            } else {
                newFireball = new Sprite("fireballL", fireballImg, 570, 440);
                newFireball.turnLeft();
            }
            firing = true;
            fireballs.add(newFireball);
            fireClip.play();
            fireballPane.getChildren().add(newFireball);
            MiniGame.gameRoot.getChildren().remove(this);
            if (direction == KeyCode.RIGHT){
                firePlayer.turnRight();
            } else {
                firePlayer.turnLeft();
            }
            MiniGame.gameRoot.getChildren().add(firePlayer);
        }
    }

    void doneFire(KeyCode direction) {
        if (direction == KeyCode.RIGHT) {
            this.turnRight();
        } else {
            this.turnLeft();
        }
        MiniGame.gameRoot.getChildren().add(this);
        MiniGame.gameRoot.getChildren().remove(firePlayer);
        firing = false;
    }

    public static void resetLives() {
        livesPane.getChildren().clear();
        ImageView life1 = new ImageView(heartImg);
        ImageView life2 = new ImageView(heartImg);
        ImageView life3 = new ImageView(heartImg);
        life1.setX((MiniGame.W_WIDTH-heartImg.getWidth() - 100)/2);
        life1.setY(560);
        life2.setX((MiniGame.W_WIDTH-heartImg.getWidth())/2);
        life2.setY(560);
        life3.setX((MiniGame.W_WIDTH-heartImg.getWidth() + 100)/2);
        life3.setY(560);
        livesPane.getChildren().addAll(life1, life2, life3);
    }
}
