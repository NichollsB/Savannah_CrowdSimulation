package EvolutionaryAlgorithm;


import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.badlogic.gdx.utils.Array;

/**
 * @author M J Odinga
 */

public class EA2 {
    
	//Setup
	private  int popNum = 0;
	private int breedingAge = 4;
	//Crossover rate
	private double crossRate = 0.3;
	//Mutation rate
	private double muteRate = 0.02;
	public static int chromosomeLength = 11;
	public boolean eaON = false;
	
	
	public SimulationManager sm; 
	
	public HashMap<Byte, Float[]> heldValues = new HashMap<Byte,Float[]>();
	private ArrayList<Boid> population = new ArrayList<Boid>();
	private ArrayList<Float> fitnessList = new ArrayList<Float>();
	private ArrayList<Float> probabilityList = new ArrayList<Float>();
	private Random r = new Random();
	private Float[] newChromosome = new Float[chromosomeLength];
	private Float[] chromosome1 = new Float[chromosomeLength];
	private Float[] chromosome2 = new Float[chromosomeLength];
	private ArrayList<Float[]> chromosomeList = new ArrayList<Float[]>();
	private ArrayList<Float[]> newChromosomeList = new ArrayList<Float[]>();
	private Byte currentSpecies = 0;
	public static Byte totalSpecies = 5;
	 
	 /**
	  * Called after creation in SimulationManager to create the heldValue constructs
	  * @param SimulationManager sm
	  */
	
	 public void setup(SimulationManager sm) {
		this.sm=sm;
		 
		 Float[] held = new Float[chromosomeLength];
		
		 for(Byte species = 0; species<totalSpecies; species++){
			 heldValues.put(species,held);
		 }
		
	 }
	 
	 /**
	  * Evolves a whole species at a time for all species
	  */
	 
	public void Evolve() {	
		for(byte i =0 ; i<totalSpecies; i++){
			currentSpecies = i ;
			
			validCheck();
			
			if(1<popNum){
			calculateFitness();
			calculateProbabilty();
			//Generates new chromosome
			for(int num = 0; num <popNum; num++){
				Float[] tmp = new Float[chromosomeLength];
				System.arraycopy(selection(),0 ,tmp , 0, chromosomeLength);	
				newChromosomeList.add(tmp);
			}
			// Replaces the old chromosome with the new
				for(int j = 0; j<popNum ; j++){
					population.get(j).setChromosome(newChromosomeList.get(j));
				}
			}	
				reset();
			}	
		}
	
	/**
	 * Creates the chromosome for the new boid made through the reproduction states
	 * @param parent
	 * @param potentialMates
	 * @return newChromosome
	 */
	
	public Float[] createBaby(Boid parent, Array<Boid> potentialMates) {
		population.add(parent);
		popNum++;
		
		for(int i = 0; i < potentialMates.size ; i++){
			Boid temp = potentialMates.get(i);
			population.add(i+1,temp);
			popNum++;
			
		}
			calculateFitness();
			calculateProbabilty();
			Float[] tmp = new Float[chromosomeLength];
			System.arraycopy(selection(),0 ,tmp , 0, chromosomeLength);
			reset();
			return tmp;
	}
	
	/**
	 * Fully resets the EA called after each species has been run through or after every new boid
	 */

	private void reset(){
		population.clear();
		newChromosomeList.clear();
		fitnessList.clear();
		probabilityList.clear();
		chromosomeList.clear();
		popNum = 0;
	}
	
	/**
	 * Check for valid population members for Evolve() method
	 */
	
	private  void validCheck() {
		for(Boid b : BoidManager.boids){
			if(b.getAge() >= breedingAge && b.getSpecies()==currentSpecies){
				population.add(b);
				
				//add to population
				popNum++;
			}
		}
		
	}

	/**
	 * Calculate fitness values
	 */
	
	private  void calculateFitness() {
		fitnessList.clear();
		float fitness = 0f;
		float lifespan = 0f;
		double y = -1;
		
		//Gets the species. Only done once as method is called for each species.
		
		byte species = population.get(0).getSpecies();
		lifespan = sm.speciesData.get(species).getLifespan();
		double x = (double) lifespan;
		
		for(Boid b : population){
			float age = (float) b.getAge();
		
			if(age<=(lifespan/2)){
				float m = (float) Math.pow(x/2,y);	
				fitness=m*age+b.getNumberOfOffspring();
			
			}
			else if (age>(lifespan/2)){
				float m = (float) Math.pow(x/2,y);	
				m=m*-1f;
				fitness=m*age+b.getNumberOfOffspring();
				
			
			} else{
			}
							
			//Add to fitnessList
			fitnessList.add(fitness);	
		}	
	}
	
	/**
	 * Calculates the probability values from the fitness list
	 */
	
	private  void calculateProbabilty() {
		probabilityList.clear();
		float totalFitness = 0f;
		float p = 0f;
		float totalProbability = 0f;
		probabilityList.add(0f);
		//calculate total fitness
		for(float fitness :fitnessList) { 
			totalFitness = totalFitness + fitness;	
		}
		
		for(float val : fitnessList){
			p = val/totalFitness;
			totalProbability = totalProbability + p;
			probabilityList.add(totalProbability);
		}
	}
	
