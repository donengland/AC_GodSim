package org.ac.godsim;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.SmoothCamera;
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
import org.andengine.entity.scene.IOnSceneTouchListener;
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
public class GodSim extends SimpleBaseGameActivity implements IOnSceneTouchListener{
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

	private BitmapTextureAtlas mBitmapTextureAtlas;	
	private TiledTextureRegion mPlayerTextureRegion;
	
	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mCivTextureRegion;
	
	private TMXTiledMap mTMXTiledMap;
	protected int mWallCount;
	private int tileWidth = 32;
	
	private AnimatedSprite player;
	
	// store touch location for X and Y in touch handlers
	private float camTempX;
	private float camTempY;
	private float camTargetX = CAMERA_WIDTH/2;
	private float camTargetY = CAMERA_HEIGHT/2;

	private Scene mScene;

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

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.DEFAULT);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mCivTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "civ_tiled.png", 0, 64, 2, 1); // 128x64		

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();

		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						GodSim.this.mWallCount++;
					}					
					if(pTMXTileProperties.containsTMXProperty("civ", "true")){						
					}
					if(pTMXTileProperties.containsTMXProperty("Face", "true")){
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/World1.tmx");

			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(GodSim.this, "Wall count in this TMXTiledMap: " + GodSim.this.mWallCount, Toast.LENGTH_LONG).show();
				}
			});
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		TMXLayer tmxLayer = null;
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++){
            tmxLayer = this.mTMXTiledMap.getTMXLayers().get(i);
            if (!tmxLayer.getTMXLayerProperties().containsTMXProperty("resource", "true"))
            	this.mScene.attachChild(tmxLayer);
		}
//		for(TMXLayer tmxLayer : this.mTMXTiledMap.getTMXLayers()) {
//			  this.mScene.getChild(1).attachChild(tmxLayer);
//		}
//		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
//		System.out.println("tmx");
//		this.mScene.attachChild(tmxLayer);
		
		this.createResourceObjects(mTMXTiledMap);
		this.createCivilizationObjects(mTMXTiledMap);

		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mSmoothChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mSmoothChaseCamera.setBoundsEnabled(true);
		this.mSmoothChaseCamera.setCenter(camTargetX, camTargetY);

		this.mScene.setOnSceneTouchListener(this);

		return this.mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createCivilizationObjects(TMXTiledMap tmxmap){
		for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
            if(group.getTMXObjectGroupProperties().containsTMXProperty("civ", "true")){
            	// This is our "wall" layer. Create the boxes from it
                for(final TMXObject object : group.getTMXObjects()) {
                	addCiv(object.getX(), object.getY());
                }
            }
		}
	}
	
	private void createResourceObjects(TMXTiledMap tmxmap){
		for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
            if(group.getTMXObjectGroupProperties().containsTMXProperty("resource", "true")){
            	// This is our "wall" layer. Create the boxes from it
                for(final TMXObject object : group.getTMXObjects()) {
                	addResource(object.getX(), object.getY());
                }
            }
		}
	}
	
	private void addResource(final float pX, final float pY) {
		final AnimatedSprite resource;
		resource = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
		resource.animate(200, true);

		this.mScene.registerTouchArea(resource);
		this.mScene.attachChild(resource);
	}
	
	private void addCiv(final float pX, final float pY) {
		final AnimatedSprite civ;
		civ = new AnimatedSprite(pX, pY, this.mCivTextureRegion, this.getVertexBufferObjectManager());
		civ.animate(1000, true);

		this.mScene.registerTouchArea(civ);
		this.mScene.attachChild(civ);
	}

	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown()) {
			this.touchStart(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}
		if(pSceneTouchEvent.isActionMove()) {
			this.moveCamera(pSceneTouchEvent.getMotionEvent().getX(), pSceneTouchEvent.getMotionEvent().getY());
			return true;
		}if(pSceneTouchEvent.isActionUp()) {
			// Enable for adding "resources" at mouse location on release 
			//this.addResource(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			return true;
		}
		return false;
	}
	private void touchStart(final float pX, final float pY){
		System.out.println("Touched (x):" + pX + "(y):" +pY);
		this.camTempX = pX;
		this.camTempY = pY;
	}
	private void moveCamera(final float pX, final float pY){
		System.out.println("Moved (x):" + pX + "(y):" +pY);

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

