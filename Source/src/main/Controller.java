package src.main;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;
import src.data.CalendarCreator;
import src.data.Task;
import src.data.io.DataIO;

/**
 * The Controller class is the Controller for the Window.fxml file. 
 * 
 * <p>
 * This class contains all the logic for the program.
 * </p>
 * 
 * @author  Daschel T Fortner
 * @version 0.1
 */
public class Controller
{
    /*
     * The fields below are instance variables for the Controller class and 
     * are unique to it. The saved and currentPath variables are in place to 
     * track whether and where the current file has been saved. The lists are 
     * used in Window.fxml's ListView. Finally, a CalendarCreator object is used
     * to display the tasks in a Calendar view.
     * 
     */
    
    private String currentPath = "NULL_PATH";
    private boolean saved      = true;

    private ObservableList<Task> currentTasks = FXCollections.observableArrayList();
    private ObservableList<Task> pastTasks    = FXCollections.observableArrayList();
    
    private CalendarCreator calendar;
    
    
    /*
     * The elements below are the fxml document's elements that are referenced
     * in this class.
     * 
     */
    
    @FXML
    private Button addButton;
    
    @FXML
    private ListView<Task> currentTaskList;
    
    @FXML
    private ListView<Task> pastTaskList;
    
    @FXML
    private AnchorPane calendarPane;
    
    
    
    /**
     * This is the JavaFX controller initialize method that sets up the 
     * components and initializes the fields.
     */
    @FXML
    public void initialize()
    {
	
	// Set the place holders for an empty list
	currentTaskList.setPlaceholder(new Label("No Current Tasks"));
	pastTaskList.setPlaceholder(new Label("No Past Tasks"));
	
	// Set a Cell Factory so the tasks can be colored by due date
	currentTaskList.setCellFactory(list ->
	{
	    return new ListCell<Task>()
	    {
		@Override
		protected void updateItem(Task t, boolean empty)
		{
		    super.updateItem(t, empty);
		    
		    // Make sure there is an element present
		    if(empty || t == null)
		    {
			setText(null);
			setGraphic(null);
		    }
		    // Set the style and text based on the due date
		    else
		    {
			if(t.isOverdue())
			{
			    setStyle("-fx-text-fill: #AA0000;");
			}
			else if(t.isDueToday())
			{
			    setStyle("-fx-text-fill: #AAAA00;");
			}
			else
			{
			    setStyle("-fx-text-fill: #000000;");
			}
	
			setText(t.toString());
		    }
		}
	    };
	});
	
	// Set the first day to the beginning of the week
	int indexOfWeek = LocalDate.now().getDayOfWeek().getValue();
	LocalDate startOfWeek = LocalDate.now().minusDays(indexOfWeek);
	calendar = new CalendarCreator(currentTasks, startOfWeek);
	
	// Set the ListView's items and update the calendar
	currentTaskList.setItems(currentTasks);
	pastTaskList.setItems(pastTasks);
	
	updateCalendar();
	
    }
    
    /**
     * Creates a <code>javafx.scene.control.Alert</code> with an AlertType of
     * <code>AlertType.ERROR</code> and shows the Dialog by calling <code>
     * Alert.showAndWait()</code>.
     * 
     * <p>
     * The passed parameters are passed to the Alert through the <code>
     * Alert.setTitle</code>, <code>Alert.setHeaderText</code>, and <code>
     * Alert.setContentText</code> methods.
     * </p>
     * 
     * @param title   the title text for the alert
     * @param header  the header text for the alert
     * @param content the content text for the alert
     */
    private void showErrorDialog(String title, String header, 
	    					 String content)
    {
	showAlertDialog(title, header, content, AlertType.ERROR);
    }
    
    /**
     * Creates a <code>javafx.scene.control.Alert</code> and shows the Dialog 
     * by calling <code> Alert.showAndWait()</code>.
     * 
     * <p>
     * The passed parameters are fed to the Alert, thus setting the title, 
     * header, and content texts, along with the AlertType.
     * </p>
     * 
     * <p>
     * This method returns a <code>java.util.Optional</code> that contains the
     * button that was selected by the user. Ultimately, it is the result of 
     * <code>Alert.showAndWait</code>.
     * </p>
     * 
     * @param title   the title text for the dialog
     * @param header  the header text for the dialog
     * @param content the content text for the dialog
     * @param type    the AlertType of the dialog
     * @return        the ButtonType that was selected by the user
     */
    private Optional<ButtonType> showAlertDialog(String title, String header,
	    					 String content, AlertType type)
    {
	Alert alert = new Alert(type);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	return alert.showAndWait();
    }
    
