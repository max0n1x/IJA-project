/**
 * Project name: Robot Simulation
 * File name: MapsListView.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents a view for the list of maps.
 */

package ija.project.view;

import ija.project.utils.Loader;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Represents a view for the list of maps.
 */
public class MapsListView {

    /**    Stage of the application. */
    private Stage stage;

    /**
     * Creates a new view for the list of maps.
     */
    public MapsListView() {}

    /**
     * Adds the list of maps to the menu.
     *
     * @param menu The menu to which the maps will be added as an instance of the VBox class.
     */
    public void addMaps(VBox menu) {

        String[] maps = Loader.getMaps();
        VBox mapsList = new VBox(10);
        mapsList.setAlignment(Pos.CENTER);

        for (String map : maps) {

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER);

            Button mapButton = new Button(map);
            mapButton.setOnAction(e -> {
                stage.close();
                GameView gameView = new GameView();
                gameView.runGame(map);
            });

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> {
                stage.close();
                MapEditView mapEdit = new MapEditView();
                mapEdit.editMap(map);
            });

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> {
                Loader.deleteMap(map);
                MapsListView mapsListView = new MapsListView();
                mapsListView.show_maps();
            });

            hbox.getChildren().addAll(mapButton, editButton, deleteButton);
            mapsList.getChildren().add(hbox);
        }

        menu.getChildren().add(mapsList);

    }

    /**
     * Builds the list of maps.
     */
    public void build() {

        VBox menu = new VBox();
        menu.setSpacing(50);
        menu.setStyle("-fx-background-color: #000000;");
        menu.setAlignment(Pos.CENTER);

        Label title = new Label("Select a Map");
        menu.getChildren().add(title);

        addMaps(menu);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.close();
            MenuView menuView = new MenuView();
            menuView.showMenu();
        });

        Button mapButton = new Button("New Map");
        mapButton.setOnAction(e -> {
            stage.close();
            MapEditView mapEdit = new MapEditView();
            mapEdit.editMap("new");
        });

        VBox buttons = new VBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(mapButton, backButton);

        menu.getChildren().addAll(buttons);

        Scene scene = new Scene(menu, 700, 700);

        try {
            scene.getStylesheets().add("file:lib/styles/maps_list.css");
        } catch (NullPointerException e) {
            throw new RuntimeException("Exception is occured");
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                MenuView menuView = new MenuView();
                menuView.showMenu();
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Map Selection");
        stage.getIcons().add(new Image("file:lib/robot.jpg"));
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Shows the list of maps.
     */
    public void show_maps() {

        this.stage = new Stage();
        build();

    }
}
