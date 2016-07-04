package com.gamesbykevin.mastermind.level;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.mastermind.assets.Assets;

public class Levels implements Disposable 
{
	//list of levels
	private HashMap<Integer, ArrayList<Level>> levels;

	//the level number index
	private int numberLevel = 0;
	
	//the number of colors
	private int numberColorsIndex = 0;
	
	/**
	 * Default Constructor
	 */
	@SuppressLint("UseSparseArrays")
	public Levels() 
	{
		//create new hashmap of levels
		this.levels = new HashMap<Integer, ArrayList<Level>>();
		
		//load the levels
		this.load();
	}
	
	public void setNumberColorsIndex(final int numberColorsIndex)
	{
		this.numberColorsIndex = numberColorsIndex;
	}
	
	public int getNumberColorsIndex()
	{
		return this.numberColorsIndex;
	}

	public void setNumberLevel(final int numberLevel)
	{
		this.numberLevel = numberLevel;
		
		//if out of range go back to the beginning
		if (getNumberLevel() >= getSolutions().size())
			setNumberLevel(0);
	}
	
	public int getNumberLevel()
	{
		return this.numberLevel;
	}
	
	/**
	 * Get the solution
	 * @return The solution according to the current number colors index and number level
	 */
	public int[] getSolution()
	{
		return getSolutions().get(getNumberLevel()).getSolution();
	}
	
	/**
	 * Get the list of solutions
	 * @return The list of solutions for the assigned number of colors index 
	 */
	private ArrayList<Level> getSolutions()
	{
		return this.levels.get(getNumberColorsIndex());
	}
	
	/**
	 * Get the number of levels
	 * @return The number of levels based on the currently assigned number colors index
	 */
	public int getSize()
	{
		return this.levels.get(getNumberColorsIndex()).size();
	}
	
	/**
	 * Load the levels from what is found in the text file
	 */
	private void load()
	{
		//check every line in our text file
		for (String line : Files.getText(Assets.TextKey.Levels).getLines())
		{
			//remove empty spaces
			line = line.trim();
			
			//skip if there is no data
			if (line.length() < 1)
				continue;
			
			//create our solution array
			int[] solution = new int[line.length()];
			
			//check each character in the array
			for (int i = 0; i < solution.length; i++)
			{
				//store the solution
				solution[i] = Integer.parseInt(line.substring(i, i + 1));
			}
			
			//temp array list object
			ArrayList<Level> tmp = null;
			
			//our key
			final int key = solution.length - 3;
			
			//use list if it exists
			if (this.levels.get(key) == null)
			{
				tmp = new ArrayList<Level>();
			}
			else
			{
				tmp = this.levels.get(key);
			}
			
			//add our level to the current array list
			tmp.add(new Level(solution));
			
			//add to our hash map
			this.levels.put(key, tmp);
		}
	}
	
	@Override
	public void dispose() 
	{
		if (this.levels != null)
		{
			this.levels.clear();
			this.levels = null;
		}
	}
	
	private class Level
	{
		private final int[] solution;
		
		private Level(int[] solution)
		{
			this.solution = solution;
		}
		
		private int[] getSolution()
		{
			return this.solution;
		}
	}
}