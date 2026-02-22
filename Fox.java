import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
 
    private static int health  = 100;
    // Individual characteristics (instance fields).

    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

 


    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param location The location within the field.
     */
    public Fox(Location location, boolean randomAge)
    {
        super( location,  BREEDING_AGE,  MAX_AGE,  BREEDING_PROBABILITY, MAX_LITTER_SIZE, FOOD_VALUE, randomAge,health);
        
        
    }
    
    @Override
    public String toString() {
        return "Fox{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }
    
    @Override
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations,Field currentField)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed(currentField);
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Fox young = new Fox(loc,true);
                nextFieldState.placeOrganism(young, loc);    
            }
        }
    }
    
    
    @Override   
    public Location findFood(Field field){
        
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Organism organism = field.getOrganismAt(loc);
            if(organism instanceof Rabbit rabbit) {
                if(rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = FOOD_VALUE;
                    foodLocation = loc;
                    }
                }
            }
        return foodLocation;

    } 

}

