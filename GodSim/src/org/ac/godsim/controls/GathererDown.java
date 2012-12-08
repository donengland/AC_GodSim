package org.ac.godsim.controls;

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
	public void updateLevel() {
		System.err.println("decreasing gatherer level");
		//TODO: Handle level change request
		
	}
}