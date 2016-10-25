package ox.musicalfingers.instrument;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.InteractionBox;


public interface DiscreteDisplay {
	
	public void getNotes(boolean[] notes);
	
	public void getFingers(FingerList fingers);
	
	public int FindKey(Float x, Float y) ;
	
	public void getInteractionBox(InteractionBox box);
	
	public void draw(SpriteBatch batch);

}
