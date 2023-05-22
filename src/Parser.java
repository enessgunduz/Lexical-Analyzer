import java.util.ArrayList;

public class Parser {

    static ArrayList<String> lexList = new ArrayList<>();
    static int currentIndex = 0;
    static String currentLex;
    public static void StartParse(ArrayList<String> lexs){
        lexList=lexs;
        Program();
    }

    //Enes Part Start
    static void Program(){
        announceSyn("Program");

        setCurrentLex();

        if (lexEqual("LEFTPAR")){
            TopLevelForm();
            Program();
        } else {
            return;
        }
    }

    static void TopLevelForm(){
        announceSyn("TopLevelForm");

        SecondLevelForm();

        if (!lexEqual("RIGHTPAR")){
            error();
        }
    }

    static void SecondLevelForm(){
        announceSyn("SecondLevelForm");

        setCurrentLex();

        if (lexEqual("LEFTPAR")){
            FunCall();
            if (!(lexEqual("RIGHTPAR"))){
                error();
            }
        } else {
            Definition();
        }
    }

    static void Definition(){
        announceSyn("Definition");

        if (lexEqual("DEFINE"))
            DefinitionRight();
        else
            error();


    }

    static void DefinitionRight(){
        announceSyn("DefinitionRight");

        setCurrentLex();

        if (lexEqual("IDENTIFIER")){
            Expression();
        } else if (lexEqual("LEFTPAR")) {
            setCurrentLex();
            if (lexEqual("IDENTIFIER")){
                ArgList();
                if (lexEqual("RIGHTPAR"))
                    Statements();
                else
                    error();
            } else
                error();
        } else
            error();

    }

    static void ArgList(){
        announceSyn("ArgList");

        setCurrentLex();

        if (lexEqual("IDENTIFIER")){
            ArgList();
        }
    }

    static void Statements(){
        announceSyn("Statements");

        setCurrentLex();

        if (lexEqual("DEFINE")){
            Definition();
            Statements();
        } else {
            Expression();
        }
    }

    //Enes Part End

    //Efe Part Start
    static void Expressions(){
        announceSyn("Expressions");

    }

    static void Expression(){
        announceSyn("Expression");

    }

    static void Expr(){
        announceSyn("Expr");

    }

    static void FunCall(){
        announceSyn("FunCall");

    }

    static void LetExpression(){
        announceSyn("LetExpression");

    }

    static void LetExpr(){
        announceSyn("LetExpr");

    }

    static void VarDefs(){
        announceSyn("VarDefs");

    }

    //Efe Part End

    //Tolga Part Start
    static void VarDef(){
        announceSyn("VarDef");

    }

    static void CondExpression(){
        announceSyn("CondExpression");

    }

    static void CondBranches(){
        announceSyn("CondBranches");

    }

    static void CondBranch(){
        announceSyn("CondBranch");

    }

    static void IfExpression(){
        announceSyn("IfExpression");

    }

    static void EndExpression(){
        announceSyn("EndExpression");

    }

    static void BeginExpression(){
        announceSyn("BeginExpression");

    }

    //Tolga Part End

    static boolean lexEqual(String word){
        return currentLex.equals(word);
    }
    static void setCurrentLex(){
        currentLex = lexList.get(currentIndex);
        currentIndex++;
    }
    static void announceSyn(String s){
        System.out.println(s);
    }
    static void error(){
        System.out.println("Error at :"+currentIndex+" "+currentLex);
        System.exit(0);
    }



}
