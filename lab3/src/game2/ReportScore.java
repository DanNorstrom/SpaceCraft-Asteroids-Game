package game2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ReportScore {

    static File file = new File(System.getProperty("user.dir")+"//Top5Scores.txt");

    public static void addScore(int score){
    	
    	if (score > 0) {
	        //write to file
	        FileWriter fw = null;
	        try {
	            fw = new FileWriter(file, true);
	            fw.write(score+"\n");
	        }
	        catch(IOException e) {
	        	e.printStackTrace();
	        }
	        finally {
	            try {fw.close();}
	            catch (IOException e) { e.printStackTrace(); }
	        }
    	}
    }


    public static ArrayList<Integer> ReturnScore() {

        // get current list
        ArrayList<Integer> allScores = new ArrayList<>();
        Scanner input = null;
        int count = 0;
        try {
            input = new Scanner(file);
            while (input.hasNextLine()){
                allScores.add( Integer.parseInt(input.nextLine()));
                count += 1;
            }
        }
        catch (IOException e){
        	e.printStackTrace();
        }
        finally{
            try{ input.close(); }
            catch (NullPointerException e) { e.printStackTrace(); }
        }

        // return top five
        ArrayList<Integer> top5Scores = new ArrayList<>();
        Collections.sort(allScores);
        Collections.reverse(allScores);
        if (count >=5) {
            top5Scores = new ArrayList<Integer>(allScores.subList(0, 5));
        }
        else {
            top5Scores = new ArrayList<Integer>(allScores.subList(0, count));
        }

        return top5Scores;
    }


    }
