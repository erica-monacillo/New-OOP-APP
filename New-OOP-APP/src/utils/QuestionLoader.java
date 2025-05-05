package utils;

import model.CodeQuestion;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<CodeQuestion> loadQuestionsForLevel(int level) {
        List<CodeQuestion> questions = new ArrayList<>();
        
        // In a real game, you would load these from files or a database
        switch (level) {
            case 1:
                questions.add(new CodeQuestion(
                    "Complete the for loop:\nfor (int i = 0; i < 10; _____) {\n    System.out.println(i);\n}",
                    "i++",
                    "This increments the counter",
                    1
                ));
                break;
            case 2:
                questions.add(new CodeQuestion(
                    "Fill in the condition:\nif (_____) {\n    System.out.println(\"Even\");\n}",
                    "num % 2 == 0",
                    "Checks if number is even",
                    2
                ));
                break;
            // Add more levels as needed
        }
        
        return questions;
    }
}