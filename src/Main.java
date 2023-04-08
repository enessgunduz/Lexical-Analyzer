import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static int currentIndex = 0;
    static int currentLine = 1;

    public static void main(String[] args) throws IOException {

        // read input file
        File f = new File("input.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        // Call identify for the very first time with initial inputs.
        identify(br, br.readLine());
    }

    /*
     * This function stands for reading new line, it also checks whether there are
     * new line or not, also sets the current
     * variables. It is a must to use this function
     */
    public static String readLn(BufferedReader br) throws IOException {
        currentLine++;
        currentIndex = 0;
        String line = br.readLine();
        if (line == null) {
            System.out.println("End Of File");
            return null;
        }
        return line;
    }

    /*
     * This function stands for reading next character. It also increments
     * currentIndex.
     */
    public static char readNextCh(String line) throws IOException {
        return line.charAt(++currentIndex);
    }

    /*
     * Identify is the main function that reads new characters and lines, then
     * categorizes.
     * Functions shouldn't call identify always, because end of the identify
     * function, it calls itself recursively.
     */
    public static void identify(BufferedReader br, String line) throws IOException {
        // if current line finished, go to next line. If it is null, then return
        if (line.length() < currentIndex + 1) {
            line = readLn(br);
            if (line == null)
                return;
        }

        // Because we want to get the current char, we didn't call readNextCh. But in
        // other cases, we should use.
        char ch = line.charAt(currentIndex);

        // Bracket Control
        switch (ch) {
            case '(':
                System.out.println("LEFTPAR " + currentLine + ":" + ++currentIndex);
                break;
            case ')':
                System.out.println("RIGHTPAR " + currentLine + ":" + ++currentIndex);
                break;
            case '[':
                System.out.println("LEFTSQUAREB " + currentLine + ":" + ++currentIndex);
                break;
            case ']':
                System.out.println("RIGHTSQUAREB " + currentLine + ":" + ++currentIndex);
                break;
            case '{':
                System.out.println("LEFTCURLYB " + currentLine + ":" + ++currentIndex);
                break;
            case '}':
                System.out.println("RIGHTCURLYB " + currentLine + ":" + ++currentIndex);
                break;
            default: // Will be deleted, for now, currentIndex must be incremented in somewhere.
                // IMPORTANT: If you are working on your function, DELETE THIS DEFAULT PART and
                // do not forget
                // to increment currentIndex if none of the tokens are available now.
                ++currentIndex;
        }

        // Comment Control
        if(ch == '~'){
            line = readLn(br);
        }

        // Space Control
        if (ch == ' '){
            ++currentIndex;
        }

        // Check if it is a letter

        // Check if it is a number

        // All cases must be checked here.

        /* YOUR FUNCTIONS MUST BE HERE! */

        // Recursion baby, there must be no code after this line in this function.
        identify(br, line);
    }

}
