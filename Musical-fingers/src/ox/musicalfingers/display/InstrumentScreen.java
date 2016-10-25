package ox.musicalfingers.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ox.musicalfingers.instrument.DiscreteDisplay;
import ox.musicalfingers.instrument.DiscreteInput;
import ox.musicalfingers.instrument.DiscreteInputDisplay;
import ox.musicalfingers.instrument.DiscreteOutput;
import ox.musicalfingers.instrument.DrumOutput;
import ox.musicalfingers.instrument.GuitarOutput;
import ox.musicalfingers.instrument.sampler.SamplerOutput;
import ox.musicalfingers.instrument.FluteOutput;
import ox.musicalfingers.instrument.Piano.Piano;
import ox.musicalfingers.instrument.Random.FiveNotes;
import ox.musicalfingers.instrument.drum.Drum;
import ox.musicalfingers.instrument.guitar.Guitar;
import ox.musicalfingers.instrument.sampler.Sampler;
import ox.musicalfingers.instrument.flute.Flute;
import ox.musicalfingers.recording.Recorder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

public class InstrumentScreen implements Screen {

	// Controller for leap motion
	Controller controller;
	// Input processor
	DiscreteInput input;
	// Turns input 'notes' into sounds
	DiscreteOutput output;
	// Displays the instrument and fingers
	DiscreteDisplay display;
	// Input and display
	DiscreteInputDisplay inputDisplay;
	// Output for recorded notes
	DiscreteOutput recordedOutput;

	// Background texture
	Texture rectangle;

	// Recording & Playback stuff
	Recorder recorder = new Recorder();
	boolean recording = false;
	boolean playingBack = false;
	boolean repeatPlayback = true;
	Texture rec;
	Texture notRec;
	Texture play;
	Texture notPlay;
	
	// UI
	Stage stage;
	SelectBox instruments;
	SelectBox recordings;
	int currentInstrument = 0;
	int currentRecording = 0;
	int currentSong = 0;
	
	// Boolean to go back to meny
	boolean backToMenu =false;

