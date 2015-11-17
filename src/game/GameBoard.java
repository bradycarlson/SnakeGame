/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws game board, snake and food.
 */

package game;

import java.awt.*;
import javax.swing.JPanel;

public class GameBoard extends JPanel 
{
	
	private static final long serialVersionUID = 2605695378771312586L;
	
	// variables for columns, rows and sizes
	public static final int COLS = 25;
	public static final int ROWS = 25;
	public static final int CELL_SIZE = 20;
	private Dimension BOARD_SIZE = new Dimension(500, 500);
	
	// variables for the game and cells (food and snake)
	private SnakeCharmer game;
	private CellType[] cells;
	
	/**
	 * constructor sets game, cell types, and JPanel
	 * @param game - instance of SnakeCharmer
	 */
	public GameBoard(SnakeCharmer game) 
	{
		this.game = game;
		this.cells = new CellType[ROWS * COLS];
		
		setPreferredSize(BOARD_SIZE);
		setBackground(SnakeCharmer.boardColor);
		
	} // end constructor
	
	/**
	 * to clear game board (ie cell array)
	 */
	public void clearBoard() 
	{
		for(int i = 0; i < cells.length; i++) 
		{
			cells[i] = null;
		}
	} // end clearBoard method	

	/**
	 * draws components of game board
	 */
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(SnakeCharmer.lineColor);
		g.drawRect(0, 0, (getWidth() - 1), (getHeight() - 1));
		
		// draws game board lines
		for(int x = 0; x < COLS; x++) 
		{
			for(int y = 0; y < ROWS; y++) 
			{
				g.drawLine((x * CELL_SIZE), 0, (x * CELL_SIZE), getHeight());
				g.drawLine(0, (y * CELL_SIZE), getWidth(), (y * CELL_SIZE));
			} // end inner for
		} // end outer for
		
		// draws cell contains based on type
		for(int x = 0; x < COLS; x++) 
		{
			for(int y = 0; y < ROWS; y++) 
			{
				CellType type = getCell(x, y);
				
				if(type != null) 
				{
					drawCell((x * CELL_SIZE), (y * CELL_SIZE), type, g2);
				}
			} // end inner for
		} // end outer for
		