    /**
     * Resets the task list on the Calendar, places the Calendar in a ScrollPane,
     * and attaches it to the Window.
     */
    private void updateCalendar()
    {
	calendar.setTasks(currentTasks);
	
	ScrollPane sp = new ScrollPane();
	HBox box      = calendar.createCalendarInPane();
	
	sp.fitToHeightProperty().set(true);
	sp.fitToWidthProperty().set(true);
	
	sp.setContent(box);
	
	AnchorPane.setTopAnchor(sp, 0.0);
	AnchorPane.setBottomAnchor(sp, 50.0);
	AnchorPane.setLeftAnchor(sp, 0.0);
	AnchorPane.setRightAnchor(sp, 0.0);
	
	calendarPane.getChildren().add(sp);
    }
    
    /**
     * Handles the click event for File -> New.
     */
    @FXML
    public void handleNew()
    {
	// Warn to save data
	if(saved == false)
	{
	    
	    Optional<ButtonType> result;

	    result = showAlertDialog("", "Quit Without Saving?", 
		    		     "You will lose your unsaved changes.",
		    		     AlertType.CONFIRMATION);
	    
	    if(result.get() == ButtonType.CANCEL)
	    {
		return;
	    }
	}
	
	// Clear everything
	currentPath = "NULL_PATH";
	
	currentTasks.clear();
	pastTasks.clear();
	
	updateCalendar();
	
    }
    
    /**
     * Handles the click event for File -> Open.
     */
    @FXML
    public void handleOpenFile()
    {
	// Select the file to be opened
	FileChooser fc = new FileChooser();
	fc.setTitle("Open Task List...");
	fc.getExtensionFilters().add(new ExtensionFilter("ToDo files", "*.tdl"));
	
	File source = fc.showOpenDialog(null);
	
	// Make sure a file was selected
	if(source == null)
	{
	    showErrorDialog("Error", "No File Selected", 
		    	    "Please select a file to open");
	    return;
	}
	
	// Reset the Task list
	currentTasks.clear();
	pastTasks.clear();
	
	List<Task> fullTaskList = new ArrayList<Task>();
	fullTaskList = DataIO.getInstance(source.getAbsolutePath()).loadTaskList();
	
	// Load all the tasks into their respective lists
	for(int i = 0; i < fullTaskList.size(); ++i)
	{
	    if(fullTaskList.get(i).isCompleted() == false)
	    {
		currentTasks.add(fullTaskList.get(i));
	    }
	    else
	    {
		pastTasks.add(fullTaskList.get(i));
	    }
	}

	// Set the save directory
	currentPath = source.getAbsolutePath();
	
	// Update the Calendar information
	updateCalendar();
    }
    
    /**
     * Handles the File -> Save click event.
     */
    @FXML
    public void handleSave()
    {
	// Use the save as dialog if this hasn't been saved yet
	if(currentPath.equals("NULL_PATH"))
	{
	    handleSaveAs();
	    return;
	}

	List<Task> fullTaskList = new ArrayList<Task>();
	fullTaskList.addAll(currentTasks);
	fullTaskList.addAll(pastTasks);
	
	try
	{	    
	    DataIO.getInstance(currentPath).saveTaskList(fullTaskList);
	}
	catch(RuntimeException e)
	{
	    showErrorDialog("I/O Error", "Save Failure", "Could not save the file.");
	}
	
	saved = true;
	updateCalendar();
    }
    
    /**
     * Handles the File -> Save As click event.
     */
    @FXML
    public void handleSaveAs()
    {
	FileChooser fc = new FileChooser();
	fc.setTitle("Save As...");

	// Get the save location
	File location = fc.showSaveDialog(null);
	
	// Make sure a location was selected
	if(location == null)
	{
	    showErrorDialog("Error", "No File Selected", 
		    	    "Please select a save location.");
	    return;    
	}
	
	// Get the file path and ensure the correct extension
	String filePath = location.getAbsolutePath();
	if(!filePath.endsWith(".tdl"))
	{
	    filePath.concat(".tdl");
	}
	
	List<Task> fullTaskList = new ArrayList<Task>();
	fullTaskList.addAll(currentTasks);
	fullTaskList.addAll(pastTasks);
	
	try
	{
	    DataIO.getInstance(filePath).saveTaskList(fullTaskList);
	}
	catch(RuntimeException e)
	{
	    showErrorDialog("I/O Error", "Save Failure", "Could not save the file.");
	}
	
	currentPath = filePath;
	saved = true;
	updateCalendar();
    }
    
