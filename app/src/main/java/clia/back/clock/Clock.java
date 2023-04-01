package clia.back.clock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    private final LocalDate initialDate;
    private final LocalTime initialTime;

    private LocalDate date;
    private LocalTime time;

    private final LocalTime maxTime;

    public Clock(LocalDate date, LocalTime time, int capsuleTime) {
        LocalTime maxTime = time.plusSeconds(capsuleTime);

        initialDate = date;
        initialTime = time;
        this.date = date;
        this.time = time;
        this.maxTime = maxTime;
    }

    public void reset() {
        date = initialDate;
        time = initialTime;
    }

    public LocalTime getTime() {
        return time;
    }

    public void start() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                time = time.plusSeconds(1);
                if (time.getHour() >= 24) {
                    time = time.withHour(0);
                    date = date.plusDays(1);
                }
                if (time.truncatedTo(ChronoUnit.SECONDS).equals(maxTime.truncatedTo(ChronoUnit.SECONDS))) {
                    reset();
                }
                // System.out.println(String.format("%s %s",
                //        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                //        time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
            }
        };
        timer.schedule(task, 0, 1000); // Every minute run the timer task
    }
}
