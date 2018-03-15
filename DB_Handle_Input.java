//adaption layer interface, built to adapt the client's functionality, not the specific database's functionality.
package default_package;

/**
 * Each of the three database types will inherit from a handler interface to ease switching between the two.
 * @author Beaky
 *
 */
public interface DB_Handle_Input {
	public void insertTask(TaskItem t);
	public void removeTask(String taskName);
	public void printList();
	public void printList(String sortedBy);
	
}
