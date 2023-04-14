/**
 * The baseline method for comparison is a random generation method
 * @author Danai Mitsopoulou
 * @version 1.0
 */

import java.util.ArrayList;
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
    static ArrayList<Integer> readables;
    static ArrayList<Location> chosenLocations;
    static ArrayList<Integer> chosenCharacters;
    static int chosenItem;
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
        //actions.add(107);   //use

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
        items.add(303);
        items.add(306);
        items.add(307);
        items.add(308);

        readables.add(302);
        readables.add(304);
        readables.add(305);

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


    /**
     * one method to initialise the population randomly
     * Creates Population out of random quests
     */
    public static void initializePopulation(){
        population = new ArrayList<>();
        for (int i=0; i< POPULATION_SIZE;i++){population.add(randomQuest());}    //make method to generate random quest
    }

    public static ArrayList<ArrayList<Integer>> randomQuest(){
        ArrayList<Integer> task = new ArrayList<>();
        ArrayList<ArrayList<Integer>> quest = new ArrayList<>();

        //choose elements to use
        chooseElements();   //chooses locations, characters, and enemies to pop into the tasks
        //fill  quests with random actions in tasks
        for (int i=0; i< QUEST_SIZE;i++){
            task.add(actions.get(random_method.nextInt(actions.size())));  //adds action in the task
            quest.add(task);        //adds task to quest
            task.clear();           //clears task
        }
        //populate tasks with chosen elements
        fillTasks(quest);
        //adds completed quest to population
        return quest;
    }

    private static void fillTasks(ArrayList<ArrayList<Integer>> quest) {
        ArrayList<Integer> task;
        int currentLoc = 0;
        int currentChar;

        for(int i=0; i<quest.size(); i++){
            task = quest.get(i);  //gets task from quest

            if (task.get(0) == 101){    //goto
                //go to a location you are not already in
                for (Location loc : chosenLocations) {
                   if(loc.getLocationName() != currentLoc) {    //if this is not current location, go to that
                       task.add(loc.getLocationName());
                       break;
                   }
                }
                /*
                Location loc = chosenLocations.get(random_method.nextInt(locations.size()));
                task.add(loc.getLocationName());
                currentLoc = loc.getLocationName(); //saves the last location used, as the player is supposed to be at that location now

                 */
            } else if (task.get(0) == 102) {    //talk to
                // go find a character that is in the last location
                if (currentLoc == 0){            //if a location task has not come beforehand, find random character
                    currentChar = chosenCharacters.get(random_method.nextInt(chosenCharacters.size()));
                    task.add(currentChar);      //add character to task

                    String charToInt = String.valueOf(currentChar);
                    currentLoc = Integer.parseInt(charToInt.substring(0,3));     //sets the latest location where that character is

                } else {        //a location has come right before, get a character from that specific location
                   String charToLoc;
                    /*for (Location loc : chosenLocations){
                        if(currentLoc == loc.getLocationName()){

                        }
                    }

                     */
                    for (int character: chosenCharacters) {     //access chosen characters and fetch one with the desired location name
                       charToLoc = String.valueOf(character).substring(0, 3);
                       if(currentLoc == Integer.parseInt(charToLoc)){
                           task.add(character);     //add character to task
                           break;
                       }
                    }
                }
            } else if (task.get(0) == 103) {    //fight
                task.add(enemies.get(random_method.nextInt()));
            } else if (task.get(0) == 104) {    //read
                task.add(readables.get(random_method.nextInt()));
            } else if (task.get(0) == 105) {    //pickup
                task.add(chosenItem);
            } else if (task.get(0) == 106) {    //drop
                task.add(chosenItem);
            }  else  //else if (task.get(0) == 107) {    //use
                System.out.println("How on earth...????");
        }
    }

    private static void chooseElements() {
        // 2 locations & 2 characters - 1 from each location
        for (int i=0; i<2; i++){
            chosenLocations.add(locations.get(random_method.nextInt(locations.size())));  //gets one random location
            chosenCharacters.add(chosenLocations.get(chosenLocations.size()-1).getRandomCharacter());  //gets one random character from each location
        }
        chosenItem = items.get(random_method.nextInt()); //one random item
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
