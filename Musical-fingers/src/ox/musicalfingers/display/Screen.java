package ox.musicalfingers.display;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Screen {
	
	//Initialise the screen here
	public void init();
	
	//Main loop for the screen
	public void update();
	
	//Draw the screen
	public void draw(SpriteBatch batch);
	
	/*
	 * Change the current screen to something different 
	 * Return -1 to indicate you don't want to change state
	 */
	public int changeStateTo();
	
	//Dispose of any loaded assets here 
	public void dispose();
}
