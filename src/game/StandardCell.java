/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws the standard cell.
 */

package game;

import java.awt.*;
import javax.swing.*;

public class StandardCell extends JPanel
{

	private static final long serialVersionUID = 6370504686847343557L;
	protected static Dimension CELL_SIZE = new Dimension(20, 20);

	
	public StandardCell()
	{
		setPreferredSize(CELL_SIZE);
		setOpaque(false);
	} // end constructor
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(SnakeCharmer.boardColor);
		g2.fillRect(0, 0, CELL_SIZE.width, CELL_SIZE.height);
		
		g2.setColor(SnakeCharmer.lineColor);
		g2.drawRect(0, 0, CELL_SIZE.width, CELL_SIZE.height);
		g2.fillRect(5, 5, 10, 10);
		
		g2.setColor(SnakeCharmer.boardColor);
		g2.fillRect(7, 7, 6, 6);
		
	}// end paintComponent
	
	
	// main for testing
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Standard Cell");
		frame.setLocation(350, 300);
		frame.add(new StandardCell());
		frame.pack();
		frame.setVisible(true);
		
	} // end main
	

} // end StandardCell class