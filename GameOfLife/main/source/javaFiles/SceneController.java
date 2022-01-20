package javaFiles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import javafx.animation.*;
import javafx.event.*;
import javafx.util.Duration;


public class SceneController {
    private int COL=10;
    private int ROW=10;
    private double SPEED=5;
    private double RESTART_TIMEOUT=5;
    private int CHECK_DEPTH=4;
    private boolean finished=false;
    private boolean initSize=true;
    private Color LIVING_COLOR, DEAD_COLOR;

    private Stage stage;
    private Canvas canvas;
    private static Timeline timeLine, timeLineTwo;
    private boolean[][] squares;
    private Game[] gameArray;

    @FXML
    private Slider sliderDepth;

    @FXML
    private ColorPicker deadColor;

    @FXML
    private ColorPicker liveColor;

    @FXML
    private Slider sliderCols;

    @FXML
    private Slider sliderRows;

    @FXML
    private Slider speedSlider;

    @FXML
    private Slider restartTimeOut;



    public void switchToScene1(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Game of Life - Start Screen");
        Parent root = FXMLLoader.load(Objects.requireNonNull(MamanApplication.class.getResource("Scene1.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToScene2(ActionEvent event) {
        initParams();
        
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Button clickButtonRestart = new Button("Restart Board");
        clickButtonRestart.setOnAction(arg0 -> generateShape());
        clickButtonRestart.setMaxWidth(canvas.getWidth());
        Button clickButtonStep = new Button("One Step");
        clickButtonStep.setOnAction(arg0 -> playStep());
        clickButtonStep.setMaxWidth(canvas.getWidth());
        Button finishPlayButton = new Button("Finish This Match");
        finishPlayButton.setOnAction(arg0 -> runAnimator());
        finishPlayButton.setMaxHeight(canvas.getHeight());
        Button autoPlayButton = new Button("Don't Stop Playing");
        autoPlayButton.setOnAction(arg0 -> autoPlayRunAnimator());
        autoPlayButton.setMaxHeight(canvas.getHeight());
        Button goBack = new Button("Go Back");
        goBack.setOnAction(arg0 -> {try {switchToScene1(arg0);} catch (IOException e) {e.printStackTrace();}});
        goBack.setMaxWidth(canvas.getWidth());

        GridPane root = new GridPane();
        root.add(clickButtonStep, 1, 0);
        root.add(finishPlayButton, 2, 1);
        root.add(clickButtonRestart, 1, 2);
        root.add(autoPlayButton, 0, 1);
        root.add(canvas, 1, 1);
        root.add(goBack, 1, 3);

        root.getColumnConstraints().add(new ColumnConstraints(125));
        root.getColumnConstraints().add(new ColumnConstraints(getMonitorSizes()[0] - 250));
        root.getColumnConstraints().add(new ColumnConstraints(125));
        root.getRowConstraints().add(new RowConstraints(34));
        root.getRowConstraints().add(new RowConstraints(getMonitorSizes()[1] - 200));
        root.getRowConstraints().add(new RowConstraints(34));
        root.getRowConstraints().add(new RowConstraints(34));

        Scene scene = new Scene(root, getMonitorSizes()[0], getMonitorSizes()[1] - 50);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Game of Life - Play");
        stage.setMaximized(true);

        generateShape();
        stage.show();
    }

    private void generateShape(){
        finished = false;
        gameArray[0] = new Game(COL, ROW);
        for (int i=1; i<CHECK_DEPTH; i++) {
            gameArray[i] = gameArray[i-1].clone();
            gameArray[i].nextGeneration();}
        squares = gameArray[0].getGrid();
        drawShape();
    }

    private void generateFinishPlayButtonTimeLine() {
        timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, (EventHandler) event -> {
                    if (!finished) animateSteps();
                    else timeLine.stop();
                }),
                new KeyFrame(Duration.millis((int)(1000/SPEED))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
    }

    private void generateAutoPlayButtonTimeLineTwo() {
        timeLineTwo = new Timeline(
                new KeyFrame(Duration.ZERO, (EventHandler) event -> {
                    if (!finished) animateSteps();
                    else {
                        generateShape();
                        try {Thread.sleep((int)(RESTART_TIMEOUT*1000));}
                        catch (InterruptedException e) {e.printStackTrace();}
                    }
                }),
                new KeyFrame(Duration.millis((int)(250/SPEED))));
        timeLineTwo.setCycleCount(Timeline.INDEFINITE);
    }


    private void drawShape() {
        GraphicsContext graphics_context = canvas.getGraphicsContext2D();
        int squaresDelta = getOptimumSize();
        int current_x = 0;
        int current_y = 0;
        for (boolean[] gridRows : squares) {
            for (boolean cellValue : gridRows) {
                if (cellValue) graphics_context.setFill(LIVING_COLOR);
                else graphics_context.setFill(DEAD_COLOR);
                graphics_context.fillRect(current_x, current_y, current_x + squaresDelta, current_y + squaresDelta);
                current_y += squaresDelta;
            }
            current_x += squaresDelta;
            current_y = 0;
        }
        graphics_context.setFill(DEAD_COLOR);
        graphics_context.fillRect(0, squares[0].length * squaresDelta, canvas.getWidth(), canvas.getHeight());
        graphics_context.setFill(DEAD_COLOR);
        graphics_context.fillRect(squares.length * squaresDelta, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void playStep() {
        for (int i=0; i<CHECK_DEPTH-1; i++) gameArray[i].setGrid(gameArray[i+1].getGrid());
        squares = gameArray[0].getGrid();
        gameArray[CHECK_DEPTH-1].nextGeneration();
        drawShape();
    }


    private void runAnimator() {
        if (timeLine.getStatus() == Animation.Status.RUNNING) timeLine.stop();
        else timeLine.play();
    }

    private void autoPlayRunAnimator() {
        if (timeLineTwo.getStatus() == Animation.Status.RUNNING) timeLineTwo.stop();
        else timeLineTwo.play();
    }


    private void animateSteps() {
        playStep();
        finished = false;
        for (int i=0; i<CHECK_DEPTH-1; i++) {
            for (int j=i+1; j<CHECK_DEPTH; j++){
                if (Arrays.deepEquals(gameArray[j].getGrid(), gameArray[i].getGrid())) {finished = true; break;}
            }
            if (finished) break;
        }
    }


    private int[] getMonitorSizes() {
        int x, y;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode dm = gs[0].getDisplayMode();
        int[] minResolution = new int[]{dm.getWidth(), dm.getHeight()};

        for (int i = 1; i < gs.length; i++) {
            dm = gs[i].getDisplayMode();
            x = dm.getWidth();
            y = dm.getWidth();
            if (x < minResolution[0]) minResolution[0] = x;
            if (y < minResolution[1]) minResolution[1] = y;
        }
        return minResolution;
    }

    private int getOptimumSize() {
        int numOfSquaresX = squares.length;
        int numOfSquaresY = squares[0].length;
        double deltaX = canvas.getWidth() / numOfSquaresX;
        double deltaY = canvas.getHeight() / numOfSquaresY;
        return (int) Math.min(deltaX, deltaY);
    }

    @FXML
    public void autoSwitchToScene2(ActionEvent event) {
        int [] size = getAutoSize();
        COL = size[0];
        ROW = size[1];
        initSize = false;
        switchToScene2(event);
    }

    private int[] getAutoSize() {
        int[] size = new int[2];
        double xProportions;
        xProportions = (double)(getMonitorSizes()[0] - 250) / (double)(getMonitorSizes()[1] - 200);
        if (xProportions == 1) {
            size[0] = 50;
            size[1] = 50;
        } else if (xProportions > 1) {
            size[1] = 50;
            size[0] = (int) (xProportions * 50);
        } else {
            size[0] = 50;
            size[1] = (int) (50 / xProportions);
        }
        return size;
    }

    private void nullify(){
        gameArray = new Game[CHECK_DEPTH];

    }

    private void initParams(){
        CHECK_DEPTH = (int)sliderDepth.getValue();
        LIVING_COLOR = liveColor.getValue();
        DEAD_COLOR = deadColor.getValue();
        SPEED = speedSlider.getValue();
        RESTART_TIMEOUT = restartTimeOut.getValue();
        if (initSize) {ROW = (int) sliderRows.getValue(); COL = (int) sliderCols.getValue();}
        else initSize = true;
        System.out.println("Rows: " + ROW + "\tCols: " + COL);
        gameArray = new Game[CHECK_DEPTH];
        canvas = new Canvas(getMonitorSizes()[0] - 250, getMonitorSizes()[1] - 200);
        generateFinishPlayButtonTimeLine();
        generateAutoPlayButtonTimeLineTwo();
    }

    @FXML
    private void openURLGit(){
        openURL("https://github.com/VitalyChait");
    }

    @FXML
    private void openURLLinked(){
        openURL("https://www.linkedin.com/in/vitaly-chait-59845b142/");
    }

    private void openURL(String URL){
        try {
            Desktop.getDesktop().browse(new URI(URL));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

    }



}