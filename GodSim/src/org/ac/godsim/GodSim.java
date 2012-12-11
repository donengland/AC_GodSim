package org.ac.godsim;

import org.ac.godsim.civ.Civilization;
import java.util.List;

import org.ac.godsim.civ.units.Civ;
import org.ac.godsim.civ.units.Gatherer;
import org.ac.godsim.civ.units.Scholar;
import org.ac.godsim.civ.units.Unit;
import org.ac.godsim.civ.units.Warrior;
import org.ac.godsim.controls.CivMenu;
import org.ac.godsim.controls.CloseButton;
import org.ac.godsim.controls.GathererDown;
import org.ac.godsim.controls.GathererUp;
import org.ac.godsim.controls.ControlPanel;
import org.ac.godsim.controls.PanelLaunchButton;
import org.ac.godsim.controls.ScholarDown;
import org.ac.godsim.controls.ScholarUp;
import org.ac.godsim.controls.WarriorDown;
import org.ac.godsim.controls.WarriorUp;
import org.ac.godsim.persistentdata.GodSimDB;
import org.ac.godsim.utils.constants.GodSimConstants;
//import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
//import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
//import org.andengine.entity.IEntity;
//import org.andengine.entity.modifier.LoopEntityModifier;
//import org.andengine.entity.modifier.PathModifier;
//import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
//import org.andengine.entity.modifier.PathModifier.Path;
//import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
//import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
//import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.view.ViewGroup;

//import android.widget.Toast;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class GodSim extends SimpleBaseGameActivity implements IOnSceneTouchListener, IOnAreaTouchListener, GodSimConstants{
	// ===========================================================
	// Constants
	// ===========================================================

	private String thisPlayer;
	private int thisGame;
	
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	private static final int CAMERA_X_VELOCITY = 500;
	private static final int CAMERA_Y_VELOCITY = 500;
	private static final int CAMERA_ZOOM_FACTOR = 500;

	// ===========================================================
	// Fields
	// ===========================================================

	private SmoothCamera mSmoothChaseCamera;

	// Atlas, which is combination of several textures below
	private BitmapTextureAtlas mBitmapTextureAtlas;
	// TiledTextureRegions store the area of this texture within the atlas ^^
	private TiledTextureRegion mScholarTextureRegion;	
	private TiledTextureRegion mWarriorTextureRegion;
	private TiledTextureRegion mGathererTextureRegion;
	private TiledTextureRegion mCivilizationTextureRegion;
	
	// Storage for the tmx file, including the standard tile size
	private TMXTiledMap mTMXTiledMap;
	//private int tileWidth = 32;
	
	// store touch location for X and Y in touch handlers
	private float camTempX;
	private float camTempY;
	private float camTargetX = CAMERA_WIDTH/2;
	private float camTargetY = CAMERA_HEIGHT/2;

	//Hud top left corner
	private static int hudTop = CAMERA_HEIGHT - 91;;
	private static int hudLeft = CAMERA_WIDTH / 6;
	private int hudHeight = 91; //fills the bottom of the screen from the point where the hud starts
	private int hudWidth = CAMERA_WIDTH * 2/3;   //makes the hud take up 2/3 of the horizontal width of the screen
	private int hudControlDim = 16; //these controls are square - use for both height and width
	
	private Scene mScene;
	
	// HUD for menu displays
	private HUD hud;
	private GodSimHudController hudController;
	private BitmapTextureAtlas mMenuBitmapTextureAtlas;
	private BitmapTextureAtlas mUpArrowBitmapTextureAtlas;
	private BitmapTextureAtlas mDownArrowBitmapTextureAtlas;
	private BitmapTextureAtlas mLaunchPanelBitmapTextureAtlas;
	private BitmapTextureAtlas mCloseButtonBitmapTextureAtlas;
	private TextureRegion mMenuContainerTextureRegion;
	private TextureRegion mUpArrowTextureRegion;
	private TextureRegion mDownArrowTextureRegion;
	private TextureRegion mLaunchPanelTextureRegion;
	private TextureRegion mCloseButtonTextureRegion;
	private Font mFont;
	
	// Temp Civilization
	private Civilization myCiv;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		//Toast.makeText(this, "The tile the player is walking on will be highlighted.", Toast.LENGTH_LONG).show();

		this.mSmoothChaseCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_X_VELOCITY, CAMERA_Y_VELOCITY, CAMERA_ZOOM_FACTOR);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mSmoothChaseCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// MATH Critical Area of code:  this section is performing atlasing
		//    "manually",  care must be taken when selecting image and atlas sizes 
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.DEFAULT);
		this.mWarriorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mGathererTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mCivilizationTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "civ_tiled.png", 0, 64, 2, 1); // 128x64
		
		this.mBitmapTextureAtlas.load();
		
		this.mScholarTextureRegion = this.mGathererTextureRegion;
		
		//Set up the main hud container atlas
		this.mMenuBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), hudWidth, hudHeight, TextureOptions.DEFAULT);
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
		
