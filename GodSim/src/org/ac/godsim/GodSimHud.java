package org.ac.godsim;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

public class GodSimHud extends HUD {

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
	
	//Hud top left corner
	private int hudTop = CAMERA_HEIGHT - 91;;
	private int hudLeft = CAMERA_WIDTH / 6;
	private int hudHeight = 91; //fills the bottom of the screen from the point where the hud starts
	private int hudWidth = CAMERA_WIDTH * 2/3;   //makes the hud take up 2/3 of the horizontal width of the screen
	private int hudControlDim = 16; //these controls are square - use for both height and width

	public GodSimHud() {
		
	}
	
	public HUD makeHudWithControls() {
		
		
		
		
		
		return hud;
	}
	
	public HUD makeBasicHud() {
		return hud;
	}
	
	//Set up the main hud container atlas
//	this.mMenuBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudWidth, hudHeight, TextureOptions.DEFAULT);
//	this.mMenuContainerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuBitmapTextureAtlas, this, "container.png", 0, 0);
//	this.mMenuBitmapTextureAtlas.load();
//	
//	//Set up the up arrow hud control
//	this.mUpArrowBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudControlDim, hudControlDim, TextureOptions.DEFAULT);
//	this.mUpArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mUpArrowBitmapTextureAtlas, this, "up.png", 0, 0);
//	this.mUpArrowBitmapTextureAtlas.load();
//	
//	//Set up the up arrow hud control
//	this.mDownArrowBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudControlDim, hudControlDim, TextureOptions.DEFAULT);
//	this.mDownArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mDownArrowBitmapTextureAtlas, this, "down.png", 0, 0);
//	this.mDownArrowBitmapTextureAtlas.load();
//	
//	/* This on is for an icon to launch the primary control panel */
//	this.mLaunchPanelBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.DEFAULT);
//	this.mLaunchPanelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mLaunchPanelBitmapTextureAtlas, this, "control_panel.png", 0, 0);
//	this.mLaunchPanelBitmapTextureAtlas.load();
		
}
