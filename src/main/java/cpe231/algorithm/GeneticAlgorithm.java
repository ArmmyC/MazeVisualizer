package cpe231.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set; // Import เพิ่มเติมสำหรับ HashSet

import cpe231.helper.MazeResult;
import cpe231.maze.Block;

public class GeneticAlgorithm implements MazeStrategy<Integer> {

    // Parameters for Genetic Algorithm
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int maxGenerations;
    private int maxMoves;

    // Parameters for Optimization
    private final int MAX_NO_IMPROVEMENT_GENS = 50; // Early stopping threshold
    private final double MIN_MUTATION_RATE = 0.005; // Minimum mutation rate

    // Possible Direction
    // (-1,0) : left
    // (1,0) : right
    // (0,-1) : down
    // (0,1) : up
    private static final int[][] DIRS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private final Random random = new Random();

    // Constructor w/ custom parameter
    public GeneticAlgorithm(int popSize, double mutRate, double crossRate, int generations, int maxPathLength) {
        this.populationSize = popSize;
        this.mutationRate = mutRate;
        this.crossoverRate = crossRate;
        this.maxGenerations = generations;
        this.maxMoves = maxPathLength;
    }

    // Default constructor
    public GeneticAlgorithm() {
        this(200, 0.05, 0.7, 1000, -1);
    }

    // Inner class to represent one "Individual" (One Path)
    private class Individual implements Comparable<Individual> {
        int[] genes;
        double fitness;
        int pathCost;
        List<Block> validPath;

        public Individual(int chromosomeLength) {
            genes = new int[chromosomeLength];
            // Random initialization
            for (int i = 0; i < genes.length; i++) {
                genes[i] = random.nextInt(4);
            }
        }

        // Higher fitness is better
        @Override
        public int compareTo(Individual other) {
            return Double.compare(other.fitness, this.fitness); // Descending order
        }
    }

