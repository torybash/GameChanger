package gamechanger.parsing;

import java.util.HashMap;
import java.util.Iterator;

public class Interaction extends Element{
	String sprite1;
	String sprite2;
	String function;
	HashMap<String, String> parameters = new HashMap<String, String>();
	
	
	public String getInteractionText(){
		String result = sprite1 + " " + sprite2 + " > " +
				function;
		Iterator<String> it = parameters.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			result += " " + key + "=" + parameters.get(key);
		}
		return result;
	}
}
