import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/*Andrew Codispoti
 * using Slick2d library with lwjgl
 */
public class minefield extends BasicGame {
	public static TiledMap grassMap;
	private Animation sprite, up, down, left, right, explosion;
	private static float x = 34f, y = 34f;
	public static AppGameContainer app;

	// initalizes my sound class which plays the explosion sound
	static sound explosionsound= new sound(2);

	// intializes the background music
	static sound song = new sound(1);
	
	
	// stores dimensions for player
	// state of 0 means playing, 1 means stopped
	public int gamestate = 0;
	
	
	// block #'s
	private int blockx, blocky;
	
	
	// the image for the gauge and arrow
	Image gauge, arrow;
	
	
	// arrow state 0 = safe, 1= medium, 2=danger
	int arrowstate = 1;
	
	
	//Thread which runs the game
	static Thread game;
	// stores the current level
	private int levelCount = 1;
	/*
	 * The collision map indicating which tiles block movement - generated based
	 * on tile properties
	 */
	private static boolean[][] blocked;
	private static final int SIZE = 34;
	// hold the coordinates of the points where there are mines
	private static boolean[][] explosive;
	// hold the coordinates of the points to go to the next level
	private static boolean[][] nextLevel;

	public minefield() {
		super("Minefield");
	}

	// main method to create the main game window and run the thread for the
	// game

	public void start() {
		game = new Thread(new Runnable() {

			@Override
			public void run() {
				runGame();
			}
		});
		game.run();
	}

	@SuppressWarnings("deprecation")
	public void pause() {
		game.suspend();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		game.destroy();
	}

