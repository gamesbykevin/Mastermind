package com.gamesbykevin.mastermind.board.entries;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.mastermind.board.peg.Hint;
import com.gamesbykevin.mastermind.board.peg.Selection;

public class Entry implements Disposable
{
	//list of selections
	private ArrayList<Selection.Key> selections;
	
	//list of hints
	private ArrayList<Hint.Key> hints;
	
	//the starting render location for this entry
	private int x, y;
	
	//is this entry ready to be checked
	private boolean ready = false;
	
	//did we analyze this entry?
	private boolean analyzed = false;
	
	//does this entry solve the game
	private boolean solved = false;
	
	/**
	 * Default Constructor
	 */
	public Entry() 
	{
		//create a new list of selections
		this.selections = new ArrayList<Selection.Key>();
		
		//create a new list of hints
		this.hints = new ArrayList<Hint.Key>();
	}
	
	/**
	 * Mark the entry as solved
	 */
	public void markSolved()
	{
		this.solved = true;
	}
	
	/**
	 * Is the entry solved?
	 * @return true if all selections are correct and in the correct location, false otherwise
	 */
	public boolean isSolved()
	{
		return this.solved;
	}
	
	/**
	 * Mark the entry ready to be analyzed
	 */
	public void markReady()
	{
		this.ready = true;
	}
	
	/**
	 * Is the entry ready?
	 * @return true if all selections have been made and ready to check for the hints, false otherwise
	 */
	public boolean isReady()
	{
		return this.ready;
	}
	
	/**
	 * Mark the entry as analyzed
	 */
	public void markAnalyzed()
	{
		this.analyzed = true;
	}
	
	/**
	 * Has the entry been analyzed?
	 * @return true if the entry has been analyzed and the hints have been calculated, false otherwise
	 */
	public boolean isAnalyzed()
	{
		return this.analyzed;
	}
	
	/**
	 * Set the x-coordinate
	 * @param x The starting x-coordinate for this entry
	 */
	public void setX(final int x)
	{
		this.x = x;
	}
	
	/**
	 * Set the y-coordinate
	 * @param y The starting y-coordinate for this entry
	 */
	public void setY(final int y)
	{
		this.y = y;
	}
	
	/**
	 * Get the x-coordinate
	 * @return The starting x-coordinate for this entry
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 * Get the y-coordinate
	 * @return The starting y-coordinate for this entry
	 */
	public int getY()
	{
		return this.y;
	}
	
	@Override
	public void dispose() 
	{
		if (this.selections != null)
			this.selections.clear();
		if (this.hints != null)
			this.hints.clear();
		
		this.selections = null;
		this.hints = null;
	}

	/**
	 * Set the size of this entry
	 * @param size The number of selections and hints each for this entry
	 */
	public void setSize(final int size)
	{
		//make sure the lists are empty
		getSelections().clear();
		getHints().clear();
		
		//now add null values to match the size
		for (int i = 0; i < size; i++)
		{
			getSelections().add(null);
			getHints().add(null);
		}
	}

	/**
	 * Get a list of selections
	 * @return The list of all selections (enabled/disabled)
	 */
	public ArrayList<Selection.Key> getSelections()
	{
		return this.selections;
	}
	
	/**
	 * Get the list of hints
	 * @return The list of all hints (enabled/disabled)
	 */
	public ArrayList<Hint.Key> getHints()
	{
		return this.hints;
	}
	
	/**
	 * Is the entry complete?
	 * @return true if all selections have been made, false otherwise
	 */
	public boolean isComplete()
	{
		for (int i = 0; i < getSelections().size(); i++)
		{
			//if one of the selections is empty (null) this is not complete
			if (getSelections().get(i) == null)
				return false;
		}

		//this entry is complete
		return true;
	}
	
	/**
	 * Add the specified selection
	 * @param key The animation key we want to add
	 */
	public void addSelection(final Selection.Key key)
	{
		for (int i = 0; i < getSelections().size(); i++)
		{
			//if the current is null we can add our selection here
			if (getSelections().get(i) == null)
			{
				//add our selection
				getSelections().set(i, key);
				
				//no need to continue
				break;
			}
		}
	}
	
	/**
	 * Add the specified hint
	 * @param key The animation key we want to add
	 */
	public void addHint(final Hint.Key key)
	{
		for (int i = 0; i < getHints().size(); i++)
		{
			//if the current is null we can add our hint here
			if (getHints().get(i) == null)
			{
				//add our hint
				getHints().set(i, key);
				
				//no need to continue
				break;
			}
		}
	}
	
	/**
	 * Analyze the entry.<br>
	 * As we analyze we will add the hints
	 * @param soluiton The solution we want to compare to
	 */
	public void analyze(final ArrayList<Selection.Key> solution)
	{
		//how many selections are the correct color and in the correct position?
		int match = 0;
		
		//how many selections are the correct color in the wrong position
		int position = 0;
		
		//check each selection to see if any are correct and in the correct location
		for (int i = 0; i < this.getSelections().size(); i++)
		{
			//if the colors match in the same position
			if (this.getSelections().get(i) == solution.get(i))
			{
				//keep track of correct color in correct position
				match++;
				
				//skip to next selection
				continue;
			}
			
			//since there was no perfect match check if the color is in another location
			for (int index = 0; index < solution.size(); index++)
			{
				//don't check if the same location
				if (i == index)
					continue;
				
				//if these match but not in the same position
				if (this.getSelections().get(i) == solution.get(index))
				{
					//keep track of correct colors in the wrong position
					position++;
					
					//exit inner loop
					break;
				}
			}
		}
		
		//add the correct color/position hints
		for (int i = 0; i < match; i++)
		{
			addHint(Hint.Key.MatchColorAndPosition);
		}
		
		//add the correct color and wrong position hints
		for (int i = 0; i < position; i++)
		{
			addHint(Hint.Key.MatchColor);
		}
		
		//if each selection is the correct one, this entry solved the puzzle 
		if (this.getSelections().size() == match)
			this.markSolved();
		
		//flag this as analyzed
		this.markAnalyzed();
	}
}