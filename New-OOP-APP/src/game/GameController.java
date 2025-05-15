package game;

import model.CodeQuestion;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<CodeQuestion> questions;
    private int currentLevel = 0;

    public GameController() {
        questions = new ArrayList<>();
        questions.add(new CodeQuestion("System.out.println(\"Hello, ____!\");", "world"));
        questions.add(new CodeQuestion("for(int i = 0; i < __; i++)", "10"));
        questions.add(new CodeQuestion("String name = \"__\";", "java"));
    }

    public void startLevel(int levelNumber) {
        currentLevel = levelNumber;
    }

    public CodeQuestion getCurrentQuestion() {
        return questions.get(currentLevel);
    }

    public boolean checkAnswer(String answer) {
        return getCurrentQuestion().getAnswer().equalsIgnoreCase(answer);
    }

    public int getNextLevel() {
        currentLevel++;
        if (currentLevel >= questions.size()) currentLevel = 0;
        return currentLevel;
    }
}
