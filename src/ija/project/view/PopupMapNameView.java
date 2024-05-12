/**
 * Project name: Robot Simulation
 * File name: PopupMapNameView.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents a popup window for entering the name of the map.
 */

package ija.project.view;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;

import ija.project.utils.Loader;
import ija.project.game.Room;

/**
 * Represents a popup window for entering the name of the map.
 */
public class PopupMapNameView {

    /**    The room to save. */
    public static Room room;

    /**    The primary stage of the application. */
    public static Stage primaryStage;

    /**    The stage of the popup window. */
    public static Stage stage;

    /**
     * Creates a new popup window for entering the name of the map.
     *
     * @param room The room to save as an instance of the Room class.
     * @param primaryStage The primary stage of the application as an instance of the Stage class.
     */
    public PopupMapNameView(Room room, Stage primaryStage) {

        PopupMapNameView.room = room;
        PopupMapNameView.primaryStage = primaryStage;

    }

    /**
     * Renders the popup window for entering the name of the map.
     */
    public void render() {

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setStyle("-fx-background-color: #000000;");
        menu.setAlignment(Pos.CENTER);

        Label mapNameLabel = new Label("Enter map name:");

        TextField mapNameField = new TextField();
        mapNameField.setMaxWidth(190);
        mapNameField.setPromptText("Map name");

        Button createButton = new Button("Create Map");
        createButton.setOnAction(e -> {

            Loader loader = new Loader();
            loader.saveMap(mapNameField.getText(), room);

            stage.close();
            primaryStage.close();

            MapsListView mapsListView = new MapsListView();
            mapsListView.show_maps();

        });

        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(e -> stage.close());

        menu.getChildren().addAll(mapNameLabel, mapNameField, createButton, cancelButton);

        Scene scene = new Scene(menu, 300, 200);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }

            if (e.getCode() == KeyCode.ENTER) {
                Loader loader = new Loader();
                loader.saveMap(mapNameField.getText(), room);

                stage.close();
                primaryStage.close();

                MapsListView mapsListView = new MapsListView();
                mapsListView.show_maps();
            }

        });

        try {
            scene.getStylesheets().add("file:lib/styles/map_name_popup.css");
        } catch (NullPointerException e) {
            throw new RuntimeException("Exception is occured");
        }

        stage.setScene(scene);

        stage.setTitle("Save Map");
        stage.getIcons().add(new Image("file:lib/robot.jpg"));
        stage.setResizable(false);
        stage.show();

    }
}
