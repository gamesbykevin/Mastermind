package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.board.BoardHelper;

public final class Hint extends Peg 
{
	public enum Key
	{
		MatchColor(0, 320),
		MatchColorAndPosition(31, 320);
		
		//coordinates of animation
		private final int x, y;
		
		private Key(final int x, final int y)
		{
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Animation dimensions of the hint peg
	 */
	private static final int PEG_HINT_ANIMATION_WIDTH = 31;
	
	/**
	 * Animation dimensions of the hint peg
	 */
	private static final int PEG_HINT_ANIMATION_HEIGHT = 32;
	
	/**
	 * The ratio compared to the board background hint
	 */
	public static final float SIZE_RATIO = 0.5f;
	
	/**
	 * Dimensions of the peg hint
	 */
	public static int PEG_HINT_DIMENSION = (int)(BoardHelper.PEG_BACKGROUND_ENTRY_DIMENSION * SIZE_RATIO);
	
	public Hint()
	{
		//add each hint as an animation
		for (Key key : Key.values())
		{
			final int x = key.x;
			final int y = key.y;
			final int w = PEG_HINT_ANIMATION_WIDTH;
			final int h = PEG_HINT_ANIMATION_HEIGHT;
			
			//add animation
			addAnimation(Images.getImage(Assets.ImageGameKey.Selections), key, x, y, w, h);
		}
		
		//set a default selection
		setAnimation(Key.MatchColor);
		
		//set a dimension
		setWidth(PEG_HINT_DIMENSION);
		setHeight(PEG_HINT_DIMENSION);
	}
	
	@Override
	public void setAnimation(final Object key)
	{
		super.getSpritesheet().setKey(key);
	}
}