/**
 * Name: Kshitij Potdar
 * Student ID: 1636604
 */

import java.io.*;
import java.util.*;


/**
 * This class uses the 2 way external sort algorithm to sort a large txt file
 * It divides the source file into runs of size m and then sorts them alphabetically while
 * merging the sorted runs using priority queue.
 */
public class xSort{
    /**
     *The main method takes two commmand line arguments
     *1. Size of runs (m) to divide the source file
     *2. name of the initial runs file
     *3. Value of k which is ignored since this is a solo solution
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        try {
            int m=Integer.parseInt(args[0]);
            String initRuns=args[1];
            int k = Integer.parseInt(args[2]);

            //Initialize a list to hold the temp files
            List<File> tempFiles = new ArrayList<>();
            File outputDir = new File(System.getProperty("java.io.tmpdir"));


            BufferedReader reader = new BufferedReader(new FileReader(initRuns));
            String line;
            int runCount = 0;
            BufferedWriter writer = null;
            File currentTempFile = null;
            int lineCount = 0;
            // Read initial runs file line by line
            while ((line = reader.readLine()) != null) {

                if (lineCount == 0) {
                    currentTempFile = File.createTempFile("xSort",null,outputDir);
                    writer = new BufferedWriter(new FileWriter(currentTempFile));
                    tempFiles.add(currentTempFile);
                }
                writer.write(line);
                writer.newLine();
                lineCount++;
                if (lineCount == m) {
                    writer.close();
                    lineCount = 0;
                }
            }
            // Close the writer if not null
            if (writer!=null) {
                writer.close();
            }
            reader.close();



            List<BufferedReader> readers = new ArrayList<>();
            for (File tempFile : tempFiles) {
                readers.add(new BufferedReader(new FileReader(tempFile)));
            }
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
            PriorityQueue<String> p = new PriorityQueue<>();
            for (BufferedReader reader1 : readers) {
                String nextLine = reader1.readLine();
                if (nextLine != null) {
                    p.offer(nextLine);
                }
            }
            //Merge sorted runs
            while (!p.isEmpty()) {
                String minLine = p.poll();
                outputWriter.write(minLine);
                outputWriter.newLine();
                for (BufferedReader reader1 : readers) {
                    String nextLine = reader1.readLine();
                    if (nextLine != null) {
                        p.offer(nextLine);
                    }
                }
            }
            outputWriter.close();
            //Closing all the readers
            for(BufferedReader reader1 : readers) {
                reader1.close();
            }

            // Delete all temp files
            for(File tempFile : tempFiles) {
                tempFile.delete();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println( e.getMessage());
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
    }
}