	@Override
	public void init() {
		
		if(controller != null) {
			controller.delete();
		}

		controller = new Controller();
		
		currentInstrument = 99;

		// For background
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		//Ui elements
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		
		skin.getFont("default").setScale(1f);
		
		/*
		Table toolbar = new Table();
		toolbar.setPosition(0, MusicalFingers.height-100f);
		toolbar.setWidth(MusicalFingers.width);
		toolbar.setHeight(100f);
		//For background of table
		pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
		pixmap.fill();
		Texture tempTexture = new Texture(pixmap);
		TextureRegionDrawable t = new TextureRegionDrawable(new TextureRegion(tempTexture));
		toolbar.setBackground(t);		
		*/
		
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

		final TextButton record = new TextButton("record", skin, "small");
		record.setWidth(150f);
		record.setHeight(100f);
		record.setPosition(MusicalFingers.width/3f-200, MusicalFingers.height-105f);
		record.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!playingBack){
					if(recording) {
						record.setText("record");
					} else {
						recorder.startRecording(output);
						record.setText("stop");
						
						if(instruments.getSelectionIndex() == 0) {
							//Piano
							recordedOutput = new FiveNotes();
						} else if(instruments.getSelectionIndex() == 1) {
							//Guitar
							recordedOutput = new GuitarOutput();
						} else if(instruments.getSelectionIndex() == 2) {
							//Drum
							recordedOutput = new DrumOutput();
						} else if(instruments.getSelectionIndex() == 3) {
							// Sampler
							recordedOutput = new SamplerOutput();
						} else if(instruments.getSelectionIndex() == 4) {
							// flute
							recordedOutput = new FluteOutput();
						}
	
					}
					recording = !recording;
				}
			}
		}
		);
		
		final TextButton playButton = new TextButton("play", skin, "small");
		playButton.setWidth(150f);
		playButton.setHeight(100f);
		playButton.setPosition(MusicalFingers.width/3f+50f, MusicalFingers.height-105f);
		playButton.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!recording) {
					if(playingBack) {
						playButton.setText("play");
					} else {
						recorder.startPlaying();
						playButton.setText("stop");
					}
					playingBack = !playingBack;
				}
			}
		}
		);
		
		String[] instrumentNames = {"     piano", "     guitar", "      drum", "     sampler", "     flute"};
		
		instruments = new SelectBox(instrumentNames, skin);
		instruments.setPosition(MusicalFingers.width-325f, MusicalFingers.height-100f-5f);
		instruments.setWidth(320f);
		instruments.setHeight(100f);	
		
		String[] recordingNames = {"     track  1"	, "     track  2" , "     track  3" , "     track  4" , "     track  5"	, "     track  6" , "     track  7" , "     track  8" , "     track  9"}	;					
		
		recordings = new SelectBox (recordingNames, skin);
		recordings.setPosition(MusicalFingers.width-555f-50f , MusicalFingers.height-105f);
		recordings.setWidth(250f);
		recordings.setHeight(100f);
		
		stage.addActor(instruments);
		stage.addActor(recordings);
		stage.addActor(playButton);
		stage.addActor(record);
		stage.addActor(back);
		
		//Ui images
		notRec = MusicalFingers.manager.get("assets/notRecording.png");
		rec = MusicalFingers.manager.get("assets/recording.png");
		notPlay = MusicalFingers.manager.get("assets/notPlaying.png");
		play = MusicalFingers.manager.get("assets/playing.png");
		
		backToMenu = false;
		
		recording = false;
		playingBack = false;
	
	}

	@Override
	public void update() {
		
		//Change instruments?
		if(currentInstrument != instruments.getSelectionIndex()) {
			
			if(inputDisplay!=null) {
				controller.removeListener((Listener) inputDisplay);
			}
			
			if(instruments.getSelectionIndex() == 0) {
				//Piano
				output = new FiveNotes();
				inputDisplay = new Piano();
			} else if(instruments.getSelectionIndex() == 1) {
				//Guitar
				output = new GuitarOutput();
				inputDisplay = new Guitar();
			} else if(instruments.getSelectionIndex() == 2) {
				//Drum
				output = new DrumOutput();
				inputDisplay = new Drum();
			} else if(instruments.getSelectionIndex() == 3) {
				//Sampler
				output = new SamplerOutput();
				inputDisplay = new Sampler();
			} else if(instruments.getSelectionIndex() == 4) {
				//Flute
				output = new FluteOutput();
				inputDisplay = new Flute();
			}
			
			controller.addListener((Listener) inputDisplay);
			currentInstrument = instruments.getSelectionIndex();
		}
		
		//Change song?
		if(currentSong != recordings.getSelectionIndex()) {
			//Song changed
			recorder.changeToSong(recordings.getSelectionIndex());
			
			if(recording) {
				recorder.startRecording(output);
			}
			if(playingBack) {
				recorder.startPlaying();
			}
			
			currentSong = recordings.getSelectionIndex();
			
			recordedOutput = recorder.getInstrumentForPlayback();
		}

		
		//Instruments 
		output.playNotes(inputDisplay.getNotes());
		
		//display.getNotes(input.getNotes());
		//display.getFingers(controller.frame().fingers());
		//display.getInteractionBox(controller.frame().interactionBox());}

		//Recording and playback
		
		if (recording) {
			recorder.recordNotes(inputDisplay.getNotes());
		}

		if (playingBack) {
			if(!recorder.endOfRecording()) {
				recordedOutput.playNotes(recorder.playNotes());
			} else {
				if(repeatPlayback) {
					recorder.startPlaying();
				}
			}
		}
		
		//Update ui
		stage.act();
		
	}

	@Override
	public void draw(SpriteBatch batch) {

		// batch.setColor(147,210,255,1);
		// batch.draw(rectangle,0,0,MusicalFingers.width,MusicalFingers.height);

		//Draw instrument
		inputDisplay.draw(batch);
		
		//Draw rectangle under toolbar
		batch.setColor(200f/255f,200f/255f,200f/255f,1);
		batch.draw(rectangle,0,MusicalFingers.height-110f,MusicalFingers.width,110f);
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,0,MusicalFingers.height-115f,MusicalFingers.width,5f);
		
		batch.setColor(Color.WHITE);
		//Draw some ui images
		if(recording) {
			batch.draw(rec,MusicalFingers.width/3f-250f,MusicalFingers.height-75,40f,40f);
		} else {
			batch.draw(notRec,MusicalFingers.width/3f-250f,MusicalFingers.height-75,40f,40f);
		}
		if(playingBack) {
			batch.draw(play,MusicalFingers.width/3f,MusicalFingers.height-88f,42f,66f);
		} else {
			batch.draw(notPlay,MusicalFingers.width/3f,MusicalFingers.height-88f,42f,66f);
		}
		
		//Draw ui
		batch.end();
		stage.draw();
		batch.begin();

	}

	@Override
	public int changeStateTo() {
		if(backToMenu) { 
			return MusicalFingers.STATE_MENU;
		} else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}

}