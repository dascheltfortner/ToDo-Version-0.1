package src.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The CalendarCreator class holds a <code>java.util.List</code> of Tasks and a
 * <code>java.time.LocalDate</code> as instance variables.
 * 
 * <p>
 * The CalendarCreator class contains a method which creates a weekly view
 * calendar in a <code>javafx.scene.layout.HBox</code>.
 * </p>
 * 
 * <p>
 * This class also has mutator methods for manipulating the start date of the
 * CalendarCreator.
 * </p>
 * 
 * @author Daschel T Fortner
 * @version 0.1
 */
public class CalendarCreator
{

    /*
     * The tasks field contains the tasks that will be displayed by the
     * calendar. The startDate is the point where the calendar will start. The
     * calendar displays the next six days after the start date.
     * 
     */

    private List<Task> tasks;

    private LocalDate startDate;

    /**
     * Constructs a new CalendarCreator object.
     * 
     * <p>
     * The tasks that are passed through this constructor are the Tasks that
     * will be shown in the Calendar through the <code>createCalendarInPane
     * </code> method.
     * </p>
     * 
     * <p>
     * The passed startDate is the beginning day of the Calendar. The calendar
     * is shown in weekly view, and therefore, becomes day one of the week when
     * it is shown in Calendar view.
     * </p>
     * 
     * @param tasks
     *            a <code>java.util.List</code> of Task objects to be displayed
     *            in the Calendar
     * @param startDate
     *            the beginning day for the calendar
     */
    public CalendarCreator(List<Task> tasks, LocalDate startDate)
    {
	this.tasks = tasks;
	this.startDate = startDate;
    }

    /**
     * Creates a weekly view calendar using this CalendarCreator's task and
     * start date fields.
     * 
     * <p>
     * The calendar is shown as a series of boxes in a horizontal line, with
     * each box representing a day of the week, and each box containing a label
     * for every Task on that day.
     * </p>
     * 
     * <p>
     * The first day of the Calendar's week is specified by the instance
     * variable <code>start date</code>. Thus, the week begins at the start date
     * and advances six more days, resulting in a week. To advance the Calendar,
     * use the <code>
     * CalendarCreator</code>'s <code>advanceOneWeek</code> and
     * <code>retreatOneWeek
     * </code> methods.
     * </p>
     * 
     * @return a <code>javafx.scene.layout.HBox</code> that contains a weekly
     *         Calendar beginning at <code>CalendarCreator.startDate</code>, and
     *         shows six more days
     */
    public HBox createCalendarInPane()
    {
	// Create the parent container
	HBox root = new HBox();

	root.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
	root.setSpacing(10.0);
	root.setAlignment(Pos.CENTER);

	// Make a copy of the startDate so it can be advanced for the week
	LocalDate iDate = startDate;
	
	// Begin the week creation
	for (int i = 0; i < 7; ++i)
	{

	    // Construct a container for the day specified by iDate
	    VBox dayBox = new VBox();

	    dayBox.setSpacing(20.0);
	    dayBox.setMinHeight(400.0);
	    dayBox.getStyleClass().add("DayBox");
	    
	    // Set the border color of today to green, dark grey otherwise
	    dayBox.setStyle(dayBox.getStyle().concat("-fx-border-color: "));
	    dayBox.setStyle(dayBox.getStyle().concat((LocalDate.now().equals(iDate)
		    				     ?("#00AA00;"):("#252525;"))));

	    // Create a heading for each day
	    Label dayHeading = new Label(iDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM uu")));
	    dayHeading.getStyleClass().add("DayHeadingText");
	    
	    dayBox.alignmentProperty().set(Pos.TOP_CENTER);
	    dayBox.getChildren().add(dayHeading);

	    // Find all the tasks for this day
	    for (int j = 0; j < tasks.size(); ++j)
	    {
		if (iDate.equals(tasks.get(j).getDueDate()))
		{
		    Task currentTask = tasks.get(j);

		    Label task = new Label(currentTask.getDescription());

		    // Color overdue tasks with red, others with black
		    String color = (currentTask.isOverdue()?
			    	    ("#AA0000"):("#000000"));

		    task.setStyle("-fx-font-size: 14pt; " + 
			    	  "-fx-text-fill: " + color + ";");

		    dayBox.getChildren().add(task);
		}
	    }

	    // Add the day to the calendar
	    root.getChildren().add(dayBox);

	    HBox.setHgrow(dayBox, Priority.ALWAYS);

	    // Increment the date copy
	    iDate = iDate.plusDays(1);
	    
	}

	return root;
    }

    /**
     * Advances the calendar by one week. This is a mutator method that performs
     * the function <code>startDate.plusDays(7)</code> as defined in <code>
     * java.time.LocalDate</code>.
     */
    public void advanceOneWeek()
    {
	startDate = startDate.plusDays(7);
    }

    /**
     * Moves the calendar back by one week. This is a mutator method that 
     * performs the function <code>startDate.minusDays(7)</code> as defined in
     * <code>java.time.LocalDate</code>. 
     */
    public void retreatOneWeek()
    {
	startDate = startDate.minusDays(7);
    }

    /**
     * Sets the Tasks that will be viewed in this Calendar.
     * 
     * @param tasks the <code>java.util.List</code> of Tasks to be viewed
     */
    public void setTasks(List<Task> tasks)
    {
	this.tasks = tasks;
    }

}
