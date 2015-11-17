/**
 * Brady Carlson
 * CS4800
 * WSU Fall 2015
 * Individual Project - Snake Game
 * 
 * Comments:
 * Is responsible for creating and updating game clock.
 * Game clock is used for game speed.
 */

package game;


public class GameClock 
{
	// variables
	private float millisPerCycle;
	private long lastUpdate;
	private int elapsedCycles;
	private float excessCycles;
	private boolean isClockPaused;
	
	/**
	 * sets the cycles per second and resets the clock
	 * @param cyclesPerSecond - cycles per second
	 */
	public GameClock(float cyclesPerSecond) 
	{
		setCyclesPerSecond(cyclesPerSecond);
		resetClock();	
	} // end constructor
	
	/**
	 * updates clock's cycles
	 */
	public void updateClock() 
	{
		long currUpdate = (System.nanoTime() / 1000000L);
		float delta = (float)((currUpdate - lastUpdate) + excessCycles);

		if(!isClockPaused) 
		{
			elapsedCycles = elapsedCycles + (int)Math.floor(delta / millisPerCycle);
			excessCycles = delta % millisPerCycle;
		}
		
		lastUpdate = currUpdate;
	} // end update method
	
	/**
	 * resets clock variables back to zero/default
	 */
	public void resetClock() 
	{
		elapsedCycles = 0;
		excessCycles = 0.0f;
		lastUpdate = (System.nanoTime() / 1000000L);
		isClockPaused = false;
	} // end resetClock method
	
	/**
	 * to check if a clock cycles has elapsed or not
	 * @return - true if cycles has elapsed, false othewise
	 */
	public boolean hasElapsedCycle() 
	{
		if(elapsedCycles > 0) 
		{
			elapsedCycles--;
			return true;
		}
		else
		{
			return false;
		}
	} // end hasElapsedCycle method

	/**
	 * to check if clock is paused
	 * @return - true if clock is paused, false otherwise
	 */
	public boolean isClockPaused() { return isClockPaused; } // end isClockPaused method
	
	/**
	 * to set clock to paused
	 * @param p - sets clock to true or false
	 */
	public void setPaused(boolean p) { isClockPaused = p; } // end setPaused method

	/**
	 * to set the clock's cycles per second
	 * @param cyclesPerSecond - cycles per second
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) { millisPerCycle = (1.0f / cyclesPerSecond) * 1000; } // end setCyclesPerSecond method

} // end GameClock class
