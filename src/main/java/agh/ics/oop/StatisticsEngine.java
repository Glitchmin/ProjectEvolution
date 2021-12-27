package agh.ics.oop;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.System.out;

public class StatisticsEngine implements Runnable {
    private AbstractWorldMap map;
    private int daysCounter;
    private final Label genotypeLabel;
    private String genotypeLabelString;


    private final Vector<Vector<Double>> chartDataList;
    private static final Vector<LineChart<Number, Number>> lineChart = new Vector<>();
    private final Vector<XYChart.Series<Number, Number>> lineChartDataSeries;



    public StatisticsEngine(AbstractWorldMap map) {
        this.map = map;
        genotypeLabel = new Label("");
        genotypeLabelString = "";
        daysCounter = 0;
        chartDataList = new Vector<>();
        for (int i = 0; i < 5; i++) {
            chartDataList.add(new Vector<>());
        }
        lineChartDataSeries = new Vector<>();


        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> aliveAnimalsCounterLineChartPair = createLineChart("Number of days", "Number of animals", "Number of animals over time");
        lineChartDataSeries.add(aliveAnimalsCounterLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> grassCounterLineChartPair = createLineChart("Number of days", "Number of grass fields", "Number of grass fields over time");
        lineChartDataSeries.add(grassCounterLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgEnergyLineChartPair = createLineChart("Number of days", "Average energy", "Average animal energy over time");
        lineChartDataSeries.add(avgEnergyLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsLiveSPanLineChartPair = createLineChart("Number of days", "Average live span", "Average animal live span over time");
        lineChartDataSeries.add(avgAnimalsLiveSPanLineChartPair.getValue());

        Pair<LineChart<Number, Number>, XYChart.Series<Number, Number>> avgAnimalsChildrenNumberLineChartPair = createLineChart("Number of days", "Average children amount", "Average animal children amount over time");
        lineChartDataSeries.add(avgAnimalsChildrenNumberLineChartPair.getValue());

        if (lineChart.isEmpty()) {
            lineChart.add(aliveAnimalsCounterLineChartPair.getKey());
            lineChart.add(grassCounterLineChartPair.getKey());
            lineChart.add(avgEnergyLineChartPair.getKey());
            lineChart.add(avgAnimalsLiveSPanLineChartPair.getKey());
            lineChart.add(avgAnimalsChildrenNumberLineChartPair.getKey());
        } else {
            lineChart.get(0).getData().add(aliveAnimalsCounterLineChartPair.getValue());
            lineChart.get(1).getData().add(grassCounterLineChartPair.getValue());
            lineChart.get(2).getData().add(avgEnergyLineChartPair.getValue());
            lineChart.get(3).getData().add(avgAnimalsLiveSPanLineChartPair.getValue());
            lineChart.get(4).getData().add(avgAnimalsChildrenNumberLineChartPair.getValue());
        }

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
        return new Pair<>(lineChart, lineChartDataSeries);
    }

    public List<Vector2d> getDominantGenotypesPositions(){
        List<Vector2d> dominantGenotypesPositions = new Vector<>();
        for (Animal animal : map.getAliveAnimals()){
            if (Arrays.toString(animal.getGenotype()).equals(genotypeLabelString)){
                dominantGenotypesPositions.add(animal.getPosition());
            }
        }

        return dominantGenotypesPositions;
    }


    public void run() {
        for (int i = 0; i < 5; i++) {
            updateLineChartDataSeries(lineChartDataSeries.get(i), lastElement(chartDataList.get(i)));
        }
        genotypeLabel.setText(genotypeLabelString);
    }

    public Double lastElement(List<Double> list) {
        if (list.isEmpty()) {
            return 0.0;
        }
        return list.get(list.size() - 1);
    }

    public void updateLineChartDataSeries(XYChart.Series<Number, Number> lineChartDataSeries, Double newValue) {
        lineChartDataSeries.getData().add(new XYChart.Data<>(daysCounter, newValue));
    }


    public void newDayHasCome() {
        daysCounter++;
    }

    public int getDaysCounter() {
        return daysCounter;
    }

    public void addData(LineCharts lineCharts, Double data) {
        chartDataList.get(lineCharts.ordinal()).add(data);
    }

    public static LineChart<Number, Number> getLineChart(LineCharts lineCharts) {
        return lineChart.get(lineCharts.ordinal());
    }

    private Double getAvg(int i){
        Double sum = 0.0;
        for (Double val: chartDataList.get(i)){
            sum += val;
        }
        return sum/chartDataList.get(i).size();
    }

    public void getStatsToFile(){
        try {
            PrintWriter writer = new PrintWriter("Data.csv");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<chartDataList.get(0).size();i++){
                for (int j=0;j<5;j++){
                    stringBuilder.append(chartDataList.get(j).get(i).toString()).append(",");
                }
                stringBuilder.append("\n");
            }
            for (int i=0; i<5;i++) {
                stringBuilder.append(getAvg(i)).append(",");
            }
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Double getAvgEnergy() {
        int energySum = 0;
        for (Animal animal : map.getAliveAnimals()) {
            energySum += animal.getEnergy();
        }
        if (map.getAliveAnimalsCounter() == 0) {
            return 0.0;
        }
        return (double)energySum / map.getAliveAnimalsCounter();
    }

    public Double getAvgLiveSpan() {
        if (chartDataList.get(LineCharts.avgAnimalsLiveSpan.ordinal()).isEmpty()) {
            return 0.0;
        }
        int liveSpanSum = 0;
        for (Double liveSpan : chartDataList.get(LineCharts.avgAnimalsLiveSpan.ordinal())) {
            liveSpanSum += liveSpan;
        }
        return (double)liveSpanSum / chartDataList.get(LineCharts.avgAnimalsLiveSpan.ordinal()).size();
    }

    public Double getAvgChildrenCount() {
        int childrenCount = 0;
        for (Animal animal : map.getAliveAnimals()) {
            childrenCount += animal.getChildrenCounter();
        }
        if (map.getAliveAnimalsCounter()==0){
            return 0.0;
        }
        return (double)childrenCount / map.getAliveAnimalsCounter();
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