		// draws text is game is over, the game is paused or it's a new game
		if(game.isGameOver() || game.isNewGame() || game.isGamePaused()) 
		{	
			g2.setColor(SnakeCharmer.lineColor);
			
			int centerX = (getWidth() / 2);
			int centerY = (getHeight() / 2);

			String line1 = null;
			String line2 = null;
			
			if(game.isNewGame()) 
			{
				line1 = "Snake Game!";
				line2 = "Press Enter to Start";
			} 
			else if(game.isGameOver()) 
			{
				line1 = "Game Over!";
				line2 = "Press Enter to Restart";
			} 
			else if(game.isGamePaused()) 
			{
				line1 = "Paused";
				line2 = "Press P to Resume";
			}
			else
				System.out.println("Jeez, something went wrong...");

			g.setFont(new Font("Calibri", Font.BOLD, 36));
			g.drawString(line1, (centerX - g.getFontMetrics().stringWidth(line1) / 2), (centerY - 50));
			g.drawString(line2, (centerX - g.getFontMetrics().stringWidth(line2) / 2), (centerY + 50));
			
		} // end if
	} // end paintComponent method
	
	/**
	 * helper method decides which cell to draw (food or snake (head, body, tail))
	 * @param x - x coordinate of cell
	 * @param y - y coordinate of cell
	 * @param t - type of cell
	 * @param g2 - 2DGraphics
	 */
	private void drawCell(int x, int y, CellType t, Graphics2D g2) 
	{
		
		switch(t)
		{
			case Food:
				drawFood(x, y, g2);
				break;
			case SnakeBody:
				drawSnakeBody(x, y, g2);
				break;
			case SnakeHead:
				drawSnakeHead(x, y, g2);
				break;
			case SnakeTail:
				drawSnakeTail(x, y, g2);
				break;
			default:
				System.out.println("Oh boy, this should NOT happen...");
				break;
		} // end switch
	} // end drawCell method

	/**
	 * helper method to draw food
	 * @param x - x coordinate of food
	 * @param y - y coordinate of food 
	 * @param g2 - 2DGraphics
	 */
	private void drawFood(int x, int y, Graphics2D g2) 
	{
		// if board color is too red, food color is changed, otherwise it's red
		int redHueofBoard = SnakeCharmer.boardColor.getRed();
		
		if (redHueofBoard > 200)
			g2.setColor(Color.ORANGE);
		else
			g2.setColor(Color.RED);
		
		g2.fillOval((x + 2), (y + 2), 16, 16);	
		
	}// end drawFood method

	/**
	 * helper method that draws the snake body
	 * @param x - x coordinate of body
	 * @param y - y coordinate of body
	 * @param g2 - 2DGraphics
	 */
	private void drawSnakeBody(int x, int y, Graphics2D g2)
	{
		g2.setColor(SnakeCharmer.snakeColor1);
		g2.fillRect((x + 1), (y + 1), (CELL_SIZE - 1), (CELL_SIZE - 1));
		
		g2.setColor(SnakeCharmer.snakeColor2);
		g2.fillOval((x + 5), (y + 5), 9, 9);
		
	} // end drawSnakeBody method
	
	/**
	 * helper method that draws the snake head, updates based on direction of movement
	 * @param x - x coordinate of head
	 * @param y - y coordinate of head
	 * @param g2 - 2DGraphics
	 */
	private void drawSnakeHead(int x, int y, Graphics2D g2) 
	{
		//Depending on which way snake is moving decides which head to draw
		Direction direction = game.getCurrentDirection();
		
		g2.setColor(SnakeCharmer.snakeColor1);
		g2.fillOval(x, y, (CELL_SIZE - 1), (CELL_SIZE - 1));
		
		switch(direction) 
		{
			case Up:
				g2.fillRect((x + 1), (y + (CELL_SIZE / 2)), (CELL_SIZE - 1), ((CELL_SIZE / 2) + 1));
				g2.setColor(SnakeCharmer.snakeColor2);
				g2.fillRect((x + 6), (y + 7), 2, 8);
				g2.fillRect((x + 12), (y + 7), 2, 8);
				break;
			case Down:
				g2.fillRect((x + 1), (y+1), (CELL_SIZE - 1), ((CELL_SIZE / 2) - 1));
				g2.setColor(SnakeCharmer.snakeColor2);
				g2.fillRect((x + 6), (y + 5), 2, 8);
				g2.fillRect((x + 12), (y + 5), 2, 8);
				break;
			case Right:
				g2.fillRect((x + 1), (y + 1), (CELL_SIZE / 2), (CELL_SIZE - 1));
				g2.setColor(SnakeCharmer.snakeColor2);
				g2.fillRect((x + 5), (y + 6), 8, 2);
				g2.fillRect((x + 5), (y + 12), 8, 2);
				break;	
			case Left:
				g2.fillRect((x + (CELL_SIZE / 2)), (y + 1), (CELL_SIZE / 2), (CELL_SIZE - 1));
				g2.setColor(SnakeCharmer.snakeColor2);
				g2.fillRect((x + 5), (y + 6), 8, 2);
				g2.fillRect((x + 5), (y + 12), 8, 2);
				break;
		} // end switch
		
	} // end drawSnakeHead method
	
	/**
	 * helper method to draw snake tail
	 * @param x - x coordinate of tail
	 * @param y - y coordinate of tail
	 * @param g2 - 2DGraphics
	 */
	private void drawSnakeTail(int x, int y, Graphics2D g2) 
	{
		
		g2.setColor(SnakeCharmer.snakeColor1);
		g2.fillOval(x, y, CELL_SIZE, CELL_SIZE);
		
		g2.setColor(SnakeCharmer.snakeColor2);
		Polygon p = new Polygon();
		p.addPoint((x + (CELL_SIZE / 2)), y);
		p.addPoint((x + CELL_SIZE), (y + (CELL_SIZE / 2)));
		p.addPoint((x + (CELL_SIZE / 2)), (y + CELL_SIZE));
		p.addPoint(x, (y + (CELL_SIZE / 2)));
		g2.fillPolygon(p);
		
		g2.setColor(SnakeCharmer.snakeColor1);
		g2.fillOval((x + 5), (y + 5), 9, 9);
		

	} // end drawSnakeTail method
	
	/**
	 * to set the cell type of cell (food, snake body, snake head, snake tail, or null)
	 * @param x - x coordinate of cell
	 * @param y - y coordinate of cell
	 * @param type - type of cell
	 */
	public void setCell(int x, int y, CellType type) { cells[y * ROWS + x] = type; } // end setCell method
	
	/**
	 * to get the cell type of cell
	 * @param x - x coordinate of cell
	 * @param y - y coordinate of cell
	 * @return - type of cell
	 */
	public CellType getCell(int x, int y) { return cells[y * ROWS + x]; } // end getCell method
	
} // end GameBoard class
