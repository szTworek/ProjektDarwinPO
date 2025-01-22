package agh.oop.project.model.app;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Writer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationApp extends Application {
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startPage.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationStarter starter = loader.getController();

        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot) throws IOException {

        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Simulation app");
        scene.getStylesheets().add("startPageStyle.css");


        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
