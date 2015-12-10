/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Main class.
 * Puts all aspects together (game board, score board, game clock, etc.)
 * and controls game play.
 */

package game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class SnakeCharmer extends JFrame 
{

	private static final long serialVersionUID = 5064920135382031197L;

	// size of overall game
	private Dimension SIZE = new Dimension(540, 600);
	
	// instances of game board, score board and game clock
	private GameBoard gameBoard;
	private ScoreBoard scoreBoard;
	private GameClock gameClock;
	
	// panels for borders
	private JPanel panel;
	private GameBorderUpDown upSideBorder;
	private GameBorderUpDown downSideBorder;
	private GameBorderSide leftSideBorder;
	private GameBorderSide rightSideBorder;

	// variable to know which border so appropriate sound is played
	private BorderType border;
	
	// variables for speed and speed constants
	private float speed;
	private static final float SLOW = 6.0f;
	private static final float MEDIUM = 9.0f;
	private static final float FAST = 12.0f;
	
	// lists for the snake's coordinates and directions
	private LinkedList<Point> snakeCoords;
	private LinkedList<Direction> directions;
	
	// boolean for new game, game over, game paused and game sound
	private boolean isNewGame;
	private boolean isGameOver;
	private boolean isGamePaused;
	private boolean hasSound;

	// variables for game colors
	public static Color boardColor = Color.BLACK;
	public static Color lineColor = Color.WHITE;
	public static Color snakeColor1 = Color.GREEN;
	public static Color snakeColor2 = Color.RED;
	
	// variables for frame time, minimum snake length and maximum available directions
	private static final long FRAME_TIME = 1000L / 50L;
	private static final int MIN_SNAKE_LENGTH = 2;
	private static final int MAX_DIRECTIONS = 3;

	// variable for random generator (to decide where to place food)
	private Random random;

	// variables for game sounds
//	private PlayClip gameOver = new PlayClip("sounds/GameOver.wav", true);
	private PlayClip gameStart = new PlayClip("sounds/GameStart.wav", true);
	private PlayClip hitBorder = new PlayClip("sounds/RunsIntoBorder.wav", true);
	private PlayClip hitFire = new PlayClip("sounds/RunsIntoFire.wav", true);
	private PlayClip hitSpike = new PlayClip("sounds/RunsIntoSpike.wav", true);
	private PlayClip hitSelf = new PlayClip("sounds/RunsIntoSelf.wav", true);
	private PlayClip snakeSong = new PlayClip("sounds/SnakeSong.wav", true);
	private PlayClip hitPumpkin = new PlayClip("sounds/RunsIntoPumpkin.wav", true);
	private PlayClip hitSanta = new PlayClip("sounds/RunsIntoSanta.wav", true);
	private PlayClip points2 = new PlayClip("sounds/woo.wav", true);
//	private PlayClip points = new PlayClip("sounds/Points.wav", true);

	/**
	 * constructor to initialize JFrame and game
	 */
	public SnakeCharmer() 
	{
		super();
		initialSetUp();
		
	} // end constructor
	
	/**
	 * initializes game and variables
	 */
	private void initialSetUp()
	{
		setLayout(new BorderLayout(0, 0));
		setResizable(false);
		setTitle("My Snake Game");
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{ public void windowClosing(WindowEvent e)
			{ exit(); }
		});
		
		setUpBoard();
		initializeVariables();
		makeMenu();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(((screenSize.width / 2) - (SIZE.width / 2)), 50);
		
		addKeyListener();
		
		pack();
		setVisible(true);
	} // end initialSetUp
	
	/**
	 * sets up game frame by adding game board, score board, 
	 * and borders
	 */
	private void setUpBoard()
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		
		gameBoard = new GameBoard(this);
		scoreBoard = new ScoreBoard();
		
		upSideBorder = new GameBorderUpDown(BorderType.Standard);
		downSideBorder = new GameBorderUpDown(BorderType.Standard);
		leftSideBorder = new GameBorderSide(BorderType.Standard);
		rightSideBorder = new GameBorderSide(BorderType.Standard);
		
		border = BorderType.Standard;
		
		panel.add(gameBoard, BorderLayout.CENTER);
		panel.add(upSideBorder, BorderLayout.NORTH);
		panel.add(downSideBorder, BorderLayout.SOUTH);
		panel.add(leftSideBorder, BorderLayout.EAST);
		panel.add(rightSideBorder, BorderLayout.WEST);
		
		add(panel, BorderLayout.NORTH);
		add(scoreBoard, BorderLayout.SOUTH);
				
	} // end setUpBoard method

	/**
	 * initializes game variables
	 */
	private void initializeVariables()
	{
		directions = new LinkedList<Direction>();
		snakeCoords = new LinkedList<Point>();
		random = new Random();
		isNewGame = true;
		isGamePaused = false;
		hasSound = true;
		speed = SLOW;
		scoreBoard.score = 0;
		gameClock = new GameClock(speed);
		gameClock.setPaused(true);
		
	} // end initializeVariables
	
	/**
	 * starts game logic/loop
	 */
	private void startGame() 
	{
		if (hasSound)
			snakeSong.play();
		
		while(true) 
		{
			long start = System.nanoTime();

			gameClock.updateClock();

			if(gameClock.hasElapsedCycle()) 
			{
				updateGame();
			}
			
			gameBoard.repaint();
			scoreBoard.repaint();
			
			long delta = (System.nanoTime() - start) / 1000000L;
			if (delta < FRAME_TIME) 
			{
				try 
				{
					Thread.sleep(FRAME_TIME - delta);
				} // end try 
				catch(Exception e)
				{
					e.printStackTrace();
				} // end catch
			} // end if			
			
		} // end while
		
	} // end startGame method

	/**
	 * updates game, checks for collisions
	 */
	private void updateGame() 
	{ 
		CellType collision = updateSnake();

		// if collision cell is food, play sound, update score, & place more food
		if(collision == CellType.Food)
		{
			if (hasSound)
				points2.play();
			
			scoreBoard.score = scoreBoard.updateScore(100);
			placeFood();
		} 
		// else if collision cell is body or tail, play sound and set game over and pause clock
		else if(collision == CellType.SnakeBody || collision == CellType.SnakeTail)
		{
			if (hasSound)
				hitSelf.play();
			
			isGameOver = true;
			isGamePaused = false;
			gameClock.setPaused(true);
		} 
		// else if collision cell is a border, play sound (depending on border), set game over
		// and pause clock
		else if (collision == CellType.Border )
		{
			if (hasSound)
			{
				if (border == BorderType.Pumpkin)
					hitPumpkin.play();
				else if (border == BorderType.Fire)
					hitFire.play();
				else if (border == BorderType.Santa)
					hitSanta.play();
				else if (border == BorderType.Spike)
					hitSpike.play();
				else
					hitBorder.play();
			}
			
			isGameOver = true;
			isGamePaused = false;
			gameClock.setPaused(true);

		}

	} // end updateGame method
	
	/**
	 * updates snake's coordinates, direction and placement
	 * @return - cell type
	 */
	private CellType updateSnake() 
	{
		// get direction and coordinates of head
		Direction direction = directions.peekFirst();
		Point head = new Point(snakeCoords.peekFirst());
		
		// based on direction move head
		switch(direction) 
		{
			case Up:
				head.y--;
				break;
			case Down:
				head.y++;
				break;
			case Right:
				head.x++;
				break;	
			case Left:
				head.x--;
				break;
		} // end switch
		
		// if after moving head the snake is outside of game board (ie hit border)
		// then return that cell is of type border
		if(head.x < 0 || head.x >= GameBoard.COLS || head.y < 0 || head.y >= GameBoard.ROWS) 
		{
			return CellType.Border;
		}
		
		// get the cell type of head position (after movement)
		CellType headCell = gameBoard.getCell(head.x, head.y);
		
		// if new head position isn't food and is greater than min length
		// then remove last coordinate (tail) 
		if(headCell != CellType.Food && snakeCoords.size() > MIN_SNAKE_LENGTH) 
		{
			Point tail = snakeCoords.removeLast();
			gameBoard.setCell(tail.x, tail.y, null);
		}
		
		// if new head position isn't body, then set old head to body and last coordinate to tail
		// then add/set new head and remove first direction in list
		if(headCell != CellType.SnakeBody) 
		{
			gameBoard.setCell(snakeCoords.peekFirst().x, snakeCoords.peekFirst().y, CellType.SnakeBody);
			gameBoard.setCell(snakeCoords.peekLast().x, snakeCoords.peekLast().y, CellType.SnakeTail);
			snakeCoords.push(head);
			gameBoard.setCell(head.x, head.y, CellType.SnakeHead);
			if(directions.size() > 1) 
			{
				directions.poll();
			}
		}
				
		return headCell;
	} // end updateSnake method
	
	/**
	 * resets speed (game) if user confirms speed change
	 */
	private void resetSpeed()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Changing speed will start a new game.  Is that OK?", "Start New Snake Game?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
		{
			resetGame();	
		}	
	} // end resetSpeed method
	
	/**
	 * resets game components/variables
	 */
	private void resetGame() 
	{		
		isNewGame = false;
		isGameOver = false;
		isGamePaused = false;

		Point head = new Point(GameBoard.COLS / 2, GameBoard.ROWS / 2);

		snakeCoords.clear();
		snakeCoords.add(head);

		gameBoard.clearBoard();
		gameBoard.setCell(head.x, head.y, CellType.SnakeHead);
			
		directions.clear();
		directions.add(Direction.Up);

		scoreBoard.score = 0;
		
		gameClock = new GameClock(speed);

		if (hasSound)
			gameStart.play();
		
		placeFood();
		
	} // end resetGame method
	
	/**
	 * exits game if user confirms
	 */
	private void exit()
	{
		if (JOptionPane.showConfirmDialog(this,
			"Are you sure you want to stop playing?", "End Snake Game?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
		
			System.exit(0);
	} // end exit method
	
	/**
	 * checks if game is new
	 * @return - true if new game, false otherwise
	 */
	public boolean isNewGame() { return isNewGame; } // end isNewGame method
	
	/**
	 * checks if game is over
	 * @return - true if game over, false otherwise
	 */
	public boolean isGameOver() { return isGameOver; } // end isGameOver method
	
	/**
	 * checks if game is paused
	 * @return - true if game paused, false otherwise
	 */
	public boolean isGamePaused() { return isGamePaused; } // end isGamePaused method
	
	/**
	 * gets current direction of snake 
	 * @return - the first direction is list (most recent)
	 */
	public Direction getCurrentDirection() { return directions.peek(); } // end getCurrentDirection method
	
	/**
	 * randomly places food on empty spot on game board
	 */
	private void placeFood()
	{
		int index = random.nextInt(GameBoard.COLS * GameBoard.ROWS - snakeCoords.size());
		
		int freeFound = -1;
		for(int x = 0; x < GameBoard.COLS; x++) 
		{
			for(int y = 0; y < GameBoard.ROWS; y++) 
			{
				CellType type = gameBoard.getCell(x, y);
				if(type == null || type == CellType.Food) 
				{
					if(++freeFound == index) 
					{
						gameBoard.setCell(x, y, CellType.Food);
						break;
					} // end inner if
				} // end outer if
			} // end inner for
		} // end outer for
	} // end placeFood method

	/**
	 * add the key listener to game
	 */
	private void addKeyListener()
	{
		addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				switch(e.getKeyCode()) 
				{
					case KeyEvent.VK_W:
					case KeyEvent.VK_UP:
						if(!isGamePaused && !isGameOver) 
						{
							if(directions.size() < MAX_DIRECTIONS) 
							{
								Direction last = directions.peekLast();
								if(last != Direction.Down && last != Direction.Up) 
								{
									directions.addLast(Direction.Up);
								}
							}
						}
						break;
					case KeyEvent.VK_S:
					case KeyEvent.VK_DOWN:
						if(!isGamePaused && !isGameOver) 
						{
							if(directions.size() < MAX_DIRECTIONS) 
							{
								Direction last = directions.peekLast();
								if(last != Direction.Up && last != Direction.Down) 
								{
									directions.addLast(Direction.Down);
								}
							}
						}
						break;				
					case KeyEvent.VK_A:
					case KeyEvent.VK_LEFT:
						if(!isGamePaused && !isGameOver) 
						{
							if(directions.size() < MAX_DIRECTIONS)
							{
								Direction last = directions.peekLast();
								if(last != Direction.Right && last != Direction.Left) 
								{
									directions.addLast(Direction.Left);
								}
							}
						}
						break;	
					case KeyEvent.VK_D:
					case KeyEvent.VK_RIGHT:
						if(!isGamePaused && !isGameOver) 
						{
							if(directions.size() < MAX_DIRECTIONS) 
							{
								Direction last = directions.peekLast();
								if(last != Direction.Left && last != Direction.Right) 
								{
									directions.addLast(Direction.Right);
								}
							}
						}
						break;
					case KeyEvent.VK_P:
						if(!isGameOver) 
						{
							isGamePaused = !isGamePaused;
							gameClock.setPaused(isGamePaused);
						}
						break;
					case KeyEvent.VK_ENTER:
						if(isNewGame || isGameOver) 
						{							
							resetGame();
						}
						break;
				} // end switch
			} // end keyPressed
		}); // end addKeyListener

	} // end addKeyListener class

	/**
	 * makes the game menu and menu options
	 */
	private void makeMenu()
	{
		// make menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// add game menu
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		menuBar.add(gameMenu);
		
		JMenuItem playNew = new JMenuItem("New Game", 'N');
		playNew.setToolTipText("Play a new game");
		playNew.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		gameMenu.add(playNew);
		playNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				newGame();
			}
		});
		
		gameMenu.addSeparator();
		
		JMenuItem exit = new JMenuItem("Exit", 'E');
		exit.setToolTipText("Exit Snake Game");
		exit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		gameMenu.add(exit);
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				exit();
			}
		});
		
		
		// add sound menu
		JMenu soundMenu = new JMenu("Sound");
		soundMenu.setMnemonic('S');
		menuBar.add(soundMenu);
		
		ButtonGroup soundGroup = new ButtonGroup();
		JRadioButtonMenuItem soundON = new JRadioButtonMenuItem("On", true);
		soundGroup.add(soundON);
		soundMenu.add(soundON);
		soundON.setToolTipText("Turn sound on");
		soundON.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		soundON.setMnemonic('S');
		soundON.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				hasSound = true;
			}
		});
		
		JRadioButtonMenuItem soundOFF = new JRadioButtonMenuItem("Off", false);
		soundGroup.add(soundOFF);
		soundMenu.add(soundOFF);
		soundOFF.setToolTipText("Turn sound off");
		soundOFF.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		soundOFF.setMnemonic('F');
		soundOFF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				hasSound = false;
			}
		});
		
		// add speed menu
		JMenu speedMenu = new JMenu("Speed");
		speedMenu.setMnemonic('D');
		menuBar.add(speedMenu);
		
		ButtonGroup speedGroup = new ButtonGroup();
		JRadioButtonMenuItem slow = new JRadioButtonMenuItem("Slow", true);
		speedGroup.add(slow);
		speedMenu.add(slow);
		slow.setToolTipText("Slow snake speed");
		slow.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
		slow.setMnemonic('W');
		slow.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				speed = SLOW;
				resetSpeed();
			}
		});
		
		JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium", false);
		speedGroup.add(medium);
		speedMenu.add(medium);
		medium.setToolTipText("Medium snake speed");
		medium.setAccelerator(KeyStroke.getKeyStroke("ctrl M"));
		medium.setMnemonic('M');
		medium.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				speed = MEDIUM;
				resetSpeed();
			}
		});
		
		JRadioButtonMenuItem fast = new JRadioButtonMenuItem("Fast", false);
		speedGroup.add(fast);
		speedMenu.add(fast);
		fast.setToolTipText("Fast snake speed");
		fast.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		fast.setMnemonic('F');
		fast.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				speed = FAST;
				resetSpeed();
			}
		});
		
		// add border menu
		JMenu borderMenu = new JMenu("Border");
		borderMenu.setMnemonic('B');
		menuBar.add(borderMenu);
		
		ButtonGroup borderGroup = new ButtonGroup();
		JRadioButtonMenuItem standard = new JRadioButtonMenuItem("Standard", true);
		borderGroup.add(standard);
		borderMenu.add(standard);
		standard.setToolTipText("Standard border");
		standard.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		standard.setMnemonic('D');
		standard.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				changeBorder(BorderType.Standard);
			}
		});
		
		JRadioButtonMenuItem spike = new JRadioButtonMenuItem("Spike", false);
		borderGroup.add(spike);
		borderMenu.add(spike);
		spike.setToolTipText("Spike border");
		spike.setAccelerator(KeyStroke.getKeyStroke("ctrl K"));
		spike.setMnemonic('K');
		spike.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				changeBorder(BorderType.Spike);
			}
		});
		
		JRadioButtonMenuItem fire = new JRadioButtonMenuItem("Fire", false);
		borderGroup.add(fire);
		borderMenu.add(fire);
		fire.setToolTipText("Fire border");
		fire.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		fire.setMnemonic('F');
		fire.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				changeBorder(BorderType.Fire);
			}
		});
		
		JRadioButtonMenuItem pumpkin = new JRadioButtonMenuItem("Halloween", false);
		borderGroup.add(pumpkin);
		borderMenu.add(pumpkin);
		pumpkin.setToolTipText("Halloween border");
		pumpkin.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
		pumpkin.setMnemonic('H');
		pumpkin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				changeBorder(BorderType.Pumpkin);
			}
		});
		
		JRadioButtonMenuItem santa = new JRadioButtonMenuItem("Christmas", false);
		borderGroup.add(santa);
		borderMenu.add(santa);
		santa.setToolTipText("Christmas border");
		santa.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		santa.setMnemonic('C');
		santa.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				changeBorder(BorderType.Santa);
			}
		});
		
		
		// add color menu
		JMenuItem colorMenu = new JMenu("Colors");
		colorMenu.setMnemonic('B');
		menuBar.add(colorMenu);

		JMenuItem changeColor3 = new JMenuItem("Board Color");
		colorMenu.add(changeColor3);
		changeColor3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				boardColor = JColorChooser.showDialog(SnakeCharmer.this, "Choose a color", boardColor);
				gameBoard.setBackground(boardColor);
				scoreBoard.setBackground(boardColor);

				panel.repaint();
				gameBoard.repaint();
				scoreBoard.repaint();
			}
		});
		
		JMenuItem changeColor4 = new JMenuItem("Line Color");
		colorMenu.add(changeColor4);
		changeColor4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				lineColor = JColorChooser.showDialog(SnakeCharmer.this, "Choose a color", lineColor);
				scoreBoard.setBackground(lineColor);

				panel.repaint();
				gameBoard.repaint();
				scoreBoard.repaint();
			}
		});

		colorMenu.setMnemonic('C');
		menuBar.add(colorMenu);

		JMenuItem changeColor1 = new JMenuItem("Snake Color 1");
		colorMenu.add(changeColor1);
		changeColor1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				snakeColor1 = JColorChooser.showDialog(SnakeCharmer.this, "Choose a color", snakeColor1);
				repaint();
			}
		});

		
		JMenuItem changeColor2 = new JMenuItem("Snake Color 2");
		colorMenu.add(changeColor2);
		changeColor2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				snakeColor2 = JColorChooser.showDialog(SnakeCharmer.this, "Choose a color", snakeColor2);
				repaint();
			}
		});
		
		
		// add help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		
		JMenuItem operation = new JMenuItem("Operation", 'O');
		operation.setToolTipText("Game Operations");
		operation.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		operation.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				showHelpGameOps();
			}
		});
		helpMenu.add(operation);
		
		JMenuItem rules = new JMenuItem("Game Rules", 'M');
		rules.setToolTipText("Game rules");
		rules.setAccelerator(KeyStroke.getKeyStroke("ctrl M"));
		rules.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				showHelpGameRules();
			}
		});
		helpMenu.add(rules);
		
	} // end makeMenu method
	
	/**
	 * updates/changes game border
	 * @param type - type of border to be displayed/changed into
	 */
	private void changeBorder(BorderType type)
	{	
		// saved in variable so that when player hits border the right sound play
		border = type;
		
		// make panels the new border type
		upSideBorder = new GameBorderUpDown(type);
		downSideBorder = new GameBorderUpDown(type);
		leftSideBorder = new GameBorderSide(type);
		rightSideBorder = new GameBorderSide(type);
		
		// remove old border panels
		panel.removeAll();
		
		// add new border panels
		panel.add(gameBoard, BorderLayout.CENTER);
		panel.add(upSideBorder, BorderLayout.NORTH);
		panel.add(downSideBorder, BorderLayout.SOUTH);
		panel.add(leftSideBorder, BorderLayout.EAST);
		panel.add(rightSideBorder, BorderLayout.WEST);
		
		// add border panel to main JFrame
		add(panel, BorderLayout.NORTH);
		
		repaint();
		pack();
		setVisible(true);
	} // end changeBorder method
	
	/**
	 * starts new game if user confirms
	 */
	private void newGame()
	{
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to start a new game?", "Start New Snake Game?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
		{
			resetGame();
			
		} // end if
	} // end newGame method
	
	/**
	 * shows help menu game operations
	 */
	private void showHelpGameOps()
	{
		JOptionPane.showMessageDialog(this, 
				"Game operations are as follows:\n\n" +
				"Controls:\n" +
				"Enter:  to start game\n" +
				"P:  to pause game\n" +
				"Up or W:  to move up (or north)\n" +
				"Down or S:  to move down (or south)\n" +
				"Left or A:  to move left (or east)\n" +
				"Right or D:  to move right (or west)\n\n" +
				"Menu Options:\n" +
				"Game:  to start a new game or exit\n" +
				"Sound:  to toggle sound on or off\n" +
				"Speed:  to change speed\n" +
				"Border:  to change game border\n" +
				"Colors:  to change colors of board and snake\n" +
				"Help:  to see game rules and operations",
				"Snake Game Operation", 
				JOptionPane.INFORMATION_MESSAGE);
		
	} // end showHelpGameOps method
	
	/**
	 * show help menu game rules
	 */
	private void showHelpGameRules()
	{
		JOptionPane.showMessageDialog(this, 
				"Game rules are as follows:\n" +
				"The snake can move up, down, left and right and will move at a constant speed.\n" +
				"The snake's body and tail follow the path made by the snake's head.\n" +
				"Food for the snake to eat is randomly put somewhere on the game board.\n"   +
				"When the food is eaten (or run into) a new piece of food will appear, \n" +
				" the snake's body will grow and 100 points will be awarded.\n" +
				"The game ends when the snake cannot move because it ran into the game border or itself.\n" +
				"Have fun! ", 
				"Snake Game Rules", 
				JOptionPane.INFORMATION_MESSAGE);
		
	} // end showHelpGameRules method
	
	/**
	 * creates instance of game and starts it
	 * @param args - command line arguments
	 */
	public static void main(String[] args)
	{
		SnakeCharmer sc = new SnakeCharmer();
		sc.startGame();
	} // end main method

} // end SnakeCharmer class
