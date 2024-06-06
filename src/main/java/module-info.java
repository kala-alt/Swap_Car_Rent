module com.example.mutualrentacar {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires java.desktop;
    requires java.sql;



    opens com.example.mutualrentacar to javafx.fxml;
    exports com.example.mutualrentacar;
}