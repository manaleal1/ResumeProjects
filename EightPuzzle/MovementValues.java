/*
 * The value of the index of the location of the blank tile increases or decreases
 * depending on which direction it will move.
 */
public enum MovementValues {
	LEFT (-1),
	RIGHT (1),
	UP (-3),
	DOWN  (3);
	
	private final int value;
	
	MovementValues(int value){
		this.value = value;
	}
	
	int value() {
		return value;
	}
	
}
