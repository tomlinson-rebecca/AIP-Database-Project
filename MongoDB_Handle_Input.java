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
   //    collection.insert({"date":ISODate("2016-03-03T08:00:00.000")});
		
	}

	@Override
	public void removeItem(String taskName) {
		Document query = new Document("name", taskName);
		MongoCursor<Document> cursor = collection.find(query).iterator();
        collection.deleteOne(cursor.next());
		
		
	}

	
	//List<Document> all = coll.find().toArray();
	@Override
	public void printList() {
		printList("1");
         
		
	}

	@Override
	public void printList(String sortedBy) {
		MongoCursor<Document> cursor = collection.find().iterator();
		  
		  //sort by due date
		  if(sortedBy.equals("2")){
			  
			  cursor = (collection.find().sort(Sorts.orderBy(Sorts.descending("dueDate")))).iterator();
		//	  cursor.sort(new BasicDBObject("dueDate", 1));
		  }
		  
		  //aggregate to set priorities//TODO: Learn what aggregate does, then convert it w this. Found similar result, copy that
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
						                    								  new Document("$eq", Arrays.asList("$priority", "high"))))
						                    				  				  .append("then", 2)
						                    				  				  
						                    				  				  .append("else",
												                    				  new Document("$cond", 
												                    						  new Document("if", 
												                    								  new Document("$eq", Arrays.asList("$priority", "medium"))))
												                    				  				  .append("then", 3)
												                    				  				  
												                    				  				  .append("else", 4)
												                    		  )
						                    				  				  
						                    				  				  
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
			  
			  /*
			  AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
				  new Document("$project", 
						  new Document("$order", 
								  new Document ("$cond", new Document ("$if", 
										  new Document ("$eq", Arrays.asList("$priority", "very high")))_
								  .append("then",1)
								  .append("else",2 )))
	                     
				  
			  ))));
			  
			  /*
			  AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
					  
					  (Document) new Document("$project",
							  (Document) new Document("$order",
									  (Document) new Document("$cond",
											  (Document) new Document("$if",
													  (Document) new Document("$eq", Arrays.asList("$priority", "very high")))
											  .append("then", 1)
											  .append("else", 2) ))),
					  (Document) new Document("$sort", new Document("$order", 1))));
					  */
					  /*
			  for (Document Document : output)
			  {
			      System.out.println(Document);
			  }
		//	  System.out.println(output.first());
			/*  
			MongoCollection<Document> my_collection = db.getCollection("todoList1");

			  AggregateIterable<Document> output = my_collection.aggregate(Arrays.asList(
			          new Document("$unwind", "$views"),
			          new Document("$match", new Document("views.isActive", true)),
			          new Document("$sort", new Document("views.date", 1)),
			          new Document("$limit", 200),
			          new Document("$project", new Document("_id", 0)
			                      .append("url", "$views.url")
			                      .append("date", "$views.date"))
			          ));

			  // Print for demo
			  for (Document Document : output)
			  {
			      System.out.println(Document);
			  }
			 
			  /*
			  AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
				        new Document("$unwind", "$views"),
				        new Document("$match", new Document("views.isActive", true)),
				        new Document("$sort", new Document("views.date", 1)),
				        new Document("$limit", 200),
				        new Document("$project", new Document("_id", 0)
				                    .append("url", "$views.url")
				                    .append("date", "$views.date"))
				        ));
				
			 
			  /*
			 Document order = (Document) new Document("$order",
					  (Document) new Document("$cond",
							  (Document) new Document("$if",
									  (Document) new Document("$eq", Arrays.asList("$priority", "very high")))
							  .append("then", 1)
							  .append("else", 2) ));
			  AggregationOutput output = collection.aggregate(Arrays.asList(
					  
					  (Document) new Document("$project", order
							  ),
					  (Document) new Document("$sort", order)));
			  System.out.println(output);
			  */
					  
					  
					  /*
				        (Document) new Document("$unwind", "$views"),
				        (Document) new Document("$match", new Document("views.isActive", true)),
				        (Document) new Document("$sort", new Document("views.date", 1)),
				        (Document) new Document("$limit", 200),
				        (Document) new Document("$project", new Document("_id", 0)
				                    .append("url", "$views.url")
				                    .append("date", "$views.date"))
				        )).results(); */

		  }
		  /*
		  db.task.aggregate([
		                     { "$project" : {
		                         "_id" : 1,
		                         "task" : 1,
		                         "status" : 1,
		                         "order" : {
		                             "$cond" : {
		                                 if : { "$eq" : ["$status", "new"] }, then : 1,
		                                 else  : { "$cond" : {
		                                     "if" : { "$eq" : ["$status", "pending"] }, then : 2, 
		                                     else  : 3
		                                     }
		                                 }
		                             }
		                         }
		                     } }, 
		                     {"$sort" : {"order" : 1} },
		                     { "$project" : { "_id" : 1, "task" : 1, "status" : 1 } }
		                 ])
		                 */
		  
		  /*
		  while(cursor.hasNext()){
	        	 System.out.println(cursor.next());
	      }*/
		
		
	}
	
	
	
}