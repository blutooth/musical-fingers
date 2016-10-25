package ox.musicalfingers.display;

import com.badlogic.gdx.Gdx;
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

public class MenuScreen implements Screen {
	
	private Stage stage;
	private BitmapFont font;
	private boolean goToInstrumentScreen = false;
	private boolean goToGameScreen = false;

	@Override
	public void init() {
		
		//Stage for adding buttons to
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Font
		font = MusicalFingers.manager.get("assets/font/pixel.fnt");
		
		/*
		//Skin
		Skin skin = new Skin();
		//Set default font
		skin.add("default", font);
		
		//TextButton style
		TextButtonStyle buttonStyle = new TextButtonStyle();
		
		skin.add("up", MusicalFingers.manager.get("assets/ui/buttonUp.png"));
		skin.add("down", MusicalFingers.manager.get("assets/ui/buttonDown.png"));
		skin.add("over", MusicalFingers.manager.get("assets/ui/buttonOver.png"));
		buttonStyle.font = skin.getFont("default");
		buttonStyle.up = skin.newDrawable("up");
		buttonStyle.down = skin.newDrawable("down");
		buttonStyle.over = skin.newDrawable("over");
		buttonStyle.pressedOffsetY = 0f;
		buttonStyle.unpressedOffsetY=20f;
		buttonStyle.font.setScale(2);
		skin.add("default", buttonStyle);
		
		*/
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		
		skin.getFont("default").setScale(2f);
		
		//Label for the title
		Label title = new Label("MUSICAL FINGERS", skin);
		title.setFontScale(3.5f);
		title.setX(0);
		title.setWidth(MusicalFingers.width);
		title.setY(MusicalFingers.height*3f/4f);
		title.setAlignment(Align.center);
		
		stage.addActor(title);
		
		//Button to go to instrument screen
		TextButton button = new TextButton("compose", skin);
		button.setWidth(400f);
		button.setHeight(200f);
		button.setPosition(MusicalFingers.width/2f - 200f, MusicalFingers.height/3f-200f);
		button.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToInstrumentScreen = true;
			}
		}
		);
		
		TextButton button2 = new TextButton("play game", skin);
		button2.setWidth(400f);
		button2.setHeight(200f);
		button2.setPosition(MusicalFingers.width/2f - 200f, MusicalFingers.height/2f - 100f);
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
