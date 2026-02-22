import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).
    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    private static final int FOOD_VALUE = 9;

    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;

    private int foodLevel;

    private static  int health = 50;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param location The location within the field.
     */
    public Rabbit(Location location,boolean randomAge)
    {
        super( location,  BREEDING_AGE,  MAX_AGE,  BREEDING_PROBABILITY, MAX_LITTER_SIZE, FOOD_VALUE, randomAge,health);

    }
    
   

    @Override
    public String toString() {
        return "Rabbit{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
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
                Rabbit young = new Rabbit(loc,true);
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
            if(organism instanceof Grass grass) {
                if(grass.isAlive()) {
                    grass.setDead();
                    foodLevel = FOOD_VALUE;
                    foodLocation = loc;
                    }
                }
            }
        return foodLocation;
    }     
    
}

