package clia.back.clock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    private Timer timer;
    private final LocalDate initialDate;
    private final LocalTime initialTime;

    private LocalDate date;
    private LocalTime time;

    private final LocalTime maxTime;

    public Clock(LocalDate date, LocalTime time, int capsuleTime) {
        timer = new Timer();
        LocalTime maxTime = time.plusSeconds(capsuleTime);

        initialDate = date;
        initialTime = time;
        this.date = date;
        this.time = time;
        this.maxTime = maxTime;
    }

    public LocalTime getTime() {
        return time;
    }

    public void backTrack() {
        timer.cancel();
        timer = new Timer();
        final int[] i = {0};
        final int[] scale = {1};
        System.out.println("Backtracking");
        TimerTask task = new TimerTask() {
            public void run() {
                if (i[0] == 0) {
                    time = time.minusSeconds(scale[0]);
                    scale[0]++;
                } else {
                    time = time.minusSeconds(scale[0]);
                    scale[0] = scale[0] * scale[0];
                }
                i[0]++;
                if (time.truncatedTo(ChronoUnit.SECONDS).compareTo(initialTime.truncatedTo(ChronoUnit.SECONDS)) <= 0) {
                    reset();
                }
            }
        };
        timer.schedule(task, 0, 1000); // Every minute run the timer tas
    }

    public void reset() {
        date = initialDate;
        time = initialTime;
        timer.cancel();
        timer = new Timer();
        start();
    }

    public void start() {
        TimerTask task = new TimerTask() {
            public void run() {
                time = time.plusSeconds(1);
                if (time.getHour() >= 24) {
                    time = time.withHour(0);
                    date = date.plusDays(1);
                }

                System.out.println(date + " " + time);
            }
        };
        timer.schedule(task, 0, 1000); // Every minute run the timer tas
    }

    public static void main(String[] args) {
        Clock clock = new Clock(LocalDate.now(), LocalTime.now(), 10);
        clock.start();
    }
}
