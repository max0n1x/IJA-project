/**
 * Project name: Robot Simulation
 * File name: MapEditView.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Description: Represents a view for editing maps.
 */

package ija.project.view;

import ija.project.game.*;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import ija.project.utils.Loader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;

/**
 * Represents a view for editing maps.
 */
public class MapEditView {

    /** The room to be edited. */
    private Room room;

    /** The type of the object to be added. */
    private boolean wall = false;

    /** The type of the object to be added. */
    private boolean robot = false;

    /** The type of the object to be added. */
    private boolean controlledRobot = false;

    /**
     * Creates a new view for editing maps.
     */
    public MapEditView() {}

    /**
     * Edits the specified map.
     *
     * @param map The name of the map to be edited.
     */
    public void editMap(String map) {
        Stage primaryStage = new Stage();

        primaryStage.getIcons().add(new Image("file:lib/robot.jpg"));
        primaryStage.setResizable(false);

        VBox root = new VBox(15);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #000000;");
        borderPane.setBottom(new Rectangle(1260, 38, Color.BLACK));
        borderPane.setTop(new Rectangle(1260, 38, Color.BLACK));
        borderPane.setLeft(new Rectangle(41, 682, Color.BLACK));
        borderPane.setRight(new Rectangle(41, 682, Color.BLACK));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 38; i++) {
            for (int j = 0; j < 22; j++) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                grid.add(rect, i, j);
            }
        }

        borderPane.setCenter(grid);

        root.getChildren().add(borderPane);

        Loader loader = new Loader();
        loader.load(map);
        this.room = loader.getRoom();

        for (Obstacle obstacle : room.getObstacles()) {
            Position position = obstacle.getPosition();
            Rectangle rect = new Rectangle(30, 30);
            rect.setFill(Color.BLACK);
            grid.add(rect, (int)position.getX(), (int)position.getY());
        }

        for (AutomatedRobot robot : room.getRobots()) {
            Position position = robot.getPosition();
            Circle circle = new Circle(14);
            circle.setFill(Color.CYAN);
            circle.setTranslateX(1.5);
            circle.setTranslateY(-0.5);
            grid.add(circle, (int)position.getX(), (int)position.getY());
        }

        for (ControlledRobot robot : room.getControlledRobots()) {
            Position position = robot.getPosition();
            Circle circle = new Circle(14);
            circle.setFill(Color.RED);
            circle.setTranslateX(1.5);
            circle.setTranslateY(-0.5);
            grid.add(circle, (int)position.getX(), (int)position.getY());
        }

        HBox parent = new HBox(10);

        HBox buttons = new HBox(10);

        HBox checkBoxes = new HBox(10);

        buttons.setAlignment(Pos.CENTER);
        checkBoxes.setAlignment(Pos.CENTER);
        parent.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {

            if (!map.equals("new")) {
                loader.saveMap(map, room);

                primaryStage.close();

                MapsListView mapsListView = new MapsListView();
                mapsListView.show_maps();

                return;
            }

            PopupMapNameView popupMapNameView = new PopupMapNameView(room, primaryStage);

            popupMapNameView.render();

        });

        Button backButton = new Button("Back");

        backButton.setOnAction(e -> {

            primaryStage.close();
            MapsListView mapsListView = new MapsListView();
            mapsListView.show_maps();

        });

        CheckBox checkBox3 = new CheckBox("Controlled Robot");
        CheckBox checkBox2 = new CheckBox("Robot");
        CheckBox checkBox = new CheckBox("Wall");
        CheckBox eraser = new CheckBox("Eraser");

        checkBox.setSelected(true);
        wall = true;

        checkBox.setOnAction(e -> {
            wall = !wall;
            robot = false;
            controlledRobot = false;
            checkBox2.setSelected(false);
            checkBox3.setSelected(false);
            eraser.setSelected(false);
        });

        checkBox2.setOnAction(e -> {
            robot = !robot;
            wall = false;
            controlledRobot = false;
            checkBox.setSelected(false);
            checkBox3.setSelected(false);
            eraser.setSelected(false);
        });

        checkBox3.setOnAction(e -> {
            controlledRobot = !controlledRobot;
            wall = false;
            robot = false;
            checkBox.setSelected(false);
            checkBox2.setSelected(false);
            eraser.setSelected(false);
        });

        eraser.setOnAction(e -> {
            wall = false;
            robot = false;
            controlledRobot = false;
            checkBox.setSelected(false);
            checkBox2.setSelected(false);
            checkBox3.setSelected(false);
        });

        grid.setOnMouseClicked(e -> {

            Node source = (Node)e.getTarget();
            Integer x = GridPane.getColumnIndex(source);
            Integer y = GridPane.getRowIndex(source);

            if (x == null || y == null) {
                return;
            }

            if (x < 0 || x >= 38 || y < 0 || y >= 22) {
                return;
            }

            if (wall) {

                if (room.addObstacle(new Obstacle(new Position(x, y)))) {

                    Rectangle rect = new Rectangle(30, 30);
                    rect.setFill(Color.BLACK);
                    grid.add(rect, x, y);

                }

            }

            if (robot) {

                if (room.addRobot(new AutomatedRobot(new Position(x, y), room))) {

                    Circle circle = new Circle(14);
                    circle.setFill(Color.CYAN);
                    circle.setTranslateX(1.5);
                    circle.setTranslateY(-0.5);
                    grid.add(circle, x, y);

                }

            }

            if (controlledRobot) {

                if (!room.getControlledRobots().isEmpty()) {

                    ControlledRobot robot = room.getControlledRobots().get(0);
                    Rectangle rect = new Rectangle(30, 30);
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.BLACK);
                    grid.add(rect, (int)robot.getPosition().getX(), (int)robot.getPosition().getY());

                    room.removeControlledRobot(robot);

                }

                if(room.addControlledRobot(new ControlledRobot(new Position(x, y), room))) {

                    Circle circle = new Circle(14);
                    circle.setFill(Color.RED);
                    circle.setTranslateX(1.5);
                    circle.setTranslateY(-0.5);
                    grid.add(circle, x, y);

                }

            }

            if (eraser.isSelected()) {
                for (Obstacle obstacle : room.getObstacles()) {
                    Position position = obstacle.getPosition();
                    if (position.getX() == x && position.getY() == y) {
                        room.removeObstacle(obstacle);
                        break;
                    }
                }

                for (AutomatedRobot robot : room.getRobots()) {
                    Position position = robot.getPosition();
                    if (position.getX() == x && position.getY() == y) {
                        room.removeRobot(robot);
                        break;
                    }
                }

                for (ControlledRobot robot : room.getControlledRobots()) {
                    Position position = robot.getPosition();
                    if (position.getX() == x && position.getY() == y) {
                        room.removeControlledRobot(robot);
                        break;
                    }
                }

                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                grid.add(rect, x, y);

            }

        });

        buttons.getChildren().addAll(saveButton, backButton);
        checkBoxes.getChildren().addAll(checkBox, checkBox2, checkBox3, eraser);

        parent.getChildren().addAll(buttons, checkBoxes);
        root.getChildren().add(parent);

        Scene scene = new Scene(root, 1260, 830);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
                MapsListView mapsListView = new MapsListView();
                mapsListView.show_maps();
            }
        });

        try {
            scene.getStylesheets().add("file:lib/styles/map_edit.css");
        } catch (NullPointerException e) {
            throw new RuntimeException("Exception is occured");
        }

        if (map.equals("new")) {
            primaryStage.setTitle("You are editing map: Untitled");
        } else {
            primaryStage.setTitle("You are editing map: " + map);
        }

        primaryStage.getIcons().add(new Image("file:lib/robot.jpg"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
