package gamechanger.writer;

import gamechanger.parsing.Interaction;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;

import java.util.ArrayList;

public class Writer {
	public void writeGameOutput(ArrayList[] elements){
		
		
		ArrayList<Sprite> sprites = elements[0];
		ArrayList<Interaction> interacts = elements[1];
		ArrayList<LevelMapping> mappings = elements[2];
		ArrayList<Termination> terms = elements[3];
		
		
		
		
		String result = "BasicGame\n";
		String previous = "\t\t";
		
		result += "\tSpriteSet\n";
		for (Sprite sp : sprites) {			
			result += previous + sp.getSpriteText() + "\n";
		}
		result += "\tInteractionSet\n";
		for (Interaction in : interacts) {			
			result += previous + in.getInteractionText() + "\n";
		}
		result += "\tLevelMapping\n";
		for (LevelMapping ma : mappings) {			
			result += previous + ma.getMappingText() + "\n";
		}
		result += "\tTerminationSet\n";
		for (Termination te : terms) {			
			result += previous + te.getTerminationText() + "\n";
		}
		
		
		System.out.println(result);
		
		

	}
}
