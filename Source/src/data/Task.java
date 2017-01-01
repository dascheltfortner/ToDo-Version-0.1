package src.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is comprised of fields for a single To Do list item. A "Task"
 * defined as this class is a simple construct containing a description, a
 * due date, and a completion status.
 * 
 * <p>
 * The Task class contains all the fields and access methods to accurately 
 * represent such a "Task". 
 * </p>
 * 
 * @author  Daschel T Fortner
 * @version 0.1
 */
public class Task
{
    
    /*
     * The following are the fields contained by this class.
     * 
     * The description and dueDate fields are the only local variables
     * that this class hold. The completed variable is used for IO
     * purposes, so past and current tasks can be exported using a 
     * single List.
     * 
     */

    private String description = "";
    
    private LocalDate dueDate;
    
    private boolean completed = false;
    
    /**
     * Creates a new Task, setting the description and due date for 
     * the task. 
     * 
     * <p>
     * By default, the boolean fields are false. Thus,
     * to use the dueToday, overdue, or completed fields, they must 
     * be set manually after instantiation.
     * </p>
     * 
     * @param name The description of this task, such as "Finish ToDo"
     * @param date The date that this Task is due
     */
    public Task(String name, LocalDate date)
    {
	description = name;
	dueDate     = date;
    }
    
    /**
     * Creates a new Task, setting the description, due date, and
     * completed properties of the Task. 
     * 
     * <p>
     * By default, the boolean fields are false. Thus,
     * to use the dueToday or overdue fields, they must 
     * be set manually after instantiation.
     * </p>
     * 
     * @param name The description of this task, such as "Finish ToDo"
     * @param date The date that this Task is due
     * @param done Whether this task is already completed
     */
    public Task(String name, LocalDate date, boolean done)
    {
	description = name;
	dueDate     = date;
	completed   = done;
    }
    
    @Override
    public String toString()
    {
	return String.format("%s \t %s", dueDate.format(DateTimeFormatter.ofPattern("EEE dd")),
					 description);
    }

    /**
     * @return this Task's description field.
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * @return this Task's due date.
     */
    public LocalDate getDueDate()
    {
	return dueDate;
    }
    
    /**
     * Sets the Due Date for this Task.
     * 
     * @param date the date to set
     */
    public void setDueDate(LocalDate date)
    {
	dueDate = date;
    }
    
    /**
     * Makes a comparison of this Task's due date with the result of 
     * LocalDate.now(). This method evaluates to <em>true</em> if and
     * only if the two LocalDate objects represent the same day. 
     * 
     * @return whether this Task's due date is equivalent to today
     */
    public boolean isDueToday()
    {
	// Compare the Due Date to today
	return (LocalDate.now().compareTo(dueDate) == 0);
    }
    
    /**
     * Makes a comparison of this Task's due date with the result of 
     * LocalDate.now(). This method evaluates to <em>true</em> if and
     * only if this Task's due date is "less than" today, by LocalDate's
     * <code>compareTo()</code> method. 
     * 
     * @return whether this Task is overdue
     */
    public boolean isOverdue()
    {
	// Compare the due date to today
	return (LocalDate.now().compareTo(dueDate) > 0);
    }
    
    /**
     * @return this value of Task's <code>completed</code> variable
     */
    public boolean isCompleted()
    {
	return completed;
    }
    
    /**
     * Sets the completion status of this Task.
     * 
     * @param completed the new completion status for this Task
     */
    public void setCompleted(boolean completed)
    {
	this.completed = completed;
    }
    
}
