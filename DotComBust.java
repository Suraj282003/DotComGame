import java.io.*;
import java.util.*;

public class DotComBust {
    private GameHelper helper = new GameHelper();
    private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
    private int numOfGuesses = 0;

    private void setUpGame(){
        // first make some dot coms and give them locations;
        DotCom one = new DotCom();
        one.setName("Pets.com");
        DotCom two = new DotCom();
        two.setName("eToys.com");
        DotCom three = new DotCom();
        three.setName("Go2.com");
        dotComsList.add(one);
        dotComsList.add(two);
        dotComsList.add(three);

        System.out.println("Your goal is to sink three dot coms.");
        System.out.println("Pets.com, eToys.com, Go2.com");
        System.out.println("Try to sink them all in the fewest number of guesses");

        for (DotCom dotComToSet : dotComsList){
            ArrayList<String> newLocation = helper.placeDotCom(3);
            dotComToSet.setLocationCells(newLocation);
        }//close loop
    }//close setUpGame method.
    private void startPlaying() {
        while(!dotComsList.isEmpty()) {
            String userGuess = helper.getUserInput("Enter a guess");
            checkUserGuess(userGuess);
        } // close while
        finishGame();
    } // close startPlaying method
    private void checkUserGuess(String userGuess) {
        numOfGuesses++;
        String result = "miss";
        for (DotCom dotComToTest : dotComsList) {
            result = dotComToTest.checkYourself(userGuess);
            if (result.equals("hit")) {
                break;
            }
            if (result.equals("kill")) {
                dotComsList.remove(dotComToTest);
                break;
            }
        } // close for
        System.out.println(result);
    } // close method
    private void finishGame() {
        System.out.println("All Dot Coms are dead! Your stock is now worthless.");
        if (numOfGuesses <= 18) {
            System.out.println("It only took you " + numOfGuesses + " guesses.");
            System.out.println(" You got out before your options sank.");
        } else {
            System.out.println("Took you long enough. "+ numOfGuesses + " guesses.");
            System.out.println("Fish are dancing with your options.");
        }
    } // close method

    public static void main (String[] args) {
        DotComBust game = new DotComBust();
        game.setUpGame();
        game.startPlaying();
    } // close method
}

class DotCom {
    private ArrayList<String> locationCells;
    private String name;
    public void setLocationCells(ArrayList<String> loc) {
        locationCells = loc;
    }
    public void setName(String n) {
        name = n;
    }
    public String checkYourself(String userInput) {
        String result = "miss";
        int index = locationCells.indexOf(userInput);
        if (index >= 0) {
            locationCells.remove(index);

            if (locationCells.isEmpty()) {
                result = "kill";
                System.out.println("Ouch! You sunk " + name + " : ( ");
            } else {
                result = "hit";
            } // close if
        } // close if
        return result;
    } // close method
} // close class

class GameHelper{
    private static final String alphabet = "abcdefg";
    private int gridLength = 7;
    private int gridSi = 49;
    private int [] grid = new int[gridSi];
    private int comCount = 0;

    public String getUserInput(String prompt){
        String inputLine = null;
        System.out.println(prompt+"  ");
        try{
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(System.in));
            inputLine = is.readLine();
            if (inputLine.length() == 0){
                return null;
            }
        }catch(IOException e){
            System.out.println("IOException : "+ e);
        }
        return inputLine.toLowerCase();
    }

    public ArrayList<String> placeDotCom(int comSi){
        ArrayList<String> alphaCells = new ArrayList<>();

        String temp = null;
        int [] coords = new int[comSi];
        int attempts = 0;
        boolean success = false;
        int location = 0;

        comCount++;
        int incr = 1;
        if ((comCount % 2) == 1){
            incr = gridLength;
        }
        while (!success & attempts++ < 200){
            location = (int) (Math.random()*gridSi);
            int x = 0;
            success = true;
            while (success && x < comSi){
                if (grid[location] == 0) {
                    coords[x++] = location;
                    location += incr;
                    if (location >= gridSi) {
                        success = false;
                    }
                    if (x > 0 && (location % gridLength == 0)) {
                        success = false;
                    }
                }else {
                    success = false;
                }
            }
        }
        int x = 0;
        int row = 0;
        int column = 0;

        while(x < comSi){
            grid[coords[x]] = 1;
            row = (int) (coords[x] / gridLength);
            column = coords[x] % gridLength;
            temp = String.valueOf(alphabet.charAt(column));

            alphaCells.add(temp.concat(Integer.toString(row)));
            x++;
        }
        return alphaCells;
    }
}