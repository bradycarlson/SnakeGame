/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Class builds a JPanel for the sides of the border (depending on type).
 */

package game;

import java.awt.*;
import javax.swing.*;

public class GameBorderSide extends JPanel
{

	private static final long serialVersionUID = -5612049146275946003L;
	
	// standard size
	private Dimension BORDER_SIZE = new Dimension(20, 500);
	
	// cell arrays for each game border type
	private StandardCell[] cells = new StandardCell[GameBoard.ROWS];
	private StandardCell[] spikeCells = new SpikeCell[GameBoard.ROWS];
	private StandardCell[] fireCells = new FireCell[GameBoard.ROWS];
	private StandardCell[] pumpkinCells = new PumpkinCell[GameBoard.ROWS];
	private StandardCell[] santaCells = new SantaCell[GameBoard.ROWS];
	
	// constructor sets up and defines border sides based on type
	public GameBorderSide(BorderType type)
	{
		setLayout(new GridLayout(GameBoard.ROWS, 1, 0, 0));
		
		if (type == BorderType.Spike)
		{
			for (int x = 0; x < GameBoard.ROWS; x++)
			{
				spikeCells[x] = new SpikeCell();
				add(spikeCells[x]);
			}		
		}
		else if (type == BorderType.Fire)
		{
			for (int x = 0; x < GameBoard.ROWS; x++)
			{
				fireCells[x] = new FireCell();
				add(fireCells[x]);
			}
		}
		else if (type == BorderType.Pumpkin) 
		{
			for (int x = 0; x < (GameBoard.ROWS); x++)
			{
				pumpkinCells[x] = new PumpkinCell();
				add(pumpkinCells[x]);
			}
		}
		else if (type == BorderType.Santa)
		{
			for (int x = 0; x < (GameBoard.ROWS); x++)
			{
				santaCells[x] = new SantaCell();
				add(santaCells[x]);
			}
		}
		else
		{
			for (int x = 0; x < GameBoard.ROWS; x++)
			{
				cells[x] = new StandardCell();
				add(cells[x]);
			}
		}
		
		setPreferredSize(BORDER_SIZE);
		setOpaque(false);
	} // end constructor
	
	
	// main for testing
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GameBorder");
		frame.setLocation(200, 200);
		frame.add(new GameBorderSide(BorderType.Spike));
		frame.pack();
		frame.setVisible(true);
		
	} // end main


} // end GameBorderSide class
