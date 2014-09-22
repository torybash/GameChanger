package gamechanger.parsing;

import java.util.HashMap;

public class Termination extends Element {
	public String term;
	public HashMap<String, String> parameters = new HashMap<String,String>();
	
	
	public String getTerminationText(){
		String result = term + " ";
		for (String key: parameters.keySet()) {
			result += key + "=" + parameters.get(key) + " ";
		}
		return result;
	}
}
