package ox.musicalfingers.instrument.flute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteInputDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class Flute extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	private int[] fingeringX = new int[8]; 
	private int[] fingeringY = new int[8]; // where to draw dots if things are held.
	private boolean[] fingerPressed = new boolean[8];
	
	Texture flute;
	Texture fingerPoint;
	Texture rectangle;
	
	float fingerScaleFact=1.3f;
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	ArrayList<Finger> fingerListSorted = new ArrayList<Finger>(10);
	
	float pianoLevel = 0.5f;
	
	MicrophoneVolume mV = MicrophoneVolume.getMic();
	
	public Flute() {
		//Define keys in normalised InteractionBox co-ords
		// define finger position on image
		fingeringX[0] = 24; fingeringY[0] = 40;
		fingeringX[1] = 40; fingeringY[1] = 40;
		fingeringX[2] = 56; fingeringY[2] = 40;
		fingeringX[3] = 60; fingeringY[3] = 56;
		fingeringX[4] = 78; fingeringY[4] = 40;
		fingeringX[5] = 94; fingeringY[5] = 40;
		fingeringX[6] = 108; fingeringY[6] = 40;
		fingeringX[7] = 120; fingeringY[7] = 26;
				
		flute = MusicalFingers.manager.get("assets/flute.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		//ScaleFactor
		sF = (int) (MusicalFingers.height/flute.getHeight()/1.5f);
		//sF-=2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Flute");
        fingerList = controller.frame().fingers();
        iBox = controller.frame().interactionBox();
    }
    
    
    public void onFrame(Controller controller) {
    	fingerList = controller.frame().fingers();
    	sortFingerList(fingerList);
    	if (fingerList.count() == 10) { // if we can see all 10 fingers
    		int fId = 0;
    		for (Finger fin : fingerListSorted) {
    			if (! (fin == fin.hand().fingers().leftmost())) {
    				if (fin.tipPosition().getY() - fin.hand().palmPosition().getY() < 4f) {
    					fingerPressed[fId] = true;
    				} else {
    					fingerPressed[fId] = false;
    				}
    				fId += 1; // FIXME
    			}
    			
    		}
    		
    		// interpret finger position to get note.
    		// TODO
    		
    		// test whether to make noise
    		if (mV.getMicVol() > 0.001) {
    			System.out.println("NOISE");
    			notes[2] = true; // TODO, actually, play the right note!
    		}
    		
    		
    		
    		
    	} else {
    		for (int f = 0; f < 8; f++) {
    			fingerPressed[f] = false;
    		}
    	}
    }
    
    private void sortFingerList(FingerList fingers) {
    	fingerListSorted.clear();
    	for(Finger finger : fingers) {
    		fingerListSorted.add(finger);
    	}
    	Collections.sort(fingerListSorted, new Comparator<Finger>() {
    		public int compare(Finger f1, Finger f2) {
    			return (int) (f1.tipPosition().getX() - f2.tipPosition().getX());
    		}
    	});
    }
    
    public synchronized void setNotes(boolean[] note) {
    	notes = note;
    }

	@Override
	public synchronized boolean[] getNotes() {
		return notes;
	}

	@Override
	public void draw(SpriteBatch batch) {
		//flute
		batch.setColor(Color.WHITE);
		batch.draw(flute, MusicalFingers.width/2f - flute.getWidth()/2f*sF,MusicalFingers.height/6f,flute.getWidth()*sF,flute.getHeight()*sF);
		
		// Fingerings
		batch.setColor(Color.WHITE);
		
		for (int i = 0; i < 8; i++) {
			if (fingerPressed[i]) {
				batch.draw(fingerPoint,-12f+MusicalFingers.width/2f - (flute.getWidth()/2f*sF) +fingeringX[i]*sF,-12f+MusicalFingers.height/6f+ fingeringY[i]*sF,24,24);
			}
		}
		
		//Fingers
		for(Finger finger: fingerList) {

			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
			batch.draw(fingerPoint,-12f+MusicalFingers.width/2f - (flute.getWidth()/2f*sF) +flute.getWidth()*sF*fingerPos.getX(),-12f+MusicalFingers.height/6f+(6+64*(1f-1f*fingerPos.getZ()))*sF,24,24);

		}
		
	}

}