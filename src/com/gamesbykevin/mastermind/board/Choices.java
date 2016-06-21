package com.gamesbykevin.mastermind.board;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.entries.Entries;
import com.gamesbykevin.mastermind.board.peg.Peg;
import com.gamesbykevin.mastermind.board.peg.Selection;
import com.gamesbykevin.mastermind.common.ICommon;
import com.gamesbykevin.mastermind.panel.GamePanel;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * This class will manage the choices to choose from
 * @author GOD
 */
public class Choices implements ICommon
{
	//the selection peg reference
	private final Peg selection;
	
	/**
	 * The dimension of the choice
	 */
	private static final int DIMENSION = 70;
	
	/**
	 * The starting coordinates for the choices
	 */
	private static final int START_X = GamePanel.WIDTH - 5 - (DIMENSION / 2);
	
	/**
	 * The starting coordinates for the choices
	 */
	private static final int START_Y = (DIMENSION / 2) + 15;
	
	//do we check for selection
	private boolean check = false;
	
	//the coordinates we want to check
	private int checkX, checkY;
	
	//reference to the game entries
	private final Entries entries;
	
	//keep track of where our choices are located
	private Point[] points;
	
	//the confirm buttons
	private Button confirmEnabled, confirmDisabled;
	
	/**
	 * The size of the confirmation button
	 */
	private static final int BUTTON_DIMENSION = DIMENSION;
	
	/**
	 * The location of the confirm button
	 */
	private static final int BUTTON_X = START_X - (DIMENSION / 2);
	private static final int BUTTON_Y = GamePanel.HEIGHT - DIMENSION - (DIMENSION / 2);
	
	/**
	 * Default constructor
	 */
	public Choices(final Entries entries)
	{
		//assign our references
		this.entries = entries;
		this.selection = entries.getPegSelection();
		
		//create our buttons
		this.confirmEnabled = new Button(Images.getImage(Assets.ImageGameKey.ConfirmEntryEnabled));
		this.confirmEnabled.setX(BUTTON_X);
		this.confirmEnabled.setY(BUTTON_Y);
		this.confirmEnabled.setWidth(BUTTON_DIMENSION);
		this.confirmEnabled.setHeight(BUTTON_DIMENSION);
		this.confirmEnabled.updateBounds();
		
		this.confirmDisabled = new Button(Images.getImage(Assets.ImageGameKey.ConfirmEntryDisabled));
		this.confirmDisabled.setX(BUTTON_X);
		this.confirmDisabled.setY(BUTTON_Y);
		this.confirmDisabled.setWidth(BUTTON_DIMENSION);
		this.confirmDisabled.setHeight(BUTTON_DIMENSION);
		this.confirmDisabled.updateBounds();
		
		//reset the selections
		reset();
	}

	@Override
	public void dispose() 
	{
		//recycle anything here?
	}

	private Peg getSelection()
	{
		return this.selection;
	}
	
	/**
	 * Check if a choice was made
	 * @param checkX x-coordinate
	 * @param checkY y-coordinate
	 */
	protected void check(final int checkX, final int checkY)
	{
		this.check = true;
		this.checkX = checkX;
		this.checkY = checkY;
	}
	
	@Override
	public void update() throws Exception 
	{
		//make sure we are to check
		if (this.check)
		{
			//don't need to check again
			this.check = false;
			
			//check each point for collision
			for (int i = 0; i < this.points.length; i++)
			{
				//get the current animation key
				Selection.Key key = Selection.Key.values()[i];
				
				//if not enabled don't bother checking
				if (!key.isEnabled())
					continue;
				
				//if the entry is already complete we can't check this
				if (this.entries.getEntry().isComplete())
					break;
				
				//assign the coordinates
				getSelection().setX(this.points[i].x);
				getSelection().setY(this.points[i].y);
				
				//set the appropriate dimension
				getSelection().setWidth(DIMENSION);
				getSelection().setHeight(DIMENSION);
				
				//if the coordinate is within the choice
				if (getSelection().contains(checkX, checkY))
				{
					//now flag it false
					key.setEnabled(false);
					
					//add selection to the current entry
					this.entries.getEntry().addSelection(key);
					
					//check if all selections have been made
					if (this.entries.getEntry().isComplete())
					{
						//enable confirm button
						enableConfirmation(true);
						
						//play sound effect
						
						
						//no need to continue
						return;
					}
				}
			}
			
			//if we selected the confirm button
			if (this.confirmEnabled.contains(checkX, checkY))
			{
				if (this.confirmEnabled.isVisible())
				{
					//make sure the appropriate button shows
					enableConfirmation(false);
					
					//mark the entry ready to be analyzed
					this.entries.getEntry().markReady();
					
					//make all selections enabled again (for the next entry)
					for (Selection.Key key : Selection.Key.values())
					{
						key.setEnabled(true);
					}
				}
				else
				{
					//play sound effect for invalid selection
				}
			}
		}
	}
	
	/**
	 * Flag the confirmation button enabled
	 * @param enabled true if we want to display the confirmation button, false if we want to display the disabled button
	 */
	private void enableConfirmation(final boolean enabled)
	{
		this.confirmEnabled.setVisible(enabled);
		this.confirmDisabled.setVisible(!enabled);
	}
	
	@Override
	public final void reset()
	{
		//make all selections enabled
		for (Selection.Key key : Selection.Key.values())
		{
			key.setEnabled(true);
		}
		
		//make sure correct button is displayed
		enableConfirmation(false);
		
		//if there are no points create our array
		if (this.points == null)
		{
			//create our array
			this.points = new Point[Selection.Key.values().length];
			
			//now calculate the points
			int x = START_X;
			int y = START_Y;
			
			//set the coordinates for all selections
			for (int i = 0; i < Selection.Key.values().length; i++)
			{
				//create a new point in the array
				this.points[i] = new Point(x, y);
				
				//increase the coordinate
				y += (DIMENSION * 1.2);
			}
		}
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		//set the appropriate dimension
		getSelection().setWidth(DIMENSION);
		getSelection().setHeight(DIMENSION);
		
		//render all enabled selections
		for (int i = 0; i < this.points.length; i++)
		{
			//get the current animation key
			Selection.Key key = Selection.Key.values()[i]; 
			
			//don't render if it is not enabled
			if (!key.isEnabled())
				continue;
			
			//assign the animation
			getSelection().setAnimation(key);
			
			//assign the coordinates
			getSelection().setX(this.points[i].x);
			getSelection().setY(this.points[i].y);
			
			//render the animation
			getSelection().render(canvas);
		}
		
		//render the 2 buttons
		this.confirmEnabled.render(canvas);
		this.confirmDisabled.render(canvas);
	}
}