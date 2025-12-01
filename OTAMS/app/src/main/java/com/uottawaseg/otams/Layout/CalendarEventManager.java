package com.uottawaseg.otams.Layout;

import java.util.HashMap;
import java.util.Map;

public class CalendarEventManager {
    private static CalendarEventManager instance;

    private final Map<String, Event> events= new HashMap<>();

    private CalendarEventManager() {
    }

    public static CalendarEventManager getInstance() {
        if (instance==null){
            instance= new CalendarEventManager();
        }
        return instance;
    }
    public Event getEvent(String key) {
        return events.get(key);
    }

    public void addEvent(String key, Event event) {
        events.put(key, event);
    }

    public static class Event {
        public final String symbol;
        public final String note;
        public Event(String symbol, String note) {
            this.symbol= symbol;
            this.note= note;
        }
    }
}
