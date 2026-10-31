package lk.jiat.smartcity360.policy;

import java.time.LocalTime;

/**
 * Terminal expression verifying if the context time of day falls within an allowable time window,
 * with full support for overnight intervals spanning midnight.
 */
public class TimeWindow implements PolicyExpression {

    private final LocalTime start;
    private final LocalTime end;

    public TimeWindow(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time cannot be null");
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean interpret(PolicyContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("PolicyContext cannot be null");
        }
        LocalTime current = ctx.getTime();
        if (start.isBefore(end)) {
            return !current.isBefore(start) && current.isBefore(end);
        } else if (start.isAfter(end)) {
            return !current.isBefore(start) || current.isBefore(end);
        } else {
            return current.equals(start);
        }
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
