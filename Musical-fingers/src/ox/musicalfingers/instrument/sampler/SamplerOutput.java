package ox.musicalfingers.instrument.sampler;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteOutput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;

public class SamplerOutput implements DiscreteOutput {

	Sound[] sounds = new Sound[12];
	boolean[] prev = new boolean[12];
	boolean[] stopInABit = new boolean[12];
	int[] stopTime = new int[12];
	int stoppingTime = 50;
	
	public SamplerOutput() {
		for (int i=0; i<12; i++) {
			if (i%4==0) sounds[i] = MusicalFingers.manager.get("assets/sampler/Kick"+(i%3+1)+".wav");
			if (i%4==1) sounds[i] = MusicalFingers.manager.get("assets/sampler/Snare"+(i%3+1)+".wav");
			if (i%4==2) sounds[i] = MusicalFingers.manager.get("assets/sampler/Clap"+(i%3+1)+".wav");
			if (i%4==3) sounds[i] = MusicalFingers.manager.get("assets/sampler/Hat"+(i%3+1)+".wav");
		}
	}

	 
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
				sounds[i].play();
				stopInABit[i] = false;
			}
			if(prev[i] && !notes[i]) {
				stopInABit[i]=true;
				stopTime[i]=0;
			}
		}
		System.arraycopy(notes,0,prev,0,notes.length);
	}
}
