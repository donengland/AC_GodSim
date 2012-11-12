package org.ac.godsim.civ.units;

import org.ac.godsim.utils.constants.GodSimConstants;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public class Unit extends AnimatedSprite implements GodSimConstants{

	// ===========================================================
	// Fields
	// ===========================================================
	
	private IUnitType myType;
	// Id for UnitCatalog Uses, possibly multiplayer uses
	private int myId;
		
	// ===========================================================
	// Constructors
	// ===========================================================

	public Unit(IUnitType type, final float pWidth, final float pHeight, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		myType = type;
		myId = 0;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public String performGetType(){
		return myType.getType();
	}
	
	public int getId(){
		return myId;
	}
	
	public void setId(int id){
		myId = id;
	}
}