	public static void runGame() {
		try {
			app = new AppGameContainer(new minefield());
			app.setDisplayMode(700, 700, false);
			app.start();
			sound.playSound();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// initialization of all the elements
	@Override
	public void init(GameContainer container) throws SlickException {
		gauge = new Image("data/gauge.png");
		arrow = new Image("data/arrow.png");

		// arrays to hold images for movement
		Image[] movementUp = { new Image("data/back1.bmp"),
				new Image("data/back2.bmp"), new Image("data/back3.bmp"),
				new Image("data/back4.bmp"), new Image("data/back5.bmp"),
				new Image("data/back6.bmp"), new Image("data/back7.bmp"),
				new Image("data/back8.bmp"), new Image("data/back9.bmp"),
				new Image("data/back10.bmp") };

		Image[] movementDown = { new Image("data/front1.bmp"),
				new Image("data/front2.bmp"), new Image("data/front3.bmp"),
				new Image("data/front4.bmp"), new Image("data/front5.bmp"),
				new Image("data/front6.bmp"), new Image("data/front7.bmp"),
				new Image("data/front8.bmp"), new Image("data/front9.bmp"),
				new Image("data/front10.bmp") };

		Image[] movementLeft = { new Image("data/left1.bmp"),
				new Image("data/left2.bmp"), new Image("data/left3.bmp"),
				new Image("data/left4.bmp"), new Image("data/left5.bmp"),
				new Image("data/left6.bmp"), new Image("data/left7.bmp"),
				new Image("data/left8.bmp"), new Image("data/left9.bmp"),
				new Image("data/left10.bmp") };

		Image[] movementRight = { new Image("data/right1.bmp"),
				new Image("data/right2.bmp"), new Image("data/right3.bmp"),
				new Image("data/right4.bmp"), new Image("data/right5.bmp"),
				new Image("data/right6.bmp"), new Image("data/right7.bmp"),
				new Image("data/right8.bmp"), new Image("data/right9.bmp"),
				new Image("data/right10.bmp") };

		// holds the duration for each of the sprites animation to stay on
		// screen
		int[] duration = { 80, 80, 80, 80, 80, 80, 80, 80, 80, 80 };

		// image array holding the explosion sprite
		Image[] explode = new Image[25];

		// the duration of each explosion image being displayed
		int[] durationexplosion = new int[25];

		// Initialize explosion duration as well as each image for the explosion
		for (int i = 0; i < durationexplosion.length; i++) {
			durationexplosion[i] = 30;
			explode[i] = new Image("data/explosion" + (i + 1) + ".bmp");
		}
		// initialze the first map
		grassMap = new TiledMap("data/map1.tmx");

		// initialize an animation object for each of the states of a player
		up = new Animation(movementUp, duration, false);
		down = new Animation(movementDown, duration, false);
		left = new Animation(movementLeft, duration, false);
		right = new Animation(movementRight, duration, false);
		explosion = new Animation(explode, durationexplosion, true);

		// Original orientation of the sprite. It will look right.
		sprite = right;

		// build a collision map based on tile properties in the TileD map for
		// both blocking and explosives as well as new level

		blocked = new boolean[grassMap.getWidth()][grassMap.getHeight()];
		explosive = new boolean[grassMap.getWidth()][grassMap.getHeight()];
		nextLevel = new boolean[grassMap.getWidth()][grassMap.getHeight()];

		// parse the map for the special tiles
		parseMap();

	}

	/**
	 * Player uses the up, down, left, and right arrow keys to move
	 */
	/*
	 * This is the main method of the game that will determine the players
	 * locaiton, user input, and whether a player is interacting with a special
	 * tile
	 */
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		if (gamestate == 0) {
			blockx = (int) x / SIZE;
			blocky = (int) y / SIZE;
			System.out.println("X=" + blockx);
			System.out.println("Y=" + blocky);
			/*
			 * Obtain input from the user and move the character accordingly
			 */
			Input input = container.getInput();
			System.out.println("Distance to mine:"
					+ determineDistanceToMine(x, y));
			if (input.isKeyDown(Input.KEY_UP)) {
				sprite = up;
				if (!isBlocked(x, y - delta * 0.1f)) {
					sprite.update(delta);
					// The lower the delta the slowest the sprite will animate.
					y -= delta * 0.1f;
				}
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				sprite = down;
				if (!isBlocked(x, y + SIZE + delta * 0.1f)) {
					sprite.update(delta);
					y += delta * 0.1f;
				}
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				sprite = left;
				if (!isBlocked(x - delta * 0.1f, y)) {
					sprite.update(delta);
					x -= delta * 0.1f;
				}
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				sprite = right;
				if (!isBlocked(x + SIZE + delta * 0.1f, y)) {
					sprite.update(delta);
					x += delta * 0.1f;
				}
			} else if (input.isKeyDown(Input.KEY_ESCAPE)) {
				stop();
			}

			// determine the danger level and move the arrow accordingly
			if (determineDistanceToMine(x, y) <= 2.5 && arrowstate != 2) {
				arrow.setRotation(90);
				arrow.draw();
				arrowstate = 2;
				System.out.println("DANGER!!!");
			} else if ((determineDistanceToMine(x, y) < 4.5)
					&& (determineDistanceToMine(x, y) > 2.5) && arrowstate != 1) {
				arrow.setRotation(0);
				arrow.draw();

				arrowstate = 1;
			} else if (determineDistanceToMine(x, y) >= 4.5 && arrowstate != 0) {
				arrow.setRotation(270);
				arrow.draw();
				arrowstate = 0;
			}

			// determine if the player is on a new level block and change the
			// level accordingly
			/**
			 * Levels are labeled in a sequential order where the location is
			 * data/map#.tmx this enables new levels to be added easily by just
			 * appending a new digit to the end of the map in a sequential order
			 * which the game will run over and load
			 */
			if (isNewLevel(x + 1, y - 1)) {
				levelCount++;
				grassMap = new TiledMap("data/map" + levelCount + ".tmx");
				blocked = new boolean[grassMap.getWidth()][grassMap.getHeight()];
				explosive = new boolean[grassMap.getWidth()][grassMap
						.getHeight()];
				nextLevel = new boolean[grassMap.getWidth()][grassMap
						.getHeight()];
				grassMap.render(0, 0);
				x = 0;
				y = 0;
				parseMap();
			}

			// determine if the block is explosive and play the animation
			if (isExplosive(x + 1, y)) {
				System.out.println("Explosion!");
				gamestate = 1;
				try {
					sound.pause();
					explosionsound = new sound(2);
					explosionsound.playSound();
					Thread.sleep(1700);
				} catch (Exception e) {
				}
			}
			// run distance to mine method and update indicator

		} else {
			sprite = explosion;
			sprite.update(delta);
		}
	}

	// Initially renders the screen when the game initialized
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		grassMap.render(0, 0);
		gauge.draw(500, 0);
		arrow.draw(583, 23);
		sprite.draw((int) x, (int) y);
	}

