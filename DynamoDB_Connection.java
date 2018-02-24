package default_package;

import java.util.Arrays;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


//might want to comment out this one's uses to save data after table is initiated
public class DynamoDB_Connection {

	public DynamoDB_Connection(String t_name){
		
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
    			.withRegion(Regions.US_WEST_2)
    			.build();
    		
        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = t_name;
        
        //tables have a primary key (hash) and a nomenclature. Maybe can add extra cols when adding items?

        try {
            System.out.println("Attempting to create table; please wait...");
            
            
            Table table = dynamoDB.createTable(tableName,
                Arrays.asList(new KeySchemaElement("ID", KeyType.HASH), // Partition
                    new KeySchemaElement("taskName", KeyType.RANGE)), // Sort key
                Arrays.asList(new AttributeDefinition("ID", ScalarAttributeType.N),
                    new AttributeDefinition("taskName", ScalarAttributeType.S)),
                new ProvisionedThroughput(10L, 10L));
                
            /*
            //table with just a primary key
            Table table = dynamoDB.createTable(tableName,
                    Arrays.asList(new KeySchemaElement("ID", KeyType.HASH)), // Partition
                       
                    Arrays.asList(new AttributeDefinition("ID", ScalarAttributeType.S)
                        ),
                    new ProvisionedThroughput(10L, 10L));
                    */
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

        }
        catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }

    
	}
}
