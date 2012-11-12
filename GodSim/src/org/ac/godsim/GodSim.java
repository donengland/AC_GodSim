package org.ac.godsim;

import org.ac.godsim.civ.Civilization;
import org.ac.godsim.civ.units.Civ;
import org.ac.godsim.civ.units.Gatherer;
import org.ac.godsim.civ.units.Scholar;
import org.ac.godsim.civ.units.Unit;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
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
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 10-November-2012
 */
public class GodSim extends SimpleBaseGameActivity implements IOnSceneTouchListener, IOnAreaTouchListener{
	// ===========================================================
	// Constants
	// ===========================================================

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
	private int tileWidth = 32;
	
	// store touch location for X and Y in touch handlers
	private float camTempX;
	private float camTempY;
	private float camTargetX = CAMERA_WIDTH/2;
	private float camTargetY = CAMERA_HEIGHT/2;

	private Scene mScene;
	
	// HUD for menu displays
	private HUD hud;
	
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
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();

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
		this.createResourceObjects(mTMXTiledMap);
		this.createCivilizationObjects(mTMXTiledMap);

		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mSmoothChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mSmoothChaseCamera.setBoundsEnabled(true);
		this.mSmoothChaseCamera.setCenter(camTargetX, camTargetY);
		
		//====================
		//  TEMP HUD section
		//-----
		hud = new HUD();
		// Add a circle to the upper left corner of the hud
		final Unit circle;
		circle = new Unit(new Scholar(), 0, 0, this.mScholarTextureRegion, this.getVertexBufferObjectManager());
		circle.animate(200, true);
		this.hud.registerTouchArea(circle);
		// attach circle to the hud
		hud.attachChild(circle);
		// attach hud to main camera
		this.mSmoothChaseCamera.setHUD(hud);

		// we handle our own scene touches below "onSceneTouchEvent"
		this.mScene.setOnSceneTouchListener(this);

		// we handle our own scene touches below "onAreaTouchEvent"
		this.mScene.setOnAreaTouchListener(this);
		this.hud.setOnAreaTouchListener(this);
		
		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createCivilizationObjects(TMXTiledMap tmxmap){
		for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
            if(group.getTMXObjectGroupProperties().containsTMXProperty("civ", "true")){
            	// This is our "civ" layer. Create the civilizations from it
                for(final TMXObject object : group.getTMXObjects()) {
                	addCiv(object.getX(), object.getY());
                }
            }
		}
	}
	
	private void createResourceObjects(TMXTiledMap tmxmap){
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
		final Unit resource;
		resource = new Unit(new Gatherer(), pX, pY, this.mWarriorTextureRegion, this.getVertexBufferObjectManager());
		resource.animate(200, true);

		this.mScene.registerTouchArea(resource);
		this.mScene.attachChild(resource);
	}
	
	// Add the Civilization visuals and register/attach them to the scene.
	private void addCiv(final float pX, final float pY) {
		final Unit civ;
		civ = new Unit(new Civ(), pX, pY, this.mCivilizationTextureRegion, this.getVertexBufferObjectManager());
		civ.animate(1000, true);

		this.mScene.registerTouchArea(civ);
		this.mScene.attachChild(civ);
	}

	// The games main touch handler, there is also an area touch handler
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			this.touchStart(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}
		if(pSceneTouchEvent.isActionMove()) {
			this.moveCamera(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}if(pSceneTouchEvent.isActionUp()) {
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
			this.removeItem((Unit)pTouchArea);
			return true;
		}
		// let the handler know if we did not handle an area event
		return false;
	}
	
	// removes the touched sprite, placeholder for handling menu on sprites
	private void removeItem(Unit unit){
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
		System.out.println("Touched (x):" + pX + "(y):" +pY);
		this.camTempX = pX;
		this.camTempY = pY;
	}
	
	// Perform relative movement from starting location
	private void moveCamera(final float pX, final float pY){
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
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
