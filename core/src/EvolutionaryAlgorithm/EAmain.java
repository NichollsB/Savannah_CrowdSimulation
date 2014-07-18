package EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;

public class EAmain {

	//Setup
	private int popNum = 0;
	private int breedingAge;
	//Crossover rate
	private float crossRate;
	//Mutation rate
	private float muteRate;
	
	
	
	
	private ArrayList<Boid> population = new ArrayList<Boid>();
	private ArrayList<Double> fValList = new ArrayList<Double>();
	private ArrayList<Double> fitnessList = new ArrayList<Double>();
	private ArrayList<Double> probablityList = new ArrayList<Double>();
	private Random r = new Random();
	//Check for valid population members
	
	private void validCheck() {
		if(b.getAge()>breedingAge && same species && same herd){
			//add to population
			popNum++;
		}
	}
	//Add to population
	
	
	
	
	//Calculate fitness
	public void calculateFitness() {
		double fitness = 0.0;
	//Get average thirst over lifetime
	//Get average hunger over lifetime
	//Get average panic level over lifetime
	//Get average rest over lifetime
	//Get injuries and other status effects	
		
		//Add to fitnessList
	}
	
	public void calculateProbabilty() {
		double totalFitness = 0.0;
		double f = 0.0;
		double p = 0.0;
		double totalProbability = 0.0;
		double probability;
		//calculate total fitness
		for(double fitness :fitnessList){ 
			f=1/(1+fitness);
			totalFitness = totalFitness + fitness;
			fValList.add(f);
		}
		
		for(double val : fValList){
			p = val/totalFitness;
			totalProbability = totalProbability + p;
			probablityList.add(p);
		}
	}
	
	//Selection
	public void selection() {
		double rangeMin = 0.0;
		double rangeMax = 1.0;
		ArrayList<Double> cVal = new ArrayList<Double>();
		
	}
	//Crossover
	public void crossover() {
		
	}
	//Mutation
	public void mutation() {
		
	}
	
	//create new boid with new parameters
	
}
