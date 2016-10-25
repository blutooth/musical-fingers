package ox.musicalfingers.instrument;

import ox.musicalfingers.display.MusicalFingers;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;

public class DrumOutput implements DiscreteOutput {
	 Sound[] sounds = new Sound[5];
		
	public DrumOutput() {
	   	for(int i = 1; i < 6; i++) {
	   		sounds[i-1] = MusicalFingers.manager.get("assets/drum"+ i +".wav");
	   	}
	}

	@Override
	public void playNotes(boolean[] notes) {
		boolean stop = false;
		for(int i = 0; i < 5; i++) {
			if(notes[i]) {
				stop = true;
			}
		}
		if (stop) {
			for (int i = 0; i < 5; i++) {
				sounds[i].stop();
			}
		}
		for(int i = 0; i < 5; i++) {
			if(notes[i]) {
				sounds[i].play();
			}
		}
		
	}
	
}
