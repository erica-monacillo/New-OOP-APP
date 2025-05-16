package game;

import model.CodeQuestion;
import model.Player;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<CodeQuestion> questions;
    private int currentLevel = 0;
    private Player player;

    public GameController() {
        questions = new ArrayList<>();
        
        // Easy Questions (1-10)
        questions.add(new CodeQuestion("What access modifier allows access within the same package? ____", "default"));
        questions.add(new CodeQuestion("What keyword is used to create a subclass? ____ class MyClass extends BaseClass {}", "extends"));
        questions.add(new CodeQuestion("What annotation is used to override a method? @____", "Override"));
        questions.add(new CodeQuestion("What keyword is used to define a constant in Java? ____ int MAX = 100;", "final"));
        questions.add(new CodeQuestion("What keyword is used to handle exceptions? ____ (IOException e) { ... }", "catch"));
        questions.add(new CodeQuestion("What keyword is used to inherit a class? public class Dog ____ Animal {}", "extends"));
        questions.add(new CodeQuestion("How do you start a for-each loop? for(____ item : list) { }", "Type"));
        questions.add(new CodeQuestion("What keyword is used to define an interface implementation? class MyClass ____ MyInterface {}", "implements"));
        questions.add(new CodeQuestion("How do you define a method that doesn't return anything? public ____ void run() {}", "static"));
        questions.add(new CodeQuestion("What keyword is used to prevent method overriding? public ____ void show() {}", "final"));
        questions.add(new CodeQuestion("What access modifier allows visibility only within the same package? ____ class MyClass {}", "default"));
        questions.add(new CodeQuestion("What is used to indicate a method throws an exception? public void read() ____ IOException { }", "throws"));

        questions.add(new CodeQuestion("What keyword is used to define a constant in Java? ____", "final"));
        questions.add(new CodeQuestion("What primitive type is used for decimal numbers? ____", "double"));
        questions.add(new CodeQuestion("What data structure uses LIFO order? ____", "stack"));
        questions.add(new CodeQuestion("What keyword is used to inherit a class in Java? ____", "extends"));
        questions.add(new CodeQuestion("What is the default value of a boolean variable? ____", "false"));
        questions.add(new CodeQuestion("What keyword is used to create an instance of a class? ____", "new"));
        questions.add(new CodeQuestion("What is the superclass of all classes in Java? ____", "object"));
        questions.add(new CodeQuestion("What keyword is used to exit a loop immediately? ____", "break"));
        questions.add(new CodeQuestion("What interface does a class implement to be comparable? ____", "comparable"));
        
        questions.add(new CodeQuestion("What keyword is used to define a class? ____ MyClass {}", "class"));
        questions.add(new CodeQuestion("Which keyword creates an object in Java? MyClass obj = ____ MyClass();", "new"));
        questions.add(new CodeQuestion("What keyword is used to exit a loop or method early? ____;", "break"));
        questions.add(new CodeQuestion("How do you start a switch statement? ____ (value) { ... }", "switch"));
        questions.add(new CodeQuestion("What keyword is used to compare values in an if statement? if(x ____ y)", "=="));
        questions.add(new CodeQuestion("What modifier allows access from anywhere? ____ int count;", "public"));
        questions.add(new CodeQuestion("Which loop executes at least once? ____ { ... } while(condition);", "do"));
        questions.add(new CodeQuestion("How do you define a constructor in Java? public ____() { }", "ClassName"));
        questions.add(new CodeQuestion("Which collection class allows duplicates and maintains insertion order? ____<String> list = new ArrayList<>();", "List"));
        questions.add(new CodeQuestion("Which data structure stores key-value pairs? ____<String, Integer> map = new HashMap<>();", "Map"));
        questions.add(new CodeQuestion("What keyword is used to refer to the current object? ____", "this"));
        questions.add(new CodeQuestion("What keyword is used to inherit from an abstract class? public class Cat ____ Animal { }", "extends"));
        questions.add(new CodeQuestion("What keyword is used to define a method without a body in an interface? ____ void run();", "default"));
        questions.add(new CodeQuestion("What annotation is used for unit testing methods in JUnit? @____", "Test"));

        // Medium Questions (11-20)
        questions.add(new CodeQuestion("What keyword allows a variable to be accessed without an instance? ____", "static"));
        questions.add(new CodeQuestion("What exception occurs when dividing by zero? ____", "arithmeticexception"));
        questions.add(new CodeQuestion("What method must be implemented from Runnable interface? ____", "run"));
        questions.add(new CodeQuestion("What collection class is synchronized by default? ____", "vector"));
        questions.add(new CodeQuestion("What annotation marks a method as overriding a superclass method? ____", "override"));
        questions.add(new CodeQuestion("What Java feature allows grouping of related classes? ____", "package"));
        questions.add(new CodeQuestion("What method is called when an object is garbage collected? ____", "finalize"));
        questions.add(new CodeQuestion("What keyword prevents a method from being overridden? ____", "final"));
        questions.add(new CodeQuestion("What interface represents a double-ended queue? ____", "deque"));
        questions.add(new CodeQuestion("What class provides thread-safe atomic operations? ____", "atomicinteger"));
        
        // Hard Questions (21-30)
        questions.add(new CodeQuestion("What design pattern uses a private constructor and static method? ____", "singleton"));
        questions.add(new CodeQuestion("What Java 8 feature allows operations on streams? ____", "lambda"));
        questions.add(new CodeQuestion("What annotation creates a compile-time dependency injection? ____", "autowired"));
        questions.add(new CodeQuestion("What class loader loads the Java core classes? ____", "bootstrap"));
        questions.add(new CodeQuestion("What memory area stores local variables and partial results? ____", "stack"));
        questions.add(new CodeQuestion("What keyword ensures visibility of changes across threads? ____", "volatile"));
        questions.add(new CodeQuestion("What JVM optimization reorders code for better performance? ____", "jit"));
        questions.add(new CodeQuestion("What garbage collector algorithm is default in Java 11? ____", "g1"));
        questions.add(new CodeQuestion("What Java feature allows type-safe enumerations? ____", "enum"));
        questions.add(new CodeQuestion("What annotation indicates a method is deprecated? ____", "deprecated"));
        player = new Player("Player 1");
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void startLevel(int levelNumber) {
        currentLevel = levelNumber - 1;
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
    }

    public CodeQuestion getCurrentQuestion() {
        if (currentLevel < 0 || currentLevel >= questions.size()) {
            currentLevel = 0;
        }
        return questions.get(currentLevel);
    }

    public boolean checkAnswer(String answer) {
        return getCurrentQuestion().getAnswer().equalsIgnoreCase(answer);
    }

    public int getNextLevel() {
        currentLevel++;
        if (currentLevel >= questions.size()) currentLevel = 0;
        
        int levelNumber = currentLevel + 1;
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
        
        return currentLevel + 1;
    }
    
    public void awardPoints(int points) {
        player.setScore(player.getScore() + points);
    }
    
    /**
     * Get the difficulty level of the current question
     * @return "Easy", "Medium", or "Hard"
     */
    public String getCurrentDifficulty() {
        if (currentLevel < 10) return "Easy";
        if (currentLevel < 20) return "Medium";
        return "Hard";
    }
}