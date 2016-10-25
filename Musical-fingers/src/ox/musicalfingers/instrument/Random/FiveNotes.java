package ox.musicalfingers.instrument.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteOutput;

public class FiveNotes implements DiscreteOutput {
	
	Sound[] sounds = new Sound[5];
	boolean[] prev = new boolean[5];
	boolean[] stopInABit = new boolean[5];
	int[] stopTime = new int[5];
	int stoppingTime = 50;
	
	public FiveNotes() {
   	
    	sounds[0] = MusicalFingers.manager.get("assets/sound1.mp3");
    	sounds[1] = MusicalFingers.manager.get("assets/sound2.mp3");
    	sounds[2] = MusicalFingers.manager.get("assets/sound3.mp3");
    	sounds[3] = MusicalFingers.manager.get("assets/sound4.mp3");
    	sounds[4] = MusicalFingers.manager.get("assets/sound5.mp3");
		
	}

	@Override
	public void playNotes(boolean[] notes) {
		for(int i=0; i<5;i++) {
			
			if(stopInABit[i]) {
				stopTime[i]++;
				if(stopTime[i]>stoppingTime) {
					sounds[i].stop();
					stopInABit[i]=false;
				}
			}
			
			if(!prev[i] && notes[i]) {
				if(stopInABit[i] = true) {
					sounds[i].stop();
				}
				sounds[i].play();
				stopInABit[i] = false;
			}
			if(prev[i] && !notes[i]) {
				stopInABit[i]=true;
				stopTime[i]=0;
			}
		}
		System.arraycopy(notes,0,prev,0,notes.length);
		prev = notes.clone();
	}
	
	
	
	
	public void playNotes1(boolean[] notes) {
		for(int i = 0; i < 5; i++) {
			if(notes[i]) {
				sounds[i].stop();
				sounds[i].play();
			}
		}
		
	}
	
	public void stopPlaying() {
		for(int i = 0; i < 5; i++) {
			sounds[i].stop();
		}
	}


}
