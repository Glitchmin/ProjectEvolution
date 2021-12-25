package agh.ics.oop;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Vector;

import static java.lang.System.out;

public class StatisticsEngine implements Runnable {
    private AbstractWorldMap map;
    private final List<Integer> aliveAnimalsCountersList;
    private int daysCounter;

    private final LineChart<Number, Number> avgAliveAnimalsCounterLineChart;
    private final XYChart.Series<Number,Number> avgAliveAnimalsCounterLineChartDataSeries;

    public StatisticsEngine(AbstractWorldMap map){
        this.map=map;
        daysCounter=0;
        aliveAnimalsCountersList = new Vector<>();
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of days");
        yAxis.setLabel("Number of animals");
        avgAliveAnimalsCounterLineChart = new LineChart<>(xAxis,yAxis);
        avgAliveAnimalsCounterLineChart.setTitle("Number of animals over time");
        avgAliveAnimalsCounterLineChartDataSeries = new XYChart.Series<>();
        avgAliveAnimalsCounterLineChart.getData().add(avgAliveAnimalsCounterLineChartDataSeries);
        avgAliveAnimalsCounterLineChart.setCreateSymbols(false);
    }
    public int getAliveAnimalsCounter(){
        return map.getAliveAnimalCounter();
    }

    public void run(){
        updateAliveAnimalsCountersList();
    }

    public void updateAliveAnimalsCountersList(){
        aliveAnimalsCountersList.add(getAliveAnimalsCounter());
        avgAliveAnimalsCounterLineChartDataSeries.getData().add(new XYChart.Data<>(daysCounter, getAliveAnimalsCounter()));
    }
    public List<Integer> getAliveAnimalsCountersList() {
        return aliveAnimalsCountersList;
    }

    public void newDayHasCome(){
        daysCounter++;
    }

    public int getDaysCounter() {
        return daysCounter;
    }

    public LineChart<Number, Number> getAvgAliveAnimalsCounterLineChart() {
        return avgAliveAnimalsCounterLineChart;
    }
}
