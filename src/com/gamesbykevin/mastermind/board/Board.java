package com.gamesbykevin.mastermind.board;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.entries.Entries;
import com.gamesbykevin.mastermind.board.entries.Entry;
import com.gamesbykevin.mastermind.board.peg.Selection;
import com.gamesbykevin.mastermind.common.ICommon;
import com.gamesbykevin.mastermind.entity.Entity;
import com.gamesbykevin.mastermind.number.Number;

import android.graphics.Canvas;

public class Board extends Entity implements ICommon
{
	public enum Key
	{
		Selection, Hint
	}
	
	//all our entry selections along with their hints
	private Entries entries = null;
	
	//set the size of the board
	private final int size;
	
	//the choices the player can make
	private Choices choices;
	
	//the solution for the board
	private ArrayList<Selection.Key> solution;
	
	//our number object reference
	private final Number number;
	
	/**
	 * Create a new board
	 * @param size The size of each entry on the board
	 */
	public Board(final Number number, final int size)
	{
		//store our number object reference
		this.number = number;
		
		//set the size
		this.size = size;
		
		//create solution list
		this.solution = new ArrayList<Selection.Key>();
		
		//create new entries object
		this.entries = new Entries(this);
		
		//create the choices object
		this.choices = new Choices(this.entries);
		
		//add animations
		super.getSpritesheet().add(Key.Selection, new Animation(Images.getImage(Assets.ImageGameKey.Entry)));
		super.getSpritesheet().add(Key.Hint, new Animation(Images.getImage(Assets.ImageGameKey.Entry)));
		
		//reset
		reset();
	}
	
	private int getSize()
	{
		return this.size;
	}
	
	/**
	 * Add an empty entry to our board
	 */
	public void add()
	{
		this.entries.add(getSize());
		
		try
		{
			//update our # of attempts
			this.number.setNumber(number.getNumber() + 1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//if the newest entry is to high up move it down onto the viewable screen
		if (this.entries.getEntry().getY() < BoardHelper.ENTRY_Y_MIN)
		{
			//calculate offset and assign the velocity
			super.setDY(BoardHelper.ENTRY_Y_MIN - this.entries.getEntry().getY());
			
			//update location of all entries
			updateEntryLocation();
				
			//stop velocity
			super.setDY(0);
		}
	}
	
	/**
	 * Has the board been solved?
	 * @return true if the current entry has the correct colors in the correct position, false otherwise
	 */
	public boolean isSolved()
	{
		return this.entries.getEntry().isSolved();
	}
	
	/**
	 * Check the choices and entries for a selection.<br>
	 * Here the player can make a selection, or un-select a current entry selection
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void check(final int x, final int y)
	{
		//check to see if anything changed in our choices
		this.choices.check(x, y);
		
		//check our latest entry to see if a selection was made there
		this.entries.check(x, y);
	}
	
	/**
	 * Update the location of all the entries
	 */
	private void updateEntryLocation()
	{
		//if the board has velocity
		if (super.getDY() != 0)
		{
			//make sure all entries do not go off the screen
			if (super.getDY() > 0)
			{
				//if the current entry is at the bottom of the screen, stop the velocity
				if (this.entries.getEntry().getY() >= BoardHelper.ENTRY_Y_MAX)
					super.setDY(0);
			}
			else
			{
				//if the first entry is at the top of the screen, stop the velocity
				if (this.entries.getEntries().get(0).getY() <= BoardHelper.ENTRY_Y_MIN)
					super.setDY(0);
			}
			
			//if we still have velocity update the location
			if (super.getDY() != 0)
			{
				//update the y-coordinates of the entries
				for (int i = 0; i < this.entries.getEntries().size(); i++)
				{
					//get the current entry
					final Entry entry = this.entries.getEntries().get(i); 
					
					//update the y-coordinate
					entry.setY(entry.getY() + (int)getDY());
				}
			}
		}
	}
	
	
	@Override
	public void update() throws Exception 
	{
		//update the location of the entries
		updateEntryLocation();
		
		//update our entries
		this.entries.update();
		
		//update our choices object
		this.choices.update();
	}

	@Override
	public void dispose()
	{
		if (this.entries != null)
			this.entries.dispose();
		if (this.choices != null)
			this.choices.dispose();
		
		this.entries = null;
		this.choices = null;
	}
	
	@Override
	public void reset()
	{
		//reset the entries container
		this.entries.reset();
		
		//reset the choices
		this.choices.reset();
	}

	/**
	 * Assign the current solution
	 * @param solution Array of indexes for each correct solution key
	 */
	public void assignSolution(final int[] solution)
	{
		//clear list our for starters
		getSolution().clear();
		
		//assign our solution values
		for (int i = 0; i < solution.length; i++)
		{
			//get the selection key index
			final int index = solution[i];
			
			//add the correct solution key to our array list
			getSolution().add(Selection.Key.values()[index]);
		}
	}
	
	/**
	 * Get the solution that we are trying to crack
	 * @return A list of peg selections
	 */
	public ArrayList<Selection.Key> getSolution()
	{
		return this.solution;
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render the backgrounds for all the entries first
		for (int i = 0; i < this.entries.getEntries().size(); i++)
		{
			//get the current entry
			Entry entry = this.entries.getEntries().get(i);
			
			//set the dimensions
			super.setWidth(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION);
			super.setHeight(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION);
			
			//assign the current animation
			super.getSpritesheet().setKey(Key.Selection);
			
			//render a background for each selection
			for (int index = 0; index < entry.getSelections().size(); index++)
			{
				//assign coordinates
				super.setX(BoardHelper.getSelectionX(entry, index));
				super.setY(BoardHelper.getSelectionY(entry));
				
				//render animation
				super.render(canvas);
			}
			
			//set the dimensions
			super.setWidth(BoardHelper.PEG_BACKGROUND_HINT_DIMENSION);
			super.setHeight(BoardHelper.PEG_BACKGROUND_HINT_DIMENSION);
			
			//assign the current animation
			super.getSpritesheet().setKey(Key.Hint);
			
			//render a background for each hint
			for (int index = 0; index < entry.getHints().size(); index++)
			{
				//assign coordinates
				super.setX(BoardHelper.getHintX(entry, index));
				super.setY(BoardHelper.getHintY(entry, index));
				
				//render animation
				super.render(canvas);
			}
		}
		
		//now draw entry and hint pegs accordingly
		if (this.entries != null)
			this.entries.render(canvas);
		
		if (this.choices != null)
			this.choices.render(canvas);
	}
}