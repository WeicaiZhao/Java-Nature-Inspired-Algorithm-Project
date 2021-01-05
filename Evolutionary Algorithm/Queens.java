import java.lang.Math;
import java.util.*;

/* YOU NEED TO ADD YOUR CODE TO THIS CLASS, REMOVING ALL DUMMY CODE
 *
 * DO NOT CHANGE THE NAME OR SIGNATURE OF ANY OF THE EXISTING METHODS
 * (Signature includes parameter types, return types and being static)
 *
 * You can add private methods to this class if it makes your code cleaner,
 * but this class MUST work with the UNMODIFIED Tester.java class.
 *
 * This is the ONLY class that you can submit for your assignment.
 *
 * MH 2020
 */
public class Queens
{
    private static int boardSize = 10;

    // creates a valid genotype with random values
    public static Integer [] randomGenotype()
    {
        Integer [] genotype = new Integer [boardSize];

        // YOUR CODE GOES HERE
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 1; i <= boardSize; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        for (int i = 0; i < boardSize; i++) {
            genotype[i] = positions.get(i);
        }
        // END OF YOUR CODE

        return genotype;
    }

    // swaps 2 genes in the genotype
    // the swap happens with probability p, so if p = 0.8
    // then 8 out of 10 times this method is called, a swap happens
    public static Integer[] mutate(Integer[] genotype, double p)
    {
        // YOUR CODE GOES HERE
        if (Math.random() < p) {
            int position1 = (int) (Math.random() * (double) boardSize);
            int position2 = (int) (Math.random() * (double) boardSize);
            while (position1 == position2) {
                position2 = (int) (Math.random() * (double) boardSize);
            }

            int temp = genotype[position1];
            genotype[position1] = genotype[position2];
            genotype[position2] = temp;
        }
        // END OF YOUR CODE

        return genotype;
    }

    // creates 2 child genotypes using the 'cut-and-crossfill' method
    public static Integer[][] crossover(Integer[] parent0, Integer[] parent1)
    {
        Integer [][] children = new Integer [2][boardSize];

        // YOUR CODE GOES HERE
        children[0] = createChildren(parent0, parent1);
        children[1] = createChildren(parent1, parent0);
        // END OF YOUR CODE

        return children;
    }

    private static Integer[] createChildren(Integer[] parent0, Integer[] parent1) {
        Integer[] result = new Integer[boardSize];

        for (int i = 0; i < boardSize / 2; i++) {
            result[i] = parent0[i];
        }

        int index = boardSize / 2;
        for (int i = boardSize / 2; i < boardSize; i++) {
            boolean duplicated = true;
            while (duplicated) {
                result[i] = parent1[index];
                index = (index + 1) % boardSize;
                duplicated = false;
                for (int j = 0; j < boardSize / 2; j++) {
                    if (result[i].equals(result[j])) {
                        duplicated = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    // calculates the fitness of an individual
    public static int measureFitness(Integer [] genotype)
    {
        /* The initial fitness is the maximum pairs of queens
         * that can be in check (all possible pairs in check).
         * So we are using it as the maximum fitness value.
         * We deduct 1 from this value for every pair of queens
         * found to be in check.
         * So, the lower the score, the lower the fitness.
         * For a 10x10 board the maximum fitness is 45 (no checks),
         * and the minimum fitness is 0 (all queens in a line).
         */
        int fitness = (int) (0.5 * boardSize * (boardSize - 1));

        // YOUR CODE GOES HERE
        Coordinate[] coordinates = new Coordinate[boardSize];
        for (int i = 0; i < boardSize; i++) {
            coordinates[i] = new Coordinate(i + 1, genotype[i]);
        }

        int[][] directions = new int[][]{
                new int[]{1, 1},
                new int[]{1, -1},
                new int[]{-1, 1},
                new int[]{-1, -1}
        };
        for (int i = 0; i < boardSize; i++) {
            for (int[] direction : directions) {
                int newX = coordinates[i].getX() + direction[0];
                int newY = coordinates[i].getY() + direction[1];
                while (newX > 0 && newY > 0 && newX <= boardSize && newY <= boardSize) {
                    for (int j = i; j < boardSize; j++) {
                        if (newX == coordinates[j].getX() && newY == coordinates[j].getY()) {
                            fitness--;
                        }
                    }
                    newX += direction[0];
                    newY += direction[1];
                }
            }
        }

        return fitness;
    }
}
