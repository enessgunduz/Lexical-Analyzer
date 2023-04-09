import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
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
    public static char readNextCh(String line)  {
        currentIndex++;
        if (line.length() < currentIndex+1) {
            return '#';
        }
        return line.charAt(currentIndex);
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
                System.exit(0);
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
        if (ch == '-' || ch == '+' || (ch >= '0' && ch <= '9') || ch == '.'){
            numberLiterals(br, line);
        }

        // All cases must be checked here.

        /* YOUR FUNCTIONS MUST BE HERE! */

        // Recursion baby, there must be no code after this line in this function.
        identify(br, line);
    }


    public static void numberLiterals(BufferedReader br, String line) throws IOException {
        char ch = line.charAt(currentIndex);
        int outputIndex = currentIndex+1; //for output purposes

        boolean hexOrBin = false ,hex = false, bin = false;

        if (ch == '.'){
            floatLiteral(br,line,0, outputIndex);
            return;
        } else if (ch == '0'){ //Could be hex or bin
            hexOrBin=true;
        }

        ch = readNextCh(line);
        if (ch == '.'){
            floatLiteral(br,line,0, outputIndex);
            return;
        }

        /*Bin or hex works*/
        if (hexOrBin && (ch == 'x' || ch =='b')){
            if (ch == 'x')
                hex = true;
            else
                bin = true;

            ch = readNextCh(line);

            while (hex && hexCond(ch)){
                ch = readNextCh(line);
                if (bracketCond(ch) || ch ==' '){
                    break;
                }
                if (!hexCond(ch) && ch!='#'){

                    System.out.println("Unexpected Token at "+currentLine+":"+outputIndex);
                    System.exit(0);
                }
            }
            while (bin && binCond(ch)){
                ch = readNextCh(line);
                if (bracketCond(ch)|| ch ==' '){
                    break;
                }
                if (!binCond(ch)&& ch!='#'){
                    System.out.println("Unexpected Token at "+currentLine+":"+outputIndex);
                    System.exit(0);
                }
            }
        } else if (decCond(ch)){
            /*simple number literals*/
            while (true){
                ch = readNextCh(line);
                if (ch == 'e' || ch == 'E'){
                    floatLiteral(br, line, 1, outputIndex);
                    return;
                } else if (ch == '.'){
                    floatLiteral(br, line, 0, outputIndex);
                    return;
                }
                if (bracketCond(ch) || ch ==' '){
                    break;
                }

                if (!decCond(ch)){
                    System.out.println("Unexpected Token at "+currentLine+":"+outputIndex);
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Unexpected Token at "+currentLine+":"+outputIndex);
            System.exit(0);
        }


        System.out.println("NUMBER "+ currentLine+":"+outputIndex);
        identify(br,line);

    }

    public static void floatLiteral(BufferedReader br, String line, int cond , int outputIndex) throws IOException {
        char ch;

        ch = readNextCh(line);
        if (cond == 0){
            if (!decCond(ch)){
                System.out.println(ch+" a Unexpected Token at "+currentLine+":"+outputIndex);
                System.exit(0);
            }
            while (decCond(ch)){
                ch = readNextCh(line);
                if (ch == 'e' || ch == 'E'){
                    floatLiteral(br,line,1, outputIndex);
                    return;
                }
                if (bracketCond(ch) || ch ==' '){
                    break;
                }

                if (!decCond(ch) && ch!='#'){
                    System.out.println(ch + " b Unexpected Token at "+currentLine+":"+outputIndex);
                    System.exit(0);
                }
            }



        } else if (cond == 1){
            if (ch != '+' && ch != '-' && !decCond(ch)){
                System.out.println(ch + " c Unexpected Token at "+currentLine+":"+outputIndex);
                System.exit(0);
            }

            ch = readNextCh(line);
            while (decCond(ch)){
                ch = readNextCh(line);
                if (bracketCond(ch) || ch ==' '){
                    break;
                }

                if (!decCond(ch)&& ch!='#'){
                    System.out.println(ch + " d Unexpected Token at "+currentLine+":"+outputIndex);
                    System.exit(0);
                }
            }

        }

        System.out.println("NUMBER "+ currentLine+":"+outputIndex);
        identify(br,line);


    }

    public static boolean hexCond(char ch){
        return ((ch >= 'a' && ch <= 'f') || (ch>= 'A' && ch <= 'F') || (ch >= '0' && ch <= '9'));
    }

    public static boolean binCond(char ch){
        return ((ch >= '0' && ch <= '1'));
    }

    public static boolean decCond(char ch){
        return ((ch >= '0' && ch <= '9'));
    }

    public static boolean bracketCond(char ch){
        return (ch == '(' || ch == ')' || ch == '['|| ch == ']'|| ch == '{'|| ch == '}');
    }

}
