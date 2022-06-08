package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();
        
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
                
                int numberOfThreads = commentFiles.length; // number of threads to execute the process
                Thread[] threads = new Thread[numberOfThreads];
                
                final int filesPerThread = commentFiles.length/numberOfThreads;
                final int remainingFiles = commentFiles.length%numberOfThreads;
                
                //Assiging files dynamically to each thread
                for (int i = 0; i < numberOfThreads; i++) {
                    final int thread = i;
                    threads[i] = new Thread()
                    {
                        @Override public void run(){
                            runThread(commentFiles , numberOfThreads , thread , filesPerThread , remainingFiles , totalResults);
                        }
                    };
                }
                
                for (Thread t1 : threads)
                    t1.start(); // start processing all threads.
                for(Thread t2 : threads)
                    try {
                        t2.join(); // To wait untill all thread complete its execution
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
		
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
            //check if key exsits and Add it value up
            
            if(target.containsKey(entry.getKey()))
            {
               //get value and update it 
                int latestValue =  entry.getValue();
                Integer currentValue = target.get(entry.getKey());
                currentValue += latestValue;
                target.put(entry.getKey(), currentValue);
            }else{
                  target.put(entry.getKey(), entry.getValue());
            }
	
		}
		
	}
	
	/**
	 * Assigning files equally to each thread and assign remaining files to the last thread 
	 */
        private static void runThread(File[] filesList , int numberOfThreads , int thread , int filesPerThread , int remainingFiles , Map<String, Integer> totalResults)
        {
            List<File> inFiles = new ArrayList<>();
            
            for (int i=thread*filesPerThread;  i < (thread+1*filesPerThread); i++)
               inFiles.add(filesList[i]);
            if(thread == numberOfThreads -1 && remainingFiles > 0)
                for (int j=filesList.length-remainingFiles; j<filesList.length; j++)
                    inFiles.add(filesList[j]);
            
            //Process files
            for (File commentFile : inFiles) {

              CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
		      Map<String, Integer> fileResults = commentAnalyzer.analyze();
		      addReportResults(fileResults, totalResults);
              
            }
 
        }

}
