import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Enemy extends Sprite {

    private static Image enemyImg = new Image(Enemy.class.getResourceAsStream("./img/enemy.png"));
    public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static Pane enemyPane = new Pane();
    public static int enemiesLeft;
    public static Text enemiesLeftText = MiniGame.createText("Enemies Left: " + enemiesLeft, "1000", 600, 25, TextAlignment.LEFT);

    boolean stepBack = false;
    double speed;

    public Enemy(String type, Image image, double x, double y, double speed) {
        super(type, image, x, y);
        this.speed = speed;
    }

    void move() {
        if (type.contains("L")) {
            this.relocate(this.getLayoutX() + speed*1, this.getLayoutY());
        } else {
            this.relocate(this.getLayoutX() - speed*1, this.getLayoutY());
        }
    }

    void moveBack() {
        if (type.contains("L")) {
            this.relocate(this.getLayoutX() - 10, this.getLayoutY());
        } else {
            this.relocate(this.getLayoutX() + 10, this.getLayoutY());
        }
    }

    public static void spawnEnemy() {
        if (Math.random() < 0.5) {
            Enemy newEnemy = new Enemy("enemyR", enemyImg, MiniGame.W_WIDTH, 325, MiniGame.level*3);
            enemies.add(newEnemy);
            enemyPane.getChildren().add(newEnemy);
        } else {
            Enemy newEnemy = new Enemy("enemyL", enemyImg,-210, 325, MiniGame.level*3);
            newEnemy.turnRight();
            enemies.add(newEnemy);
            enemyPane.getChildren().add(newEnemy);
        }
    }

    public static void updateEnemy() {
        // randomly spawn enemies
        MiniGame.time += 0.016* MiniGame.level + Math.random();
        if (MiniGame.time >= 2 &&  enemiesLeft - enemyPane.getChildren().size() > 0) {
            Enemy.spawnEnemy();
            MiniGame.time = 0;
        }

        // step back and update lives
        enemies.forEach( enemy -> {
            if (enemy.type.contains("R") && enemy.getLayoutX() < -600) {
                Player.livesPane.getChildren().remove(Player.livesPane.getChildren().size()-1);
            } else if (enemy.type.contains("L") && enemy.getLayoutX() > 600) {
                Player.livesPane.getChildren().remove(Player.livesPane.getChildren().size()-1);
            }
            if (enemy.type.contains("R") && (enemy.getLayoutX() < -600 || (enemy.stepBack == true && enemy.getLayoutX() < -500))) {
                enemy.stepBack = true;
                enemy.moveBack();
            } else if (enemy.type.contains("L") && (enemy.getLayoutX() > 600 || (enemy.stepBack == true && enemy.getLayoutX() > 500))){
                enemy.stepBack = true;
                enemy.moveBack();
            } else {
                enemy.stepBack = false;
            }
            if (enemy.stepBack == false){
                enemy.move();
            }
        });

        // enemies die
        String killSound = Enemy.class.getClassLoader().getResource("./sound/kill_sound.mp3").toString();
        AudioClip killClip = new AudioClip(killSound);

        for (int i = 0; i < enemyPane.getChildren().size(); i++){
            for (int j = 0; j < Player.fireballPane.getChildren().size(); j++) {
                if (enemyPane.getChildren().get(i).getBoundsInParent().intersects(Player.fireballPane.getChildren().get(j).getBoundsInParent())) {
                    MiniGame.score++;
                    MiniGame.scoreText.setText("Score: " + MiniGame.score);
                    killClip.play();
                    enemiesLeft--;
                    enemiesLeftText.setText("Enemies Left: " + enemiesLeft);
                    enemies.remove(i);
                    enemyPane.getChildren().remove(i);
                    Player.fireballs.remove(j);
                    Player.fireballPane.getChildren().remove(j);
                    break;
                }
            }
        }
    }
}
