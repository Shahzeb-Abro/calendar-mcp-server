package com.shahzeb.calendarmcp.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarTools {
    private final Calendar calendarService;

    public CalendarTools(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    @Tool(name = "listEvents", description = "List upcoming Google Calendar events from all calendars")
    public String listEvents(
            @ToolParam(description = "Maximum number of events to return", required = false) Integer maxResults
    ) throws Exception {
        int limit = maxResults != null ? maxResults : 20;
        DateTime now = new DateTime(System.currentTimeMillis());

        // Fetch all calendars the user has
        List<CalendarListEntry> calendars = calendarService.calendarList()
                .list()
                .execute()
                .getItems();

        StringBuilder result = new StringBuilder();

        for (CalendarListEntry cal : calendars) {
            try {
                Events events = calendarService.events().list(cal.getId())
                        .setMaxResults(limit)
                        .setTimeMin(now)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

                List<Event> items = events.getItems();
                if (!items.isEmpty()) {
                    result.append("\n📅 ").append(cal.getSummary()).append(":\n");
                    items.forEach(e -> {
                        String start = e.getStart().getDateTime() != null
                                ? e.getStart().getDateTime().toString()
                                : e.getStart().getDate().toString();
                        result.append("  • ").append(e.getSummary()).append(" @ ").append(start).append("\n");
                    });
                }
            } catch (Exception e) {
                // Skip calendars we can't access
                result.append("\n⚠️ Could not access: ").append(cal.getSummary()).append("\n");
            }
        }

        return result.isEmpty() ? "No upcoming events found across any calendar." : result.toString();
    }
}
