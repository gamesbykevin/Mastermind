package com.gamesbykevin.mastermind.board.entries;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.Board;
import com.gamesbykevin.mastermind.board.BoardHelper;
import com.gamesbykevin.mastermind.board.peg.Hint;
import com.gamesbykevin.mastermind.board.peg.Selection;
import com.gamesbykevin.mastermind.common.ICommon;
import com.gamesbykevin.mastermind.panel.GamePanel;

import android.graphics.Canvas;

public class Entries implements ICommon 
{
	//our list of entries
	private ArrayList<Entry> entries;
	
	//the peg selection object we will use to render
	private Selection pegSelection;
	
	//the peg hint object we will use to render
	private Hint pegHint;
	
	/**
	 * The starting x-coordinate for the first entry
	 */
	public static final int START_X = 10;
	
	/**
	 * The starting y-coordinate for the first entry
	 */
	public static final int START_Y = GamePanel.HEIGHT - (BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * 3);
	
	/**
	 * How much space do we create between entries
	 */
	private static final float ADD_ENTRY_Y_INCREMENT_RATIO = 1.2f;
	
	//our board reference object
	private final Board board;
	
	//do we check the coordinate
	private boolean check = false;
	
	//the coordinate at which we check
	private int checkX, checkY;
	
	/**
	 * Default constructor
	 */
	public Entries(final Board board)
	{
		//store our board reference object
		this.board = board;
		
		//create a new peg entry object
		this.pegSelection = new Selection();
		
		//create a new peg hint object
		this.pegHint = new Hint();
	}
	
	/**
	 * Get the selection peg
	 * @return The selection peg used to render
	 */
	public Selection getPegSelection()
	{
		return this.pegSelection;
	}
	
	/**
	 * Get the hint peg
	 * @return The hint peg used to render
	 */
	public Hint getPegHint()
	{
		return this.pegHint;
	}
	
	/**
	 * Get the entries
	 * @return The list of selections and hints
	 */
	public ArrayList<Entry> getEntries()
	{
		return this.entries;
	}
	
	/**
	 * Get the entry
	 * @return The current entry
	 */
	public Entry getEntry()
	{
		return getEntries().get(getEntries().size() - 1);
	}
	
	/**
	 * Add a new peg entry to our list
	 * @param size The number of entries in the new entry
	 */
	public void add(final int size)
	{
		//create a new entry
		Entry entry = new Entry();
		
		//set the starting position accordingly
		if (getEntries().isEmpty())
		{
			entry.setX(START_X);
			entry.setY(
				START_Y - 
				(int)(getEntries().size() * (BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * ADD_ENTRY_Y_INCREMENT_RATIO)) 
			);
		}
		else
		{
			entry.setX(START_X);
			entry.setY(
				getEntry().getY() - 
				(int)(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * ADD_ENTRY_Y_INCREMENT_RATIO) 
			);
		}
		
		//set the size of the entry
		entry.setSize(size);
		
		//add entry to list
		this.entries.add(entry);
	}

	@Override
	public void dispose() 
	{
		if (this.entries != null)
		{
			this.entries.clear();
			this.entries = null;
		}
	}

	@Override
	public void update() throws Exception 
	{
		//if we are to check the coordinates
		if (this.check)
		{
			//flag check false
			this.check = false;
			
			//make sure the entry isn't being analyzed once more etc....
			if (!getEntry().isAnalyzed() && !getEntry().isReady())
			{
				//check each selection
				for (int index = 0; index < getEntry().getSelections().size(); index++)
				{
					//don't continue if the selection is null
					if (getEntry().getSelections().get(index) == null)
						continue;
					
					//get the coordinates for the selection
					int x = BoardHelper.getSelectionX(getEntry(), index) - (Selection.PEG_SELECTION_DIMENSION / 2);
					int y = BoardHelper.getSelectionY(getEntry()) - (Selection.PEG_SELECTION_DIMENSION / 2);
					
					//if the coordinate is not in the selection bounds, we skip
					if (checkX < x || checkY < y)
						continue;
					if (checkX > x + Selection.PEG_SELECTION_DIMENSION || checkY > y + Selection.PEG_SELECTION_DIMENSION)
						continue;
					
					//enable the choice again for the user
					for (Selection.Key key : Selection.Key.values())
					{
						//if the keys match
						if (key == getEntry().getSelections().get(index))
						{
							//flag the key as enabled
							key.setEnabled(true);
							
							//exit the loop
							break;
						}
					}
					
					//mark the selection null
					getEntry().getSelections().set(index, null);
					
					//play remove sound effect
					Audio.play(Assets.AudioGameKey.Remove, false);
				}
			}
		}
		else
		{
		
			for (int index = 0; index < getEntries().size(); index++)
			{
				//get the current entry
				final Entry entry = getEntries().get(index);
				
				//is the entry ready, analyze it
				if (entry.isReady() && !entry.isAnalyzed())
				{
					//find our hints
					entry.analyze(board.getSolution());
					
					//if we did not solve yet, add another entry
					if (!entry.isSolved())
					{
						//add another entry to the board
						board.add();
						
						//play not solved sound effect
						Audio.play(Assets.AudioGameKey.NotSolved, false);
					}
					else
					{
						//play solved sound effect
						Audio.play(Assets.AudioGameKey.Solved, false);
					}
				}
			}
		}
	}

	/**
	 * Check the specified coordinate to see if an entry was selected.<br>
	 * If the entry was already analyzed "isAnalyzed()" or ready to be "isReady()" then this will be ignored
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void check(final int x, final int y)
	{
		//if ready to be analyzed or already analyzed, we won't check
		if (getEntry().isAnalyzed() || getEntry().isReady())
			return;
		
		//flag check so we check it
		this.check = true;
		
		//store the coordinates to check
		this.checkX = x;
		this.checkY = y;
	}
	
	@Override
	public void reset() 
	{
		//create a new list of entries
		this.entries = new ArrayList<Entry>();
	}
	
	@Override
	public void render(Canvas canvas) throws Exception 
	{
		for (int i = 0; i < this.entries.size(); i++)
		{
			//get the current entry
			final Entry entry = this.entries.get(i);
			
			//make sure the appropriate dimensions are set
			getPegSelection().setWidth(Selection.PEG_SELECTION_DIMENSION);
			getPegSelection().setHeight(Selection.PEG_SELECTION_DIMENSION);
			
			//render each selection
			for (int index = 0; index < entry.getSelections().size(); index++)
			{
				//don't continue if null
				if (entry.getSelections().get(index) == null)
					continue;
				
				//assign the current animation
				getPegSelection().setAnimation(entry.getSelections().get(index));
				
				//assign the coordinates
				getPegSelection().setX(BoardHelper.getSelectionX(entry, index));
				getPegSelection().setY(BoardHelper.getSelectionY(entry));
				
				//render the appropriate image
				getPegSelection().render(canvas);
			}
			
			//make sure the appropriate dimensions are set
			getPegHint().setWidth(Hint.PEG_HINT_DIMENSION);
			getPegHint().setHeight(Hint.PEG_HINT_DIMENSION);
			
			//render each hint
			for (int index = 0; index < entry.getHints().size(); index++)
			{
				//don't continue if null
				if (entry.getHints().get(index) == null)
					continue;
				
				//assign the current animation
				getPegHint().setAnimation(entry.getHints().get(index));
				
				//assign the coordinates
				getPegHint().setX(BoardHelper.getHintX(entry, index));
				getPegHint().setY(BoardHelper.getHintY(entry, index));
				
				//render the appropriate image
				getPegHint().render(canvas);
			}
		}
	}
}