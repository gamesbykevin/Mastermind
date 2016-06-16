package com.gamesbykevin.mastermind.board;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.mastermind.assets.Assets;
import com.gamesbykevin.mastermind.common.ICommon;

import android.graphics.Canvas;

public class Board extends Entity implements ICommon
{
	/**
	 * Animation dimensions of an entry peg
	 */
	public static final int PEG_ENTRY_ANIMATION_DIMENSION = 80;
	
	/**
	 * Animation dimensions of the hint peg
	 */
	public static final int PEG_HINT_ANIMATION_DIMENSION = 40;
	
	public enum Key
	{
		Entry, Hint
	}
	
	public Board() 
	{
		//add animations
		super.getSpritesheet().add(Key.Entry, new Animation(Images.getImage(Assets.ImageGameKey.Entry)));
		super.getSpritesheet().add(Key.Hint, new Animation(Images.getImage(Assets.ImageGameKey.Hint)));
	}

	@Override
	public void update() throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset()
	{
		
		
	}

	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//draw background first
		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.Background), 0, 0, null);
		
		//now draw entry and hint pegs accordingly
	}
}
