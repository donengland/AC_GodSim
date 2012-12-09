package org.ac.godsim.controls;

import org.ac.godsim.GodSim;
import org.andengine.engine.camera.hud.HUD;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eastland
 * @since 30-November-2012
 */
public interface ControlType {
	
	public String getType();
	public void updateLevel(HUD hud);
}
