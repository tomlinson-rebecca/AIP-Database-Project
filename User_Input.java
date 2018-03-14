
package default_package;

/**
 * Handles user input, redirecting commands to proper database. Try to have it be neutral of type,
 * so this can be a "hub" that sends commands to each database.
 * This is a todo list, so user can insert items, remove them, check them off.
 * Each item has: unique ID (IMPORTANT. THIS IS HOW user references each item?), task name, due date, priority...
 * User can make multiple lists (tables), and view the items in each table based on certain sortings.
 * I know typing commands vis terminal is kinda like doing the SQL itself, but this way a single command works for
 * all 3 types. Ex: User could type "show table. sort table. etc" wow they might as well type SQL lol...
 * well this is making up for my lack of a GUI, in the real world its button presses.
 * 
 * process new user language (sort table by X) or make them type in SQL? B-b-but MongoDB and FIrebase can't just type dat.
 */
//TODO: allow user to create a new list
//TODO allow for large amount of data inserted at once, file parsing etc. 
//create table todoList1(id int NOT NULL AUTO_INCREMENT primary key, taskName VARCHAR(40) NOT NULL, dueDate DATETIME, priority VARCHAR(40));

import java.io.IOException;
import java.util.Scanner;

public class User_Input {
	
	static DB_Handle_Input inputHandler;
	static Scanner input;
	
	public static void main(String[] args) {        
		input = new Scanner(System.in);
        
        String response;
        boolean quit = false;
        boolean setDB = true;
        
        
        while(!quit){
        	
        	if(setDB){ //choose which implementation to use
        		
        		System.out.println("(S) for SQL backend, (M) for MongoDB backend, (D) for DynamoDB backend.");
        		response = input.nextLine().trim().toLowerCase();
        		if(response.equals("s")){
        			inputHandler = new SQL_Handle_Input();
        		} else if (response.equals("m")){
        			inputHandler = new MongoDB_Handle_Input("todoList1");
        		} else if(response.equals("d")){
        			inputHandler = new DynamoDB_Handle_Input("todoList1");
        		} else {
        			break;
        		}
        		
        		setDB = false;
        	}
        	
        	System.out.println("Enter your command: \n"+
        			"(I) or (i) for inserting a new item. \n"+
        			"(P) or (p) to print the entire list. \n"+
        			"(D) or (d) to delete an item from list. \n"
        			);
            response = input.nextLine().trim();
            if(response.equals("q") || response.equals("quit")){
            	quit = true;
            	break;
            } else if (response.toLowerCase().equals("i")){
            	insertItem();
            } else if(response.toLowerCase().equals("p")){
            	//ask what to sort by later
            	sortChoice();
            	
            } else if(response.toLowerCase().equals("d")){
            	//ask what to sort by later
            	deleteItem();
            }
            
            
            else {
            	//System.out.println(inputHandler.getName(Integer.parseInt(response)));
            }
        	
        }
            
    }
	
	//inserts an item in the database.
	public static void insertItem(){
		TaskItem final_Item = null;
		String task;
		String date;
		String priority;
		boolean done = false;
		while(!done){
			System.out.println("Task name?");
			task = input.nextLine().trim();
			System.out.println("Due date? YYYY-MM-DD HH:MI:SS");
			date = input.nextLine().trim();
			System.out.println("Priority? (low, medium, high, very high");
			priority = input.nextLine().trim();
			
			final_Item = new TaskItem(task, date, priority);
			done = true;
		}
		inputHandler.insertTask(final_Item);
		
	}
	
	
	public static void sortChoice(){
		String sortType = null;
		boolean done = false;
		while(!done){
			System.out.println("Sort the list by: (1) id, (2) due date, (3) priority");
			sortType = input.nextLine().trim();
			done = true;
		}
		inputHandler.printList(sortType);
		
	}
	
	//how to handle error? Can we check if item is in DB before deleting?
	public static void deleteItem(){
		String name = null;
		boolean done = false;
		while(!done){
			inputHandler.printList();
			System.out.println("Give name of task you want to remove.");
			name = input.nextLine().trim();
			inputHandler.removeItem(name);
			inputHandler.printList();
			done = true;
		}
		
		
	}
	
}