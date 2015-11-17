/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Builds a score board panel with score in center.
 */

package game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;


public class ScoreBoard extends JPanel
{

	private static final long serialVersionUID = -810435750898034710L;

	// variables for font, size and score
	private static final Font SCORE_FONT = new Font("Calibri", Font.BOLD, 48);
	private Dimension SIZE = new Dimension(540, 79);
	public int score;
	
	/**
	 * constructor sets score to zero and sets up JPanel
	 */
	public ScoreBoard()
	{
		score = 0;
		setPreferredSize(SIZE);
		setOpaque(false);
		
	} // ScoreBoard constructor

	/**
	 * to update score
	 * @param p - points (int) to add to score
	 * @return - updated score
	 */
	public int updateScore(int p)
	{
		return (score = score + p);
		
	} // end updateScore method
	
	/**
	 * draws score board
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(SnakeCharmer.lineColor);
		g2.fillRect(0, 0, SIZE.width, SIZE.height);

		g2.setColor(SnakeCharmer.boardColor);
		g2.drawLine(0, 0, SIZE.width, 0);
		g2.setFont(SCORE_FONT);
		centerText("Score: " + score, SIZE.width, SIZE.height, g);
		
	} // end paintComponent method

	/**
	 * draws the text (ie score) centered
	 * @param s - String text to draw
	 * @param width - width of score board
	 * @param height - height of score board
	 * @param g - 2DGraphics
	 */
	private void centerText(String s, int width, int height, Graphics g)
	{
		FontMetrics fmetrics = g.getFontMetrics();
		int x = (width - fmetrics.stringWidth(s)) / 2;
		int y = (fmetrics.getAscent() + (height - (fmetrics.getAscent() + fmetrics.getDescent())) / 2);
	    g.drawString(s, x, y);
		
	} // end centerText method
	
} // end ScoreBoard class
