package ox.musicalfingers.recording;

class Notes {
	
	private int time = 0;
	private boolean[] notes;
	
	public Notes(int t, boolean[] n) {
		time = t;
		notes = n;
	}
	
	public int getTime() { return time; }
	
	public boolean[] getNotes() { return notes; }
}
