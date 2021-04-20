package automail;

import simulation.Building;

import java.util.Map;
import java.util.TreeMap;

// import java.util.UUID;

/**
 * Represents a mail item
 */
public class MailItem {

    /** Represents the destination floor to which the mail is intended to go */
    protected final int destination_floor;
    /** The mail identifier */
    protected final String id;
    /** The time the mail item arrived */
    protected final int arrival_time;
    /** The weight in grams of the mail item */
    protected final int weight;

    // The service fee to the recipient for delivering the MailItem
    private double serviceFee = 0;
    // The amount of activity units spent to take an item to its destination
    private double activityUnitsToDeliver = 0;

    /**
     * Constructor for a MailItem
     * @param dest_floor the destination floor intended for this mail item
     * @param arrival_time the time that the mail arrived
     * @param weight the weight of this mail item
     */
    public MailItem(int dest_floor, int arrival_time, int weight){
        this.destination_floor = dest_floor;
        this.id = String.valueOf(hashCode());
        this.arrival_time = arrival_time;
        this.weight = weight;
    }

    /** A toString method which includes the added statistics tracking if called with argument true. */
    public String toString(boolean displayCharge){
        if (displayCharge) {
            return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d | Charge: %.2f | Cost: %.2f | Fee: %.2f | Activity: %.2f",
                    id, arrival_time, destination_floor, weight, calculateCharge(calculateTotalActivityUnits()), calculateCost(calculateTotalActivityUnits()), serviceFee, calculateTotalActivityUnits());
        } else {
            return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d",
                    id, arrival_time, destination_floor, weight);
        }
    }


    /** A default implementation for the toString method, calls the toString method with statistics tracking with
     * argument false so that the statistics tracking is not included.  */
    @Override
    public String toString(){
        return toString(false);
    }

    /** Calculates the total amount of activity units required by a robot to perform a delivery and return to the
     * mail room. Assumes that the robot travels directly back to the mail room after delivering and doesn't
     * discount when robots deliver multiple items. */
    public double calculateTotalActivityUnits() {
        double activityUnitsToReturn = (destination_floor - Building.MAILROOM_LOCATION) * Robot.UNITS_PER_FLOOR;

        return activityUnitsToDeliver + activityUnitsToReturn;
    }

    /**
     * @return the destination floor of the mail item
     */
    public int getDestFloor() {
        return destination_floor;
    }
    
    /**
     * @return the ID of the mail item
     */
    public String getId() {
        return id;
    }

    /**
     * @return the arrival time of the mail item
     */
    public int getArrivalTime(){
        return arrival_time;
    }

    /**
    * @return the weight of the mail item
    */
    public int getWeight(){
       return weight;
    }
   
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	@Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}

    /**
     * Estimates the amount of activity units a robot will spend on a round trip to deliver the MailItem
     * @return The estimated amount of activity units.
     */
	public double estimateActivityToDeliver() {
	    int floorDistance = destination_floor - Building.MAILROOM_LOCATION;
        double movementUnits = Robot.UNITS_PER_FLOOR * (floorDistance * 2);  // *2 to charge to and from the mail room

	    return movementUnits + Robot.UNITS_PER_LOOKUP;  // Always want to charge for only one lookup. Our design will only do 1
    }

    /** Adds an amount of activity units taken to deliver the mail item to the total. */
    public void increaseActivityUnitsToDeliver(double increase) {
        this.activityUnitsToDeliver = this.activityUnitsToDeliver + increase;
    }

    /** Setter for the service fee of an item. */
    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    /** Getter for the service fee of an item. */
    public double getServiceFee() {
        return serviceFee;
    }
    
    /** Calculate the activity cost of an item taking in a calculation of activity units. */
    public double calculateActivityCost(double activityUnits) {
        return activityUnits * Automail.ACTIVITY_PRICE;
    }
    
    /** Calculate the total cost of an item taking in a calculation of activity units. */
    private double calculateCost(double activityUnits) {
        return serviceFee + calculateActivityCost(activityUnits);
    }
    
    /** Calculate the total charge to deliver an item taking in a calculation of activity units. */
    public double calculateCharge(double activityUnits ) {
    	return calculateCost(activityUnits) * (1 + Automail.MARKUP_PROP);
    }
}
