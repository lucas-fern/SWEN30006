package automail;

import simulation.IMailDelivery;

public class Automail {
    public static final double ACTIVITY_PRICE = 0.224;
    public static final double MARKUP_PROP = 0.059;

    public Robot[] robots;
    public MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool, i);
    }
    
}