    /**
     * Handles the click event for the Mark Completed button.
     */
    @FXML
    public void handleMarkCompleted()
    {
	int selected = currentTaskList.getSelectionModel().getSelectedIndex();
	
	if(selected >= 0)
	{
	    currentTasks.get(selected).setCompleted(true);
	    pastTasks.add(currentTasks.remove(selected));   
	}
	
	saved = false;
	
	updateCalendar();
    }
    
    /**
     * Handles the Edit -> New Task or the + button.
     */
    @FXML
    public void handleAddNewTask()
    {
	
	/*
	 * For this method, http://code.makery.ch/blog/javafx-dialogs-official/
	 * was used for reference.
	 * 
	 */
	
	// Create a Dialog
	Dialog<Task> dialogWindow = new Dialog<Task>();
	dialogWindow.setTitle("New Task");
	dialogWindow.initStyle(StageStyle.UTILITY);
	dialogWindow.setHeaderText("Create a new Task");
	dialogWindow.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
							     ButtonType.CANCEL);
	
	// Set up the components
	HBox root = new HBox();
	root.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
	root.setSpacing(20.0);
	
	Label textLabel     = new Label("Description: ");
	TextField textInput = new TextField();
	Label dateLabel     = new Label("Deadline: ");
	DatePicker date     = new DatePicker();

	VBox leftContainer  = new VBox();
	leftContainer.setSpacing(30.0);
	leftContainer.getChildren().addAll(textLabel, dateLabel);
	
	VBox rightContainer = new VBox();
	rightContainer.setSpacing(20.0);
	rightContainer.getChildren().addAll(textInput, date);
	
	root.getChildren().addAll(leftContainer, rightContainer);
	
	dialogWindow.getDialogPane().setContent(root);
	
	// Convert the result into a Task
	dialogWindow.setResultConverter(buttonClicked ->
	{
	    if(buttonClicked == ButtonType.OK)
	    {
		String description = textInput.getText();
		
		// The description can't contain a ':', because it will wreck
		// I/O
		if(description.contains(":"))
		{
		    showErrorDialog("Read Error", "Invalid Input", 
			    	    "Task descriptions cannot contain \':\'.");
		    return null;
		}
		
		LocalDate dueDate = date.getValue();
		
		// Make sure the due date was read
		if(dueDate == null)
		{
		    showErrorDialog("Read Error", "Invalid Input", 
			    	    "Could not read Date.");
		    return null;
		}
		
		return new Task(description, dueDate);
	    }
	    else
	    {
		return null;
	    }
	});
	
	Task newTask = dialogWindow.showAndWait().orElse(null);
	
	if(newTask != null)
	{
	    currentTasks.add(newTask);
	}
	
	saved = false;
	
	updateCalendar();
    }
    
    /**
     * Handles the x button in the first tab.
     */
    @FXML
    public void handleRemoveTask()
    {
	int selectedIndex = currentTaskList.getSelectionModel().getSelectedIndex();
	
	if(selectedIndex >= 0)
	{
	    currentTasks.remove(selectedIndex);
	}
	
	saved = false;
	
	updateCalendar();
    }
    
    /**
     * Handles the Mark Incomplete button.
     */
    @FXML
    public void handleMarkUnCompleted()
    {
	
	int selected = pastTaskList.getSelectionModel().getSelectedIndex();
	
	if(selected >= 0)
	{
	    currentTasks.get(selected).setCompleted(false);
	    currentTasks.add(pastTasks.remove(selected));
	}
	
	saved = false;
	
	updateCalendar();
    }
    
    /**
     * Handles the x button on the past tasks tab.
     */
    @FXML
    public void handleRemoveCompletedTask()
    {
	int selectedIndex = currentTaskList.getSelectionModel().getSelectedIndex();
	
	if(selectedIndex >= 0)
	{
	    pastTasks.remove(selectedIndex);
	}
	
	saved = false;
    }
    
    /**
     * Handles the left hand button on the calendar tab.
     */
    @FXML
    public void handleRetreatCalendar()
    {
	calendar.retreatOneWeek();
	
	updateCalendar();
    }
    
    /**
     * Handles the right hand button on the calendar tab.
     */
    @FXML
    public void handleAdvanceCalendar()
    {
	calendar.advanceOneWeek();
	
	updateCalendar();
    }
    
}
