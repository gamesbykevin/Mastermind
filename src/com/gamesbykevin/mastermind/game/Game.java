package com.gamesbykevin.mastermind.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.level.Select;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.Board;
import com.gamesbykevin.mastermind.board.BoardHelper;
import com.gamesbykevin.mastermind.board.entries.Entries;
import com.gamesbykevin.mastermind.board.peg.Hint;
import com.gamesbykevin.mastermind.board.peg.Selection;
import com.gamesbykevin.mastermind.number.Number;
import com.gamesbykevin.mastermind.panel.GamePanel;
import com.gamesbykevin.mastermind.screen.MenuScreen;
import com.gamesbykevin.mastermind.screen.OptionsScreen;
import com.gamesbykevin.mastermind.screen.ScreenManager;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //the duration we want to vibrate the phone for
    private static final long VIBRATION_DURATION = 500L;
    
    /**
     * Our value to identify if vibrate is enabled
     */
	public static final int VIBRATE_ENABLED = 0;
	
    //object for rendering a number
    private Number number;
    
    //the board of our game
    private Board board;
    
    //did we move our finger on the screen
    private boolean moved = false;
    
    //store the coordinate where our fingers is
    private float moveY;
    
    //button for home page and instructions
    private Button home, instructions;
    
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new number object and position it
        this.number = new Number();
        this.number.setNumber(0, GameHelper.ATTEMPTS_X + Images.getImage(Assets.ImageGameKey.Attempts).getWidth() + 5, GameHelper.ATTEMPTS_Y - 5);
        
        //check the options to set the size of our board
        final int size;
        
        //depending on difficulty set the board size and peg dimensions
        switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty))
        {
        	//3
	        case 0:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MAX;
	        	break;
	        	
	        //4
	        case 1:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM + 1;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MAX;
	        	break;
	        	
	        //5
	        case 2:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM + 2;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MID;
	        	break;
	        	
	        //6
	        case 3:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM + 3;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MID;
	        	break;
	        	
	        //7
	        case 4:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM + 4;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MIN;
	        	break;
	        	
	        //8
	        case 5:
	        	//set board selection size
	        	size = BoardHelper.SIZE_MINIMUM + 5;
	        	
	        	//set the dimensions of the pegs themselves
	        	BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION = BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION_MIN;
	        	break;
	        	
        	default:
        		throw new Exception("Key not handled here - " + getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty));
        }
        
    	//set the dimensions of the hints
    	BoardHelper.PEG_BACKGROUND_HINT_DIMENSION = (int)(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * BoardHelper.PEG_HINT_SIZE_RATIO);
    	
    	//now set the dimensions of the selection and hint
    	Selection.PEG_SELECTION_DIMENSION = (int)(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * Selection.SIZE_RATIO);
    	Hint.PEG_HINT_DIMENSION = 			(int)(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * Hint.SIZE_RATIO);
    	
        //create a new board with the appropriate size
        this.board = new Board(getNumber(), size);
        
    	//create buttons for in game navigation
        createButtons();
    }
    
    /**
     * Create the in game menu buttons
     */
    private void createButtons()
    {
    	//create a new button
        this.home = new Button(Images.getImage(Assets.ImageGameKey.HomeNavigation));
    	
    	//assign the location
        this.home.setX(GameHelper.HOME_X);
        this.home.setY(GameHelper.HOME_Y);
    	
    	//assign dimensions
        this.home.setWidth(GameHelper.BUTTON_DIMENSION);
        this.home.setHeight(GameHelper.BUTTON_DIMENSION);
    	
    	//update the bounds of the button
        this.home.updateBounds();
    	
    	//create a new button
        this.instructions = new Button(Images.getImage(Assets.ImageGameKey.ScreenInstructions));
    	
    	//assign the location
    	this.instructions.setX(GameHelper.INSTRUCTION_X);
    	this.instructions.setY(GameHelper.INSTRUCTION_Y);
    	
    	//assign dimensions
    	this.instructions.setWidth(GameHelper.BUTTON_DIMENSION);
    	this.instructions.setHeight(GameHelper.BUTTON_DIMENSION);
    	
    	//update the bounds of the button
    	this.instructions.updateBounds();
    }
    
    public Button getButtonHome()
    {
    	return this.home;
    }
    
    public Button getButtonInstructions()
    {
    	return this.instructions;
    }
    
    /**
     * Get the number object
     * @return Our number object reference for rendering lives
     */
    public Number getNumber()
    {
    	return this.number;
    }
    
    /**
     * Get the board
     * @return Object containing all the selections/hints etc....
     */
    public Board getBoard()
    {
    	return this.board;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
    	//if the object has not been created yet
    	if (this.paint == null)
    	{
            //create new paint object
            this.paint = new Paint();
            //this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
            this.paint.setTextSize(48f);
            this.paint.setColor(Color.WHITE);
            this.paint.setLinearText(false);
    	}
    	
        return this.paint;
    }
    
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if we can't play, don't continue
    	if (!GameHelper.canPlay())
    		return;
    	
		//if we are to show in game instructions
		if (GameHelper.INGAME_INSTRUCTIONS)
		{
			if (action == MotionEvent.ACTION_UP)
			{
				//flag display instructions false
				GameHelper.INGAME_INSTRUCTIONS = false;
			}
			
			//no need to continue
			return;
		}
		
		if (action == MotionEvent.ACTION_UP)
    	{
			//flag moved false
			this.moved = false;
			
			//stop the velocity
			this.getBoard().setDY(0);
			
			if (this.getButtonHome().contains(x, y))
				GameHelper.EXIT_GAME = true;
			if (this.getButtonInstructions().contains(x, y))
				GameHelper.INGAME_INSTRUCTIONS = true;
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
    		//make sure we aren't moving first
    		if (!this.moved)
    			getBoard().check((int)x, (int)y);
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
			//if we have not yet moved
			if (!this.moved)
			{
				//flag moving as true
				this.moved = true;
				
				//store the start coordinate
				this.moveY = y;
			}
			else
			{
				//calculate the difference
				float yDiff = (y - this.moveY) * 2;
				
				//update the y-coordinate(s) of the existing entries
				this.getBoard().setDY(yDiff);
				
				//update the move coordinate
				this.moveY = y;
			}
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
    	//update our game objects
		GameHelper.update(this);
    }
    
    /**
     * Vibrate the phone for the default duration
     */
    public void vibrate()
    {
    	this.vibrate(VIBRATION_DURATION);
    }
    
    /**
     * Vibrate the phone if the vibrate feature is enabled
     * @param duration The duration to vibrate for milliseconds
     */
    public void vibrate(final long duration)
    {
		//make sure vibrate option is enabled
		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == VIBRATE_ENABLED)
		{
    		//get our vibrate object
    		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    		 
			//vibrate for a specified amount of milliseconds
			v.vibrate(duration);
		}
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	GameHelper.render(canvas, this);
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
        
    	if (this.number != null)
    	{
    		this.number.dispose();
    		this.number = null;
    	}
    	
    	if (this.board != null)
    	{
    		this.board.dispose();
    		this.board = null;
    	}
    	
    	if (this.home != null)
    	{
    		this.home.dispose();
    		this.home = null;
    	}
    	
    	if (this.instructions != null)
    	{
    		this.instructions.dispose();
    		this.instructions = null;
    	}
    }
}