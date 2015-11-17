/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws the fire cells that will make up the fire border.
 */

package game;

import java.awt.*;
import java.net.URL;
import javax.swing.*;


public class FireCell extends StandardCell
{

	private static final long serialVersionUID = -7516770430598723960L;


	public FireCell()
	{
		super();
	} // end constructor
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(SnakeCharmer.boardColor);
		g2.fillRect(0, 0, GameBoard.CELL_SIZE, GameBoard.CELL_SIZE);
		URL url = SnakeCharmer.class.getResource("images/fire-wood.jpg");
		ImageIcon image = new ImageIcon(url);
		image = new ImageIcon(image.getImage().getScaledInstance(GameBoard.CELL_SIZE, GameBoard.CELL_SIZE, Image.SCALE_SMOOTH));
		image.paintIcon(this, g, 1, 1);

	}// end paintComponent
	
	
	// main for testing
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Fire Cell");
		frame.setLocation(450, 300);
		frame.add(new FireCell());
		frame.pack();
		frame.setVisible(true);
		
	} // end main

} // end FireCell class
