package view;

import game.GameController;
import model.CodeQuestion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {
    private GameController controller;
    private JTextArea codeArea;
    private JLabel questionLabel, timerLabel;
    private JButton submitButton;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        codeArea = new JTextArea(10, 40);
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        timerLabel = new JLabel("Time: 60", SwingConstants.CENTER);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this::onSubmit);

        add(questionLabel, BorderLayout.NORTH);
        add(new JScrollPane(codeArea), BorderLayout.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
        add(submitButton, BorderLayout.EAST);
    }

    public void startLevel(int levelNumber) {
        controller.startLevel(levelNumber);
        updateQuestionDisplay();
    }

    private void updateQuestionDisplay() {
        CodeQuestion question = controller.getCurrentQuestion();
        questionLabel.setText("<html><pre>" + question.getQuestionText() + "</pre></html>");
        codeArea.setText("");
    }

    private void onSubmit(ActionEvent e) {
        String userAnswer = codeArea.getText().trim();
        boolean isCorrect = controller.checkAnswer(userAnswer);

        if (isCorrect) {
            JOptionPane.showMessageDialog(this, "Correct! " + 
                controller.getCurrentQuestion().getExplanation());
        } else {
            JOptionPane.showMessageDialog(this, "Try again!", "Incorrect", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateTimer(int timeLeft) {
        timerLabel.setText("Time: " + timeLeft);
    }
}