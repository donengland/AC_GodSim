package org.ac.godsim.controls;

import org.andengine.engine.camera.hud.HUD;



/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class CivMenu implements ControlType {

	public String getType() {
		return "controlPanel";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("launch expanded HUD");
		
	}

}