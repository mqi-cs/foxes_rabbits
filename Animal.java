import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Common elements of foxes and rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal extends Organism
{

    private Gender gender;

    private static  Random rand = Randomizer.getRandom();
    
    private static  int BREEDING_AGE;
    // The age to which a fox can live.
    private static  int MAX_AGE;
    // The likelihood of a fox breeding.
    private static  double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static  int MAX_LITTER_SIZE;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static  int FOOD_VALUE;
    
    
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    protected int health;  // NOT final

    private boolean diseaseFlag = false;
    
    private Counter diseaseCounter;    
    
    
    
    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Location location, int BREEDING_AGE, int MAX_AGE, double BREEDING_PROBABILITY,int MAX_LITTER_SIZE,int FOOD_VALUE,boolean randomAge,int health)
    {
    
        super(location);
        
        if(rand.nextBoolean()){
            this.gender = Gender.MALE;
        }
        else{
            this.gender = Gender.FEMALE;
        }
        
        this.BREEDING_AGE = BREEDING_AGE;
        this.MAX_AGE = MAX_AGE;
        this.BREEDING_PROBABILITY = BREEDING_PROBABILITY;
        this.MAX_LITTER_SIZE = MAX_LITTER_SIZE;
        this.FOOD_VALUE = FOOD_VALUE;
        this.health = health;
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
        foodLevel = rand.nextInt(FOOD_VALUE);
        
        diseaseCounter = new Counter("disease");
    }
    
    public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
       
            disease();

            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());
            if(! freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations,currentField);
            }
            // Move towards a source of food if found.
            Location nextLocation = findFood(currentField);
            if(nextLocation == null && ! freeLocations.isEmpty()) {
                // No food found - try to move to a free location.
                nextLocation = freeLocations.remove(0);

            }
            // See if it was possible to move.
            if(nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeOrganism(this, nextLocation);
                disease();

            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    public Gender getGender(){
        return gender;
    }
    
    
    

    
    
    public void disease(){
        
        double diseaseProbability = 0.09;
        
        if(rand.nextDouble()<= diseaseProbability){
            diseaseFlag = true;
        }
        
        if(diseaseFlag){
            diseaseCounter.increment();
            if(diseaseCounter.getCount() == 3){
                health = health - 30;
                diseaseCounter.reset();
                if(health <=0){
                    setDead();
                }
            }
            
        }
         
    }    
    
      /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field)
    {
        return null;
    }    
        
    
   /**
     * Check whether this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations,Field currentField)
    {}
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed(Field currentField)
    {
        int births;
        if(canBreed(currentField) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());

        for(Location newLocation : adjacent){ 
        
            Organism currentOrganism = field.getOrganismAt(newLocation);
            

            if(currentOrganism instanceof Animal){
                
                Animal currentAnimal = (Animal) currentOrganism;
                currentAnimal.disease();
                
                if(currentAnimal instanceof Animal && currentAnimal.getGender() != getGender() ){
        
                    return age >= BREEDING_AGE;
                }   
                
            }
        
        }
        
        return false;
    }
    
}    

    
  