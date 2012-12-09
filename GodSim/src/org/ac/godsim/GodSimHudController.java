package org.ac.godsim;

import org.ac.godsim.controls.CivMenu;
import org.ac.godsim.controls.ControlPanel;
import org.ac.godsim.controls.GathererDown;
import org.ac.godsim.controls.GathererUp;
import org.ac.godsim.controls.PanelLaunchButton;
import org.ac.godsim.controls.ScholarDown;
import org.ac.godsim.controls.ScholarUp;
import org.ac.godsim.controls.WarriorDown;
import org.ac.godsim.controls.WarriorUp;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;

import android.graphics.Typeface;

public class GodSimHudController extends GodSim {

	Camera camera;
	HUD hud;
	
	//Camera settings
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	
	// HUD for menu displays
	private BitmapTextureAtlas mMenuBitmapTextureAtlas;
	private BitmapTextureAtlas mUpArrowBitmapTextureAtlas;
	private BitmapTextureAtlas mLaunchPanelBitmapTextureAtlas;
	private BitmapTextureAtlas mDownArrowBitmapTextureAtlas;
	private TextureRegion mMenuContainerTextureRegion;
	private TextureRegion mUpArrowTextureRegion;
	private TextureRegion mDownArrowTextureRegion;
	private TextureRegion mLaunchPanelTextureRegion;
	private Font mFont;
	
	//Hud top left corner
	private int hudTop = CAMERA_HEIGHT - 91;;
	private int hudLeft = CAMERA_WIDTH / 6;
	private int hudHeight = 91; //fills the bottom of the screen from the point where the hud starts
	private int hudWidth = CAMERA_WIDTH * 2/3;   //makes the hud take up 2/3 of the horizontal width of the screen
	private int hudControlDim = 16; //these controls are square - use for both height and width
	
	public GodSimHudController(Camera mCamera) {
		//super();
		camera = mCamera;
	}
	
	public HUD makeHudWithControls() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		//Set up the main hud container atlas
		mMenuBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudWidth, hudHeight, TextureOptions.DEFAULT);
		this.mMenuContainerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuBitmapTextureAtlas, this, "container.png", 0, 0);
		this.mMenuBitmapTextureAtlas.load();
		
		//Set up the up arrow hud control
		this.mUpArrowBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudControlDim, hudControlDim, TextureOptions.DEFAULT);
		this.mUpArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mUpArrowBitmapTextureAtlas, this, "up.png", 0, 0);
		this.mUpArrowBitmapTextureAtlas.load();
		
		//Set up the up arrow hud control
		this.mDownArrowBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudControlDim, hudControlDim, TextureOptions.DEFAULT);
		this.mDownArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mDownArrowBitmapTextureAtlas, this, "down.png", 0, 0);
		this.mDownArrowBitmapTextureAtlas.load();
		
		/* This on is for an icon to launch the primary control panel */
		this.mLaunchPanelBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.DEFAULT);
		this.mLaunchPanelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mLaunchPanelBitmapTextureAtlas, this, "control_panel.png", 0, 0);
		this.mLaunchPanelBitmapTextureAtlas.load();
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 12);
		this.mFont.load();

		/* Text fields for control level feedback */
		final Text scholarLevel = new Text(hudLeft+85, hudTop+50, this.mFont, "1", "1".length(), this.getVertexBufferObjectManager());
		final Text warriorLevel = new Text(hudLeft+85+85, hudTop+50, this.mFont, "2", "2".length(), this.getVertexBufferObjectManager());
		final Text gathererLevel = new Text(hudLeft+85+85*2, hudTop+50, this.mFont, "3", "3".length(), this.getVertexBufferObjectManager());
		
		final ControlPanel container;
		final ControlPanel scholarUp;
		final ControlPanel scholarDown;
		final ControlPanel warriorUp;
		final ControlPanel warriorDown;
		final ControlPanel gathererUp;
		final ControlPanel gathererDown;
		
		container = new ControlPanel(new CivMenu(), hudLeft, hudTop, this.mMenuContainerTextureRegion, this.getVertexBufferObjectManager());
		
		scholarUp = new ControlPanel(new ScholarUp(), hudLeft + 60 , hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		scholarDown = new ControlPanel(new ScholarDown(), hudLeft + 60 , hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		warriorUp = new ControlPanel(new WarriorUp(), hudLeft + 60 + 85 , hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		warriorDown = new ControlPanel(new WarriorDown(), hudLeft + 60 + 85 , hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		gathererUp = new ControlPanel(new GathererUp(), hudLeft + 60 + 85*2, hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		gathererDown = new ControlPanel(new GathererDown(), hudLeft + 60 + 85*2, hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		hud.registerTouchArea(scholarUp);
		hud.registerTouchArea(scholarDown);
		hud.registerTouchArea(warriorUp);
		hud.registerTouchArea(warriorDown);
		hud.registerTouchArea(gathererUp);
		hud.registerTouchArea(gathererDown);
		
		hud.attachChild(container);
		hud.attachChild(scholarUp);
		hud.attachChild(scholarDown);
		hud.attachChild(warriorUp);
		hud.attachChild(warriorDown);
		hud.attachChild(gathererUp);
		hud.attachChild(gathererDown);
		hud.attachChild(scholarLevel);
		hud.attachChild(warriorLevel);
		hud.attachChild(gathererLevel);
		
		return hud;
	}
	
	public HUD makeBasicHud() {
				
		this.runOnUiThread(new Runnable() {
			private TextureRegion mLaunchPanelTextureRegion;
			private BitmapTextureAtlas mLaunchPanelBitmapTextureAtlas;

			@Override
			public void run() {
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
				

				//Set up the main hud container atlas
				mLaunchPanelBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), hudWidth, hudHeight, TextureOptions.DEFAULT);
				this.mLaunchPanelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mLaunchPanelBitmapTextureAtlas, getBaseContext(), "control_panel.png", 0, 0);
				this.mLaunchPanelBitmapTextureAtlas.load();

				hud = new HUD();
				
				final ControlPanel panelLauncher = new ControlPanel(new PanelLaunchButton(), 10, CAMERA_HEIGHT - 32 - 10, this.mLaunchPanelTextureRegion, getVertexBufferObjectManager());		
				hud.registerTouchArea(panelLauncher);
				hud.attachChild(panelLauncher);		
				
			}});
		
		return hud;
	}		
}

























