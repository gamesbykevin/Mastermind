package com.gamesbykevin.mastermind.board.peg;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.mastermind.board.Board;
import com.gamesbykevin.mastermind.common.ICommon;

import android.graphics.Canvas;

public class Entries implements Disposable, ICommon
{
	//our peg hint object to render the hint etc...
	private Hint pegHint;
	
	//our peg select object to render the selection etc...
	private Selection pegSelection;

	//keep track of our selections for each turn
	private ArrayList<Selection.Key[]> selections;
	
	//keep track of our hints for each turn
	private ArrayList<Hint.Key[]> hints;
	
	//the number of selections/hints we will have
	private final int size;
	
	//the starting location of our entries
	private int locationX = 0, locationY = 0;
	
	public Entries(final int size) 
	{
		//store the size
		this.size = size;
		
		//create a new list
		this.selections = new ArrayList<Selection.Key[]>();
		
		//create a new list
		this.hints = new ArrayList<Hint.Key[]>();
	}

	private int getSize()
	{
		return this.size;
	}
	
	public void addRow()
	{
		//add a new row of selections
		this.selections.add(new Selection.Key[getSize()]);
		
		//add a new row of hints
		this.hints.add(new Hint.Key[getSize()]);
	}
	
	@Override
	public void dispose() 
	{
		this.selections = null;
		this.hints = null;
		
		if (this.pegHint != null)
		{
			this.pegHint.dispose();
			this.pegHint = null;
		}
		
		if (this.pegSelection != null)
		{
			this.pegSelection.dispose();
			this.pegSelection = null;
		}
	}

	@Override
	public void update() throws Exception 
	{
		
	}

	@Override
	public void reset() 
	{
		
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		int rowY = 0;
		
		for (int row = 0; row < this.selections.size(); row++)
		{
			//set the current row
			rowY = this.locationY + (row * Board.PEG_BACKGROUND_ENTRY_DIMENSION) + (Board.PEG_BACKGROUND_ENTRY_DIMENSION / 2);
			
			//get the list of keys for the current row
			Selection.Key[] key1 = this.selections.get(row);
			
			//render each key (if available)
			for (int col = 0; col < getSize(); col++)
			{
				//find the appropriate x-coordinate
				int colX = this.locationX + (col * Board.PEG_BACKGROUND_ENTRY_DIMENSION) + (Board.PEG_BACKGROUND_ENTRY_DIMENSION / 2);
				
				//first check if the key exists
				if (key1[col] != null)
				{
					//assign the animation
					this.pegSelection.setAnimation(key1[col]);
					
					//assign coordinates
					this.pegSelection.setX(colX);
					this.pegSelection.setY(rowY);
					
					//render the peg accordingly
					this.pegSelection.render(canvas);
				}
			}
			
			//get the list of keys for the current row
			Hint.Key[] key2 = this.hints.get(row);
			
			//render each key (if available)
			for (int col = 0; col < getSize(); col++)
			{
				//find the appropriate x-coordinate
				int colX = this.locationX + (col * Board.PEG_BACKGROUND_ENTRY_DIMENSION) + (Board.PEG_BACKGROUND_ENTRY_DIMENSION / 2);
				
				//offset the coordinates for the hints
				colX += (getSize() * Board.PEG_BACKGROUND_ENTRY_DIMENSION) + (Board.PEG_BACKGROUND_ENTRY_DIMENSION / 2);
				
				//if the key exists
				if (key2[col] != null)
				{
					//assign the animation
					this.pegHint.setAnimation(key2[col]);
					
					//assign coordinates
					this.pegHint.setX(colX);
					this.pegHint.setY(rowY);
					
					//render the peg accordingly
					this.pegHint.render(canvas);
				}
			}
		}
	}
}