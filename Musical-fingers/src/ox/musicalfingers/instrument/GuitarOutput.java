package ox.musicalfingers.instrument;

import ox.musicalfingers.display.MusicalFingers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import ox.musicalfingers.instrument.DiscreteOutput;

public class GuitarOutput implements DiscreteOutput {
	
	Sound[] sounds = new Sound[12];
	boolean[] prev = new boolean[12];
	boolean[] stopInABit = new boolean[12];
	int[] stopTime = new int[12];
	int stoppingTime = 50;
	
	public GuitarOutput() {
		
	   	for(int i = 1; i < 13; i++) {
	   		sounds[i-1] = MusicalFingers.manager.get("assets/guitar"+ i +".wav");
	   	}
		}
	
	@Override
	public void playNotes(boolean[] notes) {
		for(int i=0; i<12;i++) {
			
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
	
	
	
	

}


