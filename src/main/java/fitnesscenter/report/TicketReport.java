package fitnesscenter.report;

import java.time.Duration;

public class TicketReport {
    private final int totalVisits;
    private final Duration totalDuration;

    public TicketReport() {
        this(0, Duration.ZERO);
    }

    public TicketReport(int totalVisits, Duration totalDuration) {
        this.totalVisits = totalVisits;
        this.totalDuration = totalDuration;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public TicketReport addVisit(Duration duration) {
        return new TicketReport(totalVisits + 1, totalDuration.plus(duration));
    }

    public TicketReport mergeReports(TicketReport report) {
        return new TicketReport(totalVisits + report.totalVisits, totalDuration.plus(report.totalDuration));
    }

    @Override
    public String toString() {
        return " Total visits : " + totalVisits + ", " +
                "  Total duration : " + totalDuration + ", " +
                "  Average visit time : " + totalDuration.dividedBy(totalVisits) + " ";
    }
}
