/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws the pumpkin cells that will make up the Halloween border.
 */

package game;

import java.awt.*;
import java.net.URL;

import javax.swing.*;


public class PumpkinCell extends StandardCell
{

	private static final long serialVersionUID = -7516770430598723960L;


	public PumpkinCell()
	{
		super();
	} // end constructor
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		URL url = SnakeCharmer.class.getResource("images/JackOLantern.gif");
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
		frame.setTitle("Pumpkin Cell");
		frame.setLocation(450, 300);
		frame.add(new PumpkinCell());
		frame.pack();
		frame.setVisible(true);
		
	} // end main

} // end PumpkinCell class
