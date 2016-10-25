package ox.musicalfingers.instrument.Piano;

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




public class Piano_FiveKey implements DiscreteDisplay{
	
	private KeyPos [] keyPos = new KeyPos[5];
	private boolean[] keys = new boolean[5];
	private FingerList fingers;
	private InteractionBox iBox;
	
	Texture rectangle;
	Texture circle;
	
	Texture piano;
	Texture fingerPoint;
	
	int scaleFactor = 1;
	
	public Piano_FiveKey() {
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		pixmap = new Pixmap(16,16,Format.RGBA8888);
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle( 8, 8, 8 );
		circle = new Texture( pixmap );
		pixmap.dispose();
		
		piano = MusicalFingers.manager.get("assets/5key_piano.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		scaleFactor = ((MusicalFingers.width/160)-2);
		
		for(int i=0;i<5;i++) {
				float z=1;
				float x=160+(i*32)*scaleFactor*z;
				float y=120+2*scaleFactor*z;
				float width=32*scaleFactor*z;
				float height=67*scaleFactor*z;
				keyPos[i]= new KeyPos(x/MusicalFingers.width, y/MusicalFingers.height, width/MusicalFingers.width, 
						height/MusicalFingers.height);
		}
	}
	
	@Override
	public int FindKey(Float x, Float y) {
		for(int i=0;i<5;i++) {
			if (keyPos[i].isInRange(x, y)) {return i;}
		}
		return -1;
	}

	@Override
	public void getNotes(boolean[] notes) {
		for(int i=0;i<5;i++) {
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
		
		//Maybe not the best representation of a 5-key piano...
		/*
		batch.setColor(Color.BLUE);
		batch.draw(rectangle,385,100,510,300);
		
		for(int i=0;i<5;i++) {
			if(keys[i]) {
				batch.setColor(Color.CYAN);
			} else {
				batch.setColor(Color.GREEN);
			}
			batch.draw(rectangle,390+i*100,110,98,290);
		}
		
		for(Finger finger : fingers) {
			batch.setColor(Color.RED);
			batch.draw(circle,(finger.tipPosition().getX()-8)*(5)+640,finger.tipPosition().getZ()+300-8);
		}
		*/
		
		batch.setColor(Color.WHITE);
		
		batch.draw(piano,160,120,scaleFactor*160,scaleFactor*80);
		
		batch.setColor(0f,0f,0f,0.5f);
		for(int i=0;i<5;i++) {
			if(keys[i]) {
				float z=1;
				float x=160+(i*32)*scaleFactor*z;
				float y=120+2*scaleFactor*z;
				float width=32*scaleFactor*z;
				float height=67*scaleFactor*z;
				
				batch.draw(rectangle,x,y,width,height);
			}
		}
		
		batch.setColor(Color.WHITE);
		for(Finger finger : fingers) {
			
	        Vector Position = finger.tipPosition();
	        Vector nPos = iBox.normalizePoint(Position);

			batch.draw(fingerPoint,nPos.getX()*MusicalFingers.width-6,(1f-nPos.getZ())*MusicalFingers.height-6,12,12);
			//batch.draw(fingerPoint,finger.tipPosition().getX()*scaleFactor*160-32,finger.tipPosition().getY()*scaleFactor*80-32,64,64);
			//batch.draw(circle,(finger.tipPosition().getX()-8)*(5)+640,finger.tipPosition().getZ()+300-8);
;
		}
		
	}

}
