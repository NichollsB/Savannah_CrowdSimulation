package com.UKC_AICS.simulation.managers;


/**
 *
 * @author Emily
 */
public class SimulationManager extends Manager{
    
    static final BoidManager boidManager = new BoidManager();
    static final WorldManager worldManager = new WorldManager();
    static public int minutes = 0;
    static public int hours = 0;
    static public int days = 0;
    static public int weeks = 0;
    
    
    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * 
     * 
     * Possibly store the data lookup tables here? like species data for example
     * 
     * 
     */
    public void SimulationManager() {
        
    }
    
    public void update() {
        boidManager.update();
        worldManager.update();
        tick();
    }

    private void tick() {
        if(minutes < 59) {
            minutes += 1;
        }
        else if (hours < 23) {
            minutes = 0;
            hours += 1;
        }
        else if (days < 6) {
            hours = 0;
            days += 1;
        }
        else {
            days = 0;
            weeks += 1;
        }
//        System.out.println(minutes + " mins; " + hours + " hrs; " + days + " days; " + weeks + " wks.");
    }
    
}
