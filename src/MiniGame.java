import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MiniGame extends Application {

    final static int W_WIDTH = 1280;
    final static int W_HEIGHT = 640;

    private Group titleRoot = new Group();
    public static Group gameRoot = new Group();
    private Group gameOverRoot = new Group();
    private Group nextLevelRoot = new Group();
    private Group winGameRoot = new Group();
    private Group pauseRoot = new Group();

    final Image bkgImg = new Image(getClass().getResourceAsStream("./img/background2.png"));
    final Image idlePlayerImg  = new Image(getClass().getResourceAsStream("./img/idle.png"));

    public Player idlePlayer = new Player("idlePlayer", idlePlayerImg,(W_WIDTH-idlePlayerImg.getWidth())/2, 300);

    public static int level = 1;
    public static int time = 0;
    public static int score = 0;
    boolean win = false;

    public static Text scoreText = createText("Score: " + score, "50", 600, 25,TextAlignment.LEFT);
    Text finalScoreText = createText("Final Score: " + score, "CENTER", 275, 60, TextAlignment.CENTER);
    Text finalScoreText2 = createText("Final Score: " + score, "CENTER", 275, 60, TextAlignment.CENTER);
    Text nextLevelScoreText = createText("Total Score: " + score, "CENTER", 375, 60, TextAlignment.CENTER);
    Text nextLevelText = createText("WELL DONE!\nLevel " + level + " Completed", "CENTER", 200, 75, TextAlignment.CENTER);

    public MiniGame() throws FileNotFoundException {
    }

    public static Text createText(String s, String x, double y, int fontSize, TextAlignment textAlign) {
        Text text = new Text(s);
        text.setFont(Font.font("Krungthep", FontWeight.BOLD, fontSize));
        if (x == "CENTER"){
            text.setX((W_WIDTH - text.prefWidth(-1))/2);
        } else {
            double d = Double.parseDouble(x);
            text.setX(d);
        }
        text.setY(y);
        text.setFill(Color.WHITE);
        text.setTextAlignment(textAlign);

        return text;
    }

    private Group createTitle() {
        final ImageView bkgView = new ImageView(bkgImg);
        final Image logo = new Image(getClass().getResourceAsStream("./img/logo.png"));

        ImageView logoView = new ImageView(logo);
        logoView.setX((W_WIDTH - (logoView.prefWidth(-1)))/2);
        logoView.setY(20);
        logoView.setPreserveRatio(true);

        Text instructions = createText("CONTROLS\nPress left/right arrows to fire\nENTER to start the game\nQ to Quit",
                "CENTER", 350, 35, TextAlignment.CENTER);
        Text name = createText("Anna Lok\n20774612", "1160", 550, 15, TextAlignment.RIGHT);
        titleRoot.getChildren().addAll(bkgView, logoView, instructions, name);

        return titleRoot;
    }

    private Group createGame() {
        final ImageView bkgView = new ImageView(bkgImg);

        Player.resetLives();

        gameRoot.getChildren().addAll(bkgView, idlePlayer, Enemy.enemyPane, Player.fireballPane, scoreText,
                Player.livesPane, Enemy.enemiesLeftText);

        return gameRoot;
    }

    private Group createGameOver() {
        final ImageView bkgView = new ImageView(bkgImg);

        Text gameOverText = createText("YOU LOST!", "CENTER", 200, 75, TextAlignment.CENTER);
        Text instructions = createText("ESC to go back to main menu\nENTER to restart the game\nQ to Quit",
                "CENTER", 350, 35, TextAlignment.CENTER);

        gameOverRoot.getChildren().addAll(bkgView, gameOverText,finalScoreText, instructions);
        return gameOverRoot;
    }

    private Group createNextLevel() {
        final ImageView bkgView = new ImageView(bkgImg);
        Text instructions = createText("ESC to go back to main menu\nENTER to advance to next level\nQ to Quit",
                "CENTER", 450, 35, TextAlignment.CENTER);

        nextLevelRoot.getChildren().addAll(bkgView, nextLevelText, nextLevelScoreText, instructions);
        return nextLevelRoot;
    }

    private Group createWinGame() {
        final ImageView bkgView = new ImageView(bkgImg);
        Text winGameText = createText("CONGRATS, YOU WIN!", "CENTER", 200, 75, TextAlignment.CENTER);
        Text instructions = createText("ESC to go back to main menu\nENTER to restart the game\nQ to Quit",
                "CENTER", 325, 35, TextAlignment.CENTER);

        winGameRoot.getChildren().addAll(bkgView, winGameText, finalScoreText2, instructions);
        return winGameRoot;
    }

    private Group createPause() {
        final ImageView bkgView = new ImageView(bkgImg);
        Text pauseText = createText("GAME PAUSED!", "CENTER", 200, 75, TextAlignment.CENTER);
        Text instructions = createText("P to unpause\nESC to go back to main menu\nENTER to restart the game\nQ to Quit",
                "CENTER", 325, 35, TextAlignment.CENTER);

        pauseRoot.getChildren().addAll(bkgView, pauseText, instructions);
        return pauseRoot;
    }

    public void restart() {
        Player.fireballPane.getChildren().clear();
        Player.fireballs.clear();
        Enemy.enemyPane.getChildren().clear();
        Enemy.enemies.clear();
        scoreText.setText("Score: " + score);
        Enemy.enemiesLeft = 5*level;
        Enemy.enemiesLeftText.setText("Enemies Left: " + Enemy.enemiesLeft);
    }

    @Override
    public void start(Stage stage) {

        // SCENE ONE: titleScene
        Scene titleScene = new Scene(createTitle(), W_WIDTH, W_HEIGHT);

        // SCENE TWO: miniGame
        Scene gameScene = new Scene(createGame(), W_WIDTH, W_HEIGHT);

        // SCENE THREE: gameOver
        Scene gameOverScene = new Scene(createGameOver(), W_WIDTH, W_HEIGHT);

        // SCENE FOUR: nextLevel
        Scene nextLevelScene = new Scene(createNextLevel(), W_WIDTH, W_HEIGHT);

        // SCENE FIVE: winGame
        Scene winGameScene = new Scene(createWinGame(), W_WIDTH, W_HEIGHT);

        // SCENE SIX: pauseGame
        Scene pauseScene = new Scene(createPause(), W_WIDTH, W_HEIGHT);

        String loseSound = getClass().getClassLoader().getResource("./sound/fail.wav").toString();
        AudioClip loseClip = new AudioClip(loseSound);
        String winSound = getClass().getClassLoader().getResource("./sound/won.mp3").toString();
        AudioClip winClip = new AudioClip(winSound);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Won game (3 levels)
                if (Enemy.enemiesLeft == 0 && Enemy.enemies.size() == 0 && level == 3){
                    this.stop();
                    finalScoreText2.setText("Final Score: " + score);
                    stage.setScene(winGameScene);
                    winClip.play();
                    win = true;
                    level = 1;
                } // Lost game
                else if (Player.livesPane.getChildren().size() == 0) {
                    this.stop();
                    finalScoreText.setText("Final Score: " + score);
                    stage.setScene(gameOverScene);
                    loseClip.play();
                } // Advance to next level
                else if (Enemy.enemiesLeft == 0 && Enemy.enemies.size() == 0 && !win) {
                    this.stop();
                    nextLevelScoreText.setText("Total Score: " + score);
                    nextLevelText.setText("WELL DONE!\nLevel " + level + " Completed");
                    stage.setScene(nextLevelScene);
                    winClip.play();
                } // Still playing current level
                else {
                    idlePlayer.fireUpdate();
                    Enemy.updateEnemy();
                    scoreText.setText("Score: " + score);
                }
            }
        };

        // SCENE ONE: titleScene
        titleScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case DIGIT1:
                    level = 1;
                    break;
                case DIGIT2:
                    level = 2;
                    break;
                case DIGIT3:
                    level = 3;
                    break;
            }
            switch (event.getCode()) {
                case ENTER:
                    level = 1;
                    score = 0;
                    restart();
                    Player.resetLives();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case Q:
                    stage.close();
                    break;
                case DIGIT1:
                case DIGIT2:
                case DIGIT3:
                    score = 0;
                    restart();
                    Player.resetLives();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
            }
        });

        // SCENE TWO: gameScene
        gameScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case RIGHT:
                case LEFT:
                    idlePlayer.fire(event.getCode());
                    break;
                case P:
                    timer.stop();
                    stage.setScene(pauseScene);
                    break;
            }
        });

        gameScene.setOnKeyReleased( event -> {
            switch (event.getCode()) {
                case RIGHT:
                case LEFT:
                    idlePlayer.doneFire(event.getCode());
                    break;
            }
        });

        // SCENE SIX: PauseScene
        pauseScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case P:
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case ESCAPE:
                    stage.setScene(titleScene);
                    break;
                case ENTER:
                    level = 1;
                    score = 0;
                    restart();
                    Player.resetLives();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case Q:
                    stage.close();
                    break;
            }
        });

        // SCENE THREE: gameOverScene
        gameOverScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    stage.setScene(titleScene);
                    break;
                case ENTER:
                    level = 1;
                    score = 0;
                    restart();
                    Player.resetLives();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case Q:
                    stage.close();
                    break;
            }
        });

        // SCENE FOUR: nextLevelScene
        nextLevelScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    stage.setScene(titleScene);
                    break;
                case ENTER:
                    level++;
                    restart();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case Q:
                    stage.close();
                    break;
            }
        });

        // SCENE FIVE: winGameScene
        winGameScene.setOnKeyPressed( event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    win = false;
                    stage.setScene(titleScene);
                    break;
                case ENTER:
                    win = false;
                    level = 1;
                    score = 0;
                    restart();
                    Player.resetLives();
                    stage.setScene(gameScene);
                    timer.start();
                    break;
                case Q:
                    stage.close();
                    break;
            }
        });

        // starting scene is titleScene
        stage.setWidth(W_WIDTH);
        stage.setHeight(W_HEIGHT);
        stage.setTitle("Monster vs Monster");
        stage.setScene(titleScene);
        stage.setResizable(false);
        stage.show();
    }
}