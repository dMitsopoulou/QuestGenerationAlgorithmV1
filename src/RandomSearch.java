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

    static int QUEST_SIZE = 12;
    static int POPULATION_SIZE = 20;
    static Random random_method;
    static ArrayList<Integer> actions;
    static ArrayList<Location> locations;
    static ArrayList<Integer> characters;
    static ArrayList<Integer> items;
    static ArrayList<Integer> enemies;
    static ArrayList<Location> chosenLocations;
    static ArrayList<Integer> chosenCharacters;
    static ArrayList<ArrayList<ArrayList<Integer>>> population;
    static ArrayList<ArrayList<ArrayList<Integer>>> children;
    static ArrayList<ArrayList<ArrayList<Integer>>> parents;
    static ArrayList<Double> fitness;
    static ArrayList<ArrayList<ArrayList<Integer>>> survivors;



    public RandomSearch() {
        random_method = new Random();

        actions.add(101);   //goto
        actions.add(102);   //talk to
        actions.add(103);   //Fight
        actions.add(104);   //Read
        actions.add(105);   //pickup
        actions.add(106);   //drop
        actions.add(107);   //use

        //change to key - value pair map when you can
        locations.add(new Location(201));
        locations.add(new Location(202));
        locations.add(new Location(203));
        locations.add(new Location(204));
        locations.add(new Location(205));
        locations.add(new Location(206));


/*
        locations.add(201); //Novac
        locations.add(202); //Goodsprings
        locations.add(203); //Camp McCarran
        locations.add(204); //New Vegas strip
        locations.add(205); //Jacobstown
        locations.add(206); //Nellis AFB

        characters.add(11);
        characters.add(12);
        characters.add(21);
        characters.add(22);
        characters.add(31);
        characters.add(32);
        characters.add(41);
        characters.add(42);
        characters.add(51);
        characters.add(52);
        characters.add(61);
        characters.add(62);

 */

        items.add(301);
        items.add(302);
        items.add(303);
        items.add(304);
        items.add(305);
        items.add(306);
        items.add(307);
        items.add(308);
        items.add(309);
        items.add(310);

        enemies.add(501);   //Super mutants
        enemies.add(502);   //Robots
        enemies.add(503);   //Feral ghouls
    }

    public static void evoAlgorithm(){
        children = new ArrayList<>();
        parents = new ArrayList<>();
        fitness = new ArrayList<>();
        survivors = new ArrayList<>();


    }


    public static ArrayList<ArrayList<Integer>> randomQuest(){
        ArrayList<Integer> task = new ArrayList<>();
        ArrayList<ArrayList<Integer>> quest = new ArrayList<>();

        chooseElements();   //chooses locations, characters, and enemies to pop into the tasks
        for (int i=0; i< QUEST_SIZE;i++){
           task.add(actions.get(random_method.nextInt()));  //adds actions in the quest
            quest.add(task);
            task.clear();
        }



        chooseElements();

        quest.add(task);
        return quest;

    }

    private static void chooseElements() {
        //fill locations array, characters array, items array

        //locations - 2
        for (int i=0; i<2; i++){
            chosenLocations.add(locations.get(random_method.nextInt()));  //gets one random location
            chosenCharacters.add(chosenLocations.get(chosenLocations.size()-1).getCharacter(random_method.nextInt()));  //gets one random character from each location
        }
    }


    /**
     * one method to initialise the population randomly
     * Creates Population out of random quests
     */
    public static void initializePopulation(){
        population = new ArrayList<>();
        for (int i=0; i< POPULATION_SIZE;i++){population.add(randomQuest());}    //make method to generate random quest }
    }

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
