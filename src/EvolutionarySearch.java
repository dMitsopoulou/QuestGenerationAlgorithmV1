/**
 * The evolutionary search gets compared to the baseline method
 * @author Danai Mitsopoulou
 * @version 1.0
 */

import java.util.*;


public class EvolutionarySearch {


    static int GENERATION_NUMBER = 50;
    static int QUEST_SIZE = 12;
    static int POPULATION_SIZE = 100;
    static Random random_method;
    static ArrayList<Integer> actions;
    static ArrayList<Integer> locations;
    static ArrayList<ArrayList<Integer>> characters;
    static ArrayList<Integer> items;
    static ArrayList<Integer> enemies;
    static ArrayList<Integer> readables;
    static ArrayList<Integer> chosenLocations;
    static ArrayList<Integer> chosenCharacters;
    static int chosenItem;
    static ArrayList<ArrayList<ArrayList<Integer>>> population;
    static ArrayList<ArrayList<ArrayList<Integer>>> children;
    static ArrayList<ArrayList<ArrayList<Integer>>> candidates;
    static ArrayList<Integer> candidateFitness;
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
    public EvolutionarySearch() {
        random_method = new Random();
        actions = new ArrayList<>();
        locations = new ArrayList<>();
        characters = new ArrayList<>();
        items = new ArrayList<>();
        readables = new ArrayList<>();
        enemies = new ArrayList<>();
        taskAvg = new ArrayList<>(Arrays.asList(4,2,3,2,2,2));


        actions.add(101);   //goto
        actions.add(102);   //talk to
        actions.add(103);   //Fight
        actions.add(104);   //Read
        actions.add(105);   //pickup
        actions.add(106);   //drop
        //actions.add(107);   //use

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

        locations.add(201); //Novac
        locations.add(202); //Goodsprings
        locations.add(203); //Camp McCarran
        locations.add(204); //New Vegas strip
        locations.add(205); //Jacobstown
        locations.add(206); //Nellis AFB

        characters.add((new ArrayList<Integer>(Arrays.asList(2011, 2012)))); //one list for the chars of each location
        characters.add((new ArrayList<Integer>(Arrays.asList(2021, 2022))));
        characters.add((new ArrayList<Integer>(Arrays.asList(2031, 2032))));
        characters.add((new ArrayList<Integer>(Arrays.asList(2041, 2042))));
        characters.add((new ArrayList<Integer>(Arrays.asList(2051, 2052))));
        characters.add((new ArrayList<Integer>(Arrays.asList(2061, 2062))));

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
        survivors = new ArrayList<>();

        chooseElements();   //chooses locations, characters, and enemies to pop into the tasks

        //testQuests(); //test quests for fitness
        //evaluateQuest(randomQuest());     //test randomly generated quest for fitness

        initializePopulation();
        fitnessFunction();

        while (currentGen <= GENERATION_NUMBER){
            System.out.println("Current generation: " + currentGen);

            //setup for data collection
            if (currentGen == 1){
                maxValue = fitness.get(20);
                minValue = fitness.get(20);
                fitnessSum =0;
            }
            tournamentSelection(3,80);
            int pairsOfChildren = 50; //input half the number of children required 50*2=100 children
            for(int i =1; i<=pairsOfChildren;i++){ recombination(); } //produces two children

            survivorSelection();
            collectData();
            currentGen++;
            if (currentGen != GENERATION_NUMBER){ nextGenSetup(); } // do not setup new generation on the last gen
        }
        for (ArrayList<ArrayList<Integer>> quest: population) { fillTasks(quest); }  //fill last quests made

        int fitnessAvg = fitnessSum / (GENERATION_NUMBER * POPULATION_SIZE);
        System.out.println("Minimum fitness:" + minValue + "Maximum fitness:" + maxValue + "Average:" +fitnessAvg);

    }



