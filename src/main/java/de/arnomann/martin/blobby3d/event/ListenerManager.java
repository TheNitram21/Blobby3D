package de.arnomann.martin.blobby3d.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private static final List<EventListener> listeners = new ArrayList<>();

    private ListenerManager() {}

    public static void callEvent(Event event) {
        EventListener[] listenersCopy = listeners.toArray(new EventListener[0]);

        for(EventListener listener : listenersCopy) {
            try {
                String eventMethod = "on" + event.getClass().getSimpleName().replace("Event", "");

                listener.getClass().getMethod(eventMethod, event.getClass()).invoke(listener, event);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }
    }

    public static void registerEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public static void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

}
