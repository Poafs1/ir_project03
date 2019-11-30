//Name(s): Rattanavaree Muangamai - Pantita Wang - Prach Yothaprasert
//ID: 6088092 - 6088219 - 6088234
//Section: 3

import java.util.Arrays;
import java.util.*;
import java.math.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class implements PageRank algorithm on simple graph structure.
 * Put your name(s), ID(s), and section here.
 *
 */
public class PageRanker {
	
	/**
	 * This class reads the direct graph stored in the file "inputLinkFilename" into memory.
	 * Each line in the input file should have the following format:
	 * <pid_1> <pid_2> <pid_3> .. <pid_n>
	 * 
	 * Where pid_1, pid_2, ..., pid_n are the page IDs of the page having links to page pid_1. 
	 * You can assume that a page ID is an integer.
	 */
	
	/* 
	 * pid_n = page id of page n
	 * e.g. 1: 4 5 6
	 * means that pid 4,5,6 are link to pid_1
	 */
	
	public void loadData(String inputLinkFilename){
		
		BufferedReader reader;
		// for keeping pid
		ArrayList<String> result = new ArrayList<>();
		
		try {
			// citeseer.dat path
			//reader = new BufferedReader(new FileReader("/Users/doubleang/Documents/Year3_2019/Semester_1/IR/P3_6088092_6088219_6088234/p3_pagerank/citeseer.dat"));
			
			// test.dat path
			reader = new BufferedReader(new FileReader("/Users/doubleang/Documents/Year3_2019/Semester_1/IR/P3_6088092_6088219_6088234/p3_testcase/test.dat"));
			
			String line = null;

			while(reader.ready()) {
				
				line = reader.readLine();
				System.out.print(line +"\n");
				// read next line
				result.add(line);
			}
			
			// print ArrayList
			System.out.print("\n");
			for (String element : result) {
			    System.out.println("[" + element + "]");
			}
			System.out.print("\n");
			
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method will be called after the graph is loaded into the memory.
	 * This method initialize the parameters for the PageRank algorithm including
	 * setting an initial weight to each page.
	 */
	
	public void initialize(){
		
		// P is the set of all pages; |P| = N
		// S is the set of sink nodes, i.e., pages that have no out links
		// M(p) is the set of pages that link to page p
		// L(q) is the number of out-links from page q
		// d is the PageRank damping/teleportation factor; use d = 0.85 for this project
		
		double d = 0.85;
		
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity(){return 0;}
	
	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores). 
	 */
	public boolean isConverge(){return false;}
	
	/**
	 * The main method of PageRank algorithm. 
	 * Can assume that initialize() has been called before this method is invoked.
	 * While the algorithm is being run, this method should keep track of the perplexity
	 * after each iteration. 
	 * 
	 * Once the algorithm terminates, the method generates two output files.
	 * [1]	"perplexityOutFilename" lists the perplexity after each iteration on each line. 
	 * 		The output should look something like:
	 *  	
	 *  	183811
	 *  	79669.9
	 *  	86267.7
	 *  	72260.4
	 *  	75132.4
	 *  
	 *  Where, for example,the 183811 is the perplexity after the first iteration.
	 *
	 * [2] "prOutFilename" prints out the score for each page after the algorithm terminate.
	 * 		The output should look something like:
	 * 		
	 * 		1	0.1235
	 * 		2	0.3542
	 * 		3 	0.236
	 * 		
	 * Where, for example, 0.1235 is the PageRank score of page 1.
	 * 
	 */
	public void runPageRank(String perplexityOutFilename, String prOutFilename){}
	
	
	/**
	 * Return the top K page IDs, whose scores are highest.
	 */
	public Integer[] getRankedPages(int K){return null;}
	
	public static void main(String args[])
	{
	long startTime = System.currentTimeMillis();
		PageRanker pageRanker =  new PageRanker();
		pageRanker.loadData("test.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(100);
	double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		
		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
}
