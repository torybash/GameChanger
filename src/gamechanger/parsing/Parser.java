package gamechanger.parsing;

import java.util.ArrayList;

import ontology.Types;
import tools.IO;
import core.Node;
import core.content.InteractionContent;
import core.content.MappingContent;
import core.content.SpriteContent;
import core.content.TerminationContent;

public class Parser {
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	ArrayList<Interaction> interacts = new ArrayList<Interaction>();
	ArrayList<LevelMapping> mappings = new ArrayList<LevelMapping>();
	ArrayList<Termination> terms= new ArrayList<Termination>();
	
	public Parser(){}
	
	public int currentSet;
	
	public ArrayList[] readGameOutput(String gamedesc_file) {
	
		
		String[] desc_lines = new IO().readFile(gamedesc_file);
		
        if(desc_lines != null)
        {        	
            Node rootNode = indentTreeParser(desc_lines);
            
            for(Node n : rootNode.children)
            {
                if(n.content.identifier.equals("SpriteSet"))
                {
                    parseSpriteSet(n.children, 0);
                }
                else if(n.content.identifier.equals("InteractionSet"))
                {
                    parseInteractionSet(n.children);
                }
                else if(n.content.identifier.equals("LevelMapping"))
                {
                    parseLevelMapping(n.children);
                }else if(n.content.identifier.equals("TerminationSet"))
                {
                    parseTerminationSet(n.children);
                }
            }
            
        }
        
        return new ArrayList[]{sprites, interacts, mappings, terms};
        
	}

	
	private void parseSpriteSet(ArrayList<Node> nodes, int depth){
		
		for(Node n : nodes){
    		SpriteContent sc = (SpriteContent) n.content;
    		
    		Sprite sp = new Sprite();
			sp.identifier = sc.identifier;
			sp.referenceClass = sc.referenceClass;
			sp.parameters = sc.parameters;
			sp.depth = depth;

			sprites.add(sp);
    		
    		
    		
    		if (n.children.size() > 0){
    			int newDepth = depth + 1;
    			parseSpriteSet(n.children, newDepth);
    		}
		}
		
	}
	
	private void parseInteractionSet(ArrayList<Node> nodes){
		for(Node n : nodes){
			InteractionContent ic = (InteractionContent) n.content;
			
			Interaction in = new Interaction();
			in.sprite1 = ic.object1;
			in.sprite2 = ic.object2;
			in.function = ic.function;
			in.parameters = ic.parameters;	
			
			interacts.add(in);
		}
		
	}
	
	private void parseLevelMapping(ArrayList<Node> nodes){
		for(Node n : nodes){
			MappingContent mc = (MappingContent) n.content;
			
			LevelMapping lm = new LevelMapping();
			lm.charID = mc.charId;
			lm.references = mc.reference;
			
			mappings.add(lm);
		}
	}
	
	private void parseTerminationSet(ArrayList<Node> nodes){
		for(Node n : nodes){
			TerminationContent tc = (TerminationContent) n.content;
			
			Termination te = new Termination();
			
			te.term = tc.identifier;
			te.parameters = tc.parameters;
			
			terms.add(te);
		}	
	}
	

	
	
	
	
	
	
	
	
    private Node indentTreeParser(String[] lines)
    {
        //By default, let's make tab as four spaces
        String tabTemplate = "    ";
        Node last = null;

        for(String line : lines)
        {
            line.replaceAll("\t",tabTemplate);
            line.replace('(',' ');
            line.replace(')',' ');
            line.replace(',',' ');

            // remove comments starting with "#"
            if(line.contains("#"))
                line = line.split("#")[0];

            // handle whitespace and indentation
            String content = line.trim();

            if(content.length() > 0)
            {            	
                updateSet(content); //Identify the set we are in.
                char firstChar = content.charAt(0);
                //figure out the indent of the line.
                int indent = line.indexOf(firstChar);
                last = new Node(content, indent, last, currentSet);
            }
        }

        return last.getRoot();
    }
    
    
    private void updateSet(String line)
    {
        if(line.equalsIgnoreCase("SpriteSet"))
            currentSet = Types.VGDL_SPRITE_SET;
        if(line.equalsIgnoreCase("InteractionSet"))
            currentSet = Types.VGDL_INTERACTION_SET;
        if(line.equalsIgnoreCase("LevelMapping"))
            currentSet = Types.VGDL_LEVEL_MAPPING;
        if(line.equalsIgnoreCase("TerminationSet"))
            currentSet = Types.VGDL_TERMINATION_SET;
    }
	
}
