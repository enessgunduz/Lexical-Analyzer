/* 
Mustafa Tolga Akbaba - 150120001
Efe Özgen - 150121077
Muhammed Enes Gündüz - 150120038
*/

import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static int currentIndex = 0;
    static int currentLine = 1;

    static String outputText = "";

    public static void main(String[] args) throws IOException {
        System.out.print("Name of the input file:");
        Scanner inpt = new Scanner(System.in);
        String fileName = inpt.nextLine();
        inpt.close();
        System.out.println();
        // read input file
        File f = new File("src/" + fileName + ".txt");
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
            return null;
        }
        if (line.length() == 0) {
            return readLn(br);
        }
        return line;
    }

    /*
     * This function stands for reading next character. It also increments
     * currentIndex.
     */
    public static char readNextCh(String line) {
        currentIndex++;
        if (line.length() < currentIndex + 1) {
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
            if (line == null) {
                System.out.println(outputText);
                File myObj = new File("src/output.txt");
                myObj.createNewFile();
                FileWriter myWriter = new FileWriter("src/output.txt");
                myWriter.write(outputText);
                myWriter.close();
                System.exit(0);
            }
        }

        // Because we want to get the current char, we didn't call readNextCh. But in
        // other cases, we should use.
        char ch = line.charAt(currentIndex);

        // Bracket Control
        if (bracketCond(ch))
            bracketLiteral(ch);

        // Comment Control
        else if (ch == '~') {
            line = readLn(br);
        }

        // Space Control
        else if (ch == ' ') {
            ++currentIndex;
        }

        // Check if it is a number
        else if (ch == '-' || ch == '+' || (ch >= '0' && ch <= '9') || ch == '.')
            numberLiterals(br, line);

        else if (ch == '\'')
            charLiteral(br, line);

        else if (ch == '"') {
            stringLiteral(br, line);

        } else if ((ch <= 'z' && ch >= 'a') || (ch <= 'Z' && ch >= 'A') || (ch == '!') || (ch == '*') || (ch == '/')
                || (ch == ':') || (ch == '<')
                || (ch == '=') || (ch == '>')
                || (ch == '?')) {

            wordLiteral(br, line);
        }
        identify(br, line);

    }

    public static void wordLiteral(BufferedReader br, String line) throws IOException {
        int outputIndex = currentIndex + 1;

        String word = "";
        word += line.charAt(currentIndex);

        while (line.charAt(currentIndex) != ' ') {

            char ch = readNextCh(line);

            if (!((ch >= 97 && ch <= 122) || (ch >= 65 && ch <= 90))) {
                break;
            }
            word += ch;
        }

        if (word.equals("true") || word.equals("false")) {
            booleanLiteral(br, line, outputIndex);
            return;
        }
        if (word.equals("define") || word.equals("let") || word.equals("cond")
                || word.equals("if") || word.equals("begin")) {
            keywordLiteral(br, line, word, outputIndex);
        } else {
            identifierLiteral(br, line, outputIndex);
        }

    }

    public static void booleanLiteral(BufferedReader br, String line, int outputIndex) {
        outputText += "BOOLEAN " + currentLine + ":" + outputIndex + "\n";
    }

    public static void identifierLiteral(BufferedReader br, String line, int outputIndex) {
        outputText += "IDENTIFIER " + currentLine + ":" + outputIndex + "\n";

    }

    public static void keywordLiteral(BufferedReader br, String line, String keyword, int outputIndex) {
        outputText += keyword.toUpperCase() + " " + currentLine + ":" + outputIndex + "\n";
    }

    public static void charLiteral(BufferedReader br, String line) throws IOException {
        int outputIndex = currentIndex + 1; // for output purposes
        char ch = readNextCh(line);

        if (ch == '\'') {
            promtError(currentLine, outputIndex, line);
        } else if (ch == '\\') {
            ch = readNextCh(line);
            if (ch != '\'') {
                promtError(currentLine, outputIndex, line);
            } else {
                ch = readNextCh(line);
                if (ch != '\'') {
                    promtError(currentLine, outputIndex, line);
                }
            }

        } else if (!(ch >= 32 && ch <= 126)) {
            promtError(currentLine, outputIndex, line);
        } else {
            ch = readNextCh(line);
            if (ch != '\'') {
                promtError(currentLine, outputIndex, line);
            }

        }
        outputText += "CHAR " + currentLine + ":" + outputIndex + "\n";
        readNextCh(line);

    }

    public static void stringLiteral(BufferedReader br, String line) throws IOException {
        int outputIndex = currentIndex + 1; // for output purposes
        char chOld = line.charAt(currentIndex);
        char ch = readNextCh(line);
        if (!(ch >= 32 && ch <= 126)) {
            promtError(currentLine, outputIndex, line);
        }
        if (ch == '"') {

        }

        while (ch >= 32 && ch <= 126) {
            chOld = ch;
            ch = readNextCh(line);
            if (ch == '"' && chOld != '\\') {
                break;
            }

            if (!(ch >= 32 && ch <= 126) || ch == '#')
                promtError(currentLine, outputIndex, line);
        }

        outputText += "STRING " + currentLine + ":" + outputIndex + "\n";
        readNextCh(line);

    }

    public static void numberLiterals(BufferedReader br, String line) throws IOException {
        char ch = line.charAt(currentIndex);
        int outputIndex = currentIndex + 1; // for output purposes

        boolean hexOrBin = false, hex = false, bin = false;

        if ((ch == '.' || ch == '+' || ch == '-')
                && (currentIndex + 1 == line.length() || line.charAt(currentIndex + 1) == ' ')) {
            currentIndex++;
            identifierLiteral(br, line, outputIndex);
            return;
        }

        if (ch == '.') {
            floatLiteral(br, line, 0, outputIndex);
            return;
        } else if (ch == '0') { // Could be hex or bin
            hexOrBin = true;
        }

        ch = readNextCh(line);
        if (ch == '.') {
            floatLiteral(br, line, 0, outputIndex);
            return;
        }

        /* Bin or hex works */
        if (hexOrBin && (ch == 'x' || ch == 'b')) {
            if (ch == 'x')
                hex = true;
            else
                bin = true;

            ch = readNextCh(line);
            while (hex && hexCond(ch)) {
                ch = readNextCh(line);
                if (bracketCond(ch) || ch == ' ') {
                    break;
                }
                if (!hexCond(ch) && ch != '#') {

                    promtError(currentLine, outputIndex, line);
                }
            }
            while (bin && binCond(ch)) {
                ch = readNextCh(line);
                if (bracketCond(ch) || ch == ' ') {
                    break;
                }
                if (!binCond(ch) && ch != '#') {
                    promtError(currentLine, outputIndex, line);
                }
            }
        } else if (decCond(ch)) {
            /* simple number literals */
            while (true) {
                ch = readNextCh(line);
                if (ch == 'e' || ch == 'E') {
                    floatLiteral(br, line, 1, outputIndex);
                    return;
                } else if (ch == '.') {
                    floatLiteral(br, line, 0, outputIndex);
                    return;
                }
                if (bracketCond(ch) || ch == ' ' || ch == '#') {
                    break;
                }

                if (!decCond(ch)) {
                    promtError(currentLine, outputIndex, line);
                }
            }
        } else if (bracketCond(ch) || ch == '#') {

        } else {
            promtError(currentLine, outputIndex, line);
        }

        outputText += "NUMBER " + currentLine + ":" + outputIndex + "\n";
    }

    public static void floatLiteral(BufferedReader br, String line, int cond, int outputIndex) throws IOException {
        char ch;

        ch = readNextCh(line);
        if (cond == 0) {
            if (!decCond(ch)) {
                promtError(currentLine, outputIndex, line);
            }
            while (decCond(ch)) {
                ch = readNextCh(line);
                if (ch == 'e' || ch == 'E') {
                    floatLiteral(br, line, 1, outputIndex);
                    return;
                }
                if (bracketCond(ch) || ch == ' ') {
                    break;
                }

                if (!decCond(ch) && ch != '#') {
                    promtError(currentLine, outputIndex, line);
                }
            }

        } else if (cond == 1) {
            if (ch != '+' && ch != '-' && !decCond(ch)) {
                promtError(currentLine, outputIndex, line);
            }

            ch = readNextCh(line);
            while (decCond(ch)) {
                ch = readNextCh(line);
                if (bracketCond(ch) || ch == ' ') {
                    break;
                }

                if (!decCond(ch) && ch != '#') {
                    promtError(currentLine, outputIndex, line);
                }
            }

        }

        outputText += "NUMBER " + currentLine + ":" + outputIndex + "\n";

    }

    public static void bracketLiteral(char ch) {
        // Bracket Control
        switch (ch) {
            case '(':
                outputText += "LEFTPAR " + currentLine + ":" + ++currentIndex + "\n";
                break;
            case ')':
                outputText += "RIGHTPAR " + currentLine + ":" + ++currentIndex + "\n";
                break;
            case '[':
                outputText += "LEFTSQUAREB " + currentLine + ":" + ++currentIndex + "\n";
                break;
            case ']':
                outputText += "RIGHTSQUAREB " + currentLine + ":" + ++currentIndex + "\n";
                break;
            case '{':
                outputText += "LEFTCURLYB " + currentLine + ":" + ++currentIndex + "\n";
                break;
            case '}':
                outputText += "RIGHTCURLYB " + currentLine + ":" + ++currentIndex + "\n";
                break;
        }
    }

    public static boolean hexCond(char ch) {
        return ((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || (ch >= '0' && ch <= '9'));
    }

    public static boolean binCond(char ch) {
        return ((ch >= '0' && ch <= '1'));
    }

    public static boolean decCond(char ch) {
        return ((ch >= '0' && ch <= '9'));
    }

    public static boolean bracketCond(char ch) {
        return (ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}');
    }

    public static void promtError(int line, int index, String lineText) {
        String errorText = "";
        currentIndex = index - 1;
        char ch = lineText.charAt(currentIndex);
        while (ch != ' ' && ch != '#') {
            errorText += ch;
            ch = readNextCh(lineText);
        }

        System.out.println("LEXICAL ERROR [" + line + ":" + index + "]: Invalid token '" + errorText + "'");
        System.exit(0);
    }

}
