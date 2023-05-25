import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    static ArrayList<String> lexList = new ArrayList<>();
    static ArrayList<String> keyList = new ArrayList<>();
    static ArrayList<Integer> indexList = new ArrayList<>();


    static int currentIndex = 0;
    static int currentIndexForKey = 0;
    static int currentIndexForIndex = 0;
    static int tabs = 0;
    static String currentLex;
    static String outputText="";

    public static void StartParse(ArrayList<String> lexs, ArrayList<String> keys, ArrayList<Integer> index) throws IOException {
        lexList = lexs;
        keyList = keys;
        indexList = index;
        Program();

        System.out.print(outputText);
        File myObj = new File("src/syntaxOutput.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter("src/syntaxOutput.txt");
        myWriter.write(outputText);
        myWriter.close();
        System.exit(0);
    }

    // Enes Part Start
    static void Program() throws IOException {
        announceSyn("<Program>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            TopLevelForm();
            Program();
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void TopLevelForm() throws IOException {
        announceSyn("<TopLevelForm>");
        tabs++;
        printLex(0);

        SecondLevelForm();

        if (!((lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")))) {
            error(")");
        }
        printLex(0);

        tabs--;
    }

    static void SecondLevelForm() throws IOException {
        announceSyn("<SecondLevelForm>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            printLex(0);
            FunCall();
            if (!(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR"))) {
                error(")");
            }
            printLex(0);
        } else {
            Definition();
        }

        tabs--;
    }

    static void Definition() throws IOException {
        announceSyn("<Definition>");
        tabs++;

        if (lexEqual("DEFINE") || lexEqual("DEFİNE")) {
            printLex(0);
            DefinitionRight();
        }
        else
            error("define");

        tabs--;
    }

    static void DefinitionRight() throws IOException {
        announceSyn("<DefinitionRight>");
        tabs++;

        setCurrentLex();

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            printLex(0);
            Expression();
        } else if (lexEqual("LEFTPAR")) {
            printLex(0);
            setCurrentLex();
            if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
                printLex(0);
                ArgList();
                if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
                    printLex(0);
                    Statements();
                }
                else
                    error(")");
            } else
                error("identifier");
        } else
            error("(");

        tabs--;
    }

    static void ArgList() {
        announceSyn("<ArgList>");
        tabs++;

        setCurrentLex();

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            printLex(0);
            ArgList();
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void Statements() throws IOException {
        announceSyn("<Statements>");
        tabs++;

        setCurrentLex();

        if (lexEqual("DEFINE") || lexEqual("DEFİNE")) {
            Definition();
            Statements();
        } else {
            Expression();
        }

        tabs--;
    }

    // Enes Part End

    // Efe Part Start
    static void Expressions() throws IOException {
        announceSyn("<Expressions>");
        tabs++;

        setCurrentLex();

        if (lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN") || lexEqual("LEFTPAR")) {
            Expression();
            Expressions();
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void Expression() throws IOException {
        announceSyn("<Expression>");
        tabs++;

        if (lexEqual("LEFTPAR")) {
            printLex(0);
            Expr();


            if (lexEqual("RİGHTPAR") || lexEqual("RIGHTPAR")) {
                printLex(0);
            } else {
                error(")");
            }
        } else if (!(lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN"))) {
            error("identifier or number or char or string or boolean");
        } else {
            printLex(0);
        }

        tabs--;
    }

    static void Expr() throws IOException {
        announceSyn("<Expr>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LET")) {
            LetExpression();
            setCurrentLex();
        } else if (lexEqual("COND")) {
            CondExpression();
            setCurrentLex();
        } else if (lexEqual("IF") || lexEqual("İF")) {
            IfExpression();
            setCurrentLex();
        } else if (lexEqual("BEGIN") || lexEqual("BEGİN")) {
            BeginExpression();
            setCurrentLex();
        } else if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            FunCall();
        } else {
            error("let or cond or if or begin or identifier");
        }



        tabs--;
    }

    static void FunCall() throws IOException {
        announceSyn("<FunCall>");
        tabs++;

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            printLex(0);
            Expressions();
        } else {
            error("identifier");
        }

        tabs--;
    }

    static void LetExpression() throws IOException {
        announceSyn("<LetExpression>");
        tabs++;

        if (lexEqual("LET")) {
            printLex(0);
            LetExpr();
        } else {
            error("let");
        }

        tabs--;
    }

    static void LetExpr() throws IOException {
        announceSyn("<LetExpr>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            printLex(0);

            setCurrentLex();

            if (lexEqual("LEFTPAR")) {
                VarDefs();
            }

            setCurrentLex();

            if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
                printLex(0);
                Statements();
            } else {
                error(")");
            }
        } else if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            printLex(0);
            setCurrentLex();

            if (lexEqual("LEFTPAR")) {
                printLex(0);
                VarDefs();

                setCurrentLex();
                if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
                    printLex(0);
                    Statements();
                } else {
                    error(")");
                }
            }
        }

        tabs--;
    }

    static void VarDefs() throws IOException {
        announceSyn("<VarDefs>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            printLex(0);

            setCurrentLex();

            if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
                printLex(0);
                setCurrentLex();

                Expression();

                setCurrentLex();

                if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
                    printLex(0);
                    VarDef();
                } else {
                    error(")");
                }
            }
        }

        tabs--;

    }

    // Efe Part End

    // Tolga Part Start
    static void VarDef() throws IOException {
        announceSyn("<VarDef>");
        tabs++;

        if ( lexList.get(currentIndex).equals("LEFTPAR") ) {
            VarDefs();
        }
        else {
            printLex(1);
        }

        tabs--;
    }

    static void CondExpression() throws IOException {
        announceSyn("<CondExpression>");
        tabs++;

        if (lexEqual("COND")) {
            setCurrentLex();
            printLex(0);
            CondBranches();
        }
        else {
            error("cond");
        }

        tabs--;
    }

    static void CondBranches() throws IOException {
        announceSyn("<CondBranches>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            setCurrentLex();
            if (lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                    || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN")) {
                printLex(0);
                Expression();
                Statements();
            }
            else{
                error("identifier or number or char or string or boolean");
            }
        }
        setCurrentLex();
        if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
            printLex(0);
            CondBranch();
        }
        else{
            error(")");
        }


        tabs--;
    }

    static void CondBranch() throws IOException {
        announceSyn("<CondBranch>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            setCurrentLex();
            printLex(0);
            if (lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                    || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN")) {
                printLex(0);
                Expression();
                Statements();
            }
            Expression();
            Statements();
            setCurrentLex();
            if (!(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR"))) {
                error(")");
            }
            printLex(0);
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void IfExpression() throws IOException {
        announceSyn("<IfExpression>");
        tabs++;

        if (lexEqual("IF") || lexEqual("İF")) {
            printLex(0);
            setCurrentLex();
            Expression();
            setCurrentLex();
            Expression();
            EndExpression();
        } else {
            error("if");
        }

        tabs--;
    }

    static void EndExpression() throws IOException {
        announceSyn("<EndExpression>");
        tabs++;

        setCurrentLex();

        if ((lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN") || lexEqual("LEFTPAR"))) {
            Expression();
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void BeginExpression() throws IOException {
        announceSyn("<BeginExpression>");
        tabs++;

        if (lexEqual("BEGIN") || lexEqual("BEGİN")) {
            printLex(0);
            Statements();
        } else {
            error("begin");
        }

        tabs--;
    }

    // Tolga Part End

    static boolean lexEqual(String word) {
        return currentLex.equals(word);
    }

    static void setCurrentLex() {
        currentLex = lexList.get(currentIndex++);
        currentIndexForIndex+=2;
    }

    static void announceSyn(String s) {
        for (int i = 0; i < tabs; i++) {
            outputText+=" ";
        }
        outputText+=s+"\n";
    }

    static void printLex(int cond){
        for (int i = 0; i < tabs; i++) {
            outputText+=" ";
        }
        if (cond==0)
            outputText += currentLex+" ("+keyList.get(currentIndexForKey++)+")\n";
        else
            outputText += "-\n";
    }

    static void error(String expected) throws IOException {
        outputText+="SYNTAX ERROR [" + indexList.get(currentIndexForIndex-2) + ":" + indexList.get(currentIndexForIndex-1)+"]: '"+expected+"' is expected";
        System.out.print(outputText);
        File myObj = new File("src/syntaxOutput.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter("src/syntaxOutput.txt");
        myWriter.write(outputText);
        myWriter.close();
        System.exit(0);
    }

}
