package ox.musicalfingers.game;

import ox.musicalfingers.display.MusicalFingers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class GameNote {
	 public int note;
	  public float pos;
	  Texture gameNote;
	  public GameNote(int a, float b) {
		  note = a;
		  pos = b;
		  gameNote = MusicalFingers.manager.get("assets/game/GameNote.png");
	  }
	  
	  public void draw(SpriteBatch batch) {
		  switch(note) {
			case 0: 	batch.setColor(Color.RED);
						break;
			case 1:		batch.setColor(Color.BLUE);
						break;
			case 2:  	batch.setColor(Color.GREEN);
						break;
			case 3:		batch.setColor(Color.YELLOW);
						break;
			case 4:		batch.setColor(Color.MAGENTA);
						break;
			default:				throw new Error("not a valid state");
		}
		  batch.draw(gameNote,MusicalFingers.width/2f - 160/2f*5+note*32*5+gameNote.getWidth()*2.5f,pos,gameNote.getWidth()*10,gameNote.getHeight()*10);
	  }
	  
	  public Rectangle bounds() {
		  return new Rectangle(MusicalFingers.width/2f - 160/2f*5+note*32*5+gameNote.getWidth()*2.5f,pos,gameNote.getWidth()*10,gameNote.getHeight()*10);
	  }
	  
	  public int getX() {
		  return (int) (MusicalFingers.width/2f - 160/2f*5+note*32*5+gameNote.getWidth()*2.5f);
	  }
	  
	  public int getY() {
		  return (int) (pos);
	  }
}
