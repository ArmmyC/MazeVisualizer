package cpe231;

import java.io.FileNotFoundException;

import cpe231.algorithm.AStar;
import cpe231.algorithm.Bidirectional;
import cpe231.algorithm.GeneticAlgorithm;
import cpe231.algorithm.MazeContext;
import cpe231.algorithm.MazeStrategy;
import cpe231.helper.MazeResult;
import cpe231.maze.Maze;

public class testAlgorithm {

        public static void runWarmUp() throws FileNotFoundException {
                MazeStrategy<Integer> strategy1 = new Bidirectional();
                MazeContext<Integer> manager = new MazeContext<>(strategy1);

                Maze myMaze = new Maze("src/main/resources/cpe231/maze/m60_60.txt");

                MazeResult result = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

                MazeStrategy<Integer> strategy2 = new AStar();
                manager.setStrategy(strategy2);
                MazeResult result2 = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

                MazeStrategy<Integer> strategy3 = new GeneticAlgorithm();
                manager.setStrategy(strategy3);
                MazeResult result3 = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

        }

        public static void main(String[] args) throws FileNotFoundException {

                MazeStrategy<Integer> strategy = new Bidirectional();
                MazeContext<Integer> manager = new MazeContext<>(strategy);

                Maze myMaze = new Maze("src/main/resources/cpe231/maze/m100_100.txt");
                myMaze.printMaze();

                MazeResult result = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

                MazeStrategy<Integer> strategy2 = new AStar();

                manager.setStrategy(strategy2);

                MazeResult result2 = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

                MazeStrategy<Integer> strategy3 = new GeneticAlgorithm(1000, 0.10, 0.80, 100, 500);
                manager.setStrategy(strategy3);

                MazeResult result3 = manager.executeMaze(
                                myMaze.getGrid(),
                                myMaze.getStart(),
                                myMaze.getEnd());

                System.out.println("\n-----------------");
                System.out.println("Total Cost: " + result.getTotalWeight());
                System.out.println("Path: " + result.getPath());

                System.out.println("\n-----------------");
                System.out.println("Total Cost: " + result2.getTotalWeight());
                System.out.println("Path: " + result2.getPath());

                System.out.println("\n-----------------");
                System.out.println("Total Cost: " + result3.getTotalWeight());
                System.out.println("Path: " + result3.getPath());

        }
}
