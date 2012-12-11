package org.ac.godsim.controls;

import org.ac.godsim.GodSim;
import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class PanelLaunchButton implements ControlType {

	public String getType() {
		return "panelLaunchButton";
	}

	@Override
	public void updateLevel(HUD hud) {
		System.err.println("Launch Expanded Control Panel");
		
		//GodSim.makeHudWithControls(hud);
		
		//TODO: Switch to expanded hud
		
	}
}