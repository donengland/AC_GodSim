package org.ac.godsim.controls;

import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class GathererUp implements ControlType {

	public String getType() {
		return "gathererUp";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("increasing gatherer level");
		//TODO: Handle level change request
	}
}