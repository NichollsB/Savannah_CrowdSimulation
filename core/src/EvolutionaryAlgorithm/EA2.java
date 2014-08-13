package EvolutionaryAlgorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.utils.Array;


public class EA2 {
    
	//Setup
	private  int popNum = 0;
	private int breedingAge = 4;
	//Crossover rate
	private double crossRate = 0.3;
	//Mutation rate
	private double muteRate =0.02;
	public static int geneLength = 8;
	public boolean eaON = false;

	

	
	public HashMap<Byte, Float[]> heldValues = new HashMap<Byte,Float[]>();
	private ArrayList<Boid> population = new ArrayList<Boid>();
	private ArrayList<Double> fitnessList = new ArrayList<Double>();
	private ArrayList<Double> probabilityList = new ArrayList<Double>();
	private Random r = new Random();
	private Float[] newGene = new Float[geneLength];
	private Float[] gene1 = new Float[geneLength];
	private Float[] gene2 = new Float[geneLength];
	private ArrayList<Float[]> geneList = new ArrayList<Float[]>();
	private ArrayList<Float[]> newGeneList = new ArrayList<Float[]>();
	private Byte currentSpecies = 0;
	public static Byte totalSpecies = 5;
	 
	// private SimulationScreen ss ;
	 
//SimulationScreen ss
	// this.ss = ss;
	 
	 
	 public void setup() {
		 Float[] held = new Float[geneLength];
		
		 for(Byte species = 0; species<totalSpecies; species++){
			 heldValues.put(species,held);
		 }
		
	 }
	 
	
	 
	 
	public void Evolve() {	
		
		
		for(byte i =0 ; i<totalSpecies; i++){
			currentSpecies = i ;
			System.out.println("Species "+ currentSpecies);
			
			validCheck();
			calculateFitness();
			calculateProbabilty();
			
			
			for(int num = 0; num <popNum; num++){
				Float[] tmp = new Float[geneLength];
				System.arraycopy(selection(),0 ,tmp , 0, geneLength);	
				newGeneList.add(tmp);
			}
			System.out.println("Held Values " + Arrays.toString(heldValues.get(currentSpecies)));
			
			for(Float[] gene : newGeneList) {
			System.out.println("NEW GENE " + Arrays.toString(gene));
			}
			
				
				for(int j = 0; j<popNum ; j++){
					population.get(j).setGene(newGeneList.get(j));
				}
				
				reset();
			}
			
		}
	
	

	
	
	
	public Float[] createBaby(Boid parent, Array<Boid> potentialMates) {
		System.out.println("Baby being created");
		System.out.println("PotentialMtes "+ potentialMates);
		population.add(parent);
		popNum++;
		
		for(int i = 0; i < potentialMates.size ; i++){
			Boid temp = potentialMates.get(i);
			population.add(i+1,temp );
			popNum++;
			
		}
		System.out.println("popnum " +popNum);
			calculateFitness();
			calculateProbabilty();
			Float[] tmp = new Float[geneLength];
			System.arraycopy(selection(),0 ,tmp , 0, geneLength);
			reset();
			return tmp;
	}
	
	
	
	
	
	
	
		
	private void reset(){
		System.out.println("Resetting EA parameters");
		population.clear();
		newGeneList.clear();
		fitnessList.clear();
		probabilityList.clear();
		geneList.clear();
		popNum = 0;
	}
	
