package org.ac.godsim.controls;

import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class WarriorDown implements ControlType {

	public String getType() {
		return "warriorDown";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("decreasing warrior level");
		//TODO: Handle level change request
	}
}