import java.util.ArrayList;
import java.util.Random;

/**
 * Exists for the sole purpose of locking characters to a location
 *  @author Danai Mitsopoulou
 *  @version 1.0
 */
public class Location {

    int locationName;
    static ArrayList<Integer> characters;
    //static ArrayList<Integer> items;
    static Random random_method;

   public Location(int num){
       random_method = new Random();
       locationName = num;
       characters = new ArrayList<>();
       //items = new ArrayList<>();
       //String nums = Integer.toString(num);
       if( num == 201){
           characters.add(2011);
           characters.add(2012);
       } else if (num == 202) {
           characters.add(2021);
           characters.add(2022);
       } else if (num == 203) {
           characters.add(2031);
           characters.add(2032);
       } else if (num == 204) {
           characters.add(2041);
           characters.add(2042);
       } else if (num == 205) {
           characters.add(2051);
           characters.add(2052);
       } else if (num == 206){
           characters.add(2061);
           characters.add(2062);
       } else {
           System.out.println("how did you manage that");
       }
       printContents();

   }

   //returns random character
   public ArrayList<Integer> getCharacters(){
       return characters;
   }
   public int getLocationName(){
       return locationName;
   }

   private void printContents(){
       System.out.println(locationName);
       for (Integer character: characters) {
           System.out.println(character);

       }
   }
}
