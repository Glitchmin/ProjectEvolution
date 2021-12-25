package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface responsible for managing the moves of the animals.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 */
public interface IEngine {
    /**
     * Move the animal on the map according to the provided move directions. Every
     * n-th direction should be sent to the n-th animal on the map.
     */
    List<IDayChangeObserver> dayObservers = new ArrayList<>();
    List<IPositionChangeObserver> positionObservers = new ArrayList<>();

    void addDayObserver(IDayChangeObserver observer);
    void addPositionObserver(IPositionChangeObserver observer);

    void removeDayObserver(IDayChangeObserver observer);
    void removePositionObserver(IPositionChangeObserver observer);

    void run();
}