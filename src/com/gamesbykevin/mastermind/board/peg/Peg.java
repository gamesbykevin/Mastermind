package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;

import android.graphics.Bitmap;

public final class Peg extends Entity
{
	public enum Selections
	{
		Red(0,0), White(1,0), 
		Black(0,1), LightPurple(1,1), 
		Gray(0,2), DarkPurple(1,2), 
		Blue(0,3), Green(1,3), 
		Yellow(0,4), Orange(1,4);
		
		//cell coordinate
		private final int col, row;
		
		private Selections(final int col, final int row)
		{
			this.col = col;
			this.row = row;
		}
	}
	
	public enum Hints
	{
		MatchColor(0, 320),
		MatchColorPosition(31, 320);
		
		//coordinates of animation
		private final int x, y;
		
		private Hints(final int x, final int y)
		{
			this.x = x;
			this.y = y;
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
	 * Animation dimensions of the hint peg
	 */
	public static final int PEG_HINT_ANIMATION_WIDTH = 31;
	
	/**
	 * Animation dimensions of the hint peg
	 */
	public static final int PEG_HINT_ANIMATION_HEIGHT = 32;
	
	protected Peg() 
	{
		//add each selection as an animation
		for (Selections selection : Selections.values())
		{
			final int x = selection.col * PEG_SELECTION_ANIMATION_WIDTH;
			final int y = selection.row * PEG_SELECTION_ANIMATION_HEIGHT;
			final int w = PEG_SELECTION_ANIMATION_WIDTH;
			final int h = PEG_SELECTION_ANIMATION_HEIGHT;
			
			//add animation
			addAnimation(Images.getImage(Assets.ImageGameKey.Selections), selection, x, y, w, h);
		}
		
		//add each hint as an animation
		for (Hints hint : Hints.values())
		{
			final int x = hint.x;
			final int y = hint.y;
			final int w = PEG_HINT_ANIMATION_WIDTH;
			final int h = PEG_HINT_ANIMATION_HEIGHT;
			
			//add animation
			addAnimation(Images.getImage(Assets.ImageGameKey.Selections), hint, x, y, w, h);
		}
	}
	
	private void addAnimation(final Bitmap image, final Object key, final int x, final int y, final int w, final int h)
	{
		//create a new animation
		Animation animation = new Animation(image, x, y, w, h);
		
		//add animation to sprite sheet
		super.getSpritesheet().add(key, animation);
	}
}