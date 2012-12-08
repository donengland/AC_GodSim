package org.ac.godsim.controls;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class WarriorUp implements ControlType {

	public String getType() {
		return "warriorUp";
	}

	@Override
	public void updateLevel() {
		System.err.println("increasing warrior level");
		//TODO: Handle level change request
		
	}
}