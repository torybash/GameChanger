package gamechanger.parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import ontology.Types;
import tools.IO;
import core.Node;
import core.content.InteractionContent;
import core.content.MappingContent;
import core.content.SpriteContent;
import core.content.TerminationContent;

public class Parser {
	static ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	static ArrayList<Interaction> interacts = new ArrayList<Interaction>();
	static ArrayList<LevelMapping> mappings = new ArrayList<LevelMapping>();
	static ArrayList<Termination> terms= new ArrayList<Termination>();
	
	public Parser(){}
	
	public static int currentSet;
	
	public static ArrayList[] readGameOutput(String gamedesc_file) {
		sprites.clear();interacts.clear();mappings.clear();terms.clear();
		
		String[] desc_lines = new IO().readFile(gamedesc_file);
		
        if(desc_lines != null)
        {        	
            Node rootNode = indentTreeParser(desc_lines);
            
            for(Node n : rootNode.children)
            {
                if(n.content.identifier.equals("SpriteSet"))
                {
                    parseSpriteSet(n.children, 0, null);
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
	
	
	public static ArrayList[] readGameOutputString(String gamedesc) {
		sprites.clear();interacts.clear();mappings.clear();terms.clear();
		
		String[] desc_lines = gamedesc.split("\\n");
		
        if(desc_lines != null)
        {        	
            Node rootNode = indentTreeParser(desc_lines);
            
            for(Node n : rootNode.children)
            {
                if(n.content.identifier.equals("SpriteSet"))
                {
                    parseSpriteSet(n.children, 0, null);
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


	
	private static void parseSpriteSet(ArrayList<Node> nodes, int depth, Sprite parent){
		
		for(Node n : nodes){
    		SpriteContent sc = (SpriteContent) n.content;
    		
    		Sprite sp = new Sprite();
			sp.identifier = sc.identifier;
			sp.referenceClass = sc.referenceClass == null ? "" : sc.referenceClass;
			sp.parameters = sc.parameters;
			sp.depth = depth;
			sp.parent = parent;

			sprites.add(sp);
    		
    		
    		
    		if (n.children.size() > 0){
    			int newDepth = depth + 1;
    			parseSpriteSet(n.children, newDepth, sp);
    		}
		}
		
	}
	
	private static void parseInteractionSet(ArrayList<Node> nodes){
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
	
	private static void parseLevelMapping(ArrayList<Node> nodes){
		for(Node n : nodes){
			MappingContent mc = (MappingContent) n.content;
			
			
			LevelMapping lm = new LevelMapping();
			lm.charID = mc.charId;
			lm.references = mc.reference;
			
			mappings.add(lm);
		}
	}
	
	private static void parseTerminationSet(ArrayList<Node> nodes){
		for(Node n : nodes){
			TerminationContent tc = (TerminationContent) n.content;
			
			Termination te = new Termination();
			
			te.term = tc.identifier;
			te.parameters = tc.parameters;
			
			terms.add(te);
		}	
	}
	

	
	
	
	
	
	
	
	
    private static Node indentTreeParser(String[] lines)
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
    
    
    private static void updateSet(String line)
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


	public static String getGameDescFromPath(String gamePath) {
       String gameDesc = "";
        try{
            BufferedReader in = new BufferedReader(new FileReader(gamePath));
            String line = null;
            while ((line = in.readLine()) != null) {
            	gameDesc += line + "\n";
            }
            in.close();
        }catch(Exception e)
        {
            System.out.println("Error reading the file " + gamePath + ": " + e.toString());
            e.printStackTrace();
            return null;
        }
        return gameDesc;
    	}
	
}