//		/* This on is for an icon to launch the primary control panel */
//		this.mCloseButtonBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.DEFAULT);
//		this.mCloseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mCloseButtonBitmapTextureAtlas, this, "close.png", 0, 0);
//		this.mCloseButtonBitmapTextureAtlas.load();
		
		/* Font for control level feedback on the Hud */
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 12);
		this.mFont.load();
		
	}
	
	@Override
	public Scene onCreateScene() {
		
		/* Initialize this player's settings */
		System.err.println("getting this player");
		thisPlayer = getIntent().getExtras().getString("thisPlayer");
		System.err.printf("Player found: %s\n", thisPlayer);
		System.err.println("getting this game");
		thisGame = initGame(thisPlayer);
		System.err.printf("game found: %d\n", thisGame);
		System.err.println("starting onCreateSecne");
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();

		// TODO -- temp holder for testing civilization controls via update manager
		myCiv = new Civilization(UNIT_COLOR.red);
		
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					// area for tile properties actions, those properties that are set in the tiles themselves
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						// expand to add physical barriers, "as a stretch goal"
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/World1.tmx");
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		// Look into tmx and find all layers, perform action--attach child--on non-resource layer
		TMXLayer tmxLayer = null;
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++){
            tmxLayer = this.mTMXTiledMap.getTMXLayers().get(i);
            if (!tmxLayer.getTMXLayerProperties().containsTMXProperty("resource", "true"))
            	this.mScene.attachChild(tmxLayer);
		}
		
		// Send this map out for processing resources and civilizations
		// this initiates the creation of civilizations and units
		this.createResourceObjects(mTMXTiledMap);
		this.createCivilizationObjects(mTMXTiledMap);

		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mSmoothChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mSmoothChaseCamera.setBoundsEnabled(true);
		this.mSmoothChaseCamera.setCenter(camTargetX, camTargetY);
		
		/* Create the stationary section of the game where the player can make certain adjustments 
		 * This is called a 'hud' */
		
