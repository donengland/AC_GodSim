package org.ac.godsim.controls;

import org.ac.godsim.GodSim;
import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class CloseButton implements ControlType {

	public String getType() {
		return "closeButton";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("HUD Close Requested");
		
		//TODO: Switch to basic hud
		
	}
}