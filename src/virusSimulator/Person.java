/**
 * 
 */
package virusSimulator;

import java.util.Objects;

/**
 * Represents persons who have a coordinate location x,y and a state of health.
 * 
 * @author Penny & Tingting
 *
 */
public class Person {
	
	private int location_x_Loc;
	private int location_y_Loc;
	private State person_state;
	
	/**
	 * Constructor
	 * @param location_x
	 * @param location_y
	 * @param person_state
	 */
	public Person(int location_x, int location_y, State person_state) {
		this.location_x_Loc = location_x;
		this.location_y_Loc = location_y;
		this.person_state = person_state;
	}

	/**
	 * Setter for location x
	 * @param location_x the location_x to set
	 */
	public void setLocation_x(int location_x) {
		this.location_x_Loc = location_x;
	}

	/**
	 * Setter for location y 
	 * @param location_y the location_y to set
	 */
	public void setLocation_y(int location_y) {
		this.location_y_Loc = location_y;
	}

	/**
	 * Setter for person's health state
	 * @param person_state the person_state to set
	 */
	public void setPerson_state(State person_state) {
		this.person_state = person_state;
	}

	/**
	 * Getter for location x 
	 * @return the location_x
	 */
	public int getLocation_x() {
		return location_x_Loc;
	}

	/**
	 * Getter for location y 
	 * @return the location_y
	 */
	public int getLocation_y() {
		return location_y_Loc;
	}

	/**
	 * Getter for person's health state 
	 * @return the person_state
	 */
	public State getPerson_state() {
		return person_state;
	}
	
	/**
	 * Checks if a person exists within the board.
	 * @return if a person exist
	 */
	public boolean isExist () {
		if (getLocation_x()<150 && getLocation_x()>49 && getLocation_y()>14 && getLocation_y()<85)
			return true;
		return false;
		
	}
	
	/**
	 * Checks if a person finish the whole simulation.
	 * @return true if a person's state is either EMPTY or IMMUNE.
	 */
	public boolean isDone() {
		if (getPerson_state().equals(State.EMPTY) ||getPerson_state().equals(State.IMMUNE)) {
			return true;
		}else return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(location_x_Loc, location_y_Loc, person_state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return location_x_Loc == other.location_x_Loc && location_y_Loc == other.location_y_Loc && person_state == other.person_state;
	}
	

}
