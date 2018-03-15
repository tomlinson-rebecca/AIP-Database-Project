//cmd mongod --dbpath=. in mongodata folder, then use MongoDB Compass Community to launch localhost

package default_package;

/*
 * Handles Input for the MongoDB version
 */
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Sorts.*;



public class MongoDB_Handle_Input implements DB_Handle_Input{

	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;
	
	@SuppressWarnings("deprecation")
	public MongoDB_Handle_Input(String colName) {
		try {
			 mongoClient = new MongoClient();
			 db = mongoClient.getDatabase("BeakyDB");

			 collection = db.getCollection(colName);
	       
	       
		} catch (Exception e){
			System.out.println("Error creating MongoClient.");
		}
	}
	
	
	
	@Override
	public void insertTask(TaskItem t) {
		Document task = new Document()
                .append("name", t.name)
                .append("dueDate",(t.date))
                .append("priority" , t.priority);
       collection.insertOne(task);
		
	}

	public void removeTask(String taskName) {
		Document query = new Document("name", taskName);
		MongoCursor<Document> cursor = collection.find(query).iterator();
        collection.deleteOne(cursor.next());
		
		
	}

	@Override
	public void printList() {
		printList("1");
	}

	@Override
	public void printList(String sortedBy) {
		MongoCursor<Document> cursor = collection.find().iterator();
		  
		  //sort by due date
		  if(sortedBy.equals("2")){
			  
			  cursor = (collection.find().sort(Sorts.orderBy(Sorts.ascending("dueDate")))).iterator();
			  while(cursor.hasNext()){
				  System.out.println(cursor.next());
			  }
		//	  cursor.sort(new BasicDBObject("dueDate", 1));
		  }
		  
		  //aggregate to set priorities
		  if(sortedBy.equals("3")){
			  
			  MongoCollection<Document> my_collection = db.getCollection("todoList1");

			  AggregateIterable<Document> output = my_collection.aggregate(Arrays.asList(
			       
			          new Document("$project", new Document("name", 1)
			        		  	  .append("dueDate", 1)
			                      .append("priority", 1)   
			                      .append("order", 
			                    		  new Document("$cond", 
			                    				  new Document("if", 
						                    		  new Document("$eq", Arrays.asList("$priority", "very high")))
						                    		  .append("then", 1)
						                    		  
						                    		  .append("else",
						                    				  new Document("$cond", 
						                    						  new Document("if", 
						                    								  new Document("$eq", Arrays.asList("$priority", "high")))
						                    				  				  .append("then", 2)
						                    				  				  
						                    				  				  .append("else",
												                    				  new Document("$cond", 
												                    						  new Document("if", 
												                    								  new Document("$eq", Arrays.asList("$priority", "medium")))
												                    				  				  .append("then", 3)
												                    				  				  
												                    				  				  .append("else", 4)
												                    		  )))
						                    				  				  
						                    				  				  
						                    		  )
			                    	))                
			          ),
			          new Document("$sort", new Document("order", 1))
			   ));

			  // Print for demo
			  for (Document Document : output)
			  {
			      System.out.println("PRIORITY SORT " + Document);
			  }
			  
		  } else {
			  while(cursor.hasNext()){
				  System.out.println(cursor.next());
			  }
		  }
		
		
	}
	
	
	
}