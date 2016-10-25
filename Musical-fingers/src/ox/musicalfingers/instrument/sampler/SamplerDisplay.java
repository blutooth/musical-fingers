package ox.musicalfingers.instrument.sampler;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;


import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteDisplay;
import ox.musicalfingers.instrument.KeyPos;

public class SamplerDisplay implements DiscreteDisplay{

	
	private KeyPos [] keyPos = new KeyPos[12];
	private boolean[] keys = new boolean[12];
	private FingerList fingers;
	private InteractionBox iBox;
	
	Texture rectangle;
	Texture circle;
	
	Texture sampler;
	Texture fingerPoint;
	
	int scaleFactor = 1;
	
	public SamplerDisplay() {
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		pixmap = new Pixmap(16,16,Format.RGBA8888);
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle( 8, 8, 8 );
		circle = new Texture( pixmap );
		pixmap.dispose();
		
		sampler = MusicalFingers.manager.get("assets/sampler.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		scaleFactor = ((MusicalFingers.width/160)-2);
		
		for(int i=0;i<12;i++) {
				float z=1;
				float x=160+((i%4)*40)*scaleFactor*z;
				float y=120+((i%3)*32)*2*scaleFactor*z;
				float width=40*scaleFactor*z;
				float height=32*scaleFactor*z;
				keyPos[i]= new KeyPos(x/MusicalFingers.width, y/MusicalFingers.height, width/MusicalFingers.width, 
						height/MusicalFingers.height);
		}
	}
	
	 
	public int FindKey(Float x, Float y) {
		for(int i=0;i<12;i++) {
			if (keyPos[i].isInRange(x, y)) {return i;}
		}
		return -1;
	}

	 
	public void getNotes(boolean[] notes) {
		for(int i=0;i<12;i++) {
			keys[i] = notes[i];
		}
		
	}

	 
	public void getFingers(FingerList fingers) {
		this.fingers = fingers;
		
	}

	
	 
	public void getInteractionBox(InteractionBox iBox) {
		this.iBox = iBox;
		
	}
	
	 
	public void draw(SpriteBatch batch) {
		
		batch.setColor(Color.WHITE);
		
		batch.draw(sampler,160,120,scaleFactor*160,scaleFactor*80);
		
		batch.setColor(0f,0f,0f,0.5f);
		for(int i=0;i<12;i++) {
			if(keys[i]) {
				float z=1;
				float x=160+((i%4)*40)*scaleFactor*z;
				float y=120+((i%3)*32)*2*scaleFactor*z;
				float width=40*scaleFactor*z;
				float height=32*scaleFactor*z;
				
				batch.draw(rectangle,x,y,width,height);
			}
		}
		
		batch.setColor(Color.WHITE);
		for(Finger finger : fingers) {
			
	        Vector Position = finger.tipPosition();
	        Vector nPos = iBox.normalizePoint(Position);

			batch.draw(fingerPoint,nPos.getX()*MusicalFingers.width-6,(1f-nPos.getZ())*MusicalFingers.height-6,12,12);
;
		}
		
	}
}
