public class test {
    public static void main(String[] args) {
        String paragraph = "This is a sample paragraph for demonstration purposes.";

        // Split the paragraph into words
        String[] words = paragraph.split("\\s+"); // Using regular expression to split by whitespace

        // Output the words
        for (String word : words) {
            System.out.println(word);
        }
    }
}
