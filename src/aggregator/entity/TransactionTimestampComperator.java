package aggregator.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TransactionTimestampComperator implements Comparator<Transaction> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public int compare(Transaction o1, Transaction o2) {
        LocalDateTime time1 = LocalDateTime.parse(o1.getTimestamp(), formatter);
        LocalDateTime time2 = LocalDateTime.parse(o2.getTimestamp(), formatter);

        return time2.compareTo(time1);
    }
}