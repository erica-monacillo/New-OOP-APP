package utils;

import model.CodeQuestion;
import model.GameLevel;

import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<GameLevel> loadLevels() {
        List<GameLevel> levels = new ArrayList<>();
        levels.add(new GameLevel(new CodeQuestion(
            "Fill in the blank: System.out.____(\"Hello World\");",
            "println", // Correct answer
            "ptirln"   // Available letters
        )));
        levels.add(new GameLevel(new CodeQuestion(
            "Complete the loop: for(int i = 0; i < 10; i____)",
            "++)",
            "+)"
        )));
        return levels;
    }
}