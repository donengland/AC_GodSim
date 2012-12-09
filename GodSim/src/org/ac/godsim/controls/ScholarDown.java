package org.ac.godsim.controls;

import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class ScholarDown implements ControlType {

	public String getType() {
		return "scholarDown";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("decreasing scholar level");
		//TODO: Handle level change request
		
	}
}