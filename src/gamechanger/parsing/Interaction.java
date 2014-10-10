package gamechanger.parsing;

import java.util.HashMap;
import java.util.Iterator;

public class Interaction extends Element{
	public String sprite1;
	public String sprite2;
	public String function;
	public HashMap<String, String> parameters = new HashMap<String, String>();
	
	
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
	
	
	@Override
	public String toString() {
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