	// determines if a tile is blocked and return true or false for the
	// condition
	private boolean isBlocked(float x, float y) {
		int xBlock = (int) (x / SIZE) + 1;
		int yBlock = (int) (y / SIZE) + 2;
		return blocked[xBlock][yBlock];
	}

	// determines if a block is explosive and return true or false for the
	// condition
	private boolean isExplosive(float x, float y) {
		int xBlock = (int) (x / SIZE) + 1;
		int yBlock = (int) (y / SIZE) + 2;
		return explosive[xBlock][yBlock];
	}

	// determine the blocks that are responsible for levelling and return true
	// or false for the condition
	private boolean isNewLevel(float x, float y) {
		int xBlock = (int) (x / SIZE) + 1;
		int yBlock = (int) (y / SIZE) + 2;
		return nextLevel[xBlock][yBlock];
	}

	// Method that checks all the tiles on the map for their properties and sets
	// the respective location on an array to either true or false for the
	// properties
	private double determineDistanceToMine(float x, float y) {
		double distance = 0, tempDistance;
		int closestMineX = 10000, closestMineY = 10000;
		// iterate over all of the mines and determine the distances
		for (int j = 0; j < explosive.length; j++) {
			for (int i = 0; i < explosive.length; i++) {
				if (explosive[i][j]) {
					tempDistance = Math.sqrt(Math.pow(i - blockx, 2)
							+ Math.pow(j - blocky, 2));

					// sets the closest distance to the proximity
					if (tempDistance < Math
							.sqrt(((closestMineX * closestMineX))
									+ ((closestMineY * closestMineY)))) {
						closestMineX = i;
						closestMineY = j;
						distance = tempDistance - 1.5;
					}

				}
			}
		}
		// determines the direct distance to closest mine

		return distance;
	}

	// run a loop to determine the properties of every tile
	// if it is blocked, character can't move
	// if it is explosives, there will be an explosion
	public void parseMap() {
		purgeVariables();
		for (int xAxis = 0; xAxis < grassMap.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < grassMap.getHeight(); yAxis++) {
				int tileID = grassMap.getTileId(xAxis, yAxis, 0);
				String value = grassMap.getTileProperty(tileID, "blocked",
						"false");
				String explosion = grassMap.getTileProperty(tileID,
						"explosive", "false");
				String level = grassMap.getTileProperty(tileID, "level",
						"false");
				if ("true".equals(value)) {
					blocked[xAxis][yAxis] = true;
				}
				if ("true".equals(explosion)) {
					explosive[xAxis][yAxis] = true;
					System.out.println("mine is at " + xAxis + "," + yAxis);
				}
				if ("true".equals(level)) {
					nextLevel[xAxis][yAxis] = true;
				}
			}
		}

	}

	// method to clear the used variables that hold the coordinates of the
	// special tiles
	public void purgeVariables() {

		for (int i = 0; i < grassMap.getWidth(); i++) {
			for (int x = 0; x < grassMap.getHeight(); x++) {
				nextLevel[i][x] = false;
				blocked[i][x] = false;
				explosive[i][x] = false;
			}
		}
		System.out.println("cleared");

	}
}
