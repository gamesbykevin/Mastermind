package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;

public final class Selection extends Peg
{
	public enum Key
	{
		Red(0,0), White(1,0), 
		Black(0,1), LightPurple(1,1), 
		Gray(0,2), DarkPurple(1,2), 
		Blue(0,3), Green(1,3), 
		Yellow(0,4), Orange(1,4);
		
		//cell coordinate
		private final int col, row;
		
		private Key(final int col, final int row)
		{
			this.col = col;
			this.row = row;
		}
	}
	
	
	/**
	 * Animation dimensions of a peg
	 */
	public static final int PEG_SELECTION_ANIMATION_WIDTH = 64;
	
	/**
	 * Animation dimensions of a peg
	 */
	public static final int PEG_SELECTION_ANIMATION_HEIGHT = 64;
	
	
	/**
	 * Dimensions of the peg selection
	 */
	public static final int PEG_SELECTION_WIDTH = 32;
	
	/**
	 * Dimensions of the peg selection
	 */
	public static final int PEG_SELECTION_HEIGHT = 32;
	
	
	protected Selection() 
	{
		//add each selection as an animation
		for (Key key : Key.values())
		{
			final int x = key.col * PEG_SELECTION_ANIMATION_WIDTH;
			final int y = key.row * PEG_SELECTION_ANIMATION_HEIGHT;
			final int w = PEG_SELECTION_ANIMATION_WIDTH;
			final int h = PEG_SELECTION_ANIMATION_HEIGHT;
			
			//add animation
			addAnimation(Images.getImage(Assets.ImageGameKey.Selections), key, x, y, w, h);
		}
		
		//set a default selection
		setAnimation(Key.Black);
		
		//set a dimension
		setWidth(PEG_SELECTION_WIDTH);
		setHeight(PEG_SELECTION_HEIGHT);
	}
}