package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;
import org.andengine.entity.sprite.AnimatedSprite;
//import org.andengine.opengl.texture.region.ITiledTextureRegion;
//import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-December-2012
 */
public class Unit extends AnimatedSprite implements GodSimConstants{

	// ===========================================================
	// Fields
	// ===========================================================
	
	private IUnitType myType;
	// Id for UnitCatalog Uses, possibly multiplayer uses
	private int myId;
	
	// Targeting Floats
	private float targetX;
	private float targetY;
		
	// ===========================================================
	// Constructors
	// ===========================================================

	public Unit(IUnitType type, final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.myType = type;
		this.myId = 0;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public String performGetType(){
		return myType.getType();
	}
	
	public void performUpdate(float deltaTime){
		this.myType.update(this, deltaTime);
	}
	
	public int getId(){
		return myId;
	}
	
	public void setId(int id){
		this.myId = id;
	}

	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}
}
