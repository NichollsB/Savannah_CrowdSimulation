package EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;

public class EAmain {

	//Setup
	private static int popNum = 0;
	static private int breedingAge = 4;
	//Crossover rate
	private static double crossRate = 0.5;
	//Mutation rate
	private static double muteRate =0.5;
	static int geneLength = 4;
	private static int someconstant = 0;
	public static boolean rtmode = true;
	
	private static ArrayList<Boid> selected = new ArrayList<Boid>();
	private static ArrayList<Boid> population = new ArrayList<Boid>();
	private static ArrayList<Double> fitnessList = new ArrayList<Double>();
	private static ArrayList<Double> probablityList = new ArrayList<Double>();
	private static Random r = new Random();
	private static Float[] newGene = new Float[geneLength];
	private static ArrayList<Float[]> geneList = new ArrayList<Float[]>();
	
	
	
	
	
	
	
	
	
	
	public static void Evolve() {
		mode();
		validCheck();
		calculateFitness();
		calculateProbabilty();
		for(int num = 0; num <someconstant; num++){
		selection();
		}
		crossover();
		mutation();
		clearAllLists();
	}
	
	
	
	
	
	
	
	
	
	
	public static void mode() {
		if (rtmode == true) {
			someconstant = 2;
		}
		else {
			// use whole species population
			
			someconstant = popNum;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//Check for valid population members
	private static void validCheck() {
		
		for(Boid b : BoidManager.boids){
			if(b.getAge() >= breedingAge && b.getSpecies()==1){
				population.add(b);
				//add to population
				popNum++;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	//Any
	
	
	
	//Calculate fitness
	public static void calculateFitness() {
		double fitness = 0.0;
		
		for(Boid b : population){
			fitness=b.getAge();
			//Add to fitnessList
			fitnessList.add(fitness);	
		}
		System.out.println("fitnessList");
		System.out.println(fitnessList);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//Any
	
	public static void calculateProbabilty() {
		double totalFitness = 0.0;
		double p = 0.0;
		double totalProbability = 0.0;
		
		//calculate total fitness
		for(double fitness :fitnessList){ 
			totalFitness = totalFitness + fitness;
			
		}
		System.out.println(totalFitness);
		System.out.println();
		System.out.println("prob");
		for(double val : fitnessList){
			p = val/totalFitness;
			totalProbability = totalProbability + p;
			probablityList.add(totalProbability);
			System.out.print(totalProbability+" ");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Selection
	public static void selection() {
		System.out.println();
		System.out.println("SELECTION");
		double rangeMin = probablityList.get(0);
		double rangeMax = probablityList.get(popNum-1);
		boolean found = false;
		//Find two different boids
		System.out.println("MIN" +rangeMin);
		System.out.println("MAX" +rangeMax);
		//Select random number
	
		//TODO mode selection must be capable of selecting 2 individuals when running in real time
		// must evolve entire population when in fast mode
		
		
		// 
		
		
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			System.out.println();
			System.out.println("random "+ randomValue);
			//Find boids
			
			for(int i = 0; i<popNum ; i++){
				System.out.println("i VAL = "+i);
				if(probablityList.get(i) <=randomValue && randomValue<probablityList.get(i+1)){	
					
					System.out.println("SELECTED");
					System.out.println(population.get(i));
					selected.add(population.get(i));
				}
				else{
					System.out.println("random value not in probability range");
				}
				
			}
			System.out.println();
			System.out.println("SELECTED");
			System.out.println(selected);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Crossover
	public static void crossover() {
		double rangeMax = 0;
		double rangeMin = 1;
		for(Boid b : selected) {
			
		geneList.add(b.getGene());
		
		}
		System.out.println("geneList");
		for(Float[] gene : geneList) {
			System.out.println(Arrays.toString(gene));
		}
	        for (int i = 0; i < geneLength; i++) {
	            // Crossover
	        	double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	        	System.out.println("Random" + randomValue);
	            if (randomValue <= crossRate) {
	            	Float[] gene1 = geneList.get(0);
	             newGene[i] = gene1[i];
	            }
	            else {
	             Float[] gene2 = geneList.get(1);
	             newGene[i] = gene2[i];
	            }
	        }
	        System.out.println(Arrays.toString(newGene));
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Mutation
	public static void mutation() {
		double rangeMax = 0;
		double rangeMin = 1;
		float muteMin = 0;
		float muteMax = 1;
		
	
		
	    for (int i = 0; i < geneLength; i++) {
	    double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		float muteValue = muteMin + (muteMax - muteMin) * r.nextFloat();
		System.out.println("Random"+randomValue);
		System.out.println("MuteVal"+muteValue);
            if (randomValue <= muteRate) {
                // Create random gene
            	
            	newGene[i] = muteValue;
            }
        } 
	    System.out.println(Arrays.toString(newGene));
	}
	
	//create new boid with new parameters
	
	
	public static void clearAllLists() {
		selected.clear();
		population.clear();
		fitnessList.clear();
		probablityList.clear();
		geneList.clear();
		popNum = 0;
	
	}
}
