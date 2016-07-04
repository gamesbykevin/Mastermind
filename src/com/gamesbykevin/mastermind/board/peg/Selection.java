package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;

public final class Selection extends Peg
{
	/**
	 * All the possible selections
	 */
	public enum Key
	{
		Red(0,0), White(1,0), Black(0,1), Yellow(1,1), Orange(0,2), Brown(1,2), Blue(0,3), Green(1,3);
		
		//cell coordinate
		private final int col, row;
		
		private boolean enabled;
		
		private Key(final int col, final int row)
		{
			//store the location on the sprite sheet
			this.col = col;
			this.row = row;
			
			//flag true at default
			setEnabled(true);
		}
		
		/**
		 * Is this selection enabled?
		 * @return true if the user can select this color, false otherwise
		 */
		public boolean isEnabled()
		{
			return this.enabled;
		}
		
		/**
		 * Flag the selection enabled.
		 * @param enabled true if the user can select this color, false otherwise
		 */
		public void setEnabled(final boolean enabled)
		{
			this.enabled = enabled;
		}
	}
	
	/**
	 * Animation dimensions of a peg
	 */
	private static final int PEG_SELECTION_ANIMATION_WIDTH = 64;
	
	/**
	 * Animation dimensions of a peg
	 */
	private static final int PEG_SELECTION_ANIMATION_HEIGHT = 64;
	
	/**
	 * The ratio compared to the board background selection
	 */
	public static final float SIZE_RATIO = 0.9f;
	
	/**
	 * Dimensions of the peg selection
	 */
	public static int PEG_SELECTION_DIMENSION;
	
	/**
	 * Default Constructor
	 */
	public Selection() 
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
	}
	
	@Override
	public void setAnimation(final Object key)
	{
		super.getSpritesheet().setKey(key);
	}
}