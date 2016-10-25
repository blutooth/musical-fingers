package ox.musicalfingers.instrument.sampler;

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

public class Sampler extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[12];
	Rectangle[] keys = new Rectangle[12];
	
	Texture sampler;
	Texture fingerPoint;
	Texture rectangle;
	
	float scaleFactor=2;
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	ArrayList<Finger> fingerListSorted = new ArrayList<Finger>(10);
	
	float samplerLevel = 0.5f;
	
	public Sampler() {
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<12;i++) {
			keys[i] = new Rectangle((i%4)*(1f/4f),(i%3)*(1f/3f),1f/4f,1f/3f);
		}
		
		sampler = MusicalFingers.manager.get("assets/sampler.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		//ScaleFactor
		sF = (MusicalFingers.height-110)/sampler.getHeight();
		sF-=2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Piano");
        fingerList = controller.frame().fingers();
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	for(int i=0;i<12;i++) { notes[i]=false;}
    	
    	for(Hand hand : controller.frame().hands()) {
    		for(Finger finger : hand.fingers()) {
    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(scaleFactor);
    			
    			for(int i=0;i<12;i++) {

    				if (keys[i].contains(fingerPos.getX(), 1-fingerPos.getY()) &&
    	    				(fingerPos.getZ() < samplerLevel*scaleFactor)) {
    	    				notes[i] = true;
	    			}
    			}
    		}
    	}
    	fingerList = controller.frame().fingers();
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

	 
	public boolean[] getNotes() {
		return notes;
	}

	 
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		batch.draw(sampler, MusicalFingers.width/2f - sampler.getWidth()/2f*sF,MusicalFingers.height/6f,sampler.getWidth()*sF,sampler.getHeight()*sF);
		
		if (fingerList != null && iBox != null) {
			for(Finger finger: fingerList) {
				Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(scaleFactor);
				batch.draw(fingerPoint,-6f+MusicalFingers.width/2f - (sampler.getWidth()/2f*sF) +sampler.getWidth()*sF*fingerPos.getX(),-6f+MusicalFingers.height/6f+(6+64*(fingerPos.getY()))*sF,12,12);
			}
			
			batch.setColor(0f, 0f, 0f, 0.5f);
			for (int i = 0; i < 12; i++) {
				if (notes[i]) {
					batch.draw(rectangle, MusicalFingers.width/2f - sampler.getWidth()/2f*sF + ((i%4)*sampler.getWidth()/4f)*sF, MusicalFingers.height/6f + ((2-(i%3))*sampler.getHeight()/3f)*sF, (sampler.getWidth()/4f)*sF,sampler.getHeight()*sF / 3f);
				}
			}
			
			sortFingerList(fingerList);
			
			//Draw the levels of the fingers
			batch.setColor(Color.RED);
			for(int i=0; i < fingerListSorted.size(); i++) {
				Finger finger = fingerListSorted.get(i);
				Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),true).times(scaleFactor);
				
				batch.draw(rectangle,MusicalFingers.width/2f - fingerListSorted.size()/2f*10f+10f*i,50+(fingerPos.getZ()-samplerLevel*scaleFactor)*50f,10,5);
			}
		}	

		batch.setColor(Color.BLACK);
		batch.draw(rectangle,MusicalFingers.width/2f - 5*10f,49,100,6);
		
	}

}
