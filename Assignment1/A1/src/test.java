import java.io.*;
import java.util.*;

public class test {
    public static void main(String[] args) {
        try {
            int m = Integer.parseInt(args[0]); // Number of lines in each initial run
            String sourceFile = args[1]; // Input file with initial runs
            //String outputFile = args[2]; // Output file for sorted data

            List<File> tempFiles = new ArrayList<>();
            File outputDir = new File(System.getProperty("java.io.tmpdir"));

            // Distribute initial runs into temporary files
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            String line;
            int runCount = 0;
            BufferedWriter writer = null;
            File currentTempFile = null;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                if (lineCount == 0) {
                    currentTempFile = File.createTempFile("xSort", null, outputDir);
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
            if (writer != null) {
                writer.close();
            }
            reader.close();

            // Perform 2-way merge sort
            List<BufferedReader> readers = new ArrayList<>();
            for (File tempFile : tempFiles) {
                readers.add(new BufferedReader(new FileReader(tempFile)));
            }
            //BufferedWriter outputWriter = new BufferedWriter(new FileWriter(new File(outputFile)));
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
            PriorityQueue<String> pq = new PriorityQueue<>();
            for (BufferedReader reader1 : readers) {
                String nextLine = reader1.readLine();
                if (nextLine != null) {
                    pq.offer(nextLine);
                }
            }
            while (!pq.isEmpty()) {
                String minLine = pq.poll();
                outputWriter.write(minLine);
                outputWriter.newLine();
                for (BufferedReader reader1 : readers) {
                    String nextLine = reader1.readLine();
                    if (nextLine != null) {
                        pq.offer(nextLine);
                    }
                }
            }
            outputWriter.close();
            for (BufferedReader reader1 : readers) {
                reader1.close();
            }
            for (File tempFile : tempFiles) {
                tempFile.delete();
            }
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid argument format: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
