package org.ac.godsim.controls;

import org.ac.godsim.GodSim;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2012 Randall Eastland
 
 * @author Randall Eastland
 * @since 30-November-2012
 */
public class ControlPanel extends Sprite {

	// ===========================================================
	// Fields
	// ===========================================================
	
	private ControlType myType;
		
	// ===========================================================
	// Constructors
	// ===========================================================

	public ControlPanel(ControlType type, final float pX, final float pY, final TextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		myType = type;
	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================


	public String performGetType(){
		return myType.getType();
	}
	
	public void updateLevel(HUD hud) {
		myType.updateLevel(hud);
	}
	
}
