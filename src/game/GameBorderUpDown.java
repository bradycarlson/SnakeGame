/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Class builds a JPanel for the top and bottom of the border (depending on type).
 */

package game;

import java.awt.*;
import javax.swing.*;



public class GameBorderUpDown extends JPanel
{

	private static final long serialVersionUID = 8205035769357147L;

	// size of top/bottom borders
	private Dimension BORDER_SIZE = new Dimension(540, 20);
	
	// cell arrays for top/bottom of border
	private StandardCell[] cells = new StandardCell[GameBoard.COLS + 2];
	private SpikeCell[] spikeCells = new SpikeCell[GameBoard.COLS + 2];
	private FireCell[] fireCells = new FireCell[GameBoard.COLS + 2];
	private StandardCell[] pumpkinCells = new PumpkinCell[GameBoard.COLS + 2];
	private StandardCell[] santaCells = new SantaCell[GameBoard.COLS + 2];
	
	// constructor sets up top/bottom borders based on border type
	public GameBorderUpDown(BorderType type)
	{
		setLayout(new GridLayout(1, GameBoard.COLS, 0, 0));
		
		if (type == BorderType.Spike)
		{
			for (int x = 0; x < (GameBoard.COLS + 2); x++)
			{
				spikeCells[x] = new SpikeCell();
				add(spikeCells[x]);
			}
		}
		else if (type == BorderType.Fire)
		{
			for (int x = 0; x < (GameBoard.COLS + 2); x++)
			{
				fireCells[x] = new FireCell();
				add(fireCells[x]);
			}
		}
		else if (type == BorderType.Pumpkin) 
		{
			for (int x = 0; x < (GameBoard.COLS +2); x++)
			{
				pumpkinCells[x] = new PumpkinCell();
				add(pumpkinCells[x]);
			}
		}
		else if (type == BorderType.Santa) 
		{
			for (int x = 0; x < (GameBoard.COLS +2); x++)
			{
				santaCells[x] = new SantaCell();
				add(santaCells[x]);
			}
		}
		else
		{
			for (int x = 0; x < (GameBoard.COLS + 2); x++)
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
		frame.add(new GameBorderUpDown(BorderType.Spike));
		frame.pack();
		frame.setVisible(true);
		
	} // end main

} // end GameBorderUpDown class
