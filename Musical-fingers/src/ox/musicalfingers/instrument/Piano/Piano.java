package ox.musicalfingers.instrument.Piano;

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

public class Piano extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	Rectangle[] keys = new Rectangle[5];
	
	Texture piano;
	Texture fingerPoint;
	Texture rectangle;
	
	float fingerScaleFact=1.3f;
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	ArrayList<Finger> fingerListSorted = new ArrayList<Finger>(10);
	
	float pianoLevel = 0.5f;
	
	public Piano() {
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<5;i++) {
			keys[i] = new Rectangle(i*(1f/5f),0f,(1f/5f),1f);
		}
		
		piano = MusicalFingers.manager.get("assets/5key_piano.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		//ScaleFactor
		sF = (int) (MusicalFingers.height/piano.getHeight()/1.5f);
		//sF-=2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Piano");
        fingerList = controller.frame().fingers();
        iBox = controller.frame().interactionBox();
    }
    
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	 //for(int i=0;i<5;i++) { notes[i]=false;}
  

    	
    
    	//new method
    	for (Hand hand : controller.frame().hands()){
    		//Hand right = controller.frame().hands().rightmost();
    		Vector norm= hand.palmNormal();
    		double  fst=-1000; double snd =-100000; double count=0;
    		for (Finger finger : hand.fingers()) {fst=Math.max(finger.tipPosition().getY(), fst); count+=1;}
    		for (Finger finger : hand.fingers() ) {
    			if (finger.tipPosition().getY()!=fst) {snd=Math.max(finger.tipPosition().getY(), snd);}
    		} 
    		if (count<5){snd=-1000;}
    		if (count<4){fst=-1000;}
    		
    		for(Finger finger : hand.fingers()) {
    			Vector fingerEnd = finger.tipPosition().minus(finger.direction().normalized().times(finger.length()));
    			
    			float LRangle =fingerEnd.minus(hand.palmPosition()).angleTo(hand.direction());
		    	 double  j=0.9; double k =1;
		    	if(LRangle>1.6) {j=1.2;} else if (LRangle>1) {j=1;} else if (LRangle <0.20) {j=0.8;k=1.3;}

    			
    			//System.out.println(LRangle+"," +(fingerEnd.getX()-hand.palmPosition().getX()));

    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
    			//Vector fingerPos =finger.tipPosition();
    			boolean anyIn =false; 
    			for(int i=0;i<5;i++) {
    				if (keys[i].contains(fingerPos.getX(), fingerPos.getZ())){
    					Vector fingerDir = finger.direction();
    					float fingerSpeed=finger.tipVelocity().getY();
    						
    					      float cosAngle = fingerDir.angleTo(norm);
    					      if (cosAngle< j*8*Math.PI/16 && fingerSpeed <-200*k
    					    		  && finger.tipPosition().getY()!=fst &&
    		    						finger.tipPosition().getY()!=snd){
    					    	  
    			    				notes[i] = true;
    					      }	
    			    		  if (cosAngle> j*8*Math.PI/16 || fingerSpeed >60*k){
    	    					    	  
        			    				notes[i] = false;
 
    					      }
    				}
    			 }
    		}
    		
    		for(int i=0;i<5;i++) {
    			boolean isoff= true;
    			for(Finger finger : hand.fingers()) {
        			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
    			    if (keys[i].contains(fingerPos.getX(), fingerPos.getZ())){isoff= false;}

    			}
    			if (isoff) {notes[i]=false;}
    		}
		
    		
    	}
       
    	

    	fingerList = controller.frame().fingers();
    }
    
 /*   public void onFrame(Controller controller) {
    	
    	boolean[] noteCopy = new boolean[5];
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	for(int i=0;i<5;i++) { noteCopy[i]=false;}
    	
    	//for(Hand hand : controller.frame().hands()) {
    		for(Finger finger : controller.frame().fingers()) {
    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
    			
    			for(int i=0;i<5;i++) {
    				/*
	    			if (keys[i].contains(fingerPos.getX(), fingerPos.getZ()) && (iBox.normalizePoint(palmPos,false).getY() - fingerPos.getY() > 0.001f)) {
	    				notes[i] = true;
	    			}
	    				//*/
    				///*
 /*					if (keys[i].contains(fingerPos.getX(), fingerPos.getZ()) && (fingerPos.getY() < pianoLevel)) {
    					noteCopy[i] = true;
	    			}
    				/*
    				else {
	    				notes[i]=false;
	    			}
	    			*/
 /*				}
    		}
    	//}
    	
    	this.setNotes(noteCopy);
    	fingerList = controller.frame().fingers();
    }
 */    
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
		
		//Piano
		batch.setColor(Color.WHITE);
		batch.draw(piano, MusicalFingers.width/2f - piano.getWidth()/2f*sF,MusicalFingers.height/6f,piano.getWidth()*sF,piano.getHeight()*sF);
		
		//Keys
		batch.setColor(0f, 0f, 0f, 0.5f);
		for (int i = 0; i < 5; i++) {
			if (notes[i]) {
				batch.draw(rectangle, MusicalFingers.width/2f - piano.getWidth()/2f*sF + (i*32)*sF,MusicalFingers.height/6f + 2*sF, 32*sF,67*sF);
			}
		}
		
		sortFingerList(fingerList);
		
		//Fingers
		batch.setColor(Color.WHITE);
		for(Finger finger: fingerList) {

			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
			batch.draw(fingerPoint,-12f+MusicalFingers.width/2f - (piano.getWidth()/2f*sF) +piano.getWidth()*sF*fingerPos.getX(),-12f+MusicalFingers.height/6f+(6+64*(1f-1f*fingerPos.getZ()))*sF,24,24);

		}
		
	}

}
