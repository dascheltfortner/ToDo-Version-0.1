package src.main;

/**
 * This application was created by Daschel Fortner. 
 * Do not remove this header.
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Main class creates the window and begins the application.
 * 
 * @author  Daschel Fortner
 * @version 0.1
 */
public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws IOException
    {
	// Set up the window
	primaryStage.setTitle("ToDo");
	primaryStage.setWidth(640.0);
	primaryStage.setHeight(640.0);
	primaryStage.centerOnScreen();
	
	// Load in the FXML document
	AnchorPane content = (AnchorPane)FXMLLoader.load(getClass().getResource("Window.fxml"));
	Scene scene = new Scene(content);
	
	// Apply the main stylesheet
	scene.getStylesheets().add("main_style.css");
	
	// Set the content and show the window
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    public static void main(String[] args)
    {
	launch(args);
    }
}
