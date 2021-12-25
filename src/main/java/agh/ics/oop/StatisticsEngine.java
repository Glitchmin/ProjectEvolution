package agh.ics.oop;

public class StatisticsEngine {
    private AbstractWorldMap map;
    StatisticsEngine(AbstractWorldMap map){
        this.map=map;
    }
    public int getAliveAnimalsCounter(){
        return map.getAliveAnimalCounter();
    }
}
