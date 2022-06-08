package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommentAnalyzer {
	
	private File file;
	
	public CommentAnalyzer(File file) {
		this.file = file;
	}
	
	public Map<String, Integer> analyze() {
		
		Map<String, Integer> resultsMap = new HashMap<>();
                
                //MATRICS NAMES TO BE ADDED BELOW Matric name and identifier
                HashMap<String, String> MatricNamesAndIdentyfier = new HashMap<String,String>();
		
                MatricNamesAndIdentyfier.put("MOVER_MENTIONS", "Mover");
                MatricNamesAndIdentyfier.put("SHAKER_MENTIONS", "Shaker");
                MatricNamesAndIdentyfier.put("QUESTIONS", "?");
                MatricNamesAndIdentyfier.put("SPAM ", "http");

		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    
                   
			
			String line = null;
			while ((line = reader.readLine()) != null) {
                           
				if (line.length() < 15) {
					
					incOccurrence(resultsMap, "SHORTER_THAN_15");

				}
                                
                                for (Map.Entry<String, String> entry : MatricNamesAndIdentyfier.entrySet())
                                {
                                        //identityfier name
                                        String identyfier_name = entry.getKey();
                                        //identiryfier_characters
                                        String identyfier_Characters = entry.getValue();
                                        
                                        //convert line to smaller letters
                                        line = line.toLowerCase();
                                        
                                        //check if character appers twice
                                        int count = 0;
                                        if(identyfier_Characters.length() == 1)
                                        {
                                              count = line.length() - line.replace(identyfier_Characters, "").length();
                                        }
                                       
  
                                        identyfier_Characters = identyfier_Characters.toLowerCase();
                                        
                                        if(line.contains(identyfier_Characters))
                                        {
                                            if(count > 1 )
                                            {
                                                for (int i = 0; i < count; i++) {
                                                     incOccurrence(resultsMap, identyfier_name);
                                                }
                                            }else{
                                             incOccurrence(resultsMap, identyfier_name);
                                            }
                                           
                                        }
                                }
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}
        
       

}
