package ox.musicalfingers.instrument;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface DiscreteInputDisplay {
	
	public boolean[] getNotes();
	
	public void draw(SpriteBatch batch);

}
