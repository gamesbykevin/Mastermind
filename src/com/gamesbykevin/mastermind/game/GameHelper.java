package com.gamesbykevin.mastermind.game;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.Choices;
import com.gamesbykevin.mastermind.board.entries.Entries;
import com.gamesbykevin.mastermind.screen.MenuScreen;
import com.gamesbykevin.mastermind.screen.OptionsScreen;
import com.gamesbykevin.mastermind.screen.ScreenManager;
import com.gamesbykevin.mastermind.thread.MainThread;

import android.graphics.Canvas;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * Did we win?
	 */
	public static boolean WIN = false;
	
	/**
	 * Did we lose?
	 */
	public static boolean LOSE = false;
	
	/**
	 * Do we notify that we are reseting the game?
	 */
	public static boolean NOTIFY_RESET = true;
	
	/**
	 * Do we reset the game?
	 */
	public static boolean RESET = true;
	
	/**
	 * Is the game over?
	 */
	public static boolean GAMEOVER = false;
	
	/**
	 * Do we display the screenshot instructions
	 */
	public static boolean INGAME_INSTRUCTIONS = false;
	
	/**
	 * Do we go to the exit game screen
	 */
	public static boolean EXIT_GAME = false;
	
    /**
     * Size of in-game buttons
     */
	public static final int BUTTON_DIMENSION = Choices.DIMENSION;
	
    /**
     * Location of the home button
     */
	public static final int HOME_X = Entries.START_X;
    
    /**
     * Location of the home button
     */
	public static final int HOME_Y = 10;
    
    /**
     * Location of the in game instructions button
     */
	public static final int INSTRUCTION_X = Entries.START_X + (int)(BUTTON_DIMENSION * 1.20);
    
    /**
     * Location of the in game instructions button
     */
	public static final int INSTRUCTION_Y = HOME_Y;
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_X = INSTRUCTION_X + (int)(BUTTON_DIMENSION * 1.15);
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_Y = HOME_Y + (BUTTON_DIMENSION / 3);
	
	/**
	 * Location of the attempts text
	 */
	public static final int ATTEMPTS_X = LOGO_X;
	
	/**
	 * Location of the attempts text
	 */
	public static final int ATTEMPTS_Y = HOME_Y + (int)(BUTTON_DIMENSION * 1.15);
	
    /**
     * Update game
     * @throws Exception 
     */
    public static final void update(final Game game) throws Exception
    {
    	if (RESET)
    	{
    		//reset board
    		game.getBoard().reset();
    		
    		//add default entry
    		game.getBoard().add();
    		
    		//flag reset false now that we have finished
    		RESET = false;
    		
			//flag false now that we have finished
			NOTIFY_RESET = false;
    	}
    	else
    	{
    		//don't continue if showing in game instructions
    		if (INGAME_INSTRUCTIONS)
    			return;
    		
    		//if we want to exit the current game
    		if (EXIT_GAME)
    		{
    			//flag false
    			EXIT_GAME = false;
    			
    			//set exit screen state
    			game.getScreen().setState(ScreenManager.State.Exit);
    			
    			//no need to continue
    			return;
    		}
    		
    		game.getBoard().update();
    	}
    }
    
    /**
     * Render the game accordingly
     * @param canvas Place to write pixel data
     * @param game Our game reference object
     * @throws Exception
     */
    public static final void render(final Canvas canvas, final Game game) throws Exception
    {
    	if (!canPlay())
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
    	}
    	else
    	{
    		//render the board if it exists
    		if (game.getBoard() != null)
    			game.getBoard().render(canvas);
    		
    		//render our in game menu buttons
    		if (game.getButtonHome() != null)
    			game.getButtonHome().render(canvas);
    		if (game.getButtonInstructions() != null)
    			game.getButtonInstructions().render(canvas);
    		
    		//draw the game logo
    		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.InGameLogo), LOGO_X, LOGO_Y, null);
    		
    		//draw the attempts
    		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.Attempts), ATTEMPTS_X, ATTEMPTS_Y, null);
    		
    		if (game.getNumber() != null)
    			game.getNumber().render(canvas);
    	}
    }
    
    /**
     * Can the player play the game?
     * @return true if we are not resetting the game, false otherwise
     */
    public static final boolean canPlay()
    {
    	return (!NOTIFY_RESET && !RESET);
    }
}