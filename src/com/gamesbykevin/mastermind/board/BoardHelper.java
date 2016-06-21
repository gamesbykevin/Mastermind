package com.gamesbykevin.mastermind.board;

import com.gamesbykevin.mastermind.board.entries.Entry;

public class BoardHelper 
{
	/**
	 * The smallest size of the board entries
	 */
	public static final int SIZE_MINIMUM = 3;
	
	/**
	 * The smallest size of the board entries
	 */
	public static final int SIZE_MAXIMUM = 8;
	
	/**
	 * The render size of the peg background assuming we have the maximum board entries
	 */
	public static final int PEG_BACKGROUND_ENTRY_DIMENSION_MIN = 30;
	
	/**
	 * The render size of the peg background 
	 */
	public static final int PEG_BACKGROUND_ENTRY_DIMENSION_MID = 45;
	
	/**
	 * The render size of the peg background assuming we have the minimum board entries
	 */
	public static final int PEG_BACKGROUND_ENTRY_DIMENSION_MAX = 60;

	/**
	 * The render size of the peg background 
	 */
	public static int PEG_BACKGROUND_ENTRY_DIMENSION = 40;
	
	/**
	 * The ratio compared to the board background selection
	 */
	public static final float PEG_HINT_SIZE_RATIO = 0.5f;
	
	/**
	 * The render size of the hint background
	 */
	public static int PEG_BACKGROUND_HINT_DIMENSION = (int)(PEG_BACKGROUND_ENTRY_DIMENSION * PEG_HINT_SIZE_RATIO);
	
	/**
	 * How many hints can we show on the same row for a single entry
	 */
	private static final int HINTS_PER_ROW = 4;
	
	public static final int getSelectionX(final Entry entry, final int col)
	{
		return (entry.getX() + (col * PEG_BACKGROUND_ENTRY_DIMENSION) + (PEG_BACKGROUND_ENTRY_DIMENSION / 2));
	}
	
	public static final int getSelectionY(final Entry entry)
	{
		return (entry.getY() + (PEG_BACKGROUND_ENTRY_DIMENSION / 2));
	}
	
	public static final int getHintX(final Entry entry, final int index)
	{
		//if we meet the minimum requirement, get other x-coordinate
		if (entry.getHints().size() <= HINTS_PER_ROW)
			return (getSelectionX(entry, entry.getHints().size()) + (index * PEG_BACKGROUND_HINT_DIMENSION) + (PEG_BACKGROUND_HINT_DIMENSION / 2) - (PEG_BACKGROUND_HINT_DIMENSION / 3));
		
		//determine what row this is on
		final int row = (index / HINTS_PER_ROW);
		
		//determine which column it is
		final int col = (index - (row * HINTS_PER_ROW));
		
		//return calculated x-coordinate
		return (getSelectionX(entry, entry.getHints().size()) + (col * PEG_BACKGROUND_HINT_DIMENSION) + (PEG_BACKGROUND_HINT_DIMENSION / 2) - (PEG_BACKGROUND_HINT_DIMENSION / 3));
	}
	
	public static final int getHintY(final Entry entry, final int index)
	{
		//if we meet the minimum requirement, get other y-coordinate
		if (entry.getHints().size() <= HINTS_PER_ROW)
			return getSelectionY(entry);
		
		//determine what row this is on
		final int row = (index / HINTS_PER_ROW);
		
		//return calculated y-coordinate
		return (entry.getY() + (row * PEG_BACKGROUND_HINT_DIMENSION) + (PEG_BACKGROUND_HINT_DIMENSION / 2));
	}
}