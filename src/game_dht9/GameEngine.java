package game_dht9;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Breakout Game
 * 
 * @author David Tran
 */
public class GameEngine extends Application {
	public static final String TITLE = "JavaFX: Initial Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	public static final int WIDTH = 505;
	public static final int HEIGHT = 600;
	public static final Paint BACKGROUND = Color.BLACK;
	public static final int FRAMES_PER_SECOND = 120;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 10;
	public static final double GROWTH_RATE = 1.1;
	public double PADDLE_HEIGHT = 12;
	public double PADDLE_WIDTH = 60;
	
	static Stage primaryStage;
	public Timeline animation;

	private Scene myScene;
	private Bouncer myBouncer;
	private Paddle myPaddle1;
	private Paddle myPaddle2;
	private Brick myBrick;
	private List<Brick> myBricks = new ArrayList<>();
	private Group root;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		// attach scene to the stage and display it

		// create one top level collection to organize the things in the scene
		Group root = new Group();
		Scene level1 = setupGame(root, WIDTH, HEIGHT, BACKGROUND);
		
//		Group root2 = new Group();
//		Scene level2 = setupGame(root2, WIDTH,HEIGHT, BACKGROUND);
//		BrickLevel level1 = new BrickLevel();
//		Scene scene1 = scene1.init(WIDTH, HEIGHT, BACKGROUND);
//		BrickLevel level2 = new BrickLevel();
//		Scene scene2 = scene2.init(WIDTH, HEIGHT, BACKGROUND);
		
		Button button1= new Button("Go to scene 2");
		root.getChildren().add(button1);
//		
		button1.setOnAction(e -> changeScene(primaryStage, 0));
		
		
		primaryStage.setScene(level1);
		primaryStage.setTitle(TITLE);
		primaryStage.show();

		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

		System.out.println("scene set up");
	}
	
	public void changeScene(Stage primaryStage, int sceneNum) {
		Group root = new Group();
//		Scene level = setupGame(roota, WIDTH, HEIGHT, BACKGROUND);
//		animation.play();
		
		primaryStage.setScene(setupGame(root, WIDTH, HEIGHT, BACKGROUND));
		primaryStage.show();
		Button button1= new Button("Go to scene 2");
		root.getChildren().add(button1);

		button1.setOnAction(e -> changeScene(primaryStage, 0));
		
		// attach "game loop" to timeline to play it
//		KeyFrame frame1 = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
//		animation = new Timeline();
//		animation.setCycleCount(Timeline.INDEFINITE);
//		animation.getKeyFrames().add(frame1);
//		animation.play();
		
	}

	// Create the game's "scene": what shapes will be in the game and their starting
	// properties
	private Scene setupGame(Group root, int width, int height, Paint background) {

		// make some shapes and set their properties

		loadLevel1(root, width, height);

		Image ballImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		myBouncer = new Bouncer(ballImage, width, height);

		Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		myPaddle1 = new Paddle(paddleImage, (int) (width / 2 - PADDLE_WIDTH / 2), (int) (height - PADDLE_HEIGHT));
		myPaddle2 = new Paddle(paddleImage, (int) (width / 2 - PADDLE_WIDTH / 2), (int) 0);

		root.getChildren().add(myBouncer.getView());
		root.getChildren().add(myPaddle1.getView());
		root.getChildren().add(myPaddle2.getView());

		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);
		
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

		myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
		
		return myScene;
	}

	private void step(double elapsedTime) {
		// update attributes
		myBouncer.move(elapsedTime);
		myBouncer.bounce(myScene.getWidth(), myScene.getHeight());
		myPaddle1.move(elapsedTime);
		myPaddle2.move(elapsedTime);

		// check if ball intersects paddle
		if (myBouncer.getView().getBoundsInParent().intersects(myPaddle1.getView().getBoundsInParent())) {
			myBouncer.bounceOffPaddle(myPaddle1);
		}
		if (myBouncer.getView().getBoundsInParent().intersects(myPaddle2.getView().getBoundsInParent())) {
			myBouncer.bounceOffPaddle(myPaddle2);
		}

		// check if ball intersects brick
		int bricksHit = 0;
		int bricksLeft = 0;
		for (Brick myBrick : myBricks) {
			if (myBrick.getBoundsInParent().intersects(myBouncer.getView().getBoundsInParent())) {
				// simulate 1 bounce even if ball hits two bricks in 1 frame
				bricksHit++;
				if (bricksHit == 1) {
					myBouncer.bounceOffBrick(myBrick);
				}

//				System.out.println(myBrick.brickType);
				myBrick.decrementType();
				myBrick.setFill(myBrick.brickType.getColor());
			}
			// count non-permanent bricks left
			if (!myBrick.brickType.toString().equals("INFINITE")
					&& !(myBrick.brickType.toString().equals("DESTROYED"))) {
				bricksLeft++;
			}
		}
//		System.out.println("bricks left: " + bricksLeft);
		
		if (bricksLeft == 0) {
			System.out.println("YOU WIN");
//			Button button1= new Button("Go to scene 2");
//			root.getChildren().add(button1);
//			changeScene(0);
//			animation.stop();
//			animation.play();
//			changeScene(primaryStage, 0);
			return;
		}
	}

	/**
	 * Handle key press event
	 */
	private void handleKeyInput(KeyCode code) {
		myPaddle1.startPaddle1(code);
		myPaddle2.startPaddle2(code);
	}

	/**
	 * Handle key release event
	 */
	private void handleKeyRelease(KeyCode code) {
		myPaddle1.stopPaddle1(code);
		myPaddle2.stopPaddle2(code);
	}

	/**
	 * Load Level 1 Brick Layout
	 */
	private void loadLevel1(Group root, int screenWidth, int screenHeight) {
		Scanner s;
		int rows, cols;
		try {
			s = new Scanner(new File("Level1.txt"));
			rows = s.nextInt();
			cols = s.nextInt();
			int board[][] = new int[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					board[i][j] = s.nextInt();
					// System.out.print(board[i][j] + " ");

					// set bricks in scene, distribute bricks across the screen
					if (board[i][j] != 0) {
						myBrick = new Brick(j, i, board[i][j]);
						myBricks.add(myBrick);
						myBrick.setFill(myBrick.brickType.getColor());

						root.getChildren().add(myBrick);
					}
				}
			}
			s.close();

		} catch (FileNotFoundException e) {
			System.out.print("Error in text file.");
		}
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
