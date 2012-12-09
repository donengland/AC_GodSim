package org.ac.godsim.controls;

import org.ac.godsim.GodSim;
import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */

public class ControlPanelMenu implements ControlType {

	public String getType() {
		return "launchPanel";
	}

	@Override
	public void updateLevel(HUD hud) {
		/* In this case, this is actually an onClick method */
		GodSim.launchFullControlPanel();
	}
}