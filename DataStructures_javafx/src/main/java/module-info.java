module com.example.datastructures_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.datastructures_javafx to javafx.fxml;
    exports com.example.datastructures_javafx;
}