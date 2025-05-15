package view;

import game.GameController;
import model.CodeQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {
    private GameController controller;
    private JLabel questionLabel;
    private JPanel letterCirclePanel;
    private JTextField answerField;
    private List<RoundButton> letterButtons;
    private StringBuilder selectedLetters;
    private Set<RoundButton> visitedButtons;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private int score = 0;
    private Point dragEnd;
    private boolean isDragging = false;
    private List<Point> dragPath = new ArrayList<>();
    private boolean isMousePressed = false;
    private boolean selectionActive = false;  

    public GamePanel(GameController controller) {
        this.controller = controller;
        this.letterButtons = new ArrayList<>();
        this.selectedLetters = new StringBuilder();
        this.visitedButtons = new HashSet<>();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        scoreLabel = new JLabel("Score: " + score, SwingConstants.LEFT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(new Color(50, 50, 50));
        questionLabel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Make question label wrap text
        questionLabel.setPreferredSize(new Dimension(400, 100));
        questionLabel.setMinimumSize(new Dimension(200, 50));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.add(scoreLabel, BorderLayout.WEST);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Feedback label with new styling
        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 32));
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setOpaque(false);
        feedbackLabel.setVisible(false);

        // Circle of letters in the center
        letterCirclePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLetterCircle(g);
                drawSelectionLines(g);
            }

            @Override
            public void doLayout() {
                super.doLayout();
                // Recalculate positions when the panel is resized
                if (!letterButtons.isEmpty()) {
                    setupLetterCircle(controller.getCurrentQuestion().getAvailableLetters());
                }
                // Update feedback label position
                if (feedbackLabel != null) {
                    feedbackLabel.setBounds(0, 0, getWidth(), getHeight());
                    feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    feedbackLabel.setVerticalAlignment(SwingConstants.CENTER);
                }
            }
        };
        letterCirclePanel.setPreferredSize(new Dimension(400, 400));
        letterCirclePanel.setLayout(null);
        letterCirclePanel.setBackground(new Color(30, 30, 30));
        
        // Add feedback label to letter circle panel
        letterCirclePanel.add(feedbackLabel);
        feedbackLabel.setBounds(0, 0, letterCirclePanel.getWidth(), letterCirclePanel.getHeight());
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        add(letterCirclePanel, BorderLayout.CENTER);
    }

    private void resetSelection() {
        selectedLetters.setLength(0);
        visitedButtons.clear();
        feedbackLabel.setVisible(false);
        dragPath.clear();
        selectionActive = false;
        isMousePressed = false;
        isDragging = false;
        dragEnd = null;
        resetAllButtonColors();
        letterCirclePanel.repaint();
    }

    private void resetAllButtonColors() {
        for (RoundButton button : letterButtons) {
            button.setBackground(new Color(50, 50, 50));
        }
    }

    private void finishSelection() {
        isMousePressed = false;
        isDragging = false;
        dragEnd = null;
        selectionActive = true;
        letterCirclePanel.repaint();
    }

    private void drawSelectionLines(Graphics g) {
        if (dragPath.isEmpty()) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the connecting lines between selected letters
        if (dragPath.size() > 1) {
            // Draw the solid path between letters
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));
            
            // Draw lines between consecutive points
            for (int i = 0; i < dragPath.size() - 1; i++) {
                Point p1 = dragPath.get(i);
                Point p2 = dragPath.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        
        // Draw the current drag line to cursor (only when actively dragging)
        if (isDragging && dragPath.size() > 0 && dragEnd != null) {
            Point lastPoint = dragPath.get(dragPath.size() - 1);
            g2d.setColor(new Color(255, 0, 0, 150)); // Semi-transparent red
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(lastPoint.x, lastPoint.y, dragEnd.x, dragEnd.y);
        }
        
        // Draw the path points with a glowing effect
        for (Point p : dragPath) {
            // Draw outer glow
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.fillOval(p.x - 12, p.y - 12, 24, 24);
            // Draw middle glow
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.fillOval(p.x - 8, p.y - 8, 16, 16);
            // Draw inner point
            g2d.setColor(new Color(255, 0, 0, 200));
            g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
        }
    }

    public void startLevel(int levelNumber) {
        controller.startLevel(levelNumber);
        CodeQuestion question = controller.getCurrentQuestion();
        setupLetterCircle(question.getAvailableLetters());
        updateQuestionDisplay();
        resetSelection(); // Reset the selection state for the new level
    }

    private void updateQuestionDisplay() {
        CodeQuestion question = controller.getCurrentQuestion();
        String questionText = question.getQuestionText();
        
        // Replace all underscores with selected letters or show blank lines
        String displayText = questionText;
        int underscoreIndex = displayText.indexOf("____");
        if (underscoreIndex != -1) {
            String selectedText = selectedLetters.toString();
            if (selectedText.isEmpty()) {
                // Show blank lines when no letters are selected
                // Calculate the width based on the expected answer length
                String expectedAnswer = question.getAnswer();
                int blankWidth = Math.max(4, expectedAnswer.length()); // Minimum 4 characters
                String blankSpace = "_".repeat(blankWidth);
                displayText = displayText.substring(0, underscoreIndex) + 
                             "<span style='color: #666666; border-bottom: 2px solid #666666;'>" + blankSpace + "</span>" + 
                             displayText.substring(underscoreIndex + 4);
            } else {
                // Show selected letters with a different style
                displayText = displayText.substring(0, underscoreIndex) + 
                             "<span style='color: #00ff00; font-weight: bold;'>" + selectedText + "</span>" + 
                             displayText.substring(underscoreIndex + 4);
            }
        }
        
        // Format the text with proper spacing and line breaks
        displayText = displayText.replace("\n", "<br>");
        
        // Create a word-wrapped display with proper formatting
        String formattedText = "<html><div style='width: 100%;'>" +
                             "<div style='color: white; font-family: monospace; font-size: 14px; " +
                             "white-space: normal; word-wrap: break-word; text-align: left; padding: 10px;'>" + 
                             displayText + "</div></div></html>";
        
        questionLabel.setText(formattedText);
        letterCirclePanel.repaint();
    }

    private void setupLetterCircle(String letters) {
        letterCirclePanel.removeAll();
        letterButtons.clear();
        selectedLetters.setLength(0);
        visitedButtons.clear();
        dragPath.clear();
        dragEnd = null;
        isMousePressed = false;
        isDragging = false;
        selectionActive = false;

        // Get the panel dimensions
        int panelWidth = letterCirclePanel.getWidth();
        int panelHeight = letterCirclePanel.getHeight();
        
        // Calculate center point
        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2;

        // Calculate positions first to ensure consistent placement
        List<Point> positions = new ArrayList<>();
        double angleStep = 2 * Math.PI / letters.length();
        
        // Adjust radius based on number of letters
        int radius;
        if (letters.length() > 12) {
            radius = 220; // Larger radius for more letters
        } else if (letters.length() < 4) {
            radius = 120; // Smaller radius for fewer letters
        } else {
            radius = 150; // Default radius
        }

        // Ensure the circle fits within the panel
        int maxRadius = Math.min(panelWidth, panelHeight) / 2 - 30; // Leave some margin
        radius = Math.min(radius, maxRadius);

        for (int i = 0; i < letters.length(); i++) {
            double angle = i * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle)) - 25;
            int y = (int) (centerY + radius * Math.sin(angle)) - 25;
            positions.add(new Point(x, y));
        }

        // Create buttons with fixed positions
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            Point pos = positions.get(i);

            RoundButton letterButton = new RoundButton(String.valueOf(letter));
            letterButton.setBounds(pos.x, pos.y, 50, 50);
            letterButton.setFont(new Font("Arial", Font.BOLD, 20));
            letterButton.setBackground(new Color(50, 50, 50));
            letterButton.setForeground(Color.WHITE);
            letterButton.setFocusPainted(false);
            
            // Add modified mouse listeners
            LetterButtonListener buttonListener = new LetterButtonListener(letterButton, letter);
            letterButton.addMouseListener(buttonListener);
            letterButton.addMouseMotionListener(buttonListener);
            
            letterButtons.add(letterButton);
            letterCirclePanel.add(letterButton);
        }

        // Add feedback label and center it
        letterCirclePanel.add(feedbackLabel);
        feedbackLabel.setBounds(0, 0, letterCirclePanel.getWidth(), letterCirclePanel.getHeight());
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setVerticalAlignment(SwingConstants.CENTER);

        letterCirclePanel.revalidate();
        letterCirclePanel.repaint();
    }

    private void drawLetterCircle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Get the panel dimensions
        int panelWidth = letterCirclePanel.getWidth();
        int panelHeight = letterCirclePanel.getHeight();
        
        // Calculate center point
        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2;
        
        // Calculate radius based on number of letters
        int radius;
        if (letterButtons.size() > 12) {
            radius = 220;
        } else if (letterButtons.size() < 4) {
            radius = 120;
        } else {
            radius = 150;
        }

        // Ensure the circle fits within the panel
        int maxRadius = Math.min(panelWidth, panelHeight) / 2 - 30; // Leave some margin
        radius = Math.min(radius, maxRadius);
        
        g2d.setColor(new Color(70, 130, 180));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private void onSubmit(java.awt.event.ActionEvent e) {
        String userAnswer = selectedLetters.toString();
        if (controller.checkAnswer(userAnswer)) {
            feedbackLabel.setText("Correct!");
            feedbackLabel.setForeground(Color.GREEN);
            feedbackLabel.setVisible(true);
            
            // Award points to the player
            controller.awardPoints(10); // Award 10 points for correct answer
            
            // Update score display
            updateScoreDisplay();
            
            // Delay next level by 1 second
            Timer timer = new Timer(1000, evt -> {
                startLevel(controller.getNextLevel());
                feedbackLabel.setVisible(false); // Hide feedback after next level starts
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            feedbackLabel.setText("Incorrect. Try again.");
            feedbackLabel.setForeground(Color.RED);
            feedbackLabel.setVisible(true);
            
            // Only deduct points if score is above zero
            int currentScore = controller.getPlayer().getScore();
            if (currentScore > 0) {
                int deduction = Math.min(2, currentScore);
                controller.awardPoints(-deduction);
            }
            updateScoreDisplay();
            
            // Hide feedback after 1.5 seconds
            Timer timer = new Timer(1500, evt -> {
                feedbackLabel.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    // Method to update the score display
    private void updateScoreDisplay() {
        scoreLabel.setText("Score: " + controller.getPlayer().getScore());
    }

    // Custom round button class
    private class RoundButton extends JButton {
        public RoundButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillOval(0, 0, getWidth(), getHeight());
            
            // Draw text
            FontMetrics metrics = g2.getFontMetrics(getFont());
            Rectangle2D stringBounds = metrics.getStringBounds(getText(), g2);
            int x = (getWidth() - (int) stringBounds.getWidth()) / 2;
            int y = (getHeight() - (int) stringBounds.getHeight()) / 2 + metrics.getAscent();
            
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (getModel().isRollover()) {
                return super.contains(x, y);
            } else {
                int radius = Math.min(getWidth(), getHeight()) / 2;
                return Point.distance(x, y, getWidth() / 2, getHeight() / 2) <= radius;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int diameter = Math.max(size.width, size.height);
            return new Dimension(diameter, diameter);
        }
    }

    private class LetterButtonListener extends MouseAdapter {
        private RoundButton button;
        private char letter;

        public LetterButtonListener(RoundButton button, char letter) {
            this.button = button;
            this.letter = letter;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectionActive) {
                resetSelection();
            }
            
            // Start the selection process
            isMousePressed = true;
            isDragging = true;
            
            // Add this button as first selection
            selectButton();
            
            // Update drag points
            Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
            dragEnd = center;
            dragPath.add(center);
            
            updateQuestionDisplay();
            letterCirclePanel.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isMousePressed && !visitedButtons.contains(button)) {
                // Add this letter to selection when hovering while pressed
                selectButton();
                
                // Update drag path
                Point center = new Point(button.getX() + button.getWidth()/2, button.getY() + button.getHeight()/2);
                dragPath.add(center);
                dragEnd = center;
                
                updateQuestionDisplay();
                letterCirclePanel.repaint();
            } else if (!isMousePressed && !visitedButtons.contains(button)) {
                button.setBackground(new Color(100, 100, 100));
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (!visitedButtons.contains(button)) {
                button.setBackground(new Color(50, 50, 50));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isMousePressed) {
                finishSelection();
                // Check the answer
                String userAnswer = selectedLetters.toString();
                if (controller.checkAnswer(userAnswer)) {
                    feedbackLabel.setText("Correct!");
                    feedbackLabel.setForeground(Color.GREEN);
                    feedbackLabel.setVisible(true);
                    feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    // Award points to the player
                    controller.awardPoints(10); // Award 10 points for correct answer
                    
                    // Update score display
                    updateScoreDisplay();
                    
                    // Delay next level by 1 second
                    Timer timer = new Timer(1000, evt -> {
                        int nextLevel = controller.getNextLevel();
                        startLevel(nextLevel);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    feedbackLabel.setText("Incorrect!");
                    feedbackLabel.setForeground(Color.RED);
                    feedbackLabel.setVisible(true);
                    feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    // Only deduct points if score is above zero
                    int currentScore = controller.getPlayer().getScore();
                    if (currentScore > 0) {
                        int deduction = Math.min(2, currentScore);
                        controller.awardPoints(-deduction);
                    }
                    updateScoreDisplay();
                    
                    // Hide feedback after 1.5 seconds
                    Timer timer = new Timer(1500, evt -> {
                        feedbackLabel.setVisible(false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging) {
                // Update the drag end point for drawing the line
                dragEnd = SwingUtilities.convertPoint(button, e.getPoint(), letterCirclePanel);
                letterCirclePanel.repaint();
            }
        }
        
        private void selectButton() {
            button.setBackground(Color.RED);
            visitedButtons.add(button);
            selectedLetters.append(letter);
        }
    }
}