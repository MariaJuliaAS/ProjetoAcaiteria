module ProjetoAcaiteria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens br.edu.ufersa.view to javafx.fxml, javafx.graphics;
    exports br.edu.ufersa.view;

    opens br.edu.ufersa.controller to javafx.fxml, javafx.graphics;
    exports br.edu.ufersa.controller;

    opens br.edu.ufersa.model.entities to javafx.fxml, javafx.graphics;
    exports br.edu.ufersa.model.entities;
    exports br.edu.ufersa.facade;
    opens br.edu.ufersa.facade to javafx.fxml, javafx.graphics;
    exports br.edu.ufersa.model.interfaces;
    opens br.edu.ufersa.model.interfaces to javafx.fxml, javafx.graphics;
}