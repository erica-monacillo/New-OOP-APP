package model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class CodeQuestion {
    private String questionText;
    private String answer;

    public CodeQuestion(String questionText, String answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAvailableLetters() {
        List<Character> chars = new ArrayList<>();
        for (char c : answer.toCharArray()) {
            chars.add(c);
        }

        // Add extra random letters
        for (char c : "xyzqwe".toCharArray()) {
            chars.add(c);
        }

        Collections.shuffle(chars);
        StringBuilder shuffled = new StringBuilder();
        for (char c : chars) {
            shuffled.append(c);
        }

        return shuffled.toString();
    }
}
