/**
 * Project name: Robot Simulation
 * File name: GameView.java
 * Date: 05.05.2024
 * Last update: 05.05.2024
 * Author: Maksym Podhornyi(xpodho08)
 * Author: Neonila Mashlai(xmashl00)
 * Description: Represents the simulation view.
 */

package ija.project.view;

import ija.project.game.*;
import ija.project.utils.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Represents the simulation view.
 */
public class GameView {

    /**
    Last tick of the simulation.
     */
    private long lastTick = 0;

    /**
    Room of the simulation.
     */
    private Room room;

    /**
    Primary stage of the simulation.
     */
    private Stage primaryStage;

    /**
    Container of the canvas.
     */
    private StackPane canvas_container;

    /**
    Graphics context of the simulation.
     */
    private GraphicsContext gc;

    /**
    Timer of the simulation.
     */
    private AnimationTimer timer;

    /**
    Logger of the simulation.
     */
    private Logger logger;

    /**
    Is robot turning.
     */
    private int isTurn = 0;

    /**
    Is robot moving.
     */
    private boolean isMoving = false;

    /**
     * Creates a new simulation view.
     */
    public GameView() {}

    /**
     * States of the simulation.
     */
    private enum STATES {
        /** The simulation is currently running. */
        RUNNING,

        /** The simulation is currently stopped. */
        STOPPED,

        /** State after a replay has completed. */
        AFTER_REPLAY,

        /** Indicates the first run of the simulation. */
        FIRST_RUN,

        /** The simulation is replaying previous actions. */
        REPLAY
    }



    /**
    Current state of the simulation.
     */
    private STATES state = STATES.FIRST_RUN;

    /**
    Direction of the replay.
     */
    private boolean replay_up = false;

    /**
     * Runs the simulation.
     *
     * @param map The map to be run as a string.
     */
    public void runGame(String map) {
        this.primaryStage = new Stage();

        primaryStage.getIcons().add(new Image("file:lib/robot.jpg"));
        primaryStage.setResizable(false);

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #000000;");

        this.canvas_container = new StackPane();
        canvas_container.setAlignment(Pos.CENTER);
        canvas_container.setStyle("-fx-background-color: #00ff00;");
        canvas_container.setMinWidth(1160);
        canvas_container.setMinHeight(680);

        StackPane canvas_background = new StackPane();
        canvas_background.setAlignment(Pos.CENTER);
        canvas_background.setStyle("-fx-background-color: #ffffff;");
        canvas_background.setMaxWidth(1140);
        canvas_background.setMaxHeight(660);

        Canvas canvas = new Canvas();

        canvas.setWidth(1140);
        canvas.setHeight(660);

        this.gc = canvas.getGraphicsContext2D();

        Group group = new Group(canvas);

        canvas_background.getChildren().add(group);

        canvas_container.getChildren().add(canvas_background);
        root.getChildren().add(canvas_container);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button stopButton = new Button("Stop Simulation");
        stopButton.setOnAction(e -> simulationStop());

        Button restartButton = new Button("Restart Simulation");
        restartButton.setOnAction(e -> restartSimulation(map));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> exit());

        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(e -> simulationStart(map));

        Button replayButton = new Button("Replay Simulation");
        replayButton.setOnMousePressed(e -> startReplay(buttons, backButton, replayButton, stopButton, startButton, restartButton));

        buttons.getChildren().addAll(backButton, replayButton, stopButton, startButton, restartButton);
        root.getChildren().add(buttons);

        Scene scene = new Scene(root, 1160, 760);

        Loader loader = new Loader();
        loader.load(map);
        this.room = new Converter().convertCoords(loader.getRoom());

        scene.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case LEFT:
                    if (state == STATES.REPLAY) { timer.start(); }
                    isTurn = -1;
                    break;

                case RIGHT:
                    if (state == STATES.REPLAY) {
                        replay_up = true;
                        timer.start();
                    }
                    isTurn = 1;
                    break;

                case UP:
                    isMoving = true;
                    break;

                case ESCAPE:
                    if (state == STATES.REPLAY) {
                        endReplay(buttons, backButton, replayButton, stopButton, startButton, restartButton);
                    } else { exit(); }
                    break;

                case SPACE:
                    if (state == STATES.RUNNING) {
                        simulationStop();
                    } else { simulationStart(map); }
                    break;

