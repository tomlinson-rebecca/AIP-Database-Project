//TODO: have a string for the ID primary key, but unable to find element when searching
//TODO IMPLEMENT REMOVE
//with this key. In insert, can't even find the element by key immediately after creating it.
//Something is wrong. Google err msgs, make sure you can find elts by primary key.
//musy provide primary key and other (range key) key (name)
package default_package;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class DynamoDB_Handle_Input implements DB_Handle_Input{

	static String tableName = "";
	AmazonDynamoDB client;
	DynamoDB dynamoDB;
	Table table;
   
	
	public DynamoDB_Handle_Input(String t_name){
		//create table. Uncomment to create a new table
		//DynamoDB_Connection dbConn = new DynamoDB_Connection(t_name);
		
		tableName = t_name;
		client = AmazonDynamoDBClientBuilder.standard()
    			.withRegion(Regions.US_WEST_2)
    			.build();

       dynamoDB = new DynamoDB(client);
       table = dynamoDB.getTable(tableName);
	}
	
	@Override
	public void insertTask(TaskItem t) {
		String uniqueID = UUID.randomUUID().toString();
		// Generate the item 
		
        Item item = new Item()
           .withPrimaryKey("ID", uniqueID) 
           .withString("taskName", t.name)
           .withString("dueDate", t.date)
           .withString("priority", t.priority);
     // Add item to the table  
        
        PutItemOutcome outcome = table.putItem(item);
   	}

	@Override
	public void removeItem(String taskName) {
		
		// TODO Auto-generated method stub
		ScanRequest scanRequest = new ScanRequest()
	        	.withTableName(tableName);
	    
		String removeId = "";
		String formattedName = "{S: "+taskName+",}";
		ScanResult result = client.scan(scanRequest);
		for (Map<String, AttributeValue> i : result.getItems()){
			if (formattedName.equals((i.get("taskName"))+"")){
				removeId = (""+ i.get("ID"));
				System.out.println(removeId);
			} else {
				System.out.println(i.get("taskName") +" != "+(formattedName));
			}
		    
		}
		//cut the formatting out, we juse want the ID number
		String idNum = removeId.substring(4, removeId.length() - 2);

		DeleteItemSpec spec = new DeleteItemSpec().withPrimaryKey("ID", idNum, "taskName", taskName);
       // Item test = table.getItem(spec);//System.out.println(idNum);
        
        
		DeleteItemOutcome outcome = table.deleteItem(spec);
		System.out.println(outcome);
	
	}

	@Override
	public void printList() {
		printList("1");
		
		
	}

	@Override
	public void printList(String sortedBy) {
		ScanRequest scanRequest = new ScanRequest()
	        	.withTableName(tableName);
	        
		ScanResult result = client.scan(scanRequest);
		List<Map<String, AttributeValue>> elts = result.getItems();
	
		String sortField = "";
		
		//alphabetic sorting for both ID and dueDate, choose which one
		if(sortedBy.equals("1")){
			sortField = "ID";
		} else if(sortedBy.equals("2")){
			sortField = "dueDate";
		}
		
		if(sortedBy.equals("2") || sortedBy.equals("1")){ //sort by duedate
			
			for(int i = 0; i < elts.size()-1; i++){
				
				for(int j = 0; j < elts.size()-i-1; j++){
					//compare due date strings. Lexigraphically smaller is earlier
					if(elts.get(j).get(sortField).toString().compareTo(elts.get(j+1).get(sortField).toString()) > 0){
						Map<String, AttributeValue> tmp = elts.get(j);
						elts.set(j, elts.get(j+1));
						elts.set(j+1, tmp);
										
					}		
				}
			}
		}
		
		if (sortedBy.equals("3")){
			java.util.Collections.sort(elts , new PriorityComparator()); 
		}
		
		for(int i = 0; i < elts.size(); i++){
			System.out.println(elts.get(i));
		}
			
			
			
			
		
	}

}
