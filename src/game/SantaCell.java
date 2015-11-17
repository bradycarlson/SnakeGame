/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws the santa cells that will make up the Christmas border.
 */

package game;

import java.awt.*;
import java.net.URL;

import javax.swing.*;


public class SantaCell extends StandardCell
{

	private static final long serialVersionUID = -7516770430598723960L;


	public SantaCell()
	{
		super();
	} // end constructor
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		URL url = SnakeCharmer.class.getResource("images/angrysanta.png");
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
		frame.setTitle("Christmas Cell");
		frame.setLocation(450, 300);
		frame.add(new SantaCell());
		frame.pack();
		frame.setVisible(true);
		
	} // end main

} // end SantaCell class