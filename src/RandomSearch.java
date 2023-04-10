/**
 * The baseline method for comparison is a random generation method
 * @author Danai Mitsopoulou
 * @version 1.0
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class RandomSearch {

    static ArrayList<String> actions;
    static ArrayList<String> locations;
    static ArrayList<String> characters;
    static ArrayList<String> items;
    static ArrayList<String> enemies;
    static ArrayList<ArrayList<String>> children;
    static ArrayList<ArrayList<String>> parents;
    static ArrayList<Double> fitness;
    static ArrayList<ArrayList<Double>> survivors;



    public RandomSearch() {
        actions.add("goto" );
        actions.add("talkto");
        actions.add("fight");
        actions.add("read");
        actions.add("pickup");
        actions.add("drop");
        actions.add("use");
    }

    public static void evoAlgorithm(){
        children = new ArrayList<>();
        parents = new ArrayList<>();
        fitness = new ArrayList<>();
        survivors = new ArrayList<>();


    }

    /**
     * one method to initialise the population randomly
     * Creates Population out of random quests
     */


    /**
     * One method to evaluate quests
     * Runs fitness function on all quests of a list and puts it in a fitness list
     */

    /**
     * One method for selection, probably tournament
     * Picks out random quests and chooses the best one to be potential parent
     */

    /**
     * one method to produce the children from the parents selected above
     * Produces two children by Crossing over two parents
     */

    /**
     * Method for mutation
     *  Swapping part? mutating one field?
     */


    /**
     * Method to select survivors for the next generation
     * in this instance, the population will be remade
     */

    /**
     * Sets up new generation
     */

    /**
     * For the purpose of collecting data, returns the generation number and the best fitness
     *
     * maybe only collect data of the best fitness at the end of the run
     */


    //take elements from v1 of salesman problem
}