//		hudController = new GodSimHudController(this.getEngine().getCamera());
//		hud = hudController.makeBasicHud();
//		hud = makeBasicHud(new HUD());
		hud = makeHudWithControls(new HUD());

		
		this.mSmoothChaseCamera.setHUD(hud);
		
		// we handle our own scene touches below "onSceneTouchEvent"
		this.mScene.setOnSceneTouchListener(this);

		// we handle our own scene touches below "onAreaTouchEvent"
		this.mScene.setOnAreaTouchListener(this);
		hud.setOnAreaTouchListener(this);
		
		// updateHandler to allow units to respond to passing time
		this.mScene.registerUpdateHandler(new IUpdateHandler(){
			private float totalElasped = 0;
			public void reset(){ }
			
			public void onUpdate(final float pSecondsElasped){
				totalElasped += pSecondsElasped;
				if(totalElasped > 0.0333f){
					//System.out.println("Seconds Elasped: " + totalElasped);
					myCiv.updateUnits(totalElasped);
					totalElasped = 0.0f;
				}
			}
		});
		
		
		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createCivilizationObjects(TMXTiledMap tmxmap){
		System.err.println("createCivilizationObjects");
		for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
            if(group.getTMXObjectGroupProperties().containsTMXProperty("civ", "true")){
            	// This is our "civ" layer. Create the civilizations from it
                for(final TMXObject object : group.getTMXObjects()) {
                	addCiv(object.getX(), object.getY());
                }
            }
		}
	}
	
	//TODO: Expand this method to pull in required saved game settings
	private int initGame(String p) {
		int thisGame = GodSimDB.getGame(p);
		return thisGame;
	}
	
	public HUD makeBasicHud(HUD basicHud) {
		final ControlPanel launchPanel = new ControlPanel(new PanelLaunchButton(), 10, CAMERA_HEIGHT - 32 - 10, this.mLaunchPanelTextureRegion, this.getVertexBufferObjectManager());
		basicHud.registerTouchArea(launchPanel);
		basicHud.attachChild(launchPanel);
		return basicHud;
	}
	
	public HUD makeHudWithControls(HUD expHud) {
		
		/* Text fields for control level feedback */
		final Text scholarLevel = new Text(hudLeft+85, hudTop+50, this.mFont, "1", "1".length(), this.getVertexBufferObjectManager());
		final Text warriorLevel = new Text(hudLeft+85+85, hudTop+50, this.mFont, "2", "2".length(), this.getVertexBufferObjectManager());
		final Text gathererLevel = new Text(hudLeft+85+85*2, hudTop+50, this.mFont, "3", "3".length(), this.getVertexBufferObjectManager());
		
		final ControlPanel container;
		final ControlPanel close;
		final ControlPanel scholarUp;
		final ControlPanel scholarDown;
		final ControlPanel warriorUp;
		final ControlPanel warriorDown;
		final ControlPanel gathererUp;
		final ControlPanel gathererDown;
		
		container = new ControlPanel(new CivMenu(), hudLeft, hudTop, mMenuContainerTextureRegion, this.getVertexBufferObjectManager());
		//close = new ControlPanel(new CloseButton(), hudLeft + hudWidth - 21, hudTop + hudHeight - 21, this.mCloseButtonTextureRegion, this.getVertexBufferObjectManager());
		
		scholarUp = new ControlPanel(new ScholarUp(), hudLeft + 60 , hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		scholarDown = new ControlPanel(new ScholarDown(), hudLeft + 60 , hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		warriorUp = new ControlPanel(new WarriorUp(), hudLeft + 60 + 85 , hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		warriorDown = new ControlPanel(new WarriorDown(), hudLeft + 60 + 85 , hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		gathererUp = new ControlPanel(new GathererUp(), hudLeft + 60 + 85*2, hudTop + 40, this.mUpArrowTextureRegion, this.getVertexBufferObjectManager());
		gathererDown = new ControlPanel(new GathererDown(), hudLeft + 60 + 85*2, hudTop + 40+18, this.mDownArrowTextureRegion, this.getVertexBufferObjectManager());
		
		//hud.registerTouchArea(container); //registering this conflicts with the other touch areas
		//expHud.registerTouchArea(close);
		expHud.registerTouchArea(scholarUp);
		expHud.registerTouchArea(scholarDown);
		expHud.registerTouchArea(warriorUp);
		expHud.registerTouchArea(warriorDown);
		expHud.registerTouchArea(gathererUp);
		expHud.registerTouchArea(gathererDown);
		
		expHud.attachChild(container);
		//expHud.attachChild(close);
		expHud.attachChild(scholarUp);
		expHud.attachChild(scholarDown);
		expHud.attachChild(warriorUp);
		expHud.attachChild(warriorDown);
		expHud.attachChild(gathererUp);
		expHud.attachChild(gathererDown);
		expHud.attachChild(scholarLevel);
		expHud.attachChild(warriorLevel);
		expHud.attachChild(gathererLevel);
		
		return expHud;
	}
	
	
	private void createResourceObjects(TMXTiledMap tmxmap){
		System.err.println("createResourceObjects");
		for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
            if(group.getTMXObjectGroupProperties().containsTMXProperty("resource", "true")){
            	// This is our "resource" layer. Create the resources from it
                for(final TMXObject object : group.getTMXObjects()) {
                	addResource(object.getX(), object.getY());
                }
            }
		}
	}
	
	// Add the Resource visuals and register/attach them to the scene.	
	private void addResource(final float pX, final float pY) {
		System.err.println("addResource");
		final Unit resource;
		resource = new Unit(new Gatherer(), pX, pY, this.mWarriorTextureRegion, this.getVertexBufferObjectManager());
		resource.animate(200, true);
		// TODO -- temp add resources to  civilization for movement processing
		this.myCiv.addGatherer(resource);

		this.mScene.registerTouchArea(resource);
		this.mScene.attachChild(resource);
	}
	
	// Add the Civilization visuals and register/attach them to the scene.
	private void addCiv(final float pX, final float pY) {
		System.err.println("addCiv");
		final Unit civ;
		civ = new Unit(new Civ(), pX, pY, this.mCivilizationTextureRegion, this.getVertexBufferObjectManager());
		civ.animate(1000, true);
		// TODO -- add the civilization representation to the temp civilization
		this.myCiv.addCiv(civ);
		
		this.mScene.registerTouchArea(civ);
		this.mScene.attachChild(civ);
	}

	// The games main touch handler, there is also an area touch handler
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		System.err.println("onSceneTouchEvent");
		if(pSceneTouchEvent.isActionDown()) {
			
			this.touchStart(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}
		
		if(pSceneTouchEvent.isActionMove()) {
			this.moveCamera(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}
		
		if(pSceneTouchEvent.isActionUp()) {
			// Remove comment below to add "resources" at mouse location on release 
			//this.addResource(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			
			
			
			
			
			return true;
		}
		// let the handler know if we did not handle a specific message
		return false;
	}
	
	// The games "unit" touch handler, there is also an scene/world touch handler
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		if(pSceneTouchEvent.isActionDown()) {
			Unit unit;
			ControlPanel control;
			boolean castFailed = false;
			try {
				unit = (Unit) pTouchArea;
			} catch (Exception ex) {
				castFailed = true;
			} 
			
			if (castFailed) {
				try {
					castFailed = false;
					control = (ControlPanel) pTouchArea;
					control.updateLevel(hud);
				} catch (Exception ex) {
					castFailed = true;
					System.err.println("unknown touch event");
				} 
			}
			
			return true;
		}
		// let the handler know if we did not handle an area event
		return false;
	}
	
	public static void launchFullControlPanel() {
		//hud.makeHudWithControls();
	}
	
	
	// removes the touched sprite, placeholder for handling menu on sprites
	private void removeItem(Unit unit){
		System.err.println("removeItem");
		System.out.println("Removed " + unit.performGetType() +"!!!!");
		
		// This area should know the type of sprite before removing,
		//  but it functions as is written for now.
		//  At this point, the actual class structure should be built from the Design_Doc -D
		this.hud.unregisterTouchArea(unit);
		this.hud.detachChild(unit);
		
		this.mScene.unregisterTouchArea(unit);
		this.mScene.detachChild(unit);
		
		System.gc();
	}
	
	// Grab the starting location for any movement
	private void touchStart(final float pX, final float pY){
		System.err.println("touchStart");
		System.out.println("Touched (x):" + pX + "(y):" +pY);
		this.camTempX = pX;
		this.camTempY = pY;
	}
	
	// Perform relative movement from starting location
	private void moveCamera(final float pX, final float pY){
		System.err.println("moveCamera");
		//System.out.println("Moved (x):" + pX + "(y):" +pY);

		// Move Camera target opposite from touch direction, and reset static position
		camTargetX += camTempX - pX;
		camTempX = pX;		
		camTargetY += camTempY - pY;
		camTempY = pY;
		
		// checks that target is within map bounds
		if(camTargetX < this.mSmoothChaseCamera.getBoundsXMin()){
			camTargetX = this.mSmoothChaseCamera.getBoundsXMin();
		}else if(camTargetX > this.mSmoothChaseCamera.getBoundsXMax()){
			camTargetX = this.mSmoothChaseCamera.getBoundsXMax();
		}		
		if(camTargetY < this.mSmoothChaseCamera.getBoundsYMin()){
			camTargetY = this.mSmoothChaseCamera.getBoundsYMin();
		}else if(camTargetY > this.mSmoothChaseCamera.getBoundsYMax()){
			camTargetY = this.mSmoothChaseCamera.getBoundsYMax();
		}
		
		// update camera to move to new target position
		this.mSmoothChaseCamera.setCenter(camTargetX, camTargetY);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//TODO: save game state
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}


