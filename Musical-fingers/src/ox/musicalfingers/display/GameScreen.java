package ox.musicalfingers.display;

import java.util.ArrayList;
import java.util.List;

import ox.musicalfingers.game.Explosion;
import ox.musicalfingers.game.GameNote;
import ox.musicalfingers.game.GamePianoInput;
import ox.musicalfingers.instrument.Random.FiveNotes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.leapmotion.leap.Controller;



public class GameScreen implements Screen{
	  private SpriteBatch batch;
	  private BitmapFont font;

	  public class Note {
  		public int note;
  		public int time;
  	
  		 public Note(int a, int b) {
  		       note = a;
  		       time = b;
  		 }
	 }
	
	//Game Variables  
	Note[] song;
	float t;
	private int time;  //init 0
	//private int i;     //init 0
	private int currentNote;
	public static int score =0;  //init 0
	boolean Q;
	boolean W;
	boolean E;
	boolean R;
	boolean T;
	Texture piano;
	
	GamePianoInput input;
	
	List<GameNote> gameNotes = new ArrayList<GameNote>();
	
	//Controller for the Leap Motion
	Controller controller;
	
	// Background texture
	Texture rectangle;
	
	// Boolean to go to Winning SCreen after Game
	boolean winScreen= false;
	
	// Boolean to go to Losing SCreen after Game
	boolean loseScreen= false;
	
	// Boolean to go back to menu
	boolean backToMenu =false;
	
	// Boolean to replay song from begining
	boolean toReplay =false;
	
	//Boolean to pause game
	boolean pause= false;
	
	//TextButton scorer;
	TextButton pauseButtonCopy;
	
	// UI
	Stage stager;
	// Recording & Playback stuff
	Texture timer;
	
	// UI
	SelectBox playList;
	int currentSong = 0;

	//Array of possible songs
	Note[] []  songs;
		
	//Notespeed 
	float noteSpeed = 8f;
	
    //Vars for playing back music
	FiveNotes musicPlayer;
	 
	//cheerMessage
	private String cheerMessage;
	
	//anything to cheer?
	private int cheerTime = 0;
	
	//Explosion graphical effects
	List<Explosion> explosions = new ArrayList<Explosion>();
	
	//Buffer going to the next screen
	int gonnaChange = 0;
	
	//Between being ready and starting
	int pauseBuffer =0;


	@Override
	public void init() {
		
		if(controller!=null) {
			controller.delete();
		}
		
		controller = new Controller();
		
		// init variables
		time = -80;
		currentNote = 0;
		score = 0;
		winScreen = false;
		loseScreen = false;
		gameNotes.clear();
		cheerMessage = "";
		cheerTime=0;
		explosions.clear();
		gonnaChange=0;
		pauseBuffer=0;
		  
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(2,30),new Note(2,60),new Note(2,90),new Note(0,120),new Note(1,150),new Note(1,180),new Note(0,210),new 
        		Note(4,270),new Note(4,300),new Note(3,330),new Note(3,360),new Note(2,390),new Note(0,480),new Note(2,510),new Note(2,540),new Note(2,570),new Note(0,600),new Note(1,630),new Note(1,660),new Note(0,690),new 
        		Note(4,750),new Note(4,780),new Note(3,810),new Note(3,840),new Note(2,870),new Note(0,900), new Note(0,915)};
         Note [] song1 = new Note[] {
        		new Note(2,30),new Note(1,60),new Note(0,90),new Note(1,120),
        		new Note(2,150),new Note(2,180),new Note(2,210),
        		new Note(1,240),new Note(1,270),new Note(1,300),
        		new Note(2,330),new Note(4,360),new Note(4,390),
        		new Note(2,420),new Note(1,450),new Note(0,480),new Note(1,510),
        		new Note(2,540),new Note(2,570),new Note(2,600),
                new Note(1,630),new Note(1,670),new Note(2,700), new Note(1,730), 
                new Note(0,760) ,new Note(0,900)
         };
		songs = new Note [] [] {song,song1,song,song,song,song,song,song,song};
		musicPlayer = new FiveNotes();
		
        piano = MusicalFingers.manager.get("assets/game/game_piano.png");
        		
