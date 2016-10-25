package ox.musicalfingers.game;

import java.util.Arrays;

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
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Leap;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class GamePianoInput extends Listener {
	
	private boolean[] notes = new boolean[5];
	private boolean[] prevNotes = new boolean[5];
	boolean[] pressed = new boolean[5];
	Rectangle[] keys = new Rectangle[5];
	Rectangle[] bounds = new Rectangle[5];
	float fingerScaleFact = 1f;
	Texture fingerPoint;
	
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	Texture piano;
	Texture rectangle;
	
	float pianoLevel = 0.5f;
	
	public GamePianoInput() {
		
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		piano = MusicalFingers.manager.get("assets/game/game_piano.png");
		
		//Looks better this way
		sF = 1280/piano.getWidth() - 3;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<5;i++) {
			keys[i] = new Rectangle(i*(1f/5f),0f,(1f/5f),1f);
			bounds[i] = new Rectangle(MusicalFingers.width/2f - piano.getWidth()/2f*sF+32*sF*i,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f))+3*4,32*sF,67*4);
		}
		
	}
	
	public Rectangle getBounds(int i) {
		return bounds[i];
	}
	
	public boolean isReady() {
		return !fingerList.isEmpty();
	}
	
	public void onInit (Controller controller) {
        System.out.println("Initialized GamePiano");
        fingerList = controller.frame().fingers();
        iBox = controller.frame().interactionBox();
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        controller.config().setFloat("Gesture.KeyTap.MinDownVelocity", 1);
        controller.config().setFloat("Gesture.KeyTap.HistorySeconds", 0.2f);
        controller.config().setFloat("Gesture.KeyTap.MinDistance", 3f);
        boolean saved = controller.config().save();
        if(!saved)
        	System.out.println("Config.save failure");
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	for(int i=0;i<5;i++) { notes[i]=false;}
  
    	/* GestureList gesticulations  = controller.frame().gestures();
    	 for (Gesture gesture : gesticulations){
    		 if(gesture.type() == KeyTapGesture.classType()) {
    			    KeyTapGesture ktp = new KeyTapGesture(gesture);
    			    Vector tapPos = iBox.normalizePoint(ktp.position(),false).times(fingerScaleFact);
    				 for(int i=0;i<5;i++) {
    	    				if (keys[i].contains(tapPos.getX(), tapPos.getZ())){
    	    					if(prevNotes[i]==false) {
		    						pressed[i]=true;
		    					}
			    				notes[i] = true;
    	    				
    	    				}
    			 }
    		 }
    	 }
    	*/
    	
    	
    	
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
    			 for(int i=0;i<5;i++) {
    				if (keys[i].contains(fingerPos.getX(), fingerPos.getZ())&& finger.tipPosition().getY()!=fst &&
    						finger.tipPosition().getY()!=snd){
    					Vector fingerDir = finger.direction();
    					float fingerSpeed=finger.tipVelocity().getY();
    						
    					      float cosAngle = fingerDir.angleTo(norm);
    					      if (cosAngle< j*8*Math.PI/16 && fingerSpeed <-200*k){
    					    	  
    					    	  if(prevNotes[i]==false) {
    		    						pressed[i]=true;
    		    					}
    			    				notes[i] = true;
 
    					      }
    				}
    			 }
    				
    	
    		}
    	}
       
    	
    	System.arraycopy(notes, 0, prevNotes, 0, notes.length);
    	fingerList = controller.frame().fingers();
    }

	public boolean[] getPressed() {
		//System.out.println(Arrays.toString(pressed));
		return pressed;
	}
	
	public void draw(SpriteBatch batch) {
		
		batch.draw(piano, MusicalFingers.width/2f - piano.getWidth()/2f*sF,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f)),piano.getWidth()*sF,piano.getHeight()*(4f));

		
		batch.setColor(0f, 0f, 0f, 0.5f);
		for (int i = 0; i < 5; i++) {
			if (notes[i]) {
				batch.draw(rectangle, MusicalFingers.width/2f - piano.getWidth()/2f*sF + (i*32)*sF,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f)) + 2*4, 32*sF,67*4);
			}
		}
		
		
		batch.setColor(Color.BLACK);
		for(Finger finger: fingerList) {
			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false).times(fingerScaleFact);
			
			batch.draw(fingerPoint,-6f+MusicalFingers.width/2f - piano.getWidth()/2f*sF+160f*sF*fingerPos.getX(),MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f))+((1f-fingerPos.getZ())*67)*sF,12,12);
		}
		
		//TODO: Add the reference levels for fingers and the piano
		
	}

}