	//Check for valid population members
	private  void validCheck() {
		for(Boid b : BoidManager.boids){
			if(b.getAge() >= breedingAge && b.getSpecies()==currentSpecies){
				population.add(b);
				//add to population
				popNum++;
				System.out.print(b.getAge()+" ");	
			}
		}
		
		System.out.println();
		
		for(Boid b : population) {
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
		for(double fitness :fitnessList) { 
			totalFitness = totalFitness + fitness;	
		}
		
		for(double val : fitnessList){
			p = val/totalFitness;
			totalProbability = totalProbability + p;
			probabilityList.add(totalProbability);
		}
	}
	

	
	//Selection
	public  Float[] selection() {
		System.out.println();
		System.out.println("SELECTION");
		double rangeMin = probabilityList.get(0);
		double rangeMax = probabilityList.get(popNum-1);
		//Select random number	
		int parent1 = -1;
		int parent2 = -1;
		
		double randomValue1 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		for(int i = 0; i<popNum ; i++){	
			if(probabilityList.get(i) <=randomValue1 && randomValue1<probabilityList.get(i+1)){	
				geneList.add(population.get(i).getGene());
				parent1 = i;
				System.out.println("First Selected =" + i);
			}	
		}		
		do{
			double randomValue2 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			for(int j = 0; j<popNum ; j++){	
				if(probabilityList.get(j) <=randomValue2 && randomValue2<probabilityList.get(j+1)){	
					parent2 = j;
					System.out.println("Selected =" + parent2);
					}			
			}	
		}
		while(parent1==parent2);
		geneList.add(population.get(parent2).getGene());
		System.out.println("Final Selected =" + parent2);
				
		
		gene1 = geneList.get(0);
		gene2 = geneList.get(1);
		
		parent1 = -1;
		parent2 = -1;
		
		System.out.println("Gene 1 " + Arrays.toString(geneList.get(0)));
		System.out.println("Gene 2 " + Arrays.toString(geneList.get(1)));
		crossover(gene1 , gene2);
		
		geneList.clear();
		
		return newGene;
		
	}
	
	
	
	//Crossover
	public Float[] crossover(Float[] gene1, Float[] gene2) {
		System.out.println();
		System.out.println("CROSSOVER");
		double rangeMax = 0;
		double rangeMin = 1;
   
	        for (int i = 0; i < geneLength; i++) {
	            // Crossover
	        	double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	        	//System.out.println("Random" + randomValue);
	            if (randomValue <= crossRate) {
	            	
	             newGene[i] = gene1[i];
	            }
	            else {
	          //   System.out.println("Cross at pos "+i);
	             newGene[i] = gene2[i];
	            }
	        }
	        
	      //  System.out.println("BEFORE " +Arrays.toString( newGene));
	        mutation(newGene);
	      //  System.out.println("AFTER " +Arrays.toString( newGene));
	        return newGene;
        
	}
	

	//Mutation
	public  Float[] mutation(Float[] newGene) {
		System.out.println();
		System.out.println("MUTATION");
		double rangeMax = 0;
		double rangeMin = 1;
		float muteValMin = 0;
		//TODO get original species value
		float muteValMax = 0;
		
		for(int j =0 ; j < geneLength ; j++) {
			System.out.println("Current "+newGene[j]);
			muteValMin = 0.5f*newGene[j];
			muteValMax = 1.5f*newGene[j];
			System.out.println("Max Value "+muteValMax);
			
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			float muteValue = muteValMin + ((muteValMax) - muteValMin) * r.nextFloat();
			
			

				if (randomValue <= muteRate){ 
					newGene[j]= muteValue;
				}
			}
		overwriteHeldValues(newGene);
			return newGene;	
	}
	
	private Float[] overwriteHeldValues(Float[] newGene) {
		Float[] tmp = heldValues.get(currentSpecies);
		
		for(int i = 0 ; i<geneLength ; i++){
			if(tmp[i] != null){
				newGene[i]=tmp[i];
			}
		}
		
		return newGene;
	}
	
	public void setEaOn(Boolean set) {
		eaON = set;
	}
	public boolean getEaOn() {
		return eaON;
	}
	public static int getGeneLength() {
		return geneLength; 
	}
	public static byte getTotalSpecies() {
		return totalSpecies; 
	}
	public void setCrossRate(double newCross){
		crossRate= newCross;
	}
	public double getCrossRate(){
		return crossRate;
	}
	public void setMuteRate(double newMute){
		muteRate= newMute;
	}
	public double getMuteRate(){
		return muteRate;
	}
	
}
