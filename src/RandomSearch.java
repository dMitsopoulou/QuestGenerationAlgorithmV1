/**
 * The baseline method for comparison is a random generation method
 * @author Danai Mitsopoulou
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class RandomSearch {

    static int GENERATION_NUMBER = 50;
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
    static ArrayList<Integer> fitness;
    static ArrayList<Integer> taskAvg;
    static ArrayList<ArrayList<ArrayList<Integer>>> survivors;

    static int fitnessSum;
    static int maxValue;
    static int minValue;


    /**
     * Constructor
     */
    public RandomSearch() {
        random_method = new Random();
        actions = new ArrayList<>();
        locations = new ArrayList<>();
        items = new ArrayList<>();
        readables = new ArrayList<>();
        enemies = new ArrayList<>();

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

        taskAvg.add(4);   //goto
        taskAvg.add(2);   //talk to
        taskAvg.add(3);   //fight
        taskAvg.add(2);   //read
        taskAvg.add(2);   //pickup
        taskAvg.add(2);   //drop


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



        evoAlgorithm();
    }

    /**
     * Evolutionary algorithm main body
     */
    public static void evoAlgorithm(){

        int currentGen = 1;

        children = new ArrayList<>();
        parents = new ArrayList<>();
        fitness = new ArrayList<>();
        survivors = new ArrayList<>();
        chosenCharacters = new ArrayList<>();
        chosenLocations = new ArrayList<>();

        while (currentGen < GENERATION_NUMBER){
            initializePopulation();
            fitnessFunction();

            if (currentGen == 1){
                maxValue = fitness.get(0);
                minValue = fitness.get(0);
                fitnessSum =0;
            }
            collectData();
            currentGen++;
        }


    }


    /**
     * one method to initialise the population randomly
     * Creates Population out of random quests
     */
    public static void initializePopulation(){
        population = new ArrayList<>();
        for (int i=0; i< POPULATION_SIZE;i++){population.add(randomQuest());}    //make method to generate random quest
        //System.out.println("heyyyy");
    }

    /**
     *  Makes a randomly generated quest
     * @return the quest
     */
    public static ArrayList<ArrayList<Integer>> randomQuest(){
        ArrayList<Integer> task; //= new ArrayList<>();
        ArrayList<ArrayList<Integer>> quest = new ArrayList<>();

        //choose elements to use
        chooseElements();   //chooses locations, characters, and enemies to pop into the tasks
        //fill  quests with random actions in tasks
        for (int i=0; i< QUEST_SIZE;i++){
            task = new ArrayList<>();
            task.add(actions.get(random_method.nextInt(actions.size())));  //adds action in the task
            quest.add(task);        //adds task to quest
        }

        /*
        for (int i=0; i< QUEST_SIZE;i++){
            //task = new ArrayList<>();
            task.add(actions.get(random_method.nextInt(actions.size())));  //adds action in the task
            quest.add(task);        //adds task to quest
            //task.clear();           //clears task
        }

         */
        //populate tasks with chosen elements
        fillTasks(quest);
        //adds completed quest to population
        return quest;
    }

    /**
     * Fills the tasks of the formulated quest based on the actions already in there
     * @param quest the completed quest now populated with tasks
     * @return quest the completed quest now populated with tasks
     */
    private static ArrayList<ArrayList<Integer>> fillTasks(ArrayList<ArrayList<Integer>> quest) {
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
                task.add(enemies.get(random_method.nextInt(enemies.size())));
            } else if (task.get(0) == 104) {    //read
                task.add(readables.get(random_method.nextInt(readables.size())));
            } else if (task.get(0) == 105) {    //pickup
                task.add(chosenItem);
            } else if (task.get(0) == 106) {    //drop
                task.add(chosenItem);
            }  else  //else if (task.get(0) == 107) {    //use
                System.out.println("How on earth...????");
        }

        return quest;
    }

    /**
     * Chooses the elements to populate the actions with
     */
    private static void chooseElements() {
        // 2 locations & 2 characters - 1 from each location
        for (int i=0; i<2; i++){
            chosenLocations.add(locations.get(random_method.nextInt(locations.size())));  //gets one random location
            chosenCharacters.add(chosenLocations.get(chosenLocations.size()-1).getRandomCharacter());  //gets one random character from each location
        }
        chosenItem = items.get(random_method.nextInt(items.size())); //one random item
    }


    /**
     * Fitness function
     * puts the result for each quest on a fitness arrayList
     */
    public static void fitnessFunction(){
        int theFit;
        for (ArrayList<ArrayList<Integer>> quest : population) {
            theFit = evaluateQuest(quest);
            fitness.add(theFit);
        }
    }


    /**
     *  Evaluates a quest based on
     *  <ul>
     *      <li>Number of actions of each type</li>
     *      <li>Whether there are chains of identical actions</li>
     *      <li>Whether a drop comes before a pickup</li>
     *  </ul>
     *  When something undesirable is seen, there is a penalty on the fitness value of the quest
     * @param forEval quest for evaluation
     * @return evaluation
     */
    public static int evaluateQuest(ArrayList<ArrayList<Integer>> forEval) {
        int fitness = 0;
        ArrayList<Integer> taskCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

        //counts number of tasks in whole quest
        for (ArrayList<Integer> candidate: forEval){
           if( candidate.get(0) == 101) { taskCounts.add(0, taskCounts.get(0) + 1); //add 1 to count
           } else if (candidate.get(0) == 102) { taskCounts.add(1, taskCounts.get(0) + 1);      //increment the corresponding element
           } else if (candidate.get(0) == 103) { taskCounts.add(2, taskCounts.get(0) + 1);
           } else if (candidate.get(0) == 104) { taskCounts.add(3, taskCounts.get(0) + 1);
           } else if (candidate.get(0) == 105) { taskCounts.add(4, taskCounts.get(0) + 1);
           } else if (candidate.get(0) == 106) { taskCounts.add(5, taskCounts.get(0) + 1);
           } else System.out.println("houston we have a problem in task counting");
        }
        //penalise score if number of tasks is +-x different from expected
        //+-2 for first 3, 0 for the last 3
        for (int i = 0; i<=taskCounts.size(); i++) {    //taskAvg and task counts have the same sizes
            if(i < 3 && Math.abs(taskCounts.get(i)-taskAvg.get(i)) > 2){
              fitness = fitness + Math.abs(Math.abs(taskCounts.get(i)-taskAvg.get(i)) - 2) ;    //for every additional task of this kind beyond +-2, a point into fitness gets added
           } else if (i > 3 && Math.abs(taskCounts.get(i)-taskAvg.get(i)) > 0) {
                fitness = fitness + Math.abs(taskCounts.get(i)-taskAvg.get(i)) ;
            } else System.out.println("Something's going on in absolute calculation");
        }

        //calculate streak of continuous tasks
        fitness = longestChain(forEval, fitness);


        int pickup = -1;
        int drop = -1;
        // is there a drop before a pickup?
        for (ArrayList<Integer> task : forEval) {
            if (pickup == -1 || drop == -1) {
                if (task.get(0) == 105) {
                    pickup = forEval.indexOf(task);
                } else if (task.get(0) == 106) {
                    drop = forEval.indexOf(task);
                } else System.out.println("Fatal error in drop / pickup tracking");
            } else break;
        }
        if (drop < pickup){
           fitness =  fitness + Math.abs(drop-pickup);
        } else System.out.println("error in drop/ pickup fitness calculation");


        return fitness;
    }

    /**
     * Checks the quest for streaks of actions
     * @param forEval quest to be evaluated
     * @param fitness fitness value after checking for streaks of actions
     * @return
     */
    private static int longestChain(ArrayList<ArrayList<Integer>> forEval, int fitness ) {
        ArrayList<Integer> max = new ArrayList<>(Arrays.asList(0,0,0,0,0,0));
        //for each task in quest

        for (int i = 0; i < forEval.size(); i++) {
            int count = 0;
            int actionA = forEval.get(i).get(0);

            for (int j = i; j < forEval.size(); j++) {
                int actionB = forEval.get(i).get(0);
                //now find out what kind of action it is
                if (actionA == actionB) { count++;
                } else break;
            }
            //when done counting, exit inner loop and compare action streak against existing
            if (actionA == 101 && count > max.get(0)){ max.add(0, count);
            } else if (actionA == 102 && count > max.get(1)) { max.add(1, count);
            } else if (actionA == 103 && count > max.get(2)) { max.add(2, count);
            } else if (actionA == 104 && count > max.get(3)) { max.add(3, count);
            } else if (actionA == 105 && count > max.get(4)) { max.add(4, count);
            } else if (actionA == 106 && count > max.get(5)) { max.add(5, count);
            } else System.out.println("something wrong in streak counting");
        }

        //no streaks are allowed, except fight & read
        // for every extra action, beyond 1, add 1 to the fitness
        fitness = fitness + max.get(0) + max.get(1) + max.get(4) + max.get(5) - 4;


        return fitness;
    }


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
     * Get Best, Average and worst values of fitness
     */
    private static void collectData(){
        for (Integer fitValue: fitness) {
            fitnessSum =+ fitValue;
            if(fitValue < minValue){
                minValue = fitValue;
            } else if (fitValue > maxValue) {
                maxValue = fitValue;
            } else System.out.println("Problem in data collecting");
        }

    }


    //take elements from v1 of salesman problem
}
