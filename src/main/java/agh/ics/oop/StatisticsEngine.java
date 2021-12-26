package agh.ics.oop;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.*;

import static java.lang.System.out;

public class StatisticsEngine implements Runnable {
    private AbstractWorldMap map;
    private int daysCounter;
    private final Label genotypeLabel;
    private String genotypeLabelString;


    private final LineChart<Number, Number> aliveAnimalsCounterLineChart;
    private final XYChart.Series<Number, Number> aliveAnimalsCounterLineChartDataSeries;

    private final LineChart<Number, Number> grassCounterLineChart;
    private final XYChart.Series<Number, Number> grassCounterLineChartDataSeries;

    private final LineChart<Number, Number> avgEnergyLineChart;
    private final XYChart.Series<Number, Number> avgEnergyLineChartDataSeries;

    private final LineChart<Number, Number> avgAnimalsLiveSPanLineChart;
    private final XYChart.Series<Number, Number> avgAnimalsLiveSPanLineChartDataSeries;

    private final LineChart<Number, Number> avgAnimalsChildrenNumberLineChart;
    private final XYChart.Series<Number, Number> avgAnimalsChildrenNumberLineChartDataSeries;

    private final List<Integer> animalsLiveSpan;


    public StatisticsEngine(AbstractWorldMap map) {
        this.map = map;
        animalsLiveSpan = new Vector<Integer>();
        genotypeLabel = new Label("");
        genotypeLabelString = "";
        daysCounter = 0;
        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> aliveAnimalsCounterLineChartPair = createLineChart("Number of days", "Number of animals", "Number of animals over time");
        aliveAnimalsCounterLineChart = aliveAnimalsCounterLineChartPair.getKey();
        aliveAnimalsCounterLineChartDataSeries = aliveAnimalsCounterLineChartPair.getValue();

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> grassCounterLineChartPair = createLineChart("Number of days", "Number of grass fields", "Number of grass fields over time");
        grassCounterLineChart = grassCounterLineChartPair.getKey();
        grassCounterLineChartDataSeries = grassCounterLineChartPair.getValue();

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgEnergyLineChartPair = createLineChart("Number of days", "Average energy", "Average animal energy over time");
        avgEnergyLineChart = avgEnergyLineChartPair.getKey();
        avgEnergyLineChartDataSeries = avgEnergyLineChartPair.getValue();

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsLiveSPanLineChartPair = createLineChart("Number of days", "Average live span", "Average animal live span over time");
        avgAnimalsLiveSPanLineChart = avgAnimalsLiveSPanLineChartPair.getKey();
        avgAnimalsLiveSPanLineChartDataSeries = avgAnimalsLiveSPanLineChartPair.getValue();

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsChildrenNumberLineChartPair = createLineChart("Number of days", "Average children amount", "Average animal children amount over time");
        avgAnimalsChildrenNumberLineChart = avgAnimalsChildrenNumberLineChartPair.getKey();
        avgAnimalsChildrenNumberLineChartDataSeries = avgAnimalsChildrenNumberLineChartPair.getValue();
    }

    private Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> createLineChart(String xAxisLabel, String yAxisLabel, String chartTitle) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(chartTitle);
        XYChart.Series<Number, Number> lineChartDataSeries = new XYChart.Series<>();
        lineChart.getData().add(lineChartDataSeries);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        return new Pair<>(lineChart, lineChartDataSeries);
    }


    public void run() {
        updateLineChartDataSeries(aliveAnimalsCounterLineChartDataSeries, map.getAliveAnimalsCounter());
        updateLineChartDataSeries(grassCounterLineChartDataSeries, map.getGrassCounter());
        updateLineChartDataSeries(avgEnergyLineChartDataSeries, getAvgEnergy());
        updateLineChartDataSeries(avgAnimalsLiveSPanLineChartDataSeries, getAvgLiveSpan());
        updateLineChartDataSeries(avgAnimalsChildrenNumberLineChartDataSeries, getAvgChildrenCount());
        genotypeLabel.setText(genotypeLabelString);
    }

    public void updateLineChartDataSeries(XYChart.Series<Number, Number> lineChartDataSeries, int newValue) {
        lineChartDataSeries.getData().add(new XYChart.Data<>(daysCounter, newValue));
    }


    public void newDayHasCome() {
        daysCounter++;
    }

    public int getDaysCounter() {
        return daysCounter;
    }

    public void addDeadAnimalLiveSpan(Integer liveSpan){
        animalsLiveSpan.add(liveSpan);
    }

    public LineChart<Number, Number> getAliveAnimalsCounterLineChart() {
        return aliveAnimalsCounterLineChart;
    }

    public LineChart<Number, Number> getGrassCounterLineChart() {
        return grassCounterLineChart;
    }

    public LineChart<Number, Number> getAvgEnergyLineChart() {
        return avgEnergyLineChart;
    }

    public LineChart<Number, Number> getAvgAnimalsLiveSPanLineChart() {
        return avgAnimalsLiveSPanLineChart;
    }

    public LineChart<Number, Number> getAvgAnimalsChildrenNumberLineChart() {
        return avgAnimalsChildrenNumberLineChart;
    }

    private int getAvgEnergy() {
        int energySum = 0;
        for (Animal animal : map.getAliveAnimals()) {
            energySum += animal.getEnergy();
        }
        return energySum / map.getAliveAnimalsCounter();
    }

    private int getAvgLiveSpan(){
        if (animalsLiveSpan.isEmpty()){
            return 0;
        }
        int liveSpanSum = 0;
        for (int liveSpan: animalsLiveSpan) {
            liveSpanSum += liveSpan;
        }
        return liveSpanSum/animalsLiveSpan.size();
    }

    private int getAvgChildrenCount(){
        int childrenCount=0;
        for (Animal animal : map.getAliveAnimals()){
            childrenCount += animal.getChildrenCounter();
        }
        return childrenCount/map.getAliveAnimalsCounter();

    }

    public void updateMostPopularGenotype() {
        Map<String, Integer> genotypesCounterMap = new TreeMap<>();
        for (Animal animal : map.getAliveAnimals()) {
            genotypesCounterMap.putIfAbsent(Arrays.toString(animal.getGenotype()), 0);
            Integer currValue = genotypesCounterMap.get(Arrays.toString(animal.getGenotype()));
            genotypesCounterMap.put(Arrays.toString(animal.getGenotype()), currValue + 1);

        }
        int maxi = 0;
        String dominant = "";
        for (String genotype : genotypesCounterMap.keySet()) {
            if (genotypesCounterMap.get(genotype) != null && genotypesCounterMap.get(genotype) > maxi) {
                dominant = genotype;
                maxi = genotypesCounterMap.get(genotype);
            }
        }
        genotypeLabelString = "dominant genotype: " + dominant;
    }

    public Label getGenotypeLabel() {
        return genotypeLabel;
    }
}
