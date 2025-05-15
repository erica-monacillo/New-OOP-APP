package game;

import model.CodeQuestion;
import model.Player;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<CodeQuestion> questions;
    private int currentLevel = 0;
    private Player player;  // Added Player field

    public GameController() {
        questions = new ArrayList<>();
        // Java Fundamentals
        questions.add(new CodeQuestion("int[] numbers = {1, 2, 3, 4, 5};\nSystem.out.println(numbers[____]);", "0"));
        questions.add(new CodeQuestion("public class Example {\n    public static ____ main(String[] args) {\n        // code here\n    }\n}", "void"));
        questions.add(new CodeQuestion("boolean isActive = ____;\nif(isActive) {\n    System.out.println(\"Active\");\n}", "true"));
        questions.add(new CodeQuestion("String text = \"Java Programming\";\nSystem.out.println(text.____(\"Java\", \"Python\"));", "replace"));
        questions.add(new CodeQuestion("try {\n    int result = 10 / 0;\n} catch(____ e) {\n    System.out.println(\"Error: \" + e.getMessage());\n}", "ArithmeticException"));

        // OOP Concepts
        questions.add(new CodeQuestion("class Animal {\n    public void makeSound() {}\n}\n\nclass Dog ____ Animal {\n    // Dog code\n}", "extends"));
        questions.add(new CodeQuestion("interface Drawable {\n    void ____();\n}", "draw"));
        questions.add(new CodeQuestion("class Car {\n    private String model;\n    \n    public ____(String model) {\n        this.model = model;\n    }\n}", "Car"));
        questions.add(new CodeQuestion("public class Singleton {\n    private static Singleton instance;\n    \n    private Singleton() {}\n    \n    public static Singleton getInstance() {\n        if(instance == null) {\n            instance = new ____();\n        }\n        return instance;\n    }\n}", "Singleton"));

        // Collections and Data Structures
        questions.add(new CodeQuestion("ArrayList<String> names = new ArrayList<>();\nnames.____(\"John\");", "add"));
        questions.add(new CodeQuestion("HashMap<String, Integer> ages = new ____<>();", "HashMap"));
        questions.add(new CodeQuestion("for(String name : ____) {\n    System.out.println(name);\n}", "names"));
        questions.add(new CodeQuestion("List<Integer> numbers = Arrays.____(new Integer[]{1, 2, 3});", "asList"));

        // Control Flow
        questions.add(new CodeQuestion("int x = 10;\n____ (x > 5) {\n    System.out.println(\"Greater than 5\");\n}", "if"));
        questions.add(new CodeQuestion("for(int i = 0; ____ < 5; i++) {\n    System.out.println(i);\n}", "i"));
        questions.add(new CodeQuestion("int day = 2;\nswitch(day) {\n    case 1:\n        System.out.println(\"Monday\");\n        ____;\n    case 2:\n        System.out.println(\"Tuesday\");\n        break;\n}", "break"));
        questions.add(new CodeQuestion("int count = 0;\n____ (count < 5) {\n    System.out.println(count);\n    count++;\n}", "while"));

        // String Manipulation
        questions.add(new CodeQuestion("String message = \"Hello World\";\nSystem.out.println(message.____()); // Outputs: hello world", "toLowerCase"));
        questions.add(new CodeQuestion("String[] parts = message.____(\" \");", "split"));
        questions.add(new CodeQuestion("boolean startsWithHello = message.____(\"Hello\");", "startsWith"));

        // Modern Java Features
        questions.add(new CodeQuestion("List<String> names = Arrays.asList(\"Alice\", \"Bob\", \"Charlie\");\nnames.stream().____(System.out::println);", "forEach"));
        questions.add(new CodeQuestion("Optional<String> optional = Optional.____(\"Hello\");", "of"));
        questions.add(new CodeQuestion("Predicate<Integer> isEven = n -> n % 2 == ____;", "0"));
        questions.add(new CodeQuestion("var greeting = \"Hello\";\nvar count = ____;  // Integer literal", "10"));

        // File Operations
        questions.add(new CodeQuestion("try (FileReader reader = new FileReader(\"____\")) {\n    // Read file\n}", "filename.txt"));
        questions.add(new CodeQuestion("try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {\n    writer.____(\"Hello World\");\n}", "write"));

        // Exception Handling
        questions.add(new CodeQuestion("try {\n    // Code that might throw exception\n} catch (Exception e) {\n    // Handle exception\n} ____ {\n    // Always executes\n}", "finally"));

        // Multithreading
        questions.add(new CodeQuestion("Thread thread = new Thread(() -> {\n    System.out.println(\"Running in a separate ____\");\n});", "thread"));
        questions.add(new CodeQuestion("public class Counter {\n    private int count = 0;\n    \n    public ____ void increment() {\n        count++;\n    }\n}", "synchronized"));

        // Design Patterns
        questions.add(new CodeQuestion("// Builder Pattern\nUser user = new User.Builder()\n    .setName(\"John\")\n    .setAge(30)\n    .____();\n", "build"));
        
        // Initialize with a default player
        player = new Player("Player 1");
    }

    /**
     * Get the current player
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the current player
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Start a specific level
     * @param levelNumber the level to start
     */
    public void startLevel(int levelNumber) {
        currentLevel = levelNumber - 1; // Adjust zero-based index
        
        // Update player's highest level if needed
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
    }

    /**
     * Get the current question for the selected level
     * @return the current code question
     */
    public CodeQuestion getCurrentQuestion() {
        // Make sure currentLevel is within bounds
        if (currentLevel < 0 || currentLevel >= questions.size()) {
            currentLevel = 0;
        }
        return questions.get(currentLevel);
    }

    /**
     * Check if the provided answer is correct
     * @param answer the player's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(String answer) {
        return getCurrentQuestion().getAnswer().equalsIgnoreCase(answer);
    }

    /**
     * Advance to the next level and update player progress
     * @return the next level number
     */
    public int getNextLevel() {
        currentLevel++;
        if (currentLevel >= questions.size()) currentLevel = 0;
        
        // Update player's level if they've advanced
        int levelNumber = currentLevel + 1; // Convert to 1-based index
        if (levelNumber > player.getCurrentLevel()) {
            player.setCurrentLevel(levelNumber);
        }
        
        return currentLevel + 1; // Return 1-based level number
    }
    
    /**
     * Award points to the player for correct answers
     * @param points number of points to award
     */
    public void awardPoints(int points) {
        player.setScore(player.getScore() + points);
    }
}