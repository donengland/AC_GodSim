package org.ac.godsim.controls;

import org.ac.godsim.GodSim;

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
	public void updateLevel() {
		/* In this case, this is actually an onClick method */
		GodSim.launchFullControlPanel();
	}
}