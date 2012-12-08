package org.ac.godsim.controls;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eatland
 * @since 30-November-2012
 */
public class ScholarDown implements ControlType {

	public String getType() {
		return "scholarDown";
	}

	@Override
	public void updateLevel() {
		System.err.println("decreasing scholar level");
		//TODO: Handle level change request
		
	}
}