    @Override
    public MazeResult findShortestPath(Integer[][] maze, Block start, Block end) {

        // Start Time and Tracking
        long startTime = System.nanoTime();
        int stepsCount = 0;

        int rows = maze.length;
        int cols = maze[0].length;

        // Dynamic maxMoves calculation if set to -1
        if (maxMoves == -1) {
            int minDistance = Math.abs(start.getRow() - end.getRow()) + Math.abs(start.getCol() - end.getCol());
            // ตั้ง maxMoves ให้ยาวพอสมควร
            this.maxMoves = Math.max(minDistance * 4, rows * cols / 2);
        }

        // Initialize Population
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(maxMoves));
        }

        Individual bestSolution = null;
        double lastBestFitness = Double.NEGATIVE_INFINITY; // สำหรับ Early Stopping
        int generationsWithoutImprovement = 0; // สำหรับ Early Stopping

        // Evolution Loop
        for (int gen = 0; gen < maxGenerations; gen++) {

            // Calculate Fitness for everyone
            for (Individual ind : population) {
                calculateFitness(ind, maze, start, end);
                stepsCount++; // นับ Evaluation Step

                // Track the best global solution
                if (bestSolution == null || ind.fitness > bestSolution.fitness) {
                    // Deep copy the best solution to prevent mutation/crossover affecting it
                    bestSolution = cloneIndividual(ind);
                }
            }

            // --- Optimization: Early Stopping Check ---
            if (bestSolution != null && bestSolution.fitness > lastBestFitness) {
                lastBestFitness = bestSolution.fitness;
                generationsWithoutImprovement = 0;
            } else {
                generationsWithoutImprovement++;
            }

            if (generationsWithoutImprovement >= MAX_NO_IMPROVEMENT_GENS) {
                System.out.println("Early stopping at Generation " + gen +
                        " due to no improvement for " + MAX_NO_IMPROVEMENT_GENS + " gens.");
                break;
            }

            // Create Next Generation
            List<Individual> newPopulation = new ArrayList<>();

            // Elitism: Keep the single best individual from previous gen
            if (bestSolution != null) {
                newPopulation.add(cloneIndividual(bestSolution));
            }

            while (newPopulation.size() < populationSize) {
                // Selection (Tournament)
                Individual parent1 = tournamentSelection(population);
                Individual parent2 = tournamentSelection(population);

                // Crossover
                Individual child = crossover(parent1, parent2);

                // Mutation (ใช้ Adaptive Mutation)
                adaptiveMutate(child, gen);

                newPopulation.add(child);
            }

            population = newPopulation;

            // Optional: Print progress
            if (gen % 100 == 0 && bestSolution != null) {
                System.out.println("Gen " + gen + " Best Fitness: " + String.format("%.2f", bestSolution.fitness) +
                        ", Cost: " + bestSolution.pathCost +
                        ", Path Length: " + (bestSolution.validPath != null ? bestSolution.validPath.size() : 0));
            }
        }

        // Return Result
        long executionTimeNs = System.nanoTime() - startTime;

        if (bestSolution != null && bestSolution.validPath != null && bestSolution.validPath.size() > 0) {
            // Found a path, return the best one
            return new MazeResult(bestSolution.validPath,
                    bestSolution.pathCost,
                    executionTimeNs,
                    stepsCount);
        } else {
            // No decent path found after all generations
            return new MazeResult(Collections.emptyList(), -1, executionTimeNs, stepsCount);
        }
    }

    // --- Helper Methods ---

    private Individual cloneIndividual(Individual original) {
        Individual clone = new Individual(original.genes.length);
        System.arraycopy(original.genes, 0, clone.genes, 0, original.genes.length);
        clone.fitness = original.fitness;
        clone.pathCost = original.pathCost;

        if (original.validPath != null) {
            // Deep copy of the path list
            clone.validPath = new LinkedList<>(original.validPath);
        } else {
            clone.validPath = new LinkedList<>();
        }
        return clone;
    }

    private void calculateFitness(Individual ind, Integer[][] maze, Block start, Block end) {
        int r = start.getRow();
        int c = start.getCol();
        int totalWeightCost = 0; // Actual cost accumulated from moving.

        // Use List<Block> instead of List<String>
        List<Block> pathBlocks = new LinkedList<>();
        pathBlocks.add(new Block(r, c, maze[r][c])); // Add start block

        boolean hitGoal = false;
        int stepsTaken = 0;

        for (int move : ind.genes) {
            int nr = r + DIRS[move][0];
            int nc = c + DIRS[move][1];

            // Check if valid move
            if (isValid(nr, nc, maze)) {
                r = nr;
                c = nc;
                int weight = maze[r][c]; // Weight of the new block

                totalWeightCost += weight;
                pathBlocks.add(new Block(r, c, weight));
                stepsTaken++;

                if (r == end.getRow() && c == end.getCol()) {
                    hitGoal = true;
                    break; // Stop simulating if we hit goal
                }
            } else {
                // If hit a wall, path cost does not increase, and we stay put.
            }
        }

        ind.pathCost = totalWeightCost;
        ind.validPath = pathBlocks;

        // FITNESS FORMULA (Optimized)
        // 1. Distance to goal (Manhattan)
        int distToGoal = Math.abs(r - end.getRow()) + Math.abs(c - end.getCol());

        // Base score higher if closer to goal
        double score = 1000.0 / (distToGoal + 1);

        // Huge Bonus for hitting the goal
        if (hitGoal) {
            score += 10000.0;
            // Minimize weight
            score -= totalWeightCost;
            // Minimize steps (Penalize long, winding paths)
            score -= stepsTaken * 0.1;
        } else {
            // Penalize paths that are too long but didn't reach the goal
            score -= stepsTaken * 0.05;

            // --- OPTIMIZATION: Penalty for inefficient movement (Revisiting blocks) ---
            Set<Block> uniqueBlocks = new HashSet<>(pathBlocks);
            int visitedCellsCount = uniqueBlocks.size();
            // ลงโทษการเดินซ้ำ (stepsTaken > uniqueBlocks)
            score -= (stepsTaken - visitedCellsCount) * 1.0;
            // --------------------------------------------------------------------------
        }

        ind.fitness = score;
    }

    private Individual tournamentSelection(List<Individual> pop) {
        int tournamentSize = 5;
        Individual best = null;
        for (int i = 0; i < tournamentSize; i++) {
            if (pop.isEmpty())
                return null;
            Individual randInd = pop.get(random.nextInt(pop.size()));
            if (best == null || randInd.fitness > best.fitness) {
                best = randInd;
            }
        }
        return best != null ? cloneIndividual(best) : null;
    }

    private Individual crossover(Individual p1, Individual p2) {
        if (p1 == null || p2 == null) {
            return new Individual(maxMoves);
        }

        Individual child = new Individual(maxMoves);

        // Single point crossover
        if (random.nextDouble() < crossoverRate) {
            int midpoint = random.nextInt(maxMoves);
            for (int i = 0; i < maxMoves; i++) {
                if (i < midpoint)
                    child.genes[i] = p1.genes[i];
                else
                    child.genes[i] = p2.genes[i];
            }
        } else {
            // No crossover, just clone p1
            System.arraycopy(p1.genes, 0, child.genes, 0, maxMoves);
        }
        return child;
    }

    // --- Optimization: Adaptive Mutation Method ---
    private void adaptiveMutate(Individual ind, int currentGen) {
        // ลด mutationRate ตามสัดส่วนของ Generations ที่รันไปแล้ว
        double currentMutationRate = this.mutationRate * (1.0 - (double) currentGen / this.maxGenerations);

        // ตรวจสอบขั้นต่ำ
        if (currentMutationRate < MIN_MUTATION_RATE) {
            currentMutationRate = MIN_MUTATION_RATE;
        }

        for (int i = 0; i < maxMoves; i++) {
            if (random.nextDouble() < currentMutationRate) {
                ind.genes[i] = random.nextInt(4); // New random direction
            }
        }
    }

    private boolean isValid(int r, int c, Integer[][] maze) {
        return r >= 0 && r < maze.length && c >= 0 && c < maze[0].length && maze[r][c] != -1;
    }

    // --- Setter Methods ---
    public void setDefault() {
        this.populationSize = 200;
        this.mutationRate = 0.05;
        this.crossoverRate = 0.7;
        this.maxGenerations = 1000;
        this.maxMoves = -1;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public void setMaxMoves(int maxMoves) {
        this.maxMoves = maxMoves;
    }
    // ----------------------
}