		// For background
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);

        

		
		
		//Ui elements
		stager = new Stage();
		Gdx.input.setInputProcessor(stager);
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		skin.getFont("default").setScale(1f);	
		

		
        TextButton back = new TextButton("back", skin, "small");
		back.setWidth(100f);
		back.setHeight(100f);
		back.setPosition(5, MusicalFingers.height-105f);
		back.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMenu=true;
			}
		}
		);
		
		TextButton replayer = new TextButton("replay", skin, "small");
		replayer.setWidth(150f);
		replayer.setHeight(100f);
		replayer.setPosition(MusicalFingers.width/2f+50f, MusicalFingers.height-105f);
		replayer.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toReplay=true;
			}
		}
		);
		
		final TextButton pauseButton = new TextButton("play", skin, "small");
		pauseButton.setWidth(150f);
		pauseButton.setHeight(100f);
		pauseButton.setPosition(MusicalFingers.width/2f-50f-150f, MusicalFingers.height-105f);
		pauseButton.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!pause) {
					pauseButton.setText("play");
					
				} else {
					// method recorder.startPlaying();
					pauseButton.setText("pause");
				}
				pause = !pause;
			}
		}
		);
		pauseButtonCopy=pauseButton;
		
	
		String[] songNames = {"     old mcdonald"	, "     marys lamb" }	;					
		
		playList = new SelectBox (songNames, skin);
		playList.setPosition(MusicalFingers.width-405f , MusicalFingers.height-105f);
		playList.setWidth(400f);
		playList.setHeight(100f);
		

		stager.addActor(playList);
		stager.addActor(pauseButton);
		stager.addActor(replayer);
		stager.addActor(back);
		
		backToMenu = false;
		pause = true;
        
        input = new GamePianoInput();
        
        controller.addListener(input);
        
        font = MusicalFingers.manager.get("assets/font/pixel.fnt");
		
	}
	


	@Override
	public void update() {
		
		//replay song?
		if (toReplay) {
			 time = -80;
			 currentNote = 0;
			 score = 0; 
			 toReplay=false;
			 gameNotes.clear();
			 explosions.clear();
			 pause = true;
			 cheerMessage = "";
			 cheerTime=0;
			 pauseButtonCopy.setText("play");
			 gonnaChange=0;
			 pauseBuffer=0;
		}
		
		//Change song?
		if(currentSong != playList.getSelectionIndex()) { 
					time=-80;
					currentNote=0;
					score=0;
					currentSong=playList.getSelectionIndex();
					song= songs[currentSong];
					pause = true;
					
		}
			
		if (!pause) {
			//score++;
			time++;
			
			if(gameNotes.isEmpty() && currentNote>=song.length) {
				//End of song
				
				if(score >= 0) {
					winScreen = true;
				} else {
					loseScreen = true;
				}
				
				gonnaChange++;
				
				for(int i=0;i<explosions.size();i++){
					explosions.get(i).update();
				}
				
			} else if(!gameNotes.isEmpty() || currentNote<song.length){
				//Not end of song	
				
				//Add the note at the correct time
				if(currentNote<song.length && (song[currentNote]).time<(time)) {
					spawn(song[currentNote].note); currentNote++;
				}
				
				//Play the piano notes
				musicPlayer.playNotes(input.getPressed());
						
				//Loop through the keys checking if they are pressed and dealing with the consequences
				for(int i=0;i<5;i++) {
					
					if(input.getPressed()[i]){
						boolean wasNote =false;
						for(int j=0;j<gameNotes.size();j++) {
							GameNote note = gameNotes.get(j);
							
							if( input.getBounds(i).contains(note.bounds())) {
								//Note is inside the bounds
								reward(note.getX(), note.getY()-40,note.note,1f);
								input.getPressed()[i]=false;
								gameNotes.remove(j);
								j--;
								wasNote = true;
								break;
							} else if (input.getBounds(i).overlaps(note.bounds())) {
								//Note on edge of key
								reward(note.getX(), note.getY()-40,note.note,0.5f);
								input.getPressed()[i]=false;
								gameNotes.remove(j);
								j--;
								wasNote = true;
								break;
							} 
							
						}
						
						input.getPressed()[i]=false;
						
						//Pressed the key unnecessarily 
						if(!wasNote) {
							punish((int)(input.getBounds(i).x-55+input.getBounds(i).width/2f),(int)(input.getBounds(i).y-55f),5,0.5f);
						}
					}

				}

				//Update the notes on the screen
				for(int i=0;i<gameNotes.size();i++) {
					GameNote note = gameNotes.get(i);
					
					note.pos += noteSpeed;
					if(note.pos>=MusicalFingers.height-110f&&time>=160) {
						//Missed note
						punish(note.getX(), (int) (note.getY()-note.bounds().height-40),note.note,1f);
						gameNotes.remove(i);
						i--;
					}
				}
				
				//Update explosions
				for(int i=0;i<explosions.size();i++){
					Explosion ex = explosions.get(i);
					ex.update();
					if(ex.dead()) {
						explosions.remove(i);
						i--;
					}
					
				}
				
			}
			if(!input.isReady()) {
				pause = true;
				pauseButtonCopy.setText("play");
				pauseBuffer=0;
			}

		} else {
			for(int i=0;i<5;i++) {
				input.getPressed()[i]=false;
			}
			if(input.isReady()) {
				pauseBuffer++;
				if(pauseBuffer>179) {
					pause = false;
					pauseButtonCopy.setText("pause");
					pauseBuffer=0;
				}
			} else {
				pauseBuffer=0;
			}
		}
		stager.act();
		
	}
	
	public void reward(int x, int y, int note, float multiplier) {
		cheerMessage = "WOOH!";
		cheerTime=0;
		
		//Graphical affects will go here
		Explosion ex = new Explosion(x, y, note);
		explosions.add(ex);
		
		score +=1000*multiplier;
	}
	
	public void punish(int x, int y, int note, float multiplier) {
		cheerMessage = "BOO !";
		cheerTime=0;
		
		//Graphical affects will go here
		if(note!=5) {
			Explosion ex = new Explosion(x, y, note);
			explosions.add(ex);
		}
		
		score -= 1000*multiplier;
	}
	
	public void spawn(int x) {
		gameNotes.add(new GameNote(x,-110f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
	    //batch.draw(piano,80,400,1120,240);
	    input.draw(batch);
	    for(GameNote note:gameNotes) {
			note.draw(batch);
		}
	    
	    for(Explosion ex: explosions) {
	    	ex.draw(batch);
	    }
	    
	    
	    
	    //Draw cheering here
	    if(cheerTime < 60) {
		    if (cheerMessage =="WOOH!") {
		    	//batch.setColor(Color.WHITE);
		    	//batch.draw(rectangle,MusicalFingers.width/2-200,50,400, 100);
		    	font.setColor(1, 1,1,1);
		    } else {
		    	//batch.setColor(Color.RED);
		    	//batch.draw(rectangle,MusicalFingers.width/2-200,50,400,100);
		    	font.setColor(1,0,0,1);
		    }
		    cheerTime++;
		    font.setScale(2.5f);
			font.draw(batch, cheerMessage, MusicalFingers.width/2-80,150);
	    }
	    
		
		
	    //Draw score here
	    font.setColor(1,1,1,1);
		//font.setScale(1,-1);
	    font.setScale(2f);
		font.draw(batch, "SCORE: "+score, 10,font.getCapHeight()-font.getDescent());
		
		//Draw pause screen here
		if(pause) {
			if(pauseBuffer==0) {
				batch.setColor(0f,0f,0f,0.8f);
				batch.draw(rectangle,0,0,MusicalFingers.width,MusicalFingers.height);
				font.setScale(4f);
				font.draw(batch, "PAUSED",MusicalFingers.width/2f-260f,MusicalFingers.height/2f+100f);
				font.setScale(2f);
				font.drawMultiLine(batch, "         move fingers over\nleap motion to continue",MusicalFingers.width/2f-370f,MusicalFingers.height/2f-50f);
			} else {
				batch.setColor(0f,0f,0f,0.8f-Math.min(pauseBuffer/60f,0.8f));
				batch.draw(rectangle,0,0,MusicalFingers.width,MusicalFingers.height);
				batch.setColor(0f,0f,0f,1f);
				batch.draw(rectangle,0,MusicalFingers.height/2f-50f,MusicalFingers.width,100f);
				font.setScale(4f);
				font.draw(batch, ""+(3-pauseBuffer/60),MusicalFingers.width/2f-20f,MusicalFingers.height/2f+70f);
			}
		}
		
		
	    
		//Draw rectangle under toolbar
		batch.setColor(200f/255f,200f/255f,200f/255f,1);
		batch.draw(rectangle,0,MusicalFingers.height-110f,MusicalFingers.width,110f);
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,0,MusicalFingers.height-115f,MusicalFingers.width,5f);
		
		batch.end();
		stager.draw();
		batch.begin();


	  	
		
	}
	
	


	@Override
	public int changeStateTo() {
		if(backToMenu) { 
			//Stop playing the current sound
			musicPlayer.stopPlaying();
			return MusicalFingers.STATE_MENU;
		} else if (winScreen && gonnaChange>40){
			//Stop playing the current sound
			musicPlayer.stopPlaying();
			return MusicalFingers.STATE_WIN;
		} else if (loseScreen && gonnaChange>40){
			//Stop playing the current sound
			musicPlayer.stopPlaying();
			return MusicalFingers.STATE_LOSE;
		} else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		 batch.dispose();
	        font.dispose();
	        stager.dispose();
	}

}
