import java.util.ArrayList;
/**
 * Exists for the sole purpose of locking characters to a location
 *  @author Danai Mitsopoulou
 *  @version 1.0
 */
public class Location {

    int locationName;
    static ArrayList<Integer> characters;
    static ArrayList<Integer> items;

   public Location(int num){
       locationName = num;
       characters = new ArrayList<>();
       items = new ArrayList<>();
       //String nums = Integer.toString(num);
       if( num == 201){
           characters.add(11);
           characters.add(12);
       } else if (num == 202) {
           characters.add(21);
           characters.add(22);
       } else if (num == 203) {
           characters.add(31);
           characters.add(32);
       } else if (num == 204) {
           characters.add(41);
           characters.add(42);
       } else if (num == 205) {
           characters.add(51);
           characters.add(52);
       } else if (num == 206){
           characters.add(61);
           characters.add(62);
       } else {
           System.out.println("how did you manage that");
       }

   }
   
   public int getCharacter(int i){
       return characters.get(i);
   }
   public int getLocation(){
       return locationName;
   }
}
