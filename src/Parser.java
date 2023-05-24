import java.util.ArrayList;

public class Parser {

    static ArrayList<String> lexList = new ArrayList<>();
    static ArrayList<String> keyList = new ArrayList<>();

    static int currentIndex = 0;
    static int currentIndexForKey = 0;

    static int tabs = 0;
    static String currentLex;

    public static void StartParse(ArrayList<String> lexs, ArrayList<String> keys) {
        lexList = lexs;
        System.out.println(lexList);
        keyList = keys;
        System.out.println(keyList);
        Program();
    }

    // Enes Part Start
    static void Program() {
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

    static void TopLevelForm() {
        announceSyn("<TopLevelForm>");
        tabs++;
        printLex(0);

        SecondLevelForm();

        if (!((lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")))) {
            error();
        }
        printLex(0);

        tabs--;
    }

    static void SecondLevelForm() {
        announceSyn("<SecondLevelForm>");
        tabs++;

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            printLex(0);
            FunCall();
            if (!(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR"))) {
                error();
            }
            printLex(0);
        } else {
            Definition();
        }

        tabs--;
    }

    static void Definition() {
        announceSyn("<Definition>");
        tabs++;

        if (lexEqual("DEFINE") || lexEqual("DEFİNE")) {
            printLex(0);
            DefinitionRight();
        }
        else
            error();

        tabs--;
    }

    static void DefinitionRight() {
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
                    error();
            } else
                error();
        } else
            error();

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

    static void Statements() {
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
    static void Expressions() {
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

    static void Expression() {
        announceSyn("<Expression>");
        tabs++;

        if (lexEqual("LEFTPAR")) {
            printLex(0);
            Expr();


            if (lexEqual("RİGHTPAR") || lexEqual("RIGHTPAR")) {
                printLex(0);
            } else {
                error();
            }
        } else if (!(lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN"))) {
            error();
        } else {
            printLex(0);
        }

        tabs--;
    }

    static void Expr() {
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
            error();
        }



        tabs--;
    }

    static void FunCall() {
        announceSyn("<FunCall>");
        tabs++;

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            printLex(0);
            Expressions();
        } else {
            error();
        }

        tabs--;
    }

    static void LetExpression() {
        announceSyn("<LetExpression>");
        tabs++;

        if (lexEqual("LET")) {
            printLex(0);
            LetExpr();
        } else {
            error();
        }

        tabs--;
    }

    static void LetExpr() {
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
                error();
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
                    error();
                }
            }
        }

        tabs--;
    }

    static void VarDefs() {
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
                    error();
                }
            }
        }

        tabs--;

    }

    // Efe Part End

    // Tolga Part Start
    static void VarDef() {
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

    static void CondExpression() {
        announceSyn("<CondExpression>");
        tabs++;

        if (lexEqual("COND")) {
            setCurrentLex();
            printLex(0);
            CondBranches();
        }
        else {
            error();
        }

        tabs--;
    }

    static void CondBranches() {
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
                error();
            }
        }
        setCurrentLex();
        if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")) {
            printLex(0);
            CondBranch();
        }
        else{
            error();
        }


        tabs--;
    }

    static void CondBranch() {
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
                error();
            }
            printLex(0);
        } else {
            printLex(1);
        }

        tabs--;
    }

    static void IfExpression() {
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
            error();
        }

        tabs--;
    }

    static void EndExpression() {
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

    static void BeginExpression() {
        announceSyn("<BeginExpression>");
        tabs++;

        if (lexEqual("BEGIN") || lexEqual("BEGİN")) {
            printLex(0);
            Statements();
        } else {
            error();
        }

        tabs--;
    }

    // Tolga Part End

    static boolean lexEqual(String word) {
        return currentLex.equals(word);
    }

    static void setCurrentLex() {
        currentLex = lexList.get(currentIndex++);
    }

    static void announceSyn(String s) {
        for (int i = 0; i < tabs; i++) {
            System.out.print("      ");
        }
        System.out.println(s);

    }

    static void printLex(int cond){
        for (int i = 0; i < tabs; i++) {
            System.out.print("      ");
        }
        if (cond==0)
            System.out.println(currentLex+" ("+keyList.get(currentIndexForKey++)+")");
        else
            System.out.println(" ---- ");
    }

    static void error() {
        System.out.println("Error at :" + currentIndex + " " + currentLex);
        System.exit(0);
    }

}
