package agh.ics.oop;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.*;

public class StatisticsEngine implements Runnable {
    private AbstractWorldMap map;
    private int daysCounter;
    private final Label genotypeLabel;
    private String genotypeLabelString;

    private Vector<Vector<Integer>> chartList;
    private final Vector< LineChart<Number, Number> > lineChart;
    private final Vector< XYChart.Series<Number, Number> > lineChartDataSeries;

    private List<Integer> liveSpans;


    public StatisticsEngine(AbstractWorldMap map) {
        this.map = map;
        liveSpans = new Vector<Integer>();
        genotypeLabel = new Label("");
        genotypeLabelString = "";
        daysCounter = 0;
        chartList = new Vector<>();
        for (int i=0; i<5;i++){
            chartList.add(new Vector<>());
        }
        lineChart = new Vector<>();
        lineChartDataSeries = new Vector<>();


        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> aliveAnimalsCounterLineChartPair = createLineChart("Number of days", "Number of animals", "Number of animals over time");
        lineChart.add(aliveAnimalsCounterLineChartPair.getKey());
        lineChartDataSeries.add(aliveAnimalsCounterLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> grassCounterLineChartPair = createLineChart("Number of days", "Number of grass fields", "Number of grass fields over time");
        lineChart.add(grassCounterLineChartPair.getKey());
        lineChartDataSeries.add(grassCounterLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgEnergyLineChartPair = createLineChart("Number of days", "Average energy", "Average animal energy over time");
        lineChart.add(avgEnergyLineChartPair.getKey());
        lineChartDataSeries.add(avgEnergyLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsLiveSPanLineChartPair = createLineChart("Number of days", "Average live span", "Average animal live span over time");
        lineChart.add(avgAnimalsLiveSPanLineChartPair.getKey());
        lineChartDataSeries.add(avgAnimalsLiveSPanLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsChildrenNumberLineChartPair = createLineChart("Number of days", "Average children amount", "Average animal children amount over time");
        lineChart.add(avgAnimalsChildrenNumberLineChartPair.getKey());
        lineChartDataSeries.add(avgAnimalsChildrenNumberLineChartPair.getValue());
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
        for (int i = 0; i < 5; i++) {
            updateLineChartDataSeries(lineChartDataSeries.get(i), lastElement(chartList.get(i)));
        }
        genotypeLabel.setText(genotypeLabelString);
    }

    public Integer lastElement(List<Integer> list) {
        if (list.isEmpty()) {
            return 0;
        }
        return list.get(list.size() - 1);
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

    public void addData(LineCharts lineCharts, Integer data) {
        chartList.get(lineCharts.ordinal()).add(data);
    }

    public LineChart<Number, Number> getLineChart(LineCharts lineCharts) {
        return lineChart.get(lineCharts.ordinal());
    }

    public void addDeadAnimalLiveSpan(Integer liveSpan) {
        liveSpans.add(liveSpan);
    }


    public int getAvgEnergy() {
        int energySum = 0;
        for (Animal animal : map.getAliveAnimals()) {
            energySum += animal.getEnergy();
        }
        return energySum / map.getAliveAnimalsCounter();
    }

    public int getAvgLiveSpan() {
        if (liveSpans.isEmpty()) {
            return 0;
        }
        int liveSpanSum = 0;
        for (int liveSpan : liveSpans) {
            liveSpanSum += liveSpan;
        }
        return liveSpanSum / liveSpans.size();
    }

    public int getAvgChildrenCount() {
        int childrenCount = 0;
        for (Animal animal : map.getAliveAnimals()) {
            childrenCount += animal.getChildrenCounter();
        }
        return childrenCount / map.getAliveAnimalsCounter();
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
        genotypeLabelString = dominant;
    }

    public Label getGenotypeLabel() {
        return genotypeLabel;
    }
}
