package automail;

import simulation.Building;
import simulation.IMailDelivery;

public class Automail {
    public static final double ACTIVITY_PRICE = 0.224;
    public static final double MARKUP_PROP = 0.059;
    private static double[] serviceFees;

    public Robot[] robots;
    public MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {  	
    	/* Initialize the MailPool */
    	this.mailPool = mailPool;
    	
    	/* Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool, i);

    	/* Set the initial service fees for each of the floors */
        serviceFees = new double[Building.FLOORS];
        Robot firstRobot = robots[0];  // According to the spec. robots perform the lookup to the modem, so we need one
        for (int floor=1; floor<=Building.FLOORS; floor++) {
            while (firstRobot.attemptSetServiceFee(floor) < 0) { }  // Keep trying until we successfully set the value
        }

    }

    /** Sets a new service fee for a floor */
    public static void setServiceFee(int floor, double fee) {
        Automail.serviceFees[floor-1] = fee;  // Sets the floor-1'th element since the array is 0 indexed
    }

    /** Returns the service fee for a floor */
    public static double getServiceFee(int floor) {
        return Automail.serviceFees[floor-1];  // Returns the floor-1'th element since the array is 0 indexed
    }

}
