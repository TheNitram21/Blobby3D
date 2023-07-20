package de.arnomann.martin.blobby3d.event;

public interface EventListener {
    default void onStart(StartEvent event) {}
    default void onStop(StopEvent event) {}
    default void onUpdate(UpdateEvent event) {}
    default void onLateUpdate(LateUpdateEvent event) {}
    default void onKeyPressed(KeyPressedEvent event) {}
    default void onKeyReleased(KeyReleasedEvent event) {}
    default void onMouseButtonPressed(MouseButtonPressedEvent event) {}
    default void onMouseButtonReleased(MouseButtonReleasedEvent event) {}
    default void onCursorPositionChanged(CursorPositionChangedEvent event) {}
    default void onWindowResized(WindowResizedEvent event) {}
}
