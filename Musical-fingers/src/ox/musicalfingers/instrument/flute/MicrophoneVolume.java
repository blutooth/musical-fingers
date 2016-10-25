package ox.musicalfingers.instrument.flute;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.AudioRecorder;

public class MicrophoneVolume {
	private AudioRecorder mic;
	private short[] buffer;
	private int bufferLen = 100;
	private float bufferLenRec = 1.0f / bufferLen;
	private static volatile MicrophoneVolume thisMic = new MicrophoneVolume();
	
	private MicrophoneVolume() {
		mic = Gdx.audio.newAudioRecorder(44100, true);
		buffer = new short[bufferLen];
	}
	public float getMicVol() {
		mic.read(buffer, 0, bufferLen);
		float mean = 0;
		for (int i = 0; i < bufferLen; i++) {
			mean += Math.abs(buffer[i]);
		}
		return Math.min(mean * bufferLenRec * (1f / 3277f), 1f);
	}
	public static MicrophoneVolume getMic() {
		return thisMic;
	}
	
	
}
