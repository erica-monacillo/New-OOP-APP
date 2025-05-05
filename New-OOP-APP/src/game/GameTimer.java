package game;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameTimer {
    private int timeLeft;
    private Timer timer;
    private Runnable onTimeUp;

    public GameTimer(int initialTime) {
        this.timeLeft = initialTime;
        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft <= 0) {
                    stop();
                    if (onTimeUp != null) {
                        onTimeUp.run();
                    }
                }
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void reset() {
        timeLeft = 60;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setOnTimeUp(Runnable callback) {
        this.onTimeUp = callback;
    }
}