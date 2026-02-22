import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */


public class Grass extends Organism 
{
    // instance variables - replace the example below with your own
    private static final Random rand = Randomizer.getRandom();
    
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    private Counter growthCounter;
    
    
    /**
     * Constructor for objects of class Grass
     */
    public Grass(Location location)
    {
        super( location);
        growthCounter = new Counter("grow");
    }
    
    
    public void act(Field currentField, Field nextFieldState)
    {
        growthCounter.increment();


        
        if(isAlive()) {
         
            List<Location> freeLocations =
                        nextFieldState.getFreeAdjacentLocations(getLocation());
    
            setLocation(getLocation());
            nextFieldState.placeOrganism(this, getLocation()); 
            
            if(freeLocations.size()>=1 && growthCounter.getCount() == 3){        
                Location nextLocation = freeLocations.get(rand.nextInt(freeLocations.size()));
                Grass young = new Grass(nextLocation);
                nextFieldState.placeOrganism(young,nextLocation);
                growthCounter.reset();
            }
            

            }
        }
    
    }
    

