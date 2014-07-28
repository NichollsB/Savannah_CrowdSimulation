package EvolutionaryAlgorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;


public class EA2 {
    
	//Setup
	private  int popNum = 0;
	private int breedingAge = 4;
	//Crossover rate
	private double crossRate = 0.5;
	//Mutation rate
	private double muteRate =0.5;
	private int geneLength = 4;
	private int someconstant = 0;
	public boolean rtmode = false;
	
	
	 private ArrayList<Boid> population = new ArrayList<Boid>();
	 private ArrayList<Double> fitnessList = new ArrayList<Double>();
	 private ArrayList<Double> probablityList = new ArrayList<Double>();
	 private Random r = new Random();
	 private Float[] newGene = new Float[geneLength];
	// private Float[] gene1 = new Float[geneLength];
	// private Float[] gene2 = new Float[geneLength];
	 private ArrayList<Float[]> geneList = new ArrayList<Float[]>();
	 private ArrayList<Float[]> newGeneList = new ArrayList<Float[]>();
	

	
	public void Evolve() {
	
		validCheck();
		calculateFitness();
		calculateProbabilty();
		mode();
		for(int num = 0; num <someconstant; num++){
		//TODO  issues keep gene the same
			
			
			
			Float[] tmp = new Float[geneLength];
			System.arraycopy(selection(),0 ,tmp , 0, geneLength);
			
			
			newGeneList.add(tmp);
			

		
	
		}
		
		for(Float[] gene : newGeneList) {
		System.out.println("NEW GENE " + Arrays.toString(gene));
		}

		for(int i = 0; i<popNum ; i++){
			population.get(i).setGene(newGeneList.get(i));
		}
			
	}
		
		
	
	
	
	
	
	public   void mode() {
		if (rtmode == true) {
			someconstant = 2;
		}
		else {
			// use whole species population
			
			someconstant = popNum;
			System.out.println("someconstant"+someconstant);
		}
	}
	
	
	
	
	//Check for valid population members
	private  void validCheck() {
		
		for(Boid b : BoidManager.boids){
			if(b.getAge() >= breedingAge && b.getSpecies()==1){
				population.add(b);
				//add to population
				popNum++;
				System.out.print(b.getAge()+" ");
				
			}
			
		}System.out.println();
		for(Boid b : population){
			System.out.println(b.getAge());
		}
		System.out.println("popnum " + popNum);
	}

	
	//Calculate fitness
	public  void calculateFitness() {
		double fitness = 0.0;
		
		for(Boid b : population){
			fitness=b.getAge();
			//Add to fitnessList
			fitnessList.add(fitness);	
		}

		
	}
	
	
	public  void calculateProbabilty() {
		double totalFitness = 0.0;
		double p = 0.0;
		double totalProbability = 0.0;
		
		//calculate total fitness
		for(double fitness :fitnessList){ 
			totalFitness = totalFitness + fitness;
			
		}
		
		for(double val : fitnessList){
			p = val/totalFitness;
			totalProbability = totalProbability + p;
			probablityList.add(totalProbability);
		
		}
		
	}
	

	
	//Selection
	public  Float[] selection() {
		System.out.println();
		System.out.println("SELECTION");
		double rangeMin = probablityList.get(0);
		double rangeMax = probablityList.get(popNum-1);
		Float[] gene1 = new Float[geneLength];
		Float[] gene2 = new Float[geneLength];
		
	
		//Select random number
				
		for(int j = 0 ; j<2 ; j++){
		
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

			for(int i = 0; i<popNum ; i++){
		//	
				if(probablityList.get(i) <=randomValue && randomValue<probablityList.get(i+1)){	
					
		
					geneList.add(population.get(i).getGene());
				}
						
			}
			
			
		}
		
		gene1 = geneList.get(0);
		gene2 = geneList.get(1);
		
		System.out.println("Gene 1 " + Arrays.toString(geneList.get(0)));
		System.out.println("Gene 2 " + Arrays.toString(geneList.get(1)));
		crossover(gene1 , gene2);
		
		geneList.clear();
		
		
		return newGene;
		
	}
	
	
	
	//Crossover
	public   Float[] crossover(Float[] gene1, Float[] gene2) {
		System.out.println();
		System.out.println("CROSSOVER");
		double rangeMax = 0;
		double rangeMin = 1;
        
		
		//System.out.println("geneList");
		//for(Float[] gene : geneList) {
			//System.out.println(Arrays.toString(gene));
			
		//}
	        for (int i = 0; i < geneLength; i++) {
	            // Crossover
	        	double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	        	//System.out.println("Random" + randomValue);
	            if (randomValue <= crossRate) {
	            	
	             newGene[i] = gene1[i];
	            }
	            else {
	             System.out.println("Cross at pos "+i);
	             newGene[i] = gene2[i];
	            }
	        }
	        System.out.println("BEFORE " +Arrays.toString( newGene));
	        mutation(newGene);
	       System.out.println("AFTER " +Arrays.toString( newGene));
	        
	       
	        return newGene;
	        
	        
	    }
	
	

	
	
	//Mutation
	public  Float[] mutation(Float[] newGene) {
		System.out.println();
		System.out.println("MUTATION");
		double rangeMax = 0;
		double rangeMin = 1;
		float muteMin = 0;
		float muteMax = 1;
		
		
		// get new gene for mutation
		
		//for(Float[] gene : newGeneList) {
		//	System.out.println(Arrays.toString(gene));
		//}
		
		
		
		//for(int i =0 ; i < newGeneList.size(); i++) {
		//	System.out.println("current " + Arrays.toString(newGeneList.get(i)));	
			
			//Float[] temp = newGeneList.get(i);
			
			for(int j =0 ; j < geneLength ; j++) {
	    
				double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
				// System.out.println("RANDOM " + randomValue);
				float muteValue = muteMin + (muteMax - muteMin) * r.nextFloat();
			//	System.out.println("MUTEVAL " + muteValue);
			//	System.out.println("Current gene" + newGene[j]);
		
				// change statement
				if (randomValue >= muteRate){ 
					newGene[j]= muteValue;
				//	System.out.println("Change at position " + j);	
			
				}
			}
		//System.out.println("new gene " + Arrays.toString(newGene));
		//System.out.println();
		//for(Float[] gene : newGeneList) {
			//System.out.println(Arrays.toString(gene));
		//}
		
		//}
			//System.out.println(Arrays.toString(newGene));
		return newGene;
		
	}
	
	
	
}
