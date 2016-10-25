package ox.musicalfingers.game;

import ox.musicalfingers.display.MusicalFingers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion {
	
	private int frame =0;
	private int note = 0;
	private int x =0;
	private int y=0;
	
	private Texture[] frames = new Texture[7];
	
	private int time=0;
	private final int ticksPerFrame=3;
	
	private int sf = 10;
	
	public Explosion(int x, int y, int note) {
		for(int i=0;i<7;i++) {
			frames[i] = MusicalFingers.manager.get("assets/game/explos"+i+".png");
		}
		this.x=x;
		this.y=y;
		this.note=note;
	}
	
	public void update() {
		time++;
		if(time>ticksPerFrame){
			frame++;
			time=0;
		}
	}
	
	public boolean dead() {
		return frame>6;
	}

	public void draw(SpriteBatch batch) {
		switch (note) {
		case 0:
			batch.setColor(Color.RED);
			break;
		case 1:
			batch.setColor(Color.BLUE);
			break;
		case 2:
			batch.setColor(Color.GREEN);
			break;
		case 3:
			batch.setColor(Color.YELLOW);
			break;
		case 4:
			batch.setColor(Color.MAGENTA);
			break;
		case 5:
			batch.setColor(Color.BLACK);
			break;
		default:
			throw new Error("not a valid state");
		}
		if(frame<=6)
		batch.draw(frames[frame], x-frames[frame].getWidth()/2f*sf+55,y-frames[frame].getHeight()/2f+55,frames[frame].getWidth() * sf, frames[frame].getHeight() * sf);
	}

}
