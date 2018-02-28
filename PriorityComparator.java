package default_package;

import java.util.Comparator;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

//compares priorities of DynamoDB items. -1 means higher priority, 0 equal, 1 lower
public class PriorityComparator implements Comparator<Map<String, AttributeValue> > {

	@Override
	public int compare(Map<String, AttributeValue> arg0, Map<String, AttributeValue> arg1) {
		if(arg0.get("priority").toString().equals("{S: very high,}")) {
			
			if(arg1.get("priority").toString().equals("{S: very high,}")){
				return 0;
			}
			
			return -1;
		} else if (arg0.get("priority").toString().equals("{S: high,}")){
			if(arg1.get("priority").toString().equals("{S: very high,}")){
				return 1;
			} else if (arg1.get("priority").toString().equals("{S: high,}")){
				return 0;
			}
			
			return -1;
			
		} else if (arg0.get("priority").toString().equals("{S: medium,}")){
			if(arg1.get("priority").toString().equals("{S: very high,}") || arg1.get("priority").toString().equals("{S: high,}")){
				return 1;
			} else if (arg1.get("priority").toString().equals("{S: medium,}")){
				return 0;
			}
			
			return -1;
		}	
		// TODO Auto-generated method stub
		
		return 1;
	}

}
