package ox.musicalfingers.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.openal.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class LoadingScreen implements Screen {
	
	private AssetManager manager;
	
	private float progress = 0f;
	private Texture rectangle;
	private boolean change = false;
	private BitmapFont font;
	
	public LoadingScreen(AssetManager manager) {
		this.manager = manager;
		font = new BitmapFont(Gdx.files.internal("assets/font/pixel.fnt"), Gdx.files.internal("assets/font/pixel.png"), false);
	}

	@Override
	public void init() {
		//Load rectangle for drawing the progress bar
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		pixmap.dispose();
		
		//Load all assets here
		
		//Instrument Screen stuff
		manager.load("assets/1note.png", Texture.class);
		manager.load("assets/2note.png", Texture.class);
		manager.load("assets/3note.png", Texture.class);
		manager.load("assets/clef.png", Texture.class);
		manager.load("assets/5key_piano.png", Texture.class);
		manager.load("assets/finger.png", Texture.class);
		manager.load("assets/notRecording.png", Texture.class);
		manager.load("assets/recording.png", Texture.class);
		manager.load("assets/notPlaying.png", Texture.class);
		manager.load("assets/playing.png", Texture.class);
		manager.load("assets/bongo.png", Texture.class);

		
		//Font
		manager.load("assets/font/pixel.fnt", BitmapFont.class);
		manager.load("assets/font/pixel.png", Texture.class);
		
		//Skin stuff
		//manager.load("assets/ui/buttonUp.png", Texture.class);
		//manager.load("assets/ui/buttonDown.png", Texture.class);
		//manager.load("assets/ui/buttonOver.png", Texture.class);
		manager.load("assets/ui/pixelSkin.json",Skin.class,new SkinLoader.SkinParameter("assets/ui/pixelSkin.pack"));
		
		//Piano sounds
		manager.load("assets/sound1.mp3", Sound.class);
		manager.load("assets/sound2.mp3", Sound.class);
		manager.load("assets/sound3.mp3", Sound.class);
		manager.load("assets/sound4.mp3", Sound.class);
		manager.load("assets/sound5.mp3", Sound.class);
		
		//Guitar
		for(int i = 1; i < 13; i++) {
	   		manager.load("assets/guitar"+ i +".wav", Sound.class);
	   	}
		manager.load("assets/guitar_main.png", Texture.class);

		for(int i = 1; i < 6; i++) {
	   		manager.load("assets/drum"+ i +".wav", Sound.class);
	   	}
		manager.load("assets/drum.png", Texture.class);

		for (int i=1; i<4; i++) {
			manager.load("assets/sampler/Kick"+ i +".wav", Sound.class);
			manager.load("assets/sampler/Snare"+ i +".wav", Sound.class);
			manager.load("assets/sampler/Clap"+ i +".wav", Sound.class);
			manager.load("assets/sampler/Hat"+ i +".wav", Sound.class);
		}
		manager.load("assets/sampler.png", Texture.class);

		
		for (int i = 1; i <= 14; i++){
			manager.load("assets/flute/flute" + i + ".mp3", Sound.class);
		}
		manager.load("assets/flute.png", Texture.class);

		manager.load("assets/game/GameNote.png", Texture.class);
		manager.load("assets/game/game_piano.png", Texture.class);
		
		//Game 
		manager.load("assets/game/boo.png", Texture.class);
		manager.load("assets/game/butterfingers.png", Texture.class);
		manager.load("assets/game/fingermaster.png", Texture.class);
		manager.load("assets/game/wooh.png", Texture.class);
		
		for(int i=0;i<7;i++) {
			manager.load("assets/game/explos"+i+".png", Texture.class);
		}

	}

	@Override
	public void update() {
		if(manager.update()) {
			change = true;
		}
		
		progress = manager.getProgress();

	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,MusicalFingers.width/4f-10,MusicalFingers.height/2f-100-10,MusicalFingers.width/2f+20,200+20);
		batch.setColor(Color.RED);
		batch.draw(rectangle,MusicalFingers.width/4f,MusicalFingers.height/2f-100,progress*MusicalFingers.width/2f,200);
		batch.setColor(Color.WHITE);
		font.setScale(3f);
		font.draw(batch, "LOADING", MusicalFingers.width/2f-220f, MusicalFingers.height/2f+50f);

	}

	@Override
	public int changeStateTo() {
		if(change) {
			return MusicalFingers.STATE_MENU;
		} else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		manager.dispose();
	}

}