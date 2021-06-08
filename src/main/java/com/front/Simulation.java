package com.front;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulation extends Application {
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("/com/fxml/Simulation.fxml"));
        String style = getClass().getResource("/com/css/Simulation.css").toExternalForm();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(style);
       
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }   
}