	/**
	 * Selection selects the boids to reproduce through the roulette wheel method of selection
	 * @return newChromosome
	 */
	private  Float[] selection() {
		//Select random number	
		double rangeMin = probabilityList.get(0);
		Boid parent = null;
		int parentPos = 0;
		
		for(int pairRun = 0 ; pairRun <2 ; pairRun++){
			double rangeMax = probabilityList.get(popNum-1);
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			for(int i = 0; i<popNum ; i++){	
		
				if(probabilityList.get(i) <=randomValue && randomValue<probabilityList.get(i+1)){	
					chromosomeList.add(population.get(i).getChromosome());
					float newNumberOfOffspring = population.get(i).getNumberOfOffspring();
					newNumberOfOffspring = newNumberOfOffspring + 1f;
					population.get(i).setNumberOfOffspring(newNumberOfOffspring);
					if(pairRun==0){
						parent = population.get(i);
						parentPos = i;
						population.remove(i);	
						// Calculates new values with missing boid
						calculateFitness();
						calculateProbabilty();
						//Corrected popNum
						popNum=popNum-1;
					}
				}	
			}
		}		
		//Replaces boid for next use of selection
		population.add(parentPos, parent);
		calculateFitness();
		calculateProbabilty();
		
		//Corrected popNum
		popNum=popNum + 1;

		chromosome1 = chromosomeList.get(0);
		chromosome2 = chromosomeList.get(1);
		
		// Passes parents chromosomes to the crossover() method
		crossover(chromosome1 , chromosome2);
		
		chromosomeList.clear();
		// pass back finished new chromosome
		return newChromosome;
		
	}
	
	/**
	 * Crosses the chromosomes of the parents to form a new chromosome
	 * @param gene1
	 * @param gene2
	 * @return newChromosome
	 */
	
	private Float[] crossover(Float[] chromosome1, Float[] chromosome2) {
		double rangeMax = 0;
		double rangeMin = 1;
   
	        for (int i = 0; i < chromosomeLength; i++) {
	            // Crossover
	        	double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	            if (randomValue <= crossRate) {
	            	
	            	newChromosome[i] = chromosome1[i];
	            }
	            else {
	            	newChromosome[i] = chromosome2[i];
	            }
	        }
	        //Passes new chromosome to the mutation() method
	        mutation(newChromosome);
	       // pass back finished new chromosome
	        return newChromosome;
        
	}
	
	/**
	 * Mutates the new chromosome and passes back up the change
	 * @param newChromosome
	 * @return newChromosome
	 */
	
	private Float[] mutation(Float[] newChromosome) {
		double rangeMax = 0;
		double rangeMin = 1;
		float muteValMin = 0;
		float muteValMax = 0;
		
		// for each gene if the random number is greater than the mutation rate replace with random value
		for(int j =0 ; j < chromosomeLength ; j++) {
	
			muteValMin = 0.7f*newChromosome[j];
			muteValMax = 1.3f*newChromosome[j];
			
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			float muteValue = muteValMin + ((muteValMax) - muteValMin) * r.nextFloat();

				if (randomValue <= muteRate){ 
					newChromosome[j]= muteValue;
				}
			}
			//Passes to the overwriteHeldValues to keep held values the same
			overwriteHeldValues(newChromosome);
			return newChromosome;	
	}
	
	/**
	 * Overwrites the values in the mutation to held values
	 * @param newChromosome
	 * @return newChromosome
	 */
	
	private Float[] overwriteHeldValues(Float[] newChromosome) {
		//Get array for current species
		Float[] tmp = heldValues.get(currentSpecies);
		// Replaces the values if present in Array
		for(int i = 0 ; i<chromosomeLength ; i++){
			if(tmp[i] != null){
				newChromosome[i]=tmp[i];
			}
		}
		// pass back finished new chromosome
		return newChromosome;
	}
	
	/**
	 * Sets the EA status to on or off
	 * @param set
	 */
	
	public void setEaOn(Boolean set) {
		eaON = set;
	}
	
	/**
	 * Returns the current status of the EA on or off corresponding to true and false respectively.
	 * @return eaON
	 */
	
	public boolean getEaOn() {
		return eaON;
	}
	
	/**
	 * Returns the length of the float array which contains values to be evolved
	 * @return chromosomeLength
	 */
	
	public static int getChromosomeLength() {
		return chromosomeLength; 
		
	/**
	* Returns the total number of species in the simulation
	* @return total species
	*/
		
	}
	public static byte getTotalSpecies() {
		return totalSpecies; 
		
	/**
	 * Sets the crossover rate for the EA	
	 */
		
	}
	public void setCrossRate(double newCross){
		crossRate= newCross;
	}
	
	/**
	 * Gets the current value of the EA crossover rate
	 * @return crossRate
	 */
	
	public double getCrossRate(){
		return crossRate;
	}
	
	/**
	 * Set the mutation rate for the EA
	 * @param newMute
	 */
	
	public void setMuteRate(double newMute){
		muteRate= newMute;
	}
	
	/**
	 * Returns the current mutation rate
	 * @return muteRate
	 */
	public double getMuteRate(){
		return muteRate;
	}
	
}
