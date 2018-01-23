/**
 * A task item, holds all the fields that a single task in the database has, such as
 * name, date, priority, etc.
 * @author Beaky
 *
 */
public class TaskItem {
	public int id = -1; //id will be set once it is inserted into table
	public String name = "task";
	public String data = "No due date";
	public String priority = "low";
	
	public TaskItem(String n, String d, String p){
		name = n;
		data = d;
		priority = p;
	}
	
	
}