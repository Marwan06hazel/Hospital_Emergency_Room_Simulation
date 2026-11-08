package org.hospital.test001;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimulationState {

    public static class PatientModel {
        private final StringProperty id = new SimpleStringProperty();
        private final StringProperty details = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty();

        public PatientModel(int id, String details, String status) {
            this.id.set("P" + String.format("%02d", id));
            this.details.set(details);
            this.status.set(status);
        }

        public StringProperty idProperty() { return id; }
        public StringProperty detailsProperty() { return details; }
        public StringProperty statusProperty() { return status; }
    }

    public static class LogEvent {
        private final String timestamp;
        private final String message;
        private final String subtext;
        private final String type;

        public LogEvent(String message, String subtext, String type) {
            this.timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            this.message = message;
            this.subtext = subtext;
            this.type = type;
        }

        public String getTimestamp() { return timestamp; }
        public String getMessage() { return message; }
        public String getSubtext() { return subtext; }
        public String getType() { return type; }
    }

    public static final IntegerProperty patientsArrived = new SimpleIntegerProperty(0);
    public static final IntegerProperty patientsOutside = new SimpleIntegerProperty(0);
    public static final IntegerProperty patientsWaiting = new SimpleIntegerProperty(0);
    public static final IntegerProperty patientsInTreatment = new SimpleIntegerProperty(0);
    public static final IntegerProperty patientsCompleted = new SimpleIntegerProperty(0);
    public static final IntegerProperty totalPatients = new SimpleIntegerProperty(0);

    public static final IntegerProperty availableDoctors = new SimpleIntegerProperty(0);
    public static final IntegerProperty totalDoctors = new SimpleIntegerProperty(0);

    public static final IntegerProperty treatmentRoomsUsed = new SimpleIntegerProperty(0);
    public static final DoubleProperty throughputProgress = new SimpleDoubleProperty(0.0);

    public static final ObservableList<PatientModel> activePatients = FXCollections.observableArrayList(
            param -> new Observable[]{param.detailsProperty(), param.statusProperty()}
    );
    public static final ObservableList<LogEvent> eventStream = FXCollections.observableArrayList();

    public static void reset(int numDoctors, int numPatients) {
        Platform.runLater(() -> {
            patientsArrived.set(0);
            patientsOutside.set(0);
            patientsWaiting.set(0);
            patientsInTreatment.set(0);
            patientsCompleted.set(0);
            totalPatients.set(numPatients);
            availableDoctors.set(numDoctors);
            totalDoctors.set(numDoctors);
            treatmentRoomsUsed.set(0);
            throughputProgress.set(0.0);
            activePatients.clear();
            eventStream.clear();
        });
    }

    public static void addLog(String message, String subtext, String type) {
        Platform.runLater(() -> eventStream.add(0, new LogEvent(message, subtext, type)));
    }

    public static void updatePatient(int patientId, String details, String status) {
        Platform.runLater(() -> {
            String pIdStr = "P" + String.format("%02d", patientId);
            for (PatientModel p : activePatients) {
                if (p.idProperty().get().equals(pIdStr)) {
                    p.detailsProperty().set(details);
                    p.statusProperty().set(status);
                    return;
                }
            }
            activePatients.add(new PatientModel(patientId, details, status));
        });
    }
}