package gamechanger.parsing;

import java.util.ArrayList;
import java.util.Iterator;

public class LevelMapping extends Element{

	public Character charID;
	public ArrayList<String> references = new ArrayList<String>();
	
	
	public String getMappingText(){
		String result = charID + " > ";
		for (String s : references) {
			result += s + " ";
		}
		return result;
	}
}
