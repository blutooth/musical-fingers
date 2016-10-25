package ox.musicalfingers.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteInputDisplay;
import ox.musicalfingers.instrument.KeyPos;


public class GameOver implements Screen {
	
	
	boolean won;
	String theTitle;
	private Stage stage;
	private BitmapFont font;
	private boolean goToInstrumentScreen = false;
	private boolean goToGameScreen = false;
	Texture background;
	
	public GameOver(String args) {
		if (args =="win") {won=true;} else {won=false;}
	}
	


	@Override
	public void init() {
		
		//Stage for adding buttons to
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		
		//background
		if (!won) {
			background= MusicalFingers.manager.get("assets/game/butterfingers.png" );
		}
		else {
			background=MusicalFingers.manager.get("assets/game/fingermaster.png" );
		}
		
		//Font
		font = MusicalFingers.manager.get("assets/font/pixel.fnt");
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		
		skin.getFont("default").setScale(2f);
		
		//Label for the title
		if (won) {
			theTitle="Finger Master";
		} else {
			theTitle="Butter Fingers";
		}
		Label title = new Label(theTitle, skin);
		title.setFontScale(3.5f);
		title.setX(0);
		title.setWidth(MusicalFingers.width);
		title.setY(MusicalFingers.height*1f/2f+100);
		title.setAlignment(Align.center);
		
		Label score = new Label("score_ " +GameScreen.score, skin);
		score.setFontScale(2.5f);
		score.setX(0);
		//score.setColor(0.12f,0.97f,0.54f,0f);
		score.setWidth(MusicalFingers.width);
		score.setY(MusicalFingers.height*1f/2f);
		score.setAlignment(Align.center);
		
		stage.addActor(score);
		stage.addActor(title);
		
		//Button to go to instrument screen
		TextButton button = new TextButton("compose", skin);
		button.setWidth(400f);
		button.setHeight(200f);
		button.setPosition(MusicalFingers.width/2f - 428f, MusicalFingers.height/2f - 200f);
		button.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToInstrumentScreen = true;
			}
		}
		);
		
		TextButton button2 = new TextButton("play again", skin);
		button2.setWidth(400f);
		button2.setHeight(200f);
		button2.setPosition(MusicalFingers.width/2f+30 , MusicalFingers.height/2f - 200f);
		button2.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToGameScreen = true;
			}
		}
		);
		
		stage.addActor(button);
		stage.addActor(button2);
		
		goToInstrumentScreen = false;
		goToGameScreen = false;

	}

	@Override
	public void update() {
		stage.act();

	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		batch.draw(background,0,0,MusicalFingers.width,MusicalFingers.height);
		batch.end();
		stage.draw();
		batch.begin();

		//font.setScale(5);
		//font.drawMultiLine(batch,"MUSICAL\nFINGERS",250,MusicalFingers.height-50);
		//font.setScale(1);
		
	}

	@Override
	public int changeStateTo() {
		if(goToInstrumentScreen) {
			return MusicalFingers.STATE_INSTRUMENT;
		} else if (goToGameScreen) {
			return MusicalFingers.STATE_GAME;
		} else return -1;
	}

	@Override
	public void dispose() {
		stage.dispose();

	}

}