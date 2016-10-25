package ox.musicalfingers.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class MusicalFingers implements ApplicationListener{
	
	//Drawing 
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	
	//Screen info
	public static int width = 0;
	public static int height = 0;
	
	//To show fps
	private long time = 0;
	private int fps = 0;
	private int showfps = 0;
	
	//States
	public static final int STATE_LOADING = 0;
	public static final int STATE_MENU = 1;
	public static final int STATE_INSTRUMENT = 2;
	public static final int STATE_GAME = 3;
	public static final int STATE_WIN = 4;
	public static final int STATE_LOSE = 5;

	private int current_state = 0;
	
	//Different 'Screens' for the app
	Screen loading;
	Screen menu;
	Screen instrument;
	Screen game;
	Screen win;
	Screen lose; 
	//Current screen, used for convenience 
	Screen current;
	
	
	//AssetManager for storing assets
	public static AssetManager manager = new AssetManager();
	
	// Stuff for drawing background notes
	List<Integer> noteHeights = new ArrayList<Integer>(16);
	int count = 0;
	Texture note1;
	Texture note2;
	Texture note3;
	Texture clef;
	boolean drawBackground = false;
	float pColor=0f;
	boolean pSwitch = false;
	float pInc = 0.0005f;
	

	@Override
	public void create() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		Gdx.graphics.setVSync(true);
		camera = new OrthographicCamera(width,height);
		//Set y-axis pointing down
		camera.setToOrtho(false);
		batch = new SpriteBatch();
		//Temporary font for drawing fps in top left corner
		font = new BitmapFont(Gdx.files.internal("assets/font/pixel.fnt"), Gdx.files.internal("assets/font/pixel.png"), false);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		time = TimeUtils.nanoTime();
		
		init();
	}
	
	//Initialise stuff for the app here
	private void init() {
		
		//Create screen objects
		loading = new LoadingScreen(manager);
		menu = new MenuScreen();
		instrument = new InstrumentScreen();
		game = new GameScreen();
		win = new GameOver("win");
		lose = new GameOver("lose");
		
		//Set the state to loading
		changeState(STATE_LOADING);	
		
	}
	
	private void changeState(int changeTo) {
		
		switch(changeTo) {
			case STATE_LOADING: 	loading.init();
									current_state = STATE_LOADING;
									current = loading;
									break;
			case STATE_MENU:		menu.init();
									notes();
									current_state = STATE_MENU;
									current = menu;
									break;
			case STATE_INSTRUMENT:  instrument.init();
									current_state = STATE_INSTRUMENT;
									current = instrument;
									break;
			case STATE_GAME:		game.init();
									current_state = STATE_GAME;
									current = game;
									break;
			case STATE_WIN: 		win.init();
									current_state = STATE_WIN;
									current = win;
									break;
			case STATE_LOSE: 		lose.init();
									current_state = STATE_LOSE;
									current = lose;
									break;
			default:				throw new Error(changeTo + " is not a valid state");
		}
		
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		camera = new OrthographicCamera(width,height);
		camera.setToOrtho(false);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		current.init();
		if(drawBackground)
			notes();
		
	}
	
	private void notes() {
		// For background notes
		note1 = MusicalFingers.manager.get("assets/1note.png");
		note2 = MusicalFingers.manager.get("assets/2note.png");
		note3 = MusicalFingers.manager.get("assets/3note.png");
		clef = MusicalFingers.manager.get("assets/clef.png");

		Random rnd = new Random();

		for (int i = 0; i < 20; i++) {
			noteHeights.add(rnd.nextInt(MusicalFingers.height - 40));
		}
		
		drawBackground = true;
	}

	@Override
	public void render() {
		update();
		
		//Clear screen with black background
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(pSwitch) {
			pColor-=pInc;
		} else {
			pColor+=pInc;
		}
		
		if(pColor>1f-pInc) {
			pSwitch=true;
		}
		if(pColor < pInc) {
			pSwitch=false;
		}
		
		//System.out.println(pColor);
		Gdx.gl.glClearColor(pColor,163f/255f,255f/255f,1);
		
		batch.begin();
		
		if(drawBackground) {
			// Some notes in the background
			for(int i=0;i<noteHeights.size();i++) {
				if(i%4==0) {
					batch.draw(clef,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),clef.getWidth()*4f,clef.getHeight()*4f);
				} else if(i%4==1) {
					batch.draw(note1,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note1.getWidth()*4f,note1.getHeight()*4f);
				} else if(i%4==2) {
					batch.draw(note2,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note2.getWidth()*4f,note2.getHeight()*4f);
				} else {
					//batch.draw(note3,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i));
					batch.draw(note3,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note3.getWidth()*4f,note3.getHeight()*4f);
				}
			}
		}
		
		draw(batch);
		
		//Draw fps in top left corner
		font.setColor(1,1,1,1);
		//font.setScale(1,-1);
		//font.draw(batch, ""+showfps, MusicalFingers.width-50,30);
		
		batch.end();
		
		//Calculate the fps for the previous second
		fps++;
		if(TimeUtils.nanoTime() - time >  (1000000000 - 1)) {
			showfps = fps;
			if(showfps<=55) {
				System.out.println("Fps drop");
			}
			fps = 0;
			time = TimeUtils.nanoTime();
		}
	}
	
	//Main loop for the app
	private void update() {
		//Check if a screen wants to change state
		if(current.changeStateTo() != -1) {
			changeState(current.changeStateTo());
		}
		

		// Update background images
		count++;
		if (count > noteHeights.size() * 100) {
			count = 0;
		}
		
		//Otherwise update it
		current.update();
		
	}
	
	//Draw stuff for the here
	private void draw(SpriteBatch batch) {
		current.draw(batch);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		manager.dispose();
		
	}

}
