/**
 * Project name: Robot Simulation
 * File name: MenuView.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Represents the main menu of the application.
 */

package ija.project.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Represents the main menu of the application.
 */
public class MenuView extends Application {

    /**  Stage of the application. */
    private Stage stage;

    /**
     * Creates a new main menu of the application.
     */
    public MenuView() {}

    /**
     * Builds the main menu of the application.
     */
    public void build() {

        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: #000000;");
        menu.setSpacing(10);

        Label title = new Label("Robot Simulation");

        Button loadButton = new Button("Simulation");
        loadButton.setOnAction(e -> {
            stage.close();
            MapsListView mapsListView = new MapsListView();
            mapsListView.show_maps();
        });

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> stage.close());

        menu.getChildren().addAll(title, loadButton, exitButton);

        Scene scene = new Scene(menu, 700, 700);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        try {
            scene.getStylesheets().add("file:lib/styles/main_menu.css");
        } catch (NullPointerException e) {
            throw new RuntimeException("Exception is occured");
        }

        stage.setScene(scene);
        stage.setTitle("Main menu");
        stage.getIcons().add(new Image("file:lib/robot.jpg"));
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Shows the main menu of the application.
     */
    public void showMenu() { this.stage = new Stage(); build(); }

    /**
     * Starts the application.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start (Stage primaryStage) { this.stage = primaryStage; build(); }

    /**
     * Launches the application.
     */
    public void render() { launch(); }

}
