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
	 * Do we reset the game?
	 */
	public static boolean RESET = false;
	
	/**
	 * Should we display the loading screen to the user
	 */
	public static boolean NOTIFY = false;
	
	/**
	 * Is the game over?
	 */
	public static boolean GAMEOVER = false;
	
	/**
	 * Darken the background accordingly
	 */
	private static final int TRANSITION_ALPHA_TRANSPARENCY = 95;
	
    /**
     * Update game
     * @throws Exception 
     */
    public static final void update(final Game game) throws Exception
    {
    	
    }
    
    /**
     * Render the game accordingly
     * @param canvas Place to write pixel data
     * @param game Our game reference object
     * @throws Exception
     */
    public static final void render(final Canvas canvas, final Game game) throws Exception
    {
    	if (!NOTIFY)
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
			
			//flag that the user has been notified
			NOTIFY = true;
    	}
    	else
    	{
    		
    	}
    }
}