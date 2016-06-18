package com.gamesbykevin.mastermind.board.peg;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Peg extends Entity 
{
	protected Peg() 
	{
		//default constructor
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
	
	public void setAnimation(final Selection.Key key)
	{
		super.getSpritesheet().setKey(key);
	}
	
	public void setAnimation(final Hint.Key key)
	{
		super.getSpritesheet().setKey(key);
	}
	
	@Override
	public void render(final Canvas canvas)
	{
		//store coordinates before we offset
		final double x = getX();
		final double y = getY();
		
		//offset coordinates
		setX(x - (getWidth() / 2));
		setY(y - (getHeight() / 2));
		
		//now we can render
		render(canvas);
		
		//restore location
		setX(x);
		setY(y);
	}
}