import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class xSort {
    private static void createTempFiles() {
        for(int i = 0; i < 4; i++) {new File("file" + i);}
    }

    private static void writeToFile(String data, String fileName, boolean append) {
        try {
            FileWriter writer = new FileWriter(fileName, append);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {System.out.println(e.getMessage());}
    }

    private static void readFromFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            boolean firstLine = true;
            String head;

            while(scanner.hasNext()) {
                head = (firstLine) ? "" : System.getProperty("line.separator");
                firstLine = false;
                System.out.print(head + scanner.nextLine());
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    private static void clearFiles(String [] filePaths) {
        for(String filePath : filePaths) {writeToFile("", filePath, false);}
    }

    private static void distributeInitialRuns(int m, String filePath) {
        int tempFileIndex = 0;
        String tempFilePath, data, head;
        boolean initialPass = false;

        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(System.getProperty("line.separator"));

            int counter = 0;
            while(scanner.hasNext()){
                tempFilePath = "file" + tempFileIndex;

                head = (counter != 0 || initialPass) ? System.getProperty("line.separator") : "";
                data = head + scanner.nextLine();
                writeToFile(data, tempFilePath, true);

                initialPass = (tempFileIndex == 1) ? true : initialPass;

                counter = (counter + 1) % m;
                if(counter == 0) {tempFileIndex = (tempFileIndex + 1) % 2;}
            }

        } catch (FileNotFoundException e) {e.printStackTrace();}
    }

    private static boolean mergeFiles(int size, int dataFileIndex) {
        boolean singlePass = false;
        int tempFileIndex = (dataFileIndex + 2) % 4;

        String firstDataFile = "file" + dataFileIndex;
        String secondDataFile = "file" + (dataFileIndex + 1);
        String firstTempFile = "file" + tempFileIndex;
        String secondTempFile = "file" + (tempFileIndex + 1);

        try {
            Scanner iScanner = new Scanner(new File(firstDataFile));
            Scanner jScanner = new Scanner(new File(secondDataFile));



            String iData, jData;
            int loopVersion = 0;

            String temp, head;
            String file;

            iData = (iScanner.hasNext()) ? iScanner.nextLine() : null;
            jData = (jScanner.hasNext()) ? jScanner.nextLine() : null;

            boolean iEnded = (iData == null) ? true : false;
            boolean jEnded = (jData == null) ? true : false;

            while(!iEnded || !jEnded) {
                int i = 0;
                int j = 0;

                boolean firstEntry = (loopVersion == 0 || loopVersion == 1) ? true : false;
                while ((i < size) && (j < size)) {
                    if (!iEnded && !jEnded) {
                        if (iData.compareTo(jData) < 0) {
                            ++i;
                            head = (firstEntry) ? "" : System.getProperty("line.separator");
                            firstEntry = false;
                            temp = head + iData;
                            file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                            writeToFile(temp, file, true);
                            iEnded = !iScanner.hasNext();
                            iData = (!iEnded) ? iScanner.nextLine() : null;
                        } else {
                            ++j;
                            head = (firstEntry) ? "" : System.getProperty("line.separator");
                            firstEntry = false;
                            temp = head + jData;
                            file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                            writeToFile(temp, file, true);
                            jEnded = !jScanner.hasNext();
                            jData = (!jEnded) ? jScanner.nextLine() : null;
                        }
                    } else if (!iEnded && jEnded) {
                        ++i;
                        head = (firstEntry) ? "" : System.getProperty("line.separator");
                        firstEntry = false;
                        temp = head + iData;
                        file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                        writeToFile(temp, file, true);
                        iEnded = !iScanner.hasNext();
                        iData = (!iEnded) ? iScanner.nextLine() : null;
                    } else if (iEnded && !jEnded) {
                        ++j;
                        head = (firstEntry) ? "" : System.getProperty("line.separator");
                        firstEntry = false;
                        temp = head + jData;
                        file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                        writeToFile(temp, file, true);
                        jEnded = !jScanner.hasNext();
                        jData = (!jEnded) ? jScanner.nextLine() : null;
                    } else {break;}
                }

                if ((i >= size)) {
                    while (j < size) {
                        if (!jEnded) {
                            ++j;
                            head = (firstEntry) ? "" : System.getProperty("line.separator");
                            firstEntry = false;
                            temp = head + jData;
                            file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                            writeToFile(temp, file, true);
                            jEnded = !jScanner.hasNext();
                            jData = (!jEnded) ? jScanner.nextLine() : null;
                        } else {break;}
                    }
                }
                if ((j >= size)) {
                    while (i < size) {
                        if (!iEnded) {
                            ++i;
                            head = (firstEntry) ? "" : System.getProperty("line.separator");
                            firstEntry = false;
                            temp = head + iData;
                            file = (loopVersion % 2 == 0) ? firstTempFile : secondTempFile;
                            writeToFile(temp, file, true);
                            iEnded = !iScanner.hasNext();
                            iData = (!iEnded) ? iScanner.nextLine() : null;
                        } else {break;}
                    }
                }

                ++loopVersion;
            }

            singlePass = (loopVersion == 1);
            clearFiles(new String[] {firstDataFile, secondDataFile});
        } catch (IOException e) {e.printStackTrace();}

        return singlePass;
    }

    public static void runMethod(int m, String fileName, int k) {
        createTempFiles();
        new File("temp");

        String [] fileList = new String[]{"file0", "file1", "file2", "file3"};
        clearFiles(fileList);

        distributeInitialRuns(m, fileName);

        int size;
        int index = 0;
        boolean ended = false;

        int loopVersion = 0;
        while(!ended) {
            size = (int) (Math.pow(2, loopVersion) * m);
            index = (loopVersion % 2 == 0) ? 0 : 2;

            ended = mergeFiles(size, index);
            ++loopVersion;
        }

        readFromFile("file" + ((index + 2) % 4));

        try {
            for (String path : fileList) {Files.deleteIfExists(Path.of(path));}
        } catch (IOException e) {e.printStackTrace();}
    }




    public static void main(String[] args) {
        int m = 0;
        int k = 0;
        String fileName = "";
        try {
            m = Integer.parseInt(args[0]);
            fileName = args[1];
            k = Integer.parseInt(args[2]);
        } catch (Exception e) {e.printStackTrace();}

        runMethod(m,fileName,k);
    }
}
