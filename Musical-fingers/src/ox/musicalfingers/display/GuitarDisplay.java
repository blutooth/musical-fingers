package ox.musicalfingers.display;

import ox.musicalfingers.instrument.DiscreteDisplay;
import ox.musicalfingers.instrument.KeyPos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;

public class GuitarDisplay implements DiscreteDisplay {
	private KeyPos [] keyPos = new KeyPos[6];
	private boolean[] keys = new boolean[6];
	private FingerList fingers;
	private InteractionBox iBox;
	
	Texture rectangle;
	Texture circle;
	
	Texture piano;
	Texture fingerPoint;
	
	int scaleFactor = 1;
	
	public GuitarDisplay() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		pixmap = new Pixmap(16, 16, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, 1);
		pixmap.fillCircle(8, 8, 8);
		circle = new Texture(pixmap);
		pixmap.dispose();
		
		piano = MusicalFingers.manager.get("assets/guitar_main.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		scaleFactor = ((MusicalFingers.width/160)-2);
		
		for(int i=0;i<6;i++) {
				float z=1;
				float x=160+(i*16)*scaleFactor*z;
				float y=120+2*scaleFactor*z;
				float width=32*scaleFactor*z;
				float height=67*scaleFactor*z;
				keyPos[i]= new KeyPos(x/MusicalFingers.width, y/MusicalFingers.height, width/MusicalFingers.width, 
						height/MusicalFingers.height);
		}
	}
	
	@Override
	public int FindKey(Float x, Float y) {
		for(int i=0;i<6;i++) {
			if (keyPos[i].isInRange(x, y)) {return i;}
		}
		return -1;
	}

	@Override
	public void getNotes(boolean[] notes) {
		for(int i=0;i<6;i++) {
			keys[i] = notes[i];
		}
		
	}

	@Override
	public void getFingers(FingerList fingers) {
		this.fingers = fingers;
		
	}

	
	@Override
	public void getInteractionBox(InteractionBox iBox) {
		this.iBox = iBox;
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.setColor(Color.WHITE);
		
		batch.draw(piano,160,120,scaleFactor*160,scaleFactor*80);
		
		batch.setColor(0f,0f,0f,0.5f);
		for(int i=0;i<6;i++) {
			if(keys[i]) {
				float z=1;
				float x=147+2*scaleFactor*z;
				float y=150+(i*13)*scaleFactor*z;
				float width=160*scaleFactor*z;
				float height=5*scaleFactor*z;
				
				batch.draw(rectangle,x,y,width,height);
			}
		}
		
		batch.setColor(Color.WHITE);
		for(Finger finger : fingers) {
			
	        Vector Position = finger.tipPosition();
	        Vector nPos = iBox.normalizePoint(Position);

			batch.draw(fingerPoint,nPos.getX()*MusicalFingers.width-6,nPos.getY()*MusicalFingers.height-6,12,12);
			
		}
		
	}

}
