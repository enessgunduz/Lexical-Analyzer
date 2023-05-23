import java.util.ArrayList;

public class Parser {

    static ArrayList<String> lexList = new ArrayList<>();
    static int currentIndex = 0;
    static String currentLex;

    public static void StartParse(ArrayList<String> lexs) {
        lexList = lexs;
        Program();
    }

    // Enes Part Start
    static void Program() {
        announceSyn("Program");

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            TopLevelForm();
            Program();
        } else {
            return;
        }
    }

    static void TopLevelForm() {
        announceSyn("TopLevelForm");

        SecondLevelForm();

        if ( !( (lexEqual("RIGHTPAR")) || (lexEqual("RİGHTPAR")) ) ) {
            error();
        }
    }

    static void SecondLevelForm() {
        announceSyn("SecondLevelForm");

        setCurrentLex();

        if (lexEqual("LEFTPAR")) {
            FunCall();
            if ( !( (lexEqual("RIGHTPAR")) || (lexEqual("RİGHTPAR")) ) ) {
                error();
            }
        } else {
            Definition();
        }
    }

    static void Definition() {
        announceSyn("Definition");

        if (lexEqual("DEFINE") || lexEqual("DEFİNE"))
            DefinitionRight();
        else
            error();

    }

    static void DefinitionRight() {
        announceSyn("DefinitionRight");

        setCurrentLex();

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            Expression();
        } else if (lexEqual("LEFTPAR")) {
            setCurrentLex();
            if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
                ArgList();
                if (lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR"))
                    Statements();
                else
                    error();
            } else
                error();
        } else
            error();

    }

    static void ArgList() {
        announceSyn("ArgList");

        setCurrentLex();

        if (lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")) {
            ArgList();
        }
    }

    static void Statements() {
        announceSyn("Statements");

        setCurrentLex();

        if (lexEqual("DEFINE") || lexEqual("DEFİNE")) {
            Definition();
            Statements();
        } else {
            Expression();
        }
    }

    // Enes Part End

    // Efe Part Start
    static void Expressions() {
        announceSyn("Expressions");

        setCurrentLex();

        if (lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
                || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN") || lexEqual("LEFTPAR")) {
                    Expression();
                    Expressions();
        }
        else{
            return;
        }

    }

    static void Expression() {
        announceSyn("Expression");



        if(lexEqual("LEFTPAR")){
            Expr();

            setCurrentLex();

            if( !(lexEqual("RİGHTPAR") || lexEqual("RIGHTPAR") ) ){
                error();
            }
        }
        else if( !( lexEqual("İDENTİFİER") || lexEqual("IDENTIFIER") || lexEqual("NUMBER") || lexEqual("CHAR")
        || lexEqual("STRING") || lexEqual("STRİNG") || lexEqual("BOOLEAN") ) ){
            error();
        }
        

    }

    static void Expr() {
        announceSyn("Expr");

        setCurrentLex();

        if(lexEqual("LET")){
            LetExpression();
        }
        else if(lexEqual("COND")){
            CondExpression();
        }
        else if(lexEqual("IF") || lexEqual("İF")){
            IfExpression();
        }
        else if(lexEqual("BEGIN") || lexEqual("BEGİN")){
            BeginExpression();
        }
        else if(lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")){
            FunCall();
        }
        else{
            error();
        }

    }

    static void FunCall() {
        announceSyn("FunCall");

        if(lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")){
            Expressions();
        }
        else{
            error();
        }
        

    }

    static void LetExpression() {
        announceSyn("LetExpression");

        if(lexEqual("LET")){
            LetExpr();
        }
        else{
            error();
        }
    }

    static void LetExpr() {
        announceSyn("LetExpr");

        setCurrentLex();

        if(lexEqual("LEFTPAR")){
            VarDefs();

            setCurrentLex();

            if(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")){
                Statements();
            }
            else{
                error();
            }
        }
        else if(lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")){
            setCurrentLex();

            if(lexEqual("LEFTPAR")){
                VarDefs();

                setCurrentLex();
                if(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")){
                    Statements();
                }
                else{
                    error();
                }
            }
        }
    }

    static void VarDefs() {
        announceSyn("VarDefs");

        setCurrentLex();

        if(lexEqual("LEFTPAR")){
            setCurrentLex();
            
            if(lexEqual("IDENTIFIER") || lexEqual("İDENTİFİER")){
                Expression();

                setCurrentLex();
                if(lexEqual("RIGHTPAR") || lexEqual("RİGHTPAR")){
                    VarDef();
                }
                else{
                    error();
                }
            }
        }

    }

    // Efe Part End

    // Tolga Part Start
    static void VarDef() {
        announceSyn("VarDef");

    }

    static void CondExpression() {
        announceSyn("CondExpression");

        if(lexEqual("COND")){
            CondBranches();
        }
        else{
            error();
        }

    }

    static void CondBranches() {
        announceSyn("CondBranches");

    }

    static void CondBranch() {
        announceSyn("CondBranch");

    }

    static void IfExpression() {
        announceSyn("IfExpression");

        if(lexEqual("IF") || lexEqual("İF")){
            Expression();
            Expression();
            EndExpression();
        }
        else{
            error();
        }

    }

    static void EndExpression() {
        announceSyn("EndExpression");

    }

    static void BeginExpression() {
        announceSyn("BeginExpression");

        if(lexEqual("BEGIN") || lexEqual("BEGİN")){
            Statements();
        }
        else{
            error();
        }

    }

    // Tolga Part End

    static boolean lexEqual(String word) {
        return currentLex.equals(word);
    }

    static void setCurrentLex() {
        currentLex = lexList.get(currentIndex);
        currentIndex++;
    }

    static void announceSyn(String s) {
        System.out.println(s);
    }

    static void error() {
        System.out.println("Error at :" + currentIndex + " " + currentLex);
        System.exit(0);
    }

}
