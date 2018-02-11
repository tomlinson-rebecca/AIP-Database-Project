/*
 * Handles Input for the MongoDB version
 */
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;




public class MongoDB_Handle_Input implements DB_Handle_Input{

	private MongoClient mongoClient;
	private DB db;
	private DBCollection collection;
	
	@SuppressWarnings("deprecation")
	public MongoDB_Handle_Input(String colName) {
		try {
			 mongoClient = new MongoClient();
			 db = mongoClient.getDB("BeakyDB");

			 collection = db.getCollection(colName);
	       
	       
		} catch (Exception e){
			System.out.println("Error creating MongoClient.");
		}
	}
	
	
	
	@Override
	public void insertTask(TaskItem t) {
		DBObject task = new BasicDBObject()
                .append("name", t.name)
                .append("dueDate",(t.date))
                .append("priority" , t.priority);
                 
		
       collection.insert(task);
   //    collection.insert({"date":ISODate("2016-03-03T08:00:00.000")});
		
	}

	@Override
	public void removeItem(String taskName) {
		DBObject query = new BasicDBObject("name", taskName);
        DBCursor cursor = collection.find(query);
        collection.remove(cursor.next());
		// TODO Auto-generated method stub
		
	}

	
	//List<DBObject> all = coll.find().toArray();
	@Override
	public void printList() {
         DBCursor cursor = collection.find();
         while(cursor.hasNext()){
        	 System.out.println(cursor.next());
         }
         
		
	}

	@Override
	public void printList(String sortedBy) {
		  DBCursor cursor = collection.find();
		  
		  //sort by due date
		  if(sortedBy.equals("2")){
			  cursor.sort(new BasicDBObject("dueDate", 1));
		  }
		  
		  //aggregate to set priorities//TODO: Learn what aggregate does, then convert it w this. Found similar result, copy that
		  if(sortedBy.equals("3")){
			  
			  /*
			  Iterable<DBObject> output = collection.aggregate(Arrays.asList(
					  
					  (DBObject) new BasicDBObject("$project",
							  (DBObject) new BasicDBObject("$order",
									  (DBObject) new BasicDBObject("$cond",
											  (DBObject) new BasicDBObject("$if",
													  (DBObject) new BasicDBObject("$eq", Arrays.asList("$priority", "very high")))
											  .append("then", 1)
											  .append("else", 2) ))),
					  (DBObject) new BasicDBObject("$sort", new BasicDBObject("$order", 1)))).results();
					  */
			
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
			  for (Document dbObject : output)
			  {
			      System.out.println(dbObject);
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
			 DBObject order = (DBObject) new BasicDBObject("$order",
					  (DBObject) new BasicDBObject("$cond",
							  (DBObject) new BasicDBObject("$if",
									  (DBObject) new BasicDBObject("$eq", Arrays.asList("$priority", "very high")))
							  .append("then", 1)
							  .append("else", 2) ));
			  AggregationOutput output = collection.aggregate(Arrays.asList(
					  
					  (DBObject) new BasicDBObject("$project", order
							  ),
					  (DBObject) new BasicDBObject("$sort", order)));
			  System.out.println(output);
			  */
					  
					  
					  /*
				        (DBObject) new BasicDBObject("$unwind", "$views"),
				        (DBObject) new BasicDBObject("$match", new BasicDBObject("views.isActive", true)),
				        (DBObject) new BasicDBObject("$sort", new BasicDBObject("views.date", 1)),
				        (DBObject) new BasicDBObject("$limit", 200),
				        (DBObject) new BasicDBObject("$project", new BasicDBObject("_id", 0)
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
		  
		  while(cursor.hasNext()){
	        	 System.out.println(cursor.next());
	      }
		// TODO Auto-generated method stub
		
	}
	
	
	
}