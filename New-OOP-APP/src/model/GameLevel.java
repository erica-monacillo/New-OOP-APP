package model;

public class GameLevel {
    private CodeQuestion question;

    public GameLevel(CodeQuestion question) {
        this.question = question;
    }

    public CodeQuestion getQuestion() {
        return question;
    }
}