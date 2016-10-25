package ox.musicalfingers.instrument;

import ox.musicalfingers.display.MusicalFingers;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;

public class FluteOutput implements DiscreteOutput {
	private final static int soundCount = 14;
	Sound[] sounds = new Sound[soundCount];
	
	public FluteOutput() {
	   	for(int i = 1; i <= soundCount; i++) {
	   		sounds[i-1] = MusicalFingers.manager.get("assets/flute/flute"+ i +".mp3");
	   	}
	}

	@Override
	public void playNotes(boolean[] notes) {
		boolean stop = false;
		//for(int i = 0; i < soundCount; i++) {
		for(int i = 0; i < soundCount; i++) {
			if(notes[i]) {
				stop = true;
			}
		}
		if (stop) {
			// for (int i = 0; i < soundCount; i++) {
			for(int i = 0; i < soundCount; i++) {
				sounds[i].stop();
			}
		}
		// for(int i = 0; i < soundCount; i++) {
		for(int i = 0; i < soundCount; i++) {
			if(notes[i]) {
				sounds[i].play();
			}
		}
		
	}
}
