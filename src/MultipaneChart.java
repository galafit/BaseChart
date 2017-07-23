import data.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 23.06.17.
 */
public class MultipaneChart implements Drawable {
    private List<Chart> charts = new ArrayList<Chart>();
    private List<Integer> chartWeights = new ArrayList<Integer>();
    private boolean isChartsSynchronized = true;

    public void update(){

    }
    public boolean isChartsSynchronized() {
        return isChartsSynchronized;
    }

    public void setChartsSynchronized(boolean chartsSynchronized) {
        isChartsSynchronized = chartsSynchronized;
    }

    public void addChart(Chart chart){
       addChart(chart, 1);
    }

    public void addChart(Chart chart, int weight){
        chartWeights.add(weight);
        charts.add(chart);
    }

    public Chart getChart(int index){
        return charts.get(index);
    }

    public int chartAmount(){
        return charts.size();
    }



    public void draw(Graphics2D g2d, Rectangle fullArea) {


        int weightSum = 0;
        for (Integer chartWeight : chartWeights) {
            weightSum += chartWeight;
        }

        int oneWeightHeight = (chartAmount() == 0) ? fullArea.height : fullArea.height / weightSum;

        int chartY = fullArea.y;
        List<Rectangle> chartGraphAreas = new ArrayList<Rectangle>(chartAmount());


        for (int i = 0; i < chartAmount(); i++) {
            int chartHeight = oneWeightHeight * chartWeights.get(i);
            Rectangle chartRectangle = new Rectangle(fullArea.x,chartY,fullArea.width,chartHeight);
            chartY = chartY + chartHeight;
            chartGraphAreas.add(charts.get(i).calculateGraphArea(g2d, chartRectangle));
        }

        int maxX = Integer.MIN_VALUE;
        int minEnd = Integer.MAX_VALUE;
        for (Rectangle area : chartGraphAreas) {
            maxX = Math.max (maxX, area.x);
            minEnd = Math.min (minEnd, area.x + area.width);
        }

        for (Chart chart : charts) {
            chart.reduceGraphArea(g2d, maxX, minEnd - maxX);
            chart.draw(g2d);
        }
    }


}
