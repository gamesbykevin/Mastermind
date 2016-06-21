package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.mastermind.entity.Entity;

import android.graphics.Bitmap;

public abstract class Peg extends Entity 
{
	/**
	 * default constructor
	 */
	protected Peg() 
	{
		//call parent constructor
		super();
	}

	protected void addAnimation(final Bitmap image, final Object key, final int x, final int y, final int w, final int h)
	{
		//create a new animation
		Animation animation = new Animation(image, x, y, w, h);
		
		//add animation to sprite sheet
		super.getSpritesheet().add(key, animation);
		
		//if there is no current animation set yet
		if (super.getSpritesheet().getKey() == null)
		{
			//assign a default animation
			super.getSpritesheet().setKey(key);
			
			//assign a default dimension
			super.setWidth(w);
			super.setHeight(h);
		}
	}
	
	public abstract void setAnimation(final Object key);
}