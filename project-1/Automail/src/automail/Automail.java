package automail;

import simulation.Building;
import simulation.IMailDelivery;

public class Automail {
    public static final double ACTIVITY_PRICE = 0.224;
    public static final double MARKUP_PROP = 0.059;

    public Robot[] robots;
    public MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {  	
    	/* Initialize the MailPool */
    	this.mailPool = mailPool;
    	
    	/* Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool, i);

    	/* Set the initial service fees for each of the floors */
        Robot firstRobot = robots[0];  // According to the spec. robots perform the lookup to the modem, so we need one
        for (int floor=1; floor<=Building.FLOORS; floor++) {
            while (Robot.attemptSetServiceFee(floor) < 0) { }  // Keep trying until we successfully set the value
        }
    }
}
