
package ox.musicalfingers.instrument.drum;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.badlogic.gdx.math.Circle;
import com.leapmotion.leap.*;

public class Drum extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	Circle [] keys = new Circle [2];
	
	HandList hands;
	Texture fingerPoint;
	Texture rectangle;
	Texture drums;
	int count = 5;
	
	float fingerScaleFact=1.3f;
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	ArrayList<Finger> fingerListSorted = new ArrayList<Finger>(10);
	
	boolean timeout =false;
	
	public Drum() {
		//Define keys in normalised InteractionBox co-ords
		keys[0] = new Circle(5f/22f,0.5f, 5f/22f);
		keys[1] = new Circle(17f/22f, 0.5f, 5f/22f);

		
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		drums = MusicalFingers.manager.get("assets/bongo.png");
		//ScaleFactor
		sF =  ((MusicalFingers.height-110)/(drums.getHeight())-10);
		//sF-=2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Piano");
        hands = controller.frame().hands();
        iBox = controller.frame().interactionBox();
    }
    
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	
    	count -=1;
		for (int i = 0; i < 2; i++) {
			notes[i] = false; // Stop starting previous sound.
		}
    	
    	if (iBox.isValid()) {
    	    hands =controller.frame().hands();
			
	    		for (Hand hand : hands) { 
	    			Vector palmPos =iBox.normalizePoint(hand.palmPosition(),false);
	    			Vector palmVel =hand.palmVelocity();
	    			if (palmVel.getY()>100){timeout=false;}
	    			
	    			for(int i=0;i<2;i++) {
	    				if (count<=0 && keys[i].contains(palmPos.getX(), palmPos.getZ())){
	    					if (palmVel.getY()<-100 &&!timeout) {
	    						notes[i]=true;
	    						timeout=true;
	    						count = 5;
			    			
			    		    }
	    				}
		    		
	    		   }
	    	    }
    	}
    }

 

    
    public synchronized void setNotes(boolean[] note) {
    	notes = note;
    }

	@Override
	public synchronized boolean[] getNotes() {
		System.out.println(Arrays.toString(notes));
		return notes;
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		//Piano
		float x = MusicalFingers.width/2f;
		float y =MusicalFingers.width/4f;
		batch.draw(drums,(int ) (MusicalFingers.width/2f -drums.getWidth()*0.5*sF) ,60, drums.getWidth()*sF,drums.getHeight()*sF );
		//Keys
		batch.setColor(0f, 0f, 0f, 0.5f);
		for (int i = 0; i < 2; i++) {
			if (notes[i]) {
				//batch.setColor(Color.WHITE);
				//batch.draw(rectangle, (int ) (MusicalFingers.width/2f - 0.5*sF) ,MusicalFingers.width/2f - 55f - 0.5f*sF, sF,sF);
			}
		}
		
		
		//Fingers
		batch.setColor(Color.WHITE);
		for(Hand hand: hands) {
			Vector palm=iBox.normalizePoint(hand.palmPosition(),false).times(fingerScaleFact);
			//Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
			batch.draw(fingerPoint,-12f+MusicalFingers.width/2f - (drums.getWidth()/2f*sF) +drums.getWidth()*sF*palm.getX(),60+drums.getHeight()*(1f-palm.getZ())*sF,24,24);

		}
		
	}

}
