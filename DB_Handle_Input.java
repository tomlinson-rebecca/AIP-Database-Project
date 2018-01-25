/**
 * Each of the three database types will inherit from a handler interface to ease switching between the two.
 * @author Beaky
 *
 */
public interface DB_Handle_Input {
	public void insertTask(TaskItem t);
	public void removeItem(String taskName);
	public void printList();
	public void printList(String sortedBy);
	//public boolean contains(String taskName); //EXISTS?
}
