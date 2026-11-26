package parkinglot.payment;

import java.time.Duration;
import java.time.LocalDateTime;

public class FeeCalculator {

    private static final double RATE_PER_HOUR = 20;

    // Charges per started hour, with a minimum of one hour.
    public double calculate(LocalDateTime inTime, LocalDateTime exitTime) {
        long minutes = Duration.between(inTime, exitTime).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60);
        return hours * RATE_PER_HOUR;
    }
}
