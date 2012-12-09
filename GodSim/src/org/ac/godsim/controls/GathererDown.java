package org.ac.godsim.controls;

import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class GathererDown implements ControlType {

	public String getType() {
		return "gathererDown";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("decreasing gatherer level");
		//TODO: Handle level change request
		
	}
}