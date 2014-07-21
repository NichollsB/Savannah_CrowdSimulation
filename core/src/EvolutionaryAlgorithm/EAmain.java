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
	private double crossRate = 0.5;
	//Mutation rate
	private double muteRate =0.02;
	static int geneLength = 8;
	
	//May move this elsewhere...probably to Boid.???
	private float[] genes = new float[geneLength];
	
	private ArrayList<Boid> selected = new ArrayList<Boid>();
	private ArrayList<Boid> population = new ArrayList<Boid>();
	private ArrayList<Double> fValList = new ArrayList<Double>();
	private ArrayList<Double> fitnessList = new ArrayList<Double>();
	private ArrayList<Double> probablityList = new ArrayList<Double>();
	private Random r = new Random();
	
	
	
	//Check for valid population members
	private void validCheck() {
		//TODO 
			//add to population
			popNum++;
		}
	
	
	
	
	
	//Calculate fitness
	public void calculateFitness() {
		double fitness = 0.0;
		//TODO
		
		//Add to fitnessList
	}
	
	
	
	
	public void calculateProbabilty() {
		double totalFitness = 0.0;
		double f = 0.0;
		double p = 0.0;
		double totalProbability = 0.0;
	
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
		double rangeMin = probablityList.get(0);
		double rangeMax = probablityList.get(popNum);
		boolean found = false;
		//Find two different boids
		
		//Select random number
		while(found == false){
		
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
			//Find boids
			
			for (int i = 0; i==popNum ; i++){
				if(probablityList.get(i) <=randomValue && randomValue<probablityList.get(i+1)){	
					//TODO
					//select boid
					selected.add(population.get(i));
				}
				else{
					System.out.println("random value not in probability list range");
				}
			}
		
			if(selected.get(0)==selected.get(1)){
				found=true;
			}
			else{
				selected.remove(1);
			}
		
		}
	}
	
	
	
	
	//Crossover
	public void crossover() {
		
		
		
		
	   
	        // Loop through genes
	       // for (int i = 0; i < indiv1.size(); i++) {
	            // Crossover
	         //   if (Math.random() <= crossRate) {
	          //      newChromosome.setGene(i, getGene(i));
	          //  } else {
	          //      newChromosome.setGene(i, indiv2.getGene(i));
	          //  }
	       // }
	      
	}
	
	
	
	
	
	
	
	//Mutation
	public void mutation() {
		//loop throught genes
		//if greater mute
		
		
	   // for (int i = 0; i < indiv.size(); i++) {
           // if (Math.random() <= mutationRate) {
                // Create random gene
           //     byte gene = (byte) Math.round(Math.random());
           //     indiv.setGene(i, gene);
         //   }
      //  }
		
		
	}
	
	//create new boid with new parameters
	
}
