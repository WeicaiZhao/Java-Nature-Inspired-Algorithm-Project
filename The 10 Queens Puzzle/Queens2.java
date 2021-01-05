import java.lang.Math;
import java.util.*;

/* YOU NEED TO ADD YOUR CODE TO THIS CLASS, REMOVING ALL DUMMY CODE
 *
 * DO NOT CHANGE THE NAME OR SIGNATURE OF ANY OF THE EXISTING METHODS
 * (Signature includes parameter types, return types and being static)
 *
 * You can add private methods to this class if it makes your code cleaner,
 * but this class MUST work with the UNMODIFIED Tester2.java class.
 *
 * This is the ONLY class that you can submit for your assignment.
 *
 * MH 2020
 */
public class Queens2
{
    private static int boardSize = 10;
    
    // inverts the order of a series of genes in the genotype
    public static Integer[] inversionMutate(Integer[] genotype, double p)
    {
        // YOUR CODE GOES HERE
        if (Math.random() <= p) {
            // DUMMY CODE TO REMOVE: (pretends to invert first test genotype)
            int length = genotype.length;
            int start, end;
            do {
                start = (int) (length * Math.random());
                end = (int) (length * Math.random());
            } while (start >= end);

            while (start < end) {
                int temp = genotype[start];
                genotype[start] = genotype[end];
                genotype[end] = temp;
                start++;
                end--;
            }
        }
        // END OF YOUR CODE
        
        return genotype;
    }
    
    /* performs fitness-proportional parent selection
     * also known as 'roulette wheel' selection
     * selects two parents that are different to each other
     */
    public static Integer[][] rouletteSelect(Integer[][] population)
    {
        Integer [][] parents = new Integer [2][boardSize];

        // YOUR CODE GOES HERE
        int totalFitness = 0;
        int[] fitnessRange = new int[population.length];
        for (int i = 0; i < population.length; i++) {
            totalFitness += Queens.measureFitness(population[i]);
            fitnessRange[i] = totalFitness;
        }
        do {
            parents[0] = selectParent(population, fitnessRange, totalFitness);
            parents[1] = selectParent(population, fitnessRange, totalFitness);
        } while (parents[0] == parents[1]);
        // END OF YOUR CODE
        
        return parents;
    }

    private static Integer[] selectParent(Integer[][] population, int[] fitnessRange, int totalFitness) {
        double position = Math.random() * totalFitness;
        for (int i = 0; i < fitnessRange.length; i++) {
            if (position < fitnessRange[i]) {
                return population[i];
            }
        }
        return population[0];
    }
    
    /* creates a new population through λ + μ survivor selection
     * given a population of size n, and a set of children of size m
     * this method will measure the fitness of all individual in the
     * combined population, and return the n fittest individuals
     * as the new population
     */
    public static Integer[][] survivorSelection(Integer[][] population, Integer [][] children)
    {
        Integer [][] newPop = new Integer [population.length][boardSize];
        
        // YOUR CODE GOES HERE
        Arrays.sort(population, (gen1, gen2) -> Integer.compare(Queens.measureFitness(gen2), Queens.measureFitness(gen1)));
        Arrays.sort(children, (gen1, gen2) -> Integer.compare(Queens.measureFitness(gen2), Queens.measureFitness(gen1)));
        int index1 = 0, index2 = 0;
        for (int i=0; i<newPop.length; i++) {
            if (Queens.measureFitness(population[index1]) >= Queens.measureFitness(children[index2])) {
                newPop[i] = population[index1];
                index1++;
            } else {
                newPop[i] = children[index2];
                index2++;
            }
        }
        // END OF YOUR CODE

        return newPop;
    }
    
    // counts the number of unique genotypes in the population
    public static int genoDiversity(Integer[][] population)
    {
        int uniqueTypes = 0;
        
        // YOUR CODE GOES HERE
        Set<String> genotypeSet = new HashSet<>();
        for (Integer[] genotype : population) {
            String genotypeStr = Arrays.toString(genotype);
            if (genotypeSet.contains(genotypeStr)) continue;
            genotypeSet.add(genotypeStr);
            uniqueTypes++;
        }
        // END OF YOUR CODE
        
        return uniqueTypes;
    }
}
