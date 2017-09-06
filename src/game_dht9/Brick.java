package game_dht9;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {

	public static final int BRICK_WIDTH = 50;
	public static final int BRICK_HEIGHT = 25;
	public static final int BRICK_GAP = 5;
	Type brickType;

	enum Type {
		BARRIER(10, Color.WHITE), INFINITE(8, Color.GRAY), HIGH(3, Color.web("#FF007F")), MEDIUM(2, Color.web("#FF66B2")), LOW(1,
				Color.web("#FFCCE5")), DESTROYED(0, Color.WHITE);
		private int health;
		private Color color;

		Type(int health, Color color) {
			this.health = health;
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		public int getHealth() {
			return health;
		}
		
	}

	public Brick(double x, double y, int brickNum , int gap) {
		// set brick location
		this.setX(x * BRICK_WIDTH + gap);
		this.setY(y * BRICK_HEIGHT);

		// set brick attributes
		this.setWidth(BRICK_WIDTH - gap);
		this.setHeight(BRICK_HEIGHT - gap);

		// set brick health
		this.setBrickType(brickNum);
//		System.out.println(this.brickType.toString());
//		 System.out.print(this.brickType.getHealth() + " ");
		// this.brickType = brickType;
	}

	public void setBrickType(int brickNum) {
		switch (brickNum) {
		case 10:
			this.brickType = Type.BARRIER;
			break;
		case 8:
			this.brickType = Type.INFINITE;
			break;
		case 3:
			this.brickType = Type.HIGH;
			break;
		case 2:
			this.brickType = Type.MEDIUM;
			break;
		case 1:
			this.brickType = Type.LOW;
			break;
		default:
			this.brickType = Type.DESTROYED;
			this.destroyBrick();
			break;
		}
	}

	public void decrementType() {
		switch (this.brickType.health) {
		case 10:
			break;
		case 8:
			break;
		case 3:
			this.brickType = Type.MEDIUM;
			break;
		case 2:
			this.brickType = Type.LOW;
			break;
		case 1:
		default:
			brickType = Type.DESTROYED;
//			 System.out.println("BRICK DESTROYED");
			this.destroyBrick();
			break;
		}
	}

	public void destroyBrick() {
		this.setX(-1);
		this.setY(-1);
		this.setWidth(0);
		this.setHeight(0);
		brickType = Type.DESTROYED;
//		System.out.println("BRICK DESTROYED");
	}

}
