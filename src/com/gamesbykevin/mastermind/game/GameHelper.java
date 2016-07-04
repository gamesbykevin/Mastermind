package com.gamesbykevin.mastermind.game;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.entries.Entries;
import com.gamesbykevin.mastermind.panel.GamePanel;
import com.gamesbykevin.mastermind.screen.OptionsScreen;
import com.gamesbykevin.mastermind.screen.ScreenManager;

import android.graphics.Canvas;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
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
	public static boolean IN_GAME_INSTRUCTIONS = false;
	
	/**
	 * Do we go to the exit game screen
	 */
	public static boolean EXIT_GAME = false;
	
    /**
     * Size of in-game buttons
     */
	public static final int BUTTON_DIMENSION = 60;
	
	/**
	 * The amount of pixels between each button
	 */
	public static final int Y_INCREMENT = (int)(BUTTON_DIMENSION * 1.175);
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_X = Entries.START_X;
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_Y = 10;
	
	/**
	 * Location of the attempts text
	 */
	public static final int ATTEMPTS_X = LOGO_X;
	
	/**
	 * Location of the attempts text
	 */
	public static final int ATTEMPTS_Y = LOGO_Y + (int)(28 * 1.15);
	
    /**
     * Location of the home button
     */
	public static final int HOME_X = GamePanel.WIDTH - (int)(BUTTON_DIMENSION * 1.33);
    
    /**
     * Location of the home button
     */
	public static final int HOME_Y = LOGO_Y;
    
    /**
     * Location of the in game instructions button
     */
	public static final int INSTRUCTION_X = HOME_X;
    
    /**
     * Location of the in game instructions button
     */
	public static final int INSTRUCTION_Y = LOGO_Y + Y_INCREMENT;
	
	/**
	 * The multiplier to get the 
	 */
	public static final int ENTRY_SCROLL_MULTIPLIER = 3;
	
    /**
     * The number of columns for our level select options on a single page
     */
    protected static final int SELECT_COLS = 4;
    
    /**
     * The number of rows for our level select options on a single page
     */
    protected static final int SELECT_ROWS = 5;
    
    /**
     * The size of each level select button
     */
    protected static final int SELECT_DIMENSION = 96;
    
    /**
     * The pixel space between each level select button
     */
    protected static final int SELECT_PADDING = (GamePanel.WIDTH - (SELECT_COLS * SELECT_DIMENSION)) / (SELECT_COLS + 1);
    
    /**
     * The x-coordinate where we start displaying the level select buttons
     */
    protected static final int SELECT_START_X = SELECT_PADDING;
    
    /**
     * The y-coordinate where we start displaying the level select buttons
     */
    protected static final int SELECT_START_Y = 50;
    
    /**
     * Update our level select screen
     * @param game Our game reference object
     */
    public static final void updateSelect(final Game game)
    {
    	//get the difficulty to tell the number of colors
    	final int numberColors = game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty);
    	
    	//keep track of the highest level, so we can set that as the current page
    	int highestLevel = 0;
    	
		//the number of levels per page
		final int perPage = game.getSelect().getCols() * game.getSelect().getRows();
		
        //load the saved data checking every level
        for (int numberLevel = game.getSelect().getTotal() - 1; numberLevel >= 0; numberLevel--)
        {
        	//if the level exists then we already completed it
        	if (game.getScore().hasCompleted(numberColors, numberLevel))
        	{
        		//mark this level as completed
        		game.getSelect().setCompleted(numberLevel, true);
        		
        		//mark this level as not locked
        		game.getSelect().setLocked(numberLevel, false);
        		
        		//also make sure the next level is not locked as well
        		if (numberLevel < game.getSelect().getTotal() - 1)
        			game.getSelect().setLocked(numberLevel + 1, false);
        	}
        	else
        	{
        		//mark this level as locked
        		game.getSelect().setLocked(numberLevel, true);
        		
        		//mark this level as not completed
        		game.getSelect().setCompleted(numberLevel, false);
        	}
        	
        	//if the current level is not locked
        	if (!game.getSelect().isLocked(numberLevel))
        	{
        		//if this level is higher than the previous
        		if (numberLevel > highestLevel)
        		{
        			//update the number to beat
        			highestLevel = numberLevel;
        			
        			//assign the correct page as well
        			game.getSelect().setPageIndex((highestLevel + 1) / perPage);
        		}
        	}
        	
        	/*
        	//if debugging unlock every level
        	if (MainThread.DEBUG)
            	game.getSelect().setLocked(numberLevel, false);
           	*/
        }
        
    	//the first level can never be locked
    	game.getSelect().setLocked(0, false);
    }
    
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
    		
    		//reset attempts count
    		game.getNumber().setNumber(1);
    		
    		//assign the appropriate solution
    		game.getBoard().assignSolution(game.getLevels().getSolution());
    		
    		//flag reset false now that we have finished
    		RESET = false;
    		
			//flag false now that we have finished
			NOTIFY_RESET = false;
    	}
    	else if (!game.getSelect().hasSelection())
    	{
    		//update the object
    		game.getSelect().update();
    		
    		//if we have a selection now, reset the board
    		if (game.getSelect().hasSelection())
    		{
    			//make sure the level is not locked, if it is locked play sound effect
    			if (game.getSelect().isLocked(game.getSelect().getLevelIndex()))
    			{
    				//flag selection as false
    				game.getSelect().setSelection(false);
    				
    				//play sound effect
    				Assets.playInvalidSelection();
    			}
    			else
    			{
    				//assign the appropriate level
    				game.getLevels().setNumberLevel(game.getSelect().getLevelIndex());
    				
    				//reset the board for the next level
    				RESET = true;
    			}
    		}
    	}
    	else
    	{
    		//don't continue if showing in game instructions
    		if (IN_GAME_INSTRUCTIONS)
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
    		
    		//if the board has been solved
    		if (game.getBoard().isSolved())
    		{
    			//vibrate game
    			game.vibrate();
    			
    	    	//get the difficulty to tell the number of colors
    	    	final int numberColors = game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty);
    			
    			//save the completed level
    			game.getScore().update(numberColors, game.getLevels().getNumberLevel());
    			
    			//then update level select list
    			updateSelect(game);
    			
    			//go to the game over state
    			game.getScreen().setState(ScreenManager.State.GameOver);
    			
    			//no need to continue
    			return;
    		}
    		else
    		{
    			//update the board
	    		game.getBoard().update();
    		}
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
        	if (!game.getSelect().hasSelection())
        	{
        		//render level select screen
        		game.getSelect().render(canvas, game.getPaint());
        	}
        	else
        	{
	    		if (IN_GAME_INSTRUCTIONS)
	    		{
	    			//render helper instructions here
	    			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.InstructionsScreenshot), 0, 0, null);
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