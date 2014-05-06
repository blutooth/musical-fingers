package ox.musicalfingers.recording;

import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Recorder {
	
	private int time = 0;
	private Deque<Notes>[] recordedNotes = (LinkedList<Notes>[]) new LinkedList<?>[9];
	private Queue<Notes> copyOfNotes;
	private Notes currentNotes;
	
	private int song = 0;
	
	public Recorder() {
		for(int i=0;i<recordedNotes.length;i++) {
			recordedNotes[i] = new LinkedList<Notes>();
		}
	}

	public void startRecording() {
		time = 0;
		recordedNotes[song].clear();
	}
	
	public void recordNotes(boolean[] notes) {
		
		if(recordedNotes[song].isEmpty()) { 
			recordedNotes[song].add(new Notes(time, notes.clone()));
		}

		if(!(recordedNotes[song].peekLast().getNotes().equals(notes))) {
			recordedNotes[song].add(new Notes(time, notes.clone()));
		}
		
		time++;
		
	}
	
	public void changeToSong(int s) { 
		song = s;
	}
	
	public void startPlaying() {
		time = 0;
		copyOfNotes = new LinkedList<Notes>(recordedNotes[song]);
		currentNotes = copyOfNotes.poll();
	}
	
	public boolean[] playNotes() {
		
		if(copyOfNotes.peek() != null && copyOfNotes.peek().getTime() <= time) {
			currentNotes = copyOfNotes.poll();
		}
		time++;
		
		return currentNotes.getNotes();
	}
	
	public boolean endOfRecording() {
		return copyOfNotes.isEmpty();
	}
}
