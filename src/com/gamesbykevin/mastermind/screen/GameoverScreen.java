package com.gamesbykevin.mastermind.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.HashMap;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.mastermind.MainActivity;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.game.GameHelper;
import com.gamesbykevin.mastermind.thread.MainThread;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final ScreenManager screen;
    
    //keep track of the number of frames passed
    private int frames;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long MENU_DISPLAY_FRAMES_DELAY = MainThread.FPS;
    
    //do we display the menu
    private boolean display = false;
    
    /**
     * The text to display when playing next level
     */
    private static final String BUTTON_TEXT_NEXT = "Next";
    
    /**
     * The text to display for the menu
     */
    private static final String BUTTON_TEXT_MENU = "Menu";
    
    /**
     * The text to display for the menu
     */
    private static final String BUTTON_TEXT_LEVEL = "Level";
    
    //list of buttons
    private HashMap<Key, Button> buttons;
    
    /**
     * Keys to access each button
     * @author GOD
     *
     */
    public enum Key
    {
    	Next, Menu, Rate, Level
    }
    
    //the menu selection made
    private Key selection = null;
    
    /**
     * The x-coordinate for the text
     */
    public static final int TEXT_X = 93;
    
    /**
     * The y-coordinate for the text
     */
    public static final int TEXT_Y = ScreenManager.LOGO_Y;

    /**
     * Create the game over screen
     * @param screen Parent screen manager object
     */
    public GameoverScreen(final ScreenManager screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create buttons hash map
        this.buttons = new HashMap<Key, Button>();
        
        //the start location of the button
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;
        
        //create our buttons
        addButton(x, y, Key.Next, BUTTON_TEXT_NEXT);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, Key.Level, BUTTON_TEXT_LEVEL);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, Key.Menu, BUTTON_TEXT_MENU);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButton(x, y, Key.Rate, MenuScreen.BUTTON_TEXT_RATE_APP);
    }
    
    private ScreenManager getScreen()
    {
    	return this.screen;
    }
    
    /**
     * Add button to our list
     * @param x desired x-coordinate
     * @param y desired y-coordinate
     * @param index Position to place in our array list
     * @param description The text description to add
     */
    private void addButton(final int x, final int y, final Key key, final String description)
    {
    	//create new button
    	Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
    	
    	//position the button
    	button.setX(x);
    	button.setY(y);
    	
    	//assign the dimensions
    	button.setWidth(MenuScreen.BUTTON_WIDTH);
    	button.setHeight(MenuScreen.BUTTON_HEIGHT);
    	button.updateBounds();
    	
    	//add the text description
    	button.addDescription(description);
    	button.positionText(getScreen().getPaint());
    	
    	//add button to the list
    	this.buttons.put(key, button);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //reset frame count
    	this.frames = 0;
        
        //do we display the menu
        setDisplay(false);
        
        //make sure the button text is centered
        for (Button button : buttons.values())
        {
        	button.positionText(screen.getPaint());
        }
        
        //remove the selection
        setSelection(null);
    }
    
    /**
     * Flag display
     * @param display true if we want to display the buttons, false otherwise
     */
    private void setDisplay(final boolean display)
    {
    	this.display = display;
    }
    
    /**
     * Do we display the buttons?
     * @return true = yes, false = no
     */
    private boolean hasDisplay()
    {
    	return this.display;
    }
    
    /**
     * Get the menu selection
     * @return The unique key of the button the user pressed
     */
    private Key getSelection()
    {
    	return this.selection;
    }
    
    /**
     * Set the menu selection
     * @param selection The unique key of the button the user pressed
     */
    private void setSelection(final Key selection)
    {
    	this.selection = selection;
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!hasDisplay())
            return false;
        
        //don't continue if we already made a selection
        if (getSelection() != null)
        	return false;
        
        if (action == MotionEvent.ACTION_UP)
        {
        	for (Key key : Key.values())
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if we did not click this button skip to the next
        		if (!button.contains(x, y))
        			continue;
        		
        		//assign the user selection
        		setSelection(key);
        		
                //no more events required
                return false;
        	}
        }
        
        //no action was taken here and we may need additional events
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
    	if (getSelection() != null)
    	{
			//flag game over false
			GameHelper.GAMEOVER = false;
			
			//handle each button different
			switch (getSelection())
			{
				case Level:
					
					//flag that we need to select a level
					getScreen().getScreenGame().getGame().getSelect().setSelection(false);
					
	                //move back to the game
					getScreen().setState(ScreenManager.State.Running);
					
	                //play sound effect
					Assets.playMenuSelection();
					
	                //end of case
					break;
			
				case Next:
					
					//get the current level number
					int numberLevel = getScreen().getScreenGame().getGame().getLevels().getNumberLevel();
					
					//assign the next level number
					getScreen().getScreenGame().getGame().getLevels().setNumberLevel(numberLevel + 1);
					
	                //reset with the same settings
					GameHelper.RESET = true;
	                
	                //reset loading notification
					GameHelper.NOTIFY_RESET = true;
					
	                //move back to the game
					getScreen().setState(ScreenManager.State.Running);
	                
	                //play sound effect
					Assets.playMenuSelection();
	                
	                //end of case
	                break;
	
	    		case Menu:
	    			
	                //move to the main menu
	                getScreen().setState(ScreenManager.State.Ready);
					
	                //play sound effect
	                Assets.playMenuSelection();
	                
	                //end of case
	                break;
	    			
	    		case Rate:
	                
	                //play sound effect
	    			Assets.playMenuSelection();
	                
	                //go to rate game page
	                getScreen().getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
	                
	                //end of case
	                break;
	    			
				default:
					throw new Exception("Key not setup here: " + getSelection());
			}
			
			//remove selection
			setSelection(null);
    	}
    	else
    	{
	        //if not displaying the menu, track timer
	        if (!hasDisplay())
	        {
	            //if time has passed display menu
	            if (this.frames >= MENU_DISPLAY_FRAMES_DELAY)
	            {
	            	//determine what button text is displayed
	            	buttons.get(Key.Next).setDescription(0, BUTTON_TEXT_NEXT);
	            	
	            	//display the menu
	            	setDisplay(true);
	            }
	            else
	            {
	            	//keep counting the frames
	            	this.frames++;
	            }
	        }
    	}
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (hasDisplay())
        {
            //only darken the background when the menu is displayed
            ScreenManager.darkenBackground(canvas);
            
            //render the buttons
            for (Key key : Key.values())
            {
            	buttons.get(key).render(canvas, getScreen().getPaint());
            }
            
            //render winner text
        	canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Winner), TEXT_X, TEXT_Y, null);
        }
    }
    
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
        	for (Key key : Key.values())
	        {
	        	if (buttons.get(key) != null)
	        	{
	        		buttons.get(key).dispose();
	        		buttons.put(key, null);
	        	}
	        }
	        
	        buttons.clear();
	        buttons = null;
        }
    }
}