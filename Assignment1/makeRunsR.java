import java.io.*;
import java.util.*;

public class makeRuns {
    private static void maxHeap(String [] array, int size, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if(left < size && array[left].compareTo(array[largest]) > 0) {largest = left;}
        if(right < size && array[right].compareTo(array[largest]) > 0) {largest = right;}

        if(largest != root) {
            String temp = array[root];
            array[root] = array[largest];
            array[largest] = temp;

            maxHeap(array, size, largest);
        }
    }

    public static void heapSort(String [] array) {
        int size = array.length;

        for(int i = (size / 2) - 1; i >= 0; i--) {maxHeap(array, size, i);}

        for(int j = size - 1; j > 0; j--){
            String temp = array[0];
            array[0] = array[j];
            array[j] = temp;

            maxHeap(array, j,0);
        }

    }

    private static void clearRun(String [] run) {
        for (int i = 0; i < run.length; i++) {run[i] = null;}
    }

    private static String [] filterRun(String [] run) {
        int newLength = 0;
        for (int i = 0; i < run.length; i++) {
            if (run[i] != null) {newLength++;}
        }
        String [] newRun = Arrays.copyOf(run, newLength);

        return newRun;
    }

    private static void runMethod(int m, String fileName) {
        boolean fileEnd = false;
        String [] run = new String[m];
        File file = new File(fileName);

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(System.getProperty("line.separator"));

            while(scanner.hasNext()){
                for (int i = 0; i < m; i++) {
                    if (scanner.hasNext()) {
                        String data = scanner.nextLine();
                        run[i] = data;
                    } else {fileEnd = true;}
                }

                if(!fileEnd) {
                    heapSort(run);
                    for (String data : run) {System.out.println(data);}

                    clearRun(run);
                } else {
                    String [] lastRun = filterRun(run);
                    heapSort(lastRun);

                    int iteration = 0;
                    String tail = "";
                    for (String data : lastRun) {
                        ++iteration;
                        tail = (iteration != lastRun.length) ? System.getProperty("line.separator") : "";
                        System.out.print(data + tail);
                    }
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
    }




    public static void main(String[] args) {
        int m = 0;
        String fileName = "";

        try {
            m = Integer.parseInt(args[0]);
            fileName = args[1];
        } catch (Exception e) {e.printStackTrace();}

        runMethod(m, fileName);
    }
}
