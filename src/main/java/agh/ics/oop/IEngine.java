package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public interface IEngine {

    void addDayObserver(IDayChangeObserver observer);
    void addPositionObserver(IPositionChangeObserver observer);

    void removeDayObserver(IDayChangeObserver observer);
    void removePositionObserver(IPositionChangeObserver observer);
}