                case R:
                    startReplay(buttons, backButton, replayButton, stopButton, startButton, restartButton);
                    break;
            }

        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    if (state == STATES.REPLAY) { timer.stop(); }
                    isTurn = 0;
                    break;
                case UP:
                    isMoving = false;
                    break;
            }
        });

        drawElements();

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTick == 0L || now - lastTick > (1_000_000_000L / 120)) {
                    lastTick = now;

                    tickAction();

                }
            }
        };

        try {
            scene.getStylesheets().add("file:lib/styles/map_name_popup.css");
        } catch (NullPointerException e) {
            throw new RuntimeException("Exception is occured");
        }

        primaryStage.setTitle("Simulation - Stopped");
        primaryStage.setScene(scene);
        primaryStage.show();

        backButton.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
        startButton.setFocusTraversable(false);
        restartButton.setFocusTraversable(false);
        backButton.setFocusTraversable(false);
        replayButton.setFocusTraversable(false);
    }

    /**
     * Draws the elements of the simulation.
     */
    private void drawElements() {

        for (Obstacle obstacle : room.getObstacles()) {
            gc.setFill(Color.BLACK);
            gc.fillRect(obstacle.getPosition().getX(), obstacle.getPosition().getY(), 30, 30);
        }

        for (AutomatedRobot robot : room.getRobots()) {
            gc.setFill(Color.CYAN);
            gc.fillOval(robot.getPosition().getX() + 1, robot.getPosition().getY() + 1, 28, 28);

            gc.setFill(Color.BLACK);
            gc.strokeLine(robot.getPosition().getX() + 15, robot.getPosition().getY() + 15,
                    robot.getPosition().getX() + 15 + 14 * Math.cos(Math.toRadians(robot.getAngle())),
                    robot.getPosition().getY() + 15 + 14 * Math.sin(Math.toRadians(robot.getAngle())));

        }

        for (ControlledRobot robot : room.getControlledRobots()) {
            gc.setFill(Color.RED);
            gc.fillOval(robot.getPosition().getX() + 1, robot.getPosition().getY() + 1, 28, 28);

            gc.setFill(Color.BLACK);
            gc.strokeLine(robot.getPosition().getX() + 15, robot.getPosition().getY() + 15,
                    robot.getPosition().getX() + 15 + 14 * Math.cos(Math.toRadians(robot.getAngle())),
                    robot.getPosition().getY() + 15 + 14 * Math.sin(Math.toRadians(robot.getAngle())));

        }

    }

    /**
     * Starts the replay of the simulation.
     *
     * @param buttons The buttons of the simulation as an instance of the HBox class.
     * @param backButton The back button of the simulation as an instance of the Button class.
     * @param replayButton The replay button of the simulation as an instance of the Button class.
     * @param stopButton The stop button of the simulation as an instance of the Button class.
     * @param startButton The start button of the simulation as an instance of the Button class.
     * @param restartButton The restart button of the simulation as an instance of the Button class.
     */
    private void startReplay(HBox buttons, Button backButton, Button replayButton,
                             Button stopButton, Button startButton, Button restartButton) {

        if (state == STATES.FIRST_RUN) {
            return;
        }

        state = STATES.REPLAY;
        primaryStage.setTitle("Simulation - Replay");
        canvas_container.setStyle("-fx-background-color: #0029ff;");
        timer.stop();
        Button back = new Button("Back");
        back.setOnAction(e1 -> endReplay(buttons, backButton, replayButton, stopButton, startButton, restartButton));
        buttons.getChildren().clear();
        buttons.getChildren().addAll(back);
        back.setFocusTraversable(false);

    }

    /**
     * Ends the replay of the simulation.
     *
     * @param buttons The buttons of the simulation as an instance of the HBox class.
     * @param backButton The back button of the simulation as an instance of the Button class.
     * @param replayButton The replay button of the simulation as an instance of the Button class.
     * @param stopButton The stop button of the simulation as an instance of the Button class.
     * @param startButton The start button of the simulation as an instance of the Button class.
     * @param restartButton The restart button of the simulation as an instance of the Button class.
     */
    private void endReplay(HBox buttons, Button backButton, Button replayButton,
                           Button stopButton, Button startButton, Button restartButton) {

        state = STATES.AFTER_REPLAY;
        primaryStage.setTitle("Simulation - Stopped");
        timer.stop();
        canvas_container.setStyle("-fx-background-color: #00ff00;");
        gc.clearRect(0, 0, 1140, 660);
        drawElements();
        logger.reset();
        buttons.getChildren().clear();
        buttons.getChildren().addAll(backButton, replayButton, stopButton, startButton, restartButton);

    }

    /**
     * Restarts the simulation.
     *
     * @param map The map to be restarted as a string.
     */
    private void restartSimulation(String map) {

        timer.stop();
        Loader loader = new Loader();
        loader.load(map);
        state = STATES.FIRST_RUN;
        this.room = new Converter().convertCoords(loader.getRoom());
        gc.clearRect(0, 0, 1140, 660);
        canvas_container.setStyle("-fx-background-color: #00ff00;");
        primaryStage.setTitle("Simulation - Stopped");
        state = STATES.STOPPED;
        drawElements();

    }

    /**
     * Stops the simulation.
     */
    private void simulationStop() {

        if (state == STATES.FIRST_RUN) {
            return;
        }

        canvas_container.setStyle("-fx-background-color: #00ff00;");
        timer.stop();
        state = STATES.STOPPED;
        primaryStage.setTitle("Simulation - Stopped");

    }

    /**
     * Starts the simulation.
     *
     * @param map The map to be started as a string.
     */
    private void simulationStart(String map) {

        canvas_container.setStyle("-fx-background-color: #ff0000;");
        if (state == STATES.FIRST_RUN) {
            this.logger = new Logger(map);
        }

        if (state == STATES.AFTER_REPLAY) {
            logger.reset();
        }

        primaryStage.setTitle("Simulation - Running");
        state = STATES.RUNNING;
        timer.start();

    }

    /**
     * Performs the tick action.
     */
    private void tickAction() {
        gc.clearRect(0, 0, 1140, 660);

        if (state == STATES.RUNNING) {

            for (AutomatedRobot robot : room.getRobots()) {
                robot.move();
            }

            if (isTurn != 0) {
                for (ControlledRobot robot : room.getControlledRobots()) {
                    robot.turn(isTurn);
                }
            }

            if (isMoving) {
                for (ControlledRobot robot : room.getControlledRobots()) {
                    robot.move();
                }
            }
            logger.log(room);

        }

        if (state == STATES.REPLAY) {
            if (replay_up) {
                logger.getLastFrame(room, 1);
            } else {
                logger.getLastFrame(room, -1);
            }
        }

        if (state == STATES.RUNNING) {
            primaryStage.setTitle("Simulation - Running");
        }

        drawElements();

    }

    /**
     * Exits the simulation.
     */
    private void exit() {

        primaryStage.close();
        MapsListView mapsListView = new MapsListView();
        mapsListView.show_maps();

    }
}
