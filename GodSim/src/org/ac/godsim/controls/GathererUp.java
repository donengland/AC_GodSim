package org.ac.godsim.controls;

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
	public void updateLevel() {
		System.err.println("increasing gatherer level");
		//TODO: Handle level change request
	}
}