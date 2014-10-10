package gamechanger.parsing;

import java.util.HashMap;
import java.util.Iterator;

public class Sprite extends Element{
	public Sprite(){}
	
	public String identifier = "";
	public String referenceClass = "";
	public HashMap<String, String> parameters = new HashMap<String, String>();
	public int depth = -1;
	public Sprite parent = null;
	
	
	public String getSpriteText() {
		String result = "";
		
		for(int i = 0; i < depth; i++)
			result+= "\t";
		
		result += identifier + " > " + referenceClass;
		Iterator<String> it = parameters.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			result += " " + key + "=" + parameters.get(key);
		}
		return result;
	}
	
	
	@Override
	public String toString() {
		String result = "";
		
		for(int i = 0; i < depth; i++)
			result+= "\t";
		
		result += identifier + " > " + referenceClass;
		Iterator<String> it = parameters.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			result += " " + key + "=" + parameters.get(key);
		}
		return result;
	}
}
