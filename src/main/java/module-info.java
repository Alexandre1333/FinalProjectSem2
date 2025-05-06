module com.fp.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.fp.finalproject to javafx.fxml;
    exports com.fp.finalproject;
}