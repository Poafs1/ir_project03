//Name(s): Rattanavaree Muangamai - Pantita Wang - Prach Yothaprasert
//ID: 6088092 - 6088219 - 6088234
//Section: 3
import java.util.*;
import java.io.*;
import java.math.*;

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
	private HashMap<Integer, List<Integer>> readDocuments = new HashMap<Integer, List<Integer>>();
	private HashMap<Integer, Double> weight = new HashMap<Integer, Double>();

	public void loadData(String inputLinkFilename){
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(inputLinkFilename));
			String line = null;
			while(reader.ready()) {
				line = reader.readLine();
				String[] splitLine = line.split(" ");

				int v = Integer.parseInt(splitLine[0]);

				for(int i=1; i<splitLine.length; i++) {
					int k = Integer.parseInt(splitLine[i]);
					if(readDocuments.containsKey(k)) {
						List<Integer> values = new ArrayList<Integer>(readDocuments.get(k));
						values.add(v);
						readDocuments.put(k, values);
					} else {
						List<Integer> values = new ArrayList<Integer>();
						values.add(v);
						readDocuments.put(k, values);
					}
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will be called after the graph is loaded into the memory.
	 * This method initialize the parameters for the PageRank algorithm including
	 * setting an initial weight to each page.
	 */
	public void initialize(){
		readDocuments.keySet().forEach(k -> {
			double n = readDocuments.get(k).size();
			double w = 1 / Math.abs(n);
			weight.put(k, w);
		});

		getPerplexity();
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity(){
//		readDocuments	-> {1=[2, 3, 6], 2=[3, 4, 5, 6], 3=[4, 5], 4=[1, 3, 5, 6], 5=[1], 6=[1, 2, 5]}
//		weight			-> {1=0.3333333333333333, 2=0.25, 3=0.5, 4=0.25, 5=1.0, 6=0.3333333333333333}

		double pow = 0;
		for(int i=0; i<readDocuments.keySet().length; i++) {
			int n = readDocuments.get(k).size();
		}

//		double pow = 0;
//		readDocuments.keySet().forEach(k -> {
//			int n = readDocuments.get(k).size();
//			double w = weight.get(k);
//			double p = w * (Math.log(2) * w);
//			pow += p;
////			double pow = -1 * ((w * (Math.log(2) * w)) * n);
////			double result = Math.pow(2, pow);
//		});
//
//		double result = Math.pow(2, -1*pow);
//		System.out.println(result);
		return 0;
	}
	
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
//		pageRanker.loadData("citeseer.dat");
		pageRanker.loadData("../p3_testcase/test.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(100);
	double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		
		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
}
