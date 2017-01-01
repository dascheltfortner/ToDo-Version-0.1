package src.data.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import src.data.Task;

/**
 * The DataIO class in an Singleton class that handles all I/O 
 * operations necessary for the application. 
 * 
 * <p>
 * For each individual I/O operation, the instance must be reset with
 * <code>DataIO.getInstance()</code>. Once the instance of this class 
 * has been retrieved, the <code>loadTaskList</code> and 
 * <code>saveTaskList</code> methods can be used to export or import
 * whole lists of Task objects.
 * </p>
 * 
 * @author  Daschel T Fortner
 * @version 0.1
 */
public class DataIO
{

    /*
     * Using the Singleton design pattern, this class contains
     * its own instance to itself. An instance has its own path, 
     * and thus DataIO can only refer to one file at a time.
     * 
     */
    
    private static DataIO instance = null;
    
    private static String path;
    
    /**
     * This Constructor is private and therefore cannot be used 
     * outside this class. The constructor sets the path variable.
     * 
     * @param filePath the path to the file manipulated by this class
     */
    private DataIO(String filePath)
    {
	path = filePath;
    }
    
    /**
     * Returns a <code>java.util.LinkedList</code> that contains all of the 
     * Task items contained in the path set by calling the <code>
     * DataIO.getInstance()</code> method.
     * 
     * <p>
     * This method expects input in a very specific format, and will not 
     * function reliably if the input file deviates from that format. This
     * method reads fields from the file in the following format:
     * </p>
     * 
     * <p>
     * <code>(Task description):(Task due date):(Task completion status)</code>
     * </p>
     * 
     * <p>
     * Each Task must be on its own line.
     * </p>
     * 
     * <p>
     * If an IOException or a FileNotFoundException occurs, this method throws
     * a RuntimeException. If the operation is without error, the LinkedList
     * containing all the file's Tasks is returned. 
     * </p>
     * 
     * @return a <code>java.util.List</code> containing every Task stored in 
     * 			the file referred to by <em>path</em>
     * @throws RuntimeException if an Exception occurs during the reading of 
     * 			the file
     */
    public List<Task> loadTaskList() throws RuntimeException
    {
	List<Task> taskList = new LinkedList<Task>();
	
	try(BufferedReader in = new BufferedReader(new FileReader(path)))
	{
	    
	    String line = "";
	    while((line = in.readLine()) != null)
	    {
		String description = line.split(":")[0];
		LocalDate due      = LocalDate.parse(line.split(":")[1]);
		boolean completed  = Boolean.parseBoolean(line.split(":")[2]);
		
		taskList.add(new Task(description, due, completed));
	    }
	    	    
	}
	catch (FileNotFoundException e)
	{
	    throw new RuntimeException(e);
	}
	catch (IOException e)
	{
	    throw new RuntimeException(e);
	}
	
	return taskList;
    }
    
    
    /**
     * Writes a <code>java.util.List</code> to a file specified by the 
     * path field of this class. The List's contents are traversed, and
     * each Task is written to the file on its own line.
     * 
     * <p>
     * Each Task is written with in the format expected by the <code>
     * DataIO.readTaskList</code>. This method writes to the file in 
     * the following format:
     * </p>
     * 
     * <p>
     * <code>(Task description):(Task due date):(Task completion status)</code>
     * </p>
     * 
     * <p>
     * Each Task is written on its own line.
     * </p>
     * 
     * <p>
     * If an Exception occurs during the output operation, a RuntimeException
     * is thrown. 
     * </p>
     * 
     * @param tasks		the list of Tasks to be written to file
     * @throws RuntimeException if the write operation encounters an error.
     */
    public void saveTaskList(List<Task> tasks) throws RuntimeException
    {
	try(BufferedWriter out = new BufferedWriter(new FileWriter(path)))
	{
	    
	    for(int i = 0; i < tasks.size(); ++i)
	    {
		out.write(tasks.get(i).getDescription() + ":");
		out.write(tasks.get(i).getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		out.write(":" + tasks.get(i).isCompleted());
		out.newLine();
	    }

	}
	catch (IOException e)
	{
	    throw new RuntimeException(e);
	}
    }
    
    /**
     * Retrieves or creates an instance of this class with the specified
     * path. The passed filePath parameter will be the path used for 
     * read or write operations called with this instance.
     * 
     * @param  filePath the path to the file to be read or written
     * @return the instance of the DataIO class
     */
    public static DataIO getInstance(String filePath)
    {
	if(instance == null || !path.equals(filePath))
	{
	    instance = new DataIO(filePath);
	}
	
	return instance;
    }
    
}
