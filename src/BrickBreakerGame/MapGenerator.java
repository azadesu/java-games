package BrickBreakerGame;

import java.awt.*;

public class MapGenerator {
	// attributes
	public int[][] map;
	// values in the map array
	public final static int ON_SCREEN = 1;
	public final static int OFF_SCREEN = 0;

	// the total space the bricks occupy
	private final int MAP_WIDTH = 540;
	private final int MAP_HEIGHT= 150;

	// dimensions of each brick
	private final int brickWidth;
	private final int brickHeight;

	// number of rows and columns in the map, set in the constructor
	// not changed, so final
	private final int rows;
	private final int columns;

	public MapGenerator (int rows, int columns) {
		this.rows = rows; this.columns = columns;
		map = new int[rows][columns];
		for(int row = 0; row < rows; row++){
			for(int col =0; col < columns; col++)	{
				map[row][col] = ON_SCREEN;
			}
		}
		brickWidth = MAP_WIDTH/columns;
		brickHeight = MAP_HEIGHT/rows;
	}

	public void draw(Graphics g) {
		for(int row = 0; row < rows; row++){
			for(int col = 0; col < columns; col++)	{
				if(map[row][col] == ON_SCREEN){
					g.setColor(Color.white);
					g.fillRect(col * brickWidth + 80, row * brickHeight + 50, brickWidth, brickHeight);

					//drawing the brick's outline with a black pen.
					g.setColor(Color.black);
					g.drawRect(col * brickWidth + 80, row * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}

	// getters and setters
	// set the value of the brick in the maps array
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}

	public int getBrickHeight() {
		return brickHeight;
	}
	public int getBrickWidth() {
		return brickWidth;
	}

	public int getRows() {
		return rows;
	}
	public int getColumns() {
		return columns;
	}

	public int getTotalBricks(){
		return rows * columns;
	}
}
