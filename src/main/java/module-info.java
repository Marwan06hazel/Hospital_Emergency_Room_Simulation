module org.hospital.test001 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.hospital.test001 to javafx.fxml;
    exports org.hospital.test001;
}