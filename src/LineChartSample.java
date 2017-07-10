import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineChartSample extends Application {

    @Override public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        final NumberAxis  xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");

        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");

        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");

        series.getData().add(new XYChart.Data(100, 23));
        series.getData().add(new XYChart.Data(200, 14));
        series.getData().add(new XYChart.Data(300, 15));
        series.getData().add(new XYChart.Data(400, 10024));
        series.getData().add(new XYChart.Data(500, 34));
        series.getData().add(new XYChart.Data(600, 36));
        series.getData().add(new XYChart.Data(700, 20022));
        series.getData().add(new XYChart.Data(800, 45));
        series.getData().add(new XYChart.Data(900, 43));
        series.getData().add(new XYChart.Data(1000, 17));
        series.getData().add(new XYChart.Data(1200, 29));
        series.getData().add(new XYChart.Data(1500, 25));


        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}