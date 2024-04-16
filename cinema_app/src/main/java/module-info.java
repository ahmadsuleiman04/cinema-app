module org.example.cinema_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.sql.rowset;
    requires java.desktop;




    opens org.example.cinema_app to javafx.fxml;
    exports org.example.cinema_app;
}