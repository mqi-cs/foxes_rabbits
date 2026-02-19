import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */





public class Grass extends Animal
{
    // instance variables - replace the example below with your own
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Grass
     */
    public Grass(Location location)
    {
        super(location);
    }
    
    
    public void act(Field currentField, Field nextFieldState)
    {
 
        if(isAlive()) {
 
            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());

            setLocation(getLocation());
            nextFieldState.placeAnimal(this, getLocation()); 
                    
            if(freeLocations.size()>=1){        
                Location nextLocation = freeLocations.get(rand.nextInt(freeLocations.size()));
                Grass young = new Grass(nextLocation);
                nextFieldState.placeAnimal(young,nextLocation);
            }
            

            }

        }
    
    }
    

