	package ox.musicalfingers.leap;

	import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ox.musicalfingers.display.InstrumentScreen;
import ox.musicalfingers.instrument.DiscreteInput;
import ox.musicalfingers.instrument.DiscreteDisplay;



import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


	/** Has a list of gestures (only finger taps) and a list of fingers.
	 * The gestures and fingers are public so that they can be accessed by the main program
	 * But we may want to create accessors for this instead.  */
	public class GuitarListener extends Listener implements DiscreteInput {
		
		private DiscreteDisplay guitar;
		
		private Frame lastFrame = new Frame();
		
		 public GuitarListener(DiscreteDisplay guitar) {
			this.guitar = guitar;
		}
		 
		boolean[] notes = new boolean[12];
	    
	    public void onInit (Controller controller){
	        System.out.println("Initialized GuitarListener");
	    }
	    
	    public void onFrame(Controller controller) {
	    	processData(controller.frame());
	    	
	    }
	    
	    public boolean[] getNotes() {
	    	return notes;
	    }
	    
	    private void processData(Frame frame) {
			InteractionBox iBox = frame.interactionBox();
			notes = new boolean[12];
			for(Pointable pointable : frame.pointables()) {
				float z = iBox.normalizePoint(pointable.tipPosition()).getZ();
				float y = iBox.normalizePoint(pointable.tipPosition()).getY();
				Pointable last = lastFrame.pointable(pointable.id());
				if(last.isValid() && y < 5) {
					float lastZ = iBox.normalizePoint(pointable.tipPosition()).getZ();
					int string = processPointable(z, lastZ);
					if(string >= 0) {
						notes[string] = true;
					}
				}
			}
			lastFrame = frame;
		}

	    private int processPointable(float z, float lastZ) {
			return guitar.FindKey(z, lastZ);
		}
	}