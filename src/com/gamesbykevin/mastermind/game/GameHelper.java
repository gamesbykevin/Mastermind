package com.gamesbykevin.mastermind.game;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
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