package ox.musicalfingers.leap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ox.musicalfingers.display.InstrumentScreen;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Pointable;

public class LeapMotion {
	/*
    private InstrumentScreen screen;
	private PianoListener pianoListener = new PianoListener(this);
	private Controller controller = new Controller();
	Set<Gesture> currentGestures = new HashSet<Gesture>();
	
	public LeapMotion(InstrumentScreen screen) {
		controller.addListener(pianoListener);
		this.screen = screen;
	}
	
	public void processData(Frame frame) {
		GestureList newGestures = frame.gestures();
		boolean[] taps = new boolean[6];
		Set<Gesture> update = new HashSet<Gesture>();
		for(Iterator<Gesture> gestureIter = newGestures.iterator(); gestureIter.hasNext();) {
			Gesture gesture = gestureIter.next();
			if(!currentGestures.contains(gesture)) {
				int key = processGesture(gesture);
				if(key >= 0) {
					taps[key] = true;
				}
			}
			update.add(gesture);
		}
		currentGestures = update;
		screen.setTaps(taps);
		screen.update();
	}

	private int processGesture(Gesture gesture) {
		KeyTapGesture tap = new KeyTapGesture(gesture);
		float x = tap.position().getX();
		float y = tap.position().getY();
		double angles = Math.toDegrees(Math.atan2(x,y));
		int area = (int) Math.round(angles);
		if(-90 <= area && area < -60) {
			return 0;
		} else if (-60 <= area && area < -30) {
			return 1;
		} else if (-30 <= area && area < 0) {
			return 2;
		} else if (0 <= area && area < 30) {
			return 3;
		} else if (30 <= area && area < 60) {
			return 4;
		} else if (60 <= area && area < 90) {
			return 5;
		} else {
			return -1;
		}
	}
	*/
}
