/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Draws the spike cells that will make up the spike border.
 */

package game;

import java.awt.*;
import javax.swing.*;


public class SpikeCell extends StandardCell
{

	private static final long serialVersionUID = 1998527977945236109L;


	public SpikeCell()
	{
		super();
	} // end constructor
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(SnakeCharmer.boardColor);
		g2.fillRect(0, 0, 20, 20);
		
		g2.setColor(SnakeCharmer.lineColor);
		g2.drawRect(0, 0, 20, 20);
		
		Polygon p = new Polygon();
		p.addPoint(10, 0);
		p.addPoint(20, 10);
		p.addPoint(10, 20);
		p.addPoint(0, 10);
		g2.fill(p);
		
	}// end paintComponent
	
	
	// main for testing
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Spike Cell");
		frame.setLocation(400, 300);
		frame.add(new SpikeCell());
		frame.pack();
		frame.setVisible(true);
		
	} // end main
	

} // end SpikeCell class
