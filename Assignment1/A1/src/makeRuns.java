/** 
 * Name: Kshitij Potdar
 * Student ID: 1636604
 */
import java.io.*;
import java.util.ArrayList;
import static java.util.Collections.swap;


/**
 *
 * This class reads lines from a specific text file , divides it into chunks of m lines called runs
 * and then alphabetically sorts each line in each run and prints the output in the terminal
 */
public class makeRuns {

    /**
     *
     * @param args Terminal arguments, the first one is m which determines the size of each run and the second
     *             argument is the name of the source txt file.
     */
    public static void main(String[] args) {

        String sourceFile = args[1];

        ArrayList<String> lines = new ArrayList<>();
        int m = Integer.parseInt(args[0]);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            String line;
            int i = 0;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                i++;


                if (i % m == 0) {

                    quick(lines,0,lines.size()- 1);
                    for(String line1 : lines){
                        writer.write(line1 + " \n");
                    }

                    lines.clear();
                }

            }

            if(!lines.isEmpty()){
                quick(lines,0,lines.size()-1);
                for(String line1 : lines){
                    writer.write(line1+" \n");
                }
            }

            writer.flush();
            reader.close();
            writer.close();



        } catch (FileNotFoundException e) {
            System.err.println(  "File does not exist"+ e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Divides the array list around an element(pivot)
     *
     * @param lines Strings to be partitioned
     * @param low Starting index of sub-array
     * @param high Ending index of sub-array
     * @return index of pivot element after partitioning
     */


    static int partition(ArrayList<String> lines, int low, int high){
        String pivot = lines.get(high);
        int i = low-1;

        for (int j = low;j < high; j++){
            if(lines.get(j).compareTo(pivot) < 0){
                i++;
                swap(lines,i,j);
            }
        }

        swap(lines,i + 1, high);
        return i+1;
    }

    /**
     * Sort the arraylist using Quicksort Algorithm
     * @param lines Arraylist of strings to be sorted
     * @param low starting index of the subarray to be sorted
     * @param high ending index of the subarray to be sorted
     */

    public static void quick(ArrayList<String> lines, int low, int high) {
        if (low < high){
            int index = partition(lines,low,high);
            quick(lines,low,index - 1);
            quick(lines, index + 1, high);

        }
    }


}