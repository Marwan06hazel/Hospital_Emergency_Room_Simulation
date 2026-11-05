package org.hospital.test001;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class HospitalApp extends Application {

    private final Hospital hospitalSim = new Hospital();
    private Button startBtn;

    private final String ICON_USERS = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private final String ICON_STETHOSCOPE = "M12 2C8.69 2 6 4.69 6 8v4h2V8c0-2.21 1.79-4 4-4s4 1.79 4 4v7c0 1.66-1.34 3-3 3h-1c0-1.66-1.34-3-3-3H6v5h3c0 1.66 1.34 3 3 3h1c2.76 0 5-2.24 5-5V8c0-3.31-2.69-6-6-6z";
    private final String ICON_BED = "M19 7h-8v6H3V5H1v15h2v-3h18v3h2v-9c0-2.21-1.79-4-4-4zm-9 6V9h7c1.1 0 2 .9 2 2v2h-9z";
    private final String ICON_CHECK = "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z";
    private final String ICON_HEART = "M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z";
    private final String ICON_OUTSIDE = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z";

    @Override
    public void start(Stage primaryStage) {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setStyle("-fx-background-color: #0b1319; -fx-font-family: 'Segoe UI', sans-serif;");

        // Top Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label subHeader = new Label("REAL-TIME SIMULATION");
        subHeader.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 11px; -fx-font-weight: bold; -fx-letter-spacing: 1px;");
        Label title = new Label("Hospital command center");
        title.setStyle("-fx-text-fill: #e1e8ed; -fx-font-size: 24px; -fx-font-weight: bold;");
        titleBox.getChildren().addAll(subHeader, title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        startBtn = new Button("▶ Run simulation");
        startBtn.setStyle("-fx-background-color: #0d7d73; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand;");

        header.getChildren().addAll(titleBox, spacer, startBtn);

        // Stats Cards
        HBox statsRow = new HBox(15);
        statsRow.getChildren().addAll(
                createStatCard("Patients in system", SimulationState.patientsArrived.asString(), "Waiting & in treatment", ICON_USERS, "#103230", "#2ee6a6"),
                createStatCard("Available doctors", SimulationState.availableDoctors.asString().concat("/").concat(SimulationState.totalDoctors.asString()), "Active staff", ICON_STETHOSCOPE, "#102a3a", "#42a5f5"),
                createStatCard("Treatment rooms", SimulationState.treatmentRoomsUsed.asString().concat("/2"), "Utilization limit", ICON_BED, "#3a2a10", "#ffb74d"),
                createStatCard("Completed today", SimulationState.patientsCompleted.asString(), "Discharged patients", ICON_CHECK, "#103a1a", "#66bb6a")
        );

        // Middle Row
        HBox middleRow = new HBox(15);
        VBox patientFlowCard = createPatientFlowCard();
        VBox controlsCard = createControlsCard();
        HBox.setHgrow(patientFlowCard, Priority.ALWAYS);
        middleRow.getChildren().addAll(patientFlowCard, controlsCard);

        // Bottom Row
        HBox bottomRow = new HBox(15);
        VBox activePatientsCard = createActivePatientsCard();
        VBox eventStreamCard = createEventStreamCard();
        HBox.setHgrow(activePatientsCard, Priority.ALWAYS);
        HBox.setHgrow(eventStreamCard, Priority.ALWAYS);
        bottomRow.getChildren().addAll(activePatientsCard, eventStreamCard);

        mainContainer.getChildren().addAll(header, statsRow, middleRow, bottomRow);

        ScrollPane root = new ScrollPane(mainContainer);
        root.setFitToWidth(true);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setStyle("-fx-background: #0b1319; -fx-border-color: #0b1319;");

        Scene scene = new Scene(root, 1200, 700);

        String customScrollbarCSS = ".custom-scroll-pane .scroll-bar:vertical{-fx-background-color:transparent;-fx-pref-width:8;}" +
                ".custom-scroll-pane .scroll-bar:vertical .track{-fx-background-color:transparent;-fx-border-color:transparent;}" +
                ".custom-scroll-pane .scroll-bar:vertical .thumb{-fx-background-color:#243542;-fx-background-radius:4;}" +
                ".custom-scroll-pane .scroll-bar:vertical .thumb:hover{-fx-background-color:#5c7080;}" +
                ".custom-scroll-pane .scroll-bar:vertical .increment-button{-fx-padding:0;}" +
                ".custom-scroll-pane .scroll-bar:vertical .decrement-button{-fx-padding:0;}";
        scene.getStylesheets().add("data:text/css," + customScrollbarCSS);

        primaryStage.setTitle("Hospital Command Center");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private StackPane createIcon(String svg, String bgColor, String iconColor) {
        SVGPath path = new SVGPath();
        path.setContent(svg);
        path.setFill(Color.web(iconColor));
        path.setScaleX(0.8);
        path.setScaleY(0.8);

        StackPane bg = new StackPane(path);
        bg.setMinSize(40, 40);
        bg.setMaxSize(40, 40);
        bg.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12;");
        return bg;
    }

    private VBox createStatCard(String titleText, javafx.beans.binding.StringExpression boundValue, String subtext, String svg, String bgColor, String iconColor) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #141e26; -fx-padding: 18; -fx-background-radius: 8; -fx-border-color: #1c2a35; -fx-border-radius: 8;");
        HBox.setHgrow(card, Priority.ALWAYS);

        HBox cardHeader = new HBox();
        StackPane icon = createIcon(svg, bgColor, iconColor);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label liveBadge = new Label("LIVE");
        liveBadge.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 10px; -fx-font-weight: bold;");
        cardHeader.getChildren().addAll(icon, sp, liveBadge);

        VBox textData = new VBox(4);
        Label title = new Label(titleText);
        title.setStyle("-fx-text-fill: #8a9ba8; -fx-font-size: 13px;");

        Label value = new Label();
        value.textProperty().bind(boundValue);
        value.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label sub = new Label(subtext);
        sub.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 11px;");

        textData.getChildren().addAll(title, value, sub);
        card.getChildren().addAll(cardHeader, textData);
        return card;
    }

    private VBox createPatientFlowCard() {
        VBox card = new VBox(25);
        card.setStyle("-fx-background-color: #141e26; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #1c2a35; -fx-border-radius: 8;");

        Label title = new Label("Patient flow");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        HBox pipeline = new HBox(10);
        pipeline.setAlignment(Pos.CENTER);
        pipeline.getChildren().addAll(
                createStageNode("01.1", "Arrived", SimulationState.patientsArrived.asString(), ICON_USERS),
                createStageSeparator(),
                createStageNode("01.2", "Outside", SimulationState.patientsOutside.asString(), ICON_OUTSIDE),
                createStageSeparator(),
                createStageNode("02", "Waiting room", SimulationState.patientsWaiting.asString(), ICON_USERS),
                createStageSeparator(),
                createStageNode("03", "In treatment", SimulationState.patientsInTreatment.asString(), ICON_HEART),
                createStageSeparator(),
                createStageNode("04", "Completed", SimulationState.patientsCompleted.asString(), ICON_CHECK)
        );

        VBox progressBox = new VBox(8);
        progressBox.setStyle("-fx-background-color: #0b1319; -fx-padding: 14; -fx-background-radius: 6;");

        HBox progressHeader = new HBox();
        Label pLabel = new Label("Overall patient throughput");
        pLabel.setStyle("-fx-text-fill: #a88a94; -fx-font-size: 12px;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label pctLabel = new Label();
        pctLabel.textProperty().bind(SimulationState.throughputProgress.multiply(100).asString("%.0f%%"));
        pctLabel.setStyle("-fx-text-fill: #2ee6a6; -fx-font-weight: bold; -fx-font-size: 12px;");
        progressHeader.getChildren().addAll(pLabel, sp, pctLabel);

        ProgressBar pBar = new ProgressBar(0);
        pBar.progressProperty().bind(SimulationState.throughputProgress);
        pBar.setMaxWidth(Double.MAX_VALUE);
        pBar.setStyle("-fx-accent: #0d7d73; -fx-control-inner-background: #141e26;");

        progressBox.getChildren().addAll(progressHeader, pBar);
        card.getChildren().addAll(title, pipeline, progressBox);
        return card;
    }

    private HBox createStageNode(String step, String name, javafx.beans.binding.StringExpression val, String svg) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);

        StackPane icon = createIcon(svg, "#e1e8ed", "#141e26");

        VBox textCol = new VBox(2);
        Label stepLbl = new Label(step + " · " + name);
        stepLbl.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label valLbl = new Label();
        valLbl.textProperty().bind(val);
        valLbl.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");

        textCol.getChildren().addAll(stepLbl, valLbl);
        container.getChildren().addAll(icon, textCol);
        return container;
    }

    private Label createStageSeparator() {
        Label sep = new Label("───");
        sep.setStyle("-fx-text-fill: #243542;");
        return sep;
    }

    private VBox createControlsCard() {
        VBox card = new VBox(18);
        card.setMinWidth(300);
        card.setStyle("-fx-background-color: #141e26; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #1c2a35; -fx-border-radius: 8;");

        Label title = new Label("Simulation controls");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        Slider capSlider = createConfigSlider(1, 10, 3);
        Slider docSlider = createConfigSlider(1, 10, 4);
        Slider patSlider = createConfigSlider(1, 30, 12);

        card.getChildren().addAll(
                title,
                createSliderLabel("Waiting room capacity", capSlider, "seats"), capSlider,
                createSliderLabel("Number of doctors", docSlider, "active"), docSlider,
                createSliderLabel("Number of patients", patSlider, "total"), patSlider
        );

        startBtn.setOnAction(e -> {
            startBtn.setDisable(true);
            hospitalSim.runSimulation(
                    (int) capSlider.getValue(),
                    (int) docSlider.getValue(),
                    (int) patSlider.getValue(),
                    () -> javafx.application.Platform.runLater(() -> startBtn.setDisable(false))
            );
        });

        return card;
    }

    private HBox createSliderLabel(String text, Slider slider, String unit) {
        HBox box = new HBox();
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #8a9ba8; -fx-font-size: 12px;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label valLbl = new Label();
        valLbl.textProperty().bind(slider.valueProperty().asString("%.0f ").concat(unit));
        valLbl.setStyle("-fx-text-fill: #2ee6a6; -fx-font-size: 12px;");
        box.getChildren().addAll(lbl, sp, valLbl);
        return box;
    }

    private Slider createConfigSlider(int min, int max, int defaultVal) {
        Slider slider = new Slider(min, max, defaultVal);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setStyle("-fx-control-inner-background: #0d7d73;");
        return slider;
    }

    private VBox createActivePatientsCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #141e26; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #1c2a35; -fx-border-radius: 8;");

        HBox header = new HBox();
        Label title = new Label("Active patients");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15px; -fx-font-weight: bold;");
        Region spTitle = new Region();
        HBox.setHgrow(spTitle, Priority.ALWAYS);
        Label countBadge = new Label();
        countBadge.textProperty().bind(SimulationState.totalPatients.asString().concat(" TOTAL"));
        countBadge.setStyle("-fx-background-color: #1c2a35; -fx-text-fill: #e1e8ed; -fx-padding: 4 10; -fx-background-radius: 12; -fx-font-size: 10px; -fx-font-weight: bold;");
        header.getChildren().addAll(title, spTitle, countBadge);

        VBox listContainer = new VBox();
        listContainer.setStyle("-fx-background-color: transparent;");

        SimulationState.activePatients.addListener((javafx.collections.ListChangeListener.Change<? extends SimulationState.PatientModel> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (SimulationState.PatientModel p : c.getAddedSubList()) {
                        listContainer.getChildren().add(createPatientRow(p));
                    }
                }
                if (c.wasRemoved() && SimulationState.activePatients.isEmpty()) {
                    listContainer.getChildren().clear();
                }
            }
        });

        card.getChildren().addAll(header, listContainer);
        return card;
    }

    private HBox createPatientRow(SimulationState.PatientModel item) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-border-color: transparent transparent #1c2a35 transparent; -fx-border-width: 0 0 1 0; -fx-padding: 12 0 12 0;");

        Label idLbl = new Label(item.idProperty().get());
        idLbl.setStyle("-fx-background-color: #e1e8ed; -fx-text-fill: #0d7d73; -fx-padding: 12 10; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 12px;");

        VBox subBox = new VBox(4);
        Label nameLbl = new Label("Patient Data");
        nameLbl.setStyle("-fx-text-fill: #5c7080; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label detailLbl = new Label();
        detailLbl.textProperty().bind(item.detailsProperty());
        detailLbl.setStyle("-fx-text-fill: #8a9ba8; -fx-font-size: 12px;");
        subBox.getChildren().addAll(nameLbl, detailLbl);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label statusLbl = new Label();
        statusLbl.textProperty().bind(item.statusProperty());

        Runnable updateStyle = () -> {
            String status = item.statusProperty().get();
            String statusColor = status.equals("Completed") ? "#e1e8ed" :
                    (status.equals("In treatment") ? "#2ee6a6" :
                     (status.equals("Outside") || status.equals("Returned") ? "#ffb74d" : "#ffffff"));

            String statusBg = status.equals("Completed") ? "#0d7d73" :
                    (status.equals("Outside") || status.equals("Returned") ? "#3a2a10" : "#1c2a35");

            statusLbl.setStyle("-fx-background-color: " + statusBg + "; -fx-text-fill: " + statusColor + "; -fx-padding: 6 12; -fx-background-radius: 12; -fx-font-size: 11px; -fx-font-weight: bold;");
        };
        updateStyle.run();
        item.statusProperty().addListener((obs, oldV, newV) -> updateStyle.run());

        box.getChildren().addAll(idLbl, subBox, sp, statusLbl);
        return box;
    }

    private VBox createEventStreamCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #141e26; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #1c2a35; -fx-border-radius: 8;");

        HBox header = new HBox();
        Label title = new Label("Event stream");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15px; -fx-font-weight: bold;");
        Region spTitle = new Region();
        HBox.setHgrow(spTitle, Priority.ALWAYS);
        Label liveBadge = new Label("● LIVE");
        liveBadge.setStyle("-fx-text-fill: #2ee6a6; -fx-font-size: 10px; -fx-font-weight: bold;");
        header.getChildren().addAll(title, spTitle, liveBadge);

        VBox listContainer = new VBox();
        listContainer.setStyle("-fx-background-color: #141e26;");

        SimulationState.eventStream.addListener((javafx.collections.ListChangeListener.Change<? extends SimulationState.LogEvent> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (SimulationState.LogEvent event : c.getAddedSubList()) {
                        listContainer.getChildren().add(0, createEventRow(event));
                    }
                }
                if (c.wasRemoved() && SimulationState.eventStream.isEmpty()) {
                    listContainer.getChildren().clear();
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setPrefHeight(350);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("custom-scroll-pane");
        scrollPane.setStyle("-fx-background: #141e26; -fx-background-color: #141e26; -fx-border-color: transparent;");

        card.getChildren().addAll(header, scrollPane);
        return card;
    }

    private HBox createEventRow(SimulationState.LogEvent item) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-border-color: transparent transparent #1c2a35 transparent; -fx-border-width: 0 0 1 0; -fx-padding: 12 0 12 0;");

        String colorHex = item.getType().equals("SUCCESS") ? "#2ee6a6" : (item.getType().equals("WARN") ? "#ffb74d" : "#42a5f5");
        Circle dot = new Circle(4, Color.web(colorHex));

        VBox textContainer = new VBox(4);
        Label msgLbl = new Label(item.getMessage());
        msgLbl.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label subLbl = new Label(item.getSubtext());
        subLbl.setStyle("-fx-text-fill: #e1e8ed; -fx-font-size: 12px;");
        textContainer.getChildren().addAll(msgLbl, subLbl);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label timeLbl = new Label(item.getTimestamp());
        timeLbl.setStyle("-fx-text-fill: #5c7080; -fx-font-size: 11px;");

        box.getChildren().addAll(dot, textContainer, sp, timeLbl);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}