    /**
     * Insert human authored quests for testing fitness
     */
    private static void testQuests() {
        ArrayList<ArrayList<Integer>> testQuest1 = new ArrayList<>();
        //ArrayList<ArrayList<Integer>> testQuest2 = new ArrayList<>();

        testQuest1.add((new ArrayList<Integer>(Arrays.asList(104, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(106, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(102, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(101, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(102, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(106, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(104, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(105, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(101, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(101, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(106, 304))));
        testQuest1.add((new ArrayList<Integer>(Arrays.asList(105, 304))));

        evaluateQuest(testQuest1);

    }

    /**
     * one method to initialise the population randomly
     * Creates Population out of random quests
     */
    public static void initializePopulation(){
        population = new ArrayList<>();
        for (int i=0; i< POPULATION_SIZE;i++){population.add(randomQuest());}    //make method to generate random quest
    }

    /**
     *  Makes a randomly generated quest
     * @return the quest
     */
    public static ArrayList<ArrayList<Integer>> randomQuest(){
        ArrayList<Integer> task;
        ArrayList<ArrayList<Integer>> quest = new ArrayList<>();

        //fill  quests with random actions in tasks
        for (int i=0; i< QUEST_SIZE;i++){
            task = new ArrayList<>();
            task.add(actions.get(random_method.nextInt(actions.size())));  //adds action in the task
            quest.add(task);        //adds task to quest
        }
        //populate tasks with chosen elements
        //fillTasks(quest); only fill in tasks at the last gen to make results look *pretty*
        //adds completed quest to population
        return quest;
    }

    /**
     * Fills the tasks of the formulated quest based on the actions already in there
     * @param quest the completed quest now populated with tasks
     * @return quest the completed quest now populated with tasks
     */
    private static void fillTasks(ArrayList<ArrayList<Integer>> quest) {
        ArrayList<Integer> task;
        int currentLoc = 0;
        int currentChar;

        for(int i=0; i<quest.size(); i++){
            task = quest.get(i);  //gets task from quest

            if (task.get(0) == 101){    //goto
                //go to a location you are not already in
                for (Integer loc : chosenLocations) {
                    if(loc != currentLoc) {    //if this is not current location, go to that
                        task.add(loc);
                        currentLoc= loc;
                        break;
                    }
                }
            } else if (task.get(0) == 102) {    //talk to
                // go find a character that is in the current location
                if (currentLoc == 0){           //if a location task has not come beforehand, find random character
                    currentChar = chosenCharacters.get(random_method.nextInt(chosenCharacters.size()));
                    task.add(currentChar);      //add character to task

                    String charToInt = String.valueOf(currentChar);
                    currentLoc = Integer.parseInt(charToInt.substring(0,3));     //sets the latest location where that character is

                } else {        //a location has come right before, get a character from that specific location
                    String charToLoc;

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
            } //System.out.println("How on earth...????");
        }
    }

    /**
     * Chooses the elements to populate the actions with
     */
    private static void chooseElements() {
        // 2 locations & 2 characters - 1 from each location
        for (int i=0; i<2; i++){
            chosenLocations.add(locations.get(random_method.nextInt(locations.size())));  //gets one random location

        }
        while (Objects.equals(chosenLocations.get(0), chosenLocations.get(1))){
            chosenLocations.set(0, locations.get(random_method.nextInt(locations.size())));  //find another location for index 0
        }

        for (int i=0; i<chosenLocations.size(); i++){
            if (chosenLocations.get(i) == 201){chosenCharacters.add(characters.get(0).get(random_method.nextInt(characters.get(0).size()))); //get one character from location
            } else if (chosenLocations.get(i) == 202) { chosenCharacters.add(characters.get(1).get(random_method.nextInt(characters.get(1).size())));
            } else if (chosenLocations.get(i) == 203) { chosenCharacters.add(characters.get(2).get(random_method.nextInt(characters.get(2).size())));
            } else if (chosenLocations.get(i) == 204) { chosenCharacters.add(characters.get(3).get(random_method.nextInt(characters.get(3).size())));
            } else if (chosenLocations.get(i) == 205) { chosenCharacters.add(characters.get(4).get(random_method.nextInt(characters.get(4).size())));
            } else if (chosenLocations.get(i) == 206) { chosenCharacters.add(characters.get(5).get(random_method.nextInt(characters.get(5).size())));
            } else System.out.println("now that is not a location");

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
        //System.out.println(theFit);
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
            if( candidate.get(0) == 101) { taskCounts.set(0, taskCounts.get(0) + 1); //add 1 to count
            } else if (candidate.get(0) == 102) { taskCounts.set(1, taskCounts.get(1) + 1);      //increment the corresponding element
            } else if (candidate.get(0) == 103) { taskCounts.set(2, taskCounts.get(2) + 1);
            } else if (candidate.get(0) == 104) { taskCounts.set(3, taskCounts.get(3) + 1);
            } else if (candidate.get(0) == 105) { taskCounts.set(4, taskCounts.get(4) + 1);
            } else if (candidate.get(0) == 106) { taskCounts.set(5, taskCounts.get(5) + 1);
            } else System.out.println("houston we have a problem in task counting");
        }
        //penalise score if number of tasks is +-x different from expected
        //+-2 for first 3, 0 for the last 3
        for (int i = 0; i < taskCounts.size(); i++) {    //taskAvg and task counts have the same sizes
            if(i < 3 && Math.abs(taskCounts.get(i)-taskAvg.get(i)) > 2){
                fitness = fitness + Math.abs(Math.abs(taskCounts.get(i)-taskAvg.get(i)) - 2) ;    //for every additional task of this kind beyond +-2, a point into fitness gets added
            } else if (i > 3 && Math.abs(taskCounts.get(i)-taskAvg.get(i)) > 0) {
                fitness = fitness + Math.abs(taskCounts.get(i)-taskAvg.get(i)) ;
            } else System.out.println("case with normal task counts");
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
                } else System.out.println("action we don't care about in pickup tracking");
            } else break;
        }
        if(pickup == -1 && drop!=-1){
            //there is no pickup, have to penalise nonetheless
            fitness =  fitness + Math.abs(drop-1);
        } else if (pickup != -1 && drop == -1) {
            //player picks up somthing but never drops it, no need to penalise
            System.out.println("player picks up somthing but never drops it, no need to penalise");
        } else if (drop < pickup) {
            fitness =  fitness + Math.abs(drop-(pickup+1));
        } else System.out.println("drop comes after pickup, all good!");

        //System.out.println(fitness);
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
                int actionB = forEval.get(j).get(0);
                //now find out what kind of action it is
                if (actionA == actionB) { count++;
                } else break;
            }
            //when done counting elements of current streak, exit inner loop and compare action streak against existing
            if (actionA == 101 && count > max.get(0)){ max.add(0, count);
            } else if (actionA == 102 && count > max.get(1)) { max.set(1, count);
            } else if (actionA == 103 && count > max.get(2)) { max.set(2, count);
            } else if (actionA == 104 && count > max.get(3)) { max.set(3, count);
            } else if (actionA == 105 && count > max.get(4)) { max.set(4, count);
            } else if (actionA == 106 && count > max.get(5)) { max.set(5, count);
            } else System.out.println("streak count not changed");
        }

        //no streaks are allowed, except fight & read
        // for every extra action, beyond 1, add 1 to the fitness
        if(max.get(0)>= 1){
            max.set(0, max.get(0)-1);
        }
        if(max.get(1)>= 1){
            max.set(1, max.get(1)-1);
        }
        if(max.get(4)>= 1){
            max.set(4, max.get(4)-1);
        }
        if(max.get(5)>= 1){
            max.set(5, max.get(5)-1);
        }

        fitness = fitness + max.get(0) + max.get(1) + max.get(4) + max.get(5);

        return fitness;
    }


    /**
     * Picks out random tours and chooses the best one to be potential parent
     * Result is in parents ArrayList
     * @param tournamentSize the size of each tournament
     * @param parentSize the final number of parents
     */
    public static void tournamentSelection(int tournamentSize, int parentSize){
        candidates = new ArrayList<>(); //candidates that will compete
        candidateFitness = new ArrayList<>();
        int randNum;
        int bestFitness;
        ArrayList<ArrayList<Integer>> bestCandidate;

        for(int j=0; j<parentSize;j++) {
            //fill up tournament candidate arrayList
            for (int i = 0; i < tournamentSize; i++) {
                randNum = random_method.nextInt(population.size());
                //get random candidates and their fitness
                candidates.add(population.get(randNum));
                candidateFitness.add(fitness.get(randNum)); //ATTENTION
            }
            // select parent out of that arrayList
            bestCandidate = candidates.get(1);
            bestFitness = candidateFitness.get(1);

            for (int i = 0; i < candidateFitness.size(); i++) {
                if (candidateFitness.get(i) < bestFitness) { //minimising fitness
                    bestFitness = candidateFitness.get(i);
                    bestCandidate = candidates.get(i);
                }
            }
            parents.add(bestCandidate); //adds the best one as a parent

            //delete all entries in arrayLists
            candidates.clear();
            candidateFitness.clear();
        }
    }

    /**
     * one method to produce the children from the parents selected above
     * Produces two children by Crossing over two parents over a single point
     */
    private static void recombination() {
        ArrayList<ArrayList<Integer>> child1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> child2 = new ArrayList<>();

        //gets two random parents
        ArrayList<ArrayList<Integer>> parentOne = parents.get(random_method.nextInt(parents.size()-1));
        ArrayList<ArrayList<Integer>> parentTwo = parents.get(random_method.nextInt(parents.size()-1));

        int cut = random_method.nextInt(parentOne.size()-2);

        //first part of child one
        for (int i = 0; i<=cut; i++) { child1.add(parentOne.get(i));}

        //first part of child 2
        for (int i = 0; i<=cut; i++) { child2.add(parentTwo.get(i)); }

        //second part of child one
        for (int i = cut+1; i < parents.get(1).size(); i++){ child1.add(parentTwo.get(i)); }

        //second part of child 2
        for (int i = cut+1; i < parents.get(0).size(); i++){ child2.add(parentOne.get(i)); }

        children.add(child1);
        children.add(child2);
    }

    /**
     * Method for mutation
     * Swapping part? mutating one field?
     */
    public static void fieldMutation(){
        int randNum;
        for (ArrayList<ArrayList<Integer>> child : children) {
            randNum = random_method.nextInt(100); //gets random number from 0-99
            if(randNum < 20){
                children.set(children.indexOf(child), mutateField(child)); //replace child with mutated child
            }
        }
    }

    private static ArrayList<ArrayList<Integer>> mutateField(ArrayList<ArrayList<Integer>> child) {
        int randNum;

        for (ArrayList<Integer> task : child) {
            randNum = random_method.nextInt(101); //gets random number from 0-100
            //mutate each task by probability
            if(randNum <=20){
               task.set(child.indexOf(task), actions.get(random_method.nextInt(actions.size())));
            }
        }
        return child;
    }

    /**
     * Method to select survivors for the next generation
     * in this instance, the population will be remade
     */

    public static void survivorSelection(){
        //TODO: calculate average of generation
            //survivors have a fitness >= than average
        int genFitnessAvg = 0;
        for (int i=0; i< fitness.size(); i++) { genFitnessAvg = fitnessSum + fitness.get(i); }
        genFitnessAvg = genFitnessAvg / fitness.size();

        //select survivors from population
        for (Integer num: fitness) { if (num > genFitnessAvg) { survivors.add(population.get(fitness.indexOf(num))); } }

        //fill the rest of the survivors with the children
        for(int i = 0; i< POPULATION_SIZE - survivors.size(); i++){ survivors.add(children.get(random_method.nextInt(children.size()))); }

    }

    /**
     * Sets up new generation
     */
    public static void nextGenSetup(){
        //survivors are the new population
        population.clear();
        population.addAll(survivors);

        fitness.clear();
        fitnessFunction();

        //empty arrays
        parents.clear();
        children.clear();
        survivors.clear();
    }


    /**
     * Get Best, Average and worst values of fitness
     */
    private static void collectData(){
        for (int i=0; i< fitness.size(); i++){
            fitnessSum = fitnessSum + fitness.get(i);

            if(fitness.get(i) < minValue) {
                minValue = fitness.get(i);
            } else if (fitness.get(i) > maxValue){
                maxValue = fitness.get(i);
            }
        }
    }

}
