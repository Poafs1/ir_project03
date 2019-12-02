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
	private static final double d = 0.85;
	private HashMap<Integer, List<Integer>> inCommingLinks = new HashMap<Integer, List<Integer>>();
	private HashMap<Integer, List<Integer>> outCommingLinks = new HashMap<Integer, List<Integer>>();
	private HashMap<Integer, Double> weight = new HashMap<Integer, Double>();

	private List<Double> perplexity = new ArrayList<Double>();

	public void loadData(String inputLinkFilename){
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(inputLinkFilename));
			String line = null;
			while(reader.ready()) {
				line = reader.readLine();
				String[] splitLine = line.split(" ");

				int kv = Integer.parseInt(splitLine[0]);

//				In Comming Links
				List<Integer> inCommingValues = new ArrayList<Integer>();
				for(int i=1; i<splitLine.length; i++) inCommingValues.add(Integer.parseInt(splitLine[i]));
				inCommingLinks.put(kv, inCommingValues);

//				Out Comming Links
				for(int i=1; i<splitLine.length; i++) {
					int k = Integer.parseInt(splitLine[i]);
					if(outCommingLinks.containsKey(k)) {
						List<Integer> values = new ArrayList<Integer>(outCommingLinks.get(k));
						values.add(kv);
						outCommingLinks.put(k, values);
					} else {
						List<Integer> values = new ArrayList<Integer>();
						values.add(kv);
						outCommingLinks.put(k, values);
					}
				}
			}

//			System.out.println(inCommingLinks);		-> {1=[4, 5, 6], 2=[1, 6], 3=[1, 2, 4], 4=[2, 3], 5=[2, 3, 4, 6], 6=[1, 2, 4]}
//			System.out.println(outCommingLinks);	-> {1=[2, 3, 6], 2=[3, 4, 5, 6], 3=[4, 5], 4=[1, 3, 5, 6], 5=[1], 6=[1, 2, 5]}

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
	public void initialize() {
		double N = inCommingLinks.size();
		inCommingLinks.keySet().forEach(k -> {
			double w = 1 / N;
			weight.put(k, w);
		});

//		System.out.println(weight);		-> {1=0.16666666666666666, 2=0.16666666666666666
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity() {
		double en = 0.0;
		for(int k : weight.keySet()) {
			double v = weight.get(k);
			en += v * (Math.log(2) / v);
		}

		return en;
	}
	
	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores). 
	 */
	public boolean isConverge(){
		return false;
	}
	
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
	public void runPageRank(String perplexityOutFilename, String prOutFilename){
//		d -> 0.85

		double N = inCommingLinks.size();
//		test loop for 10 times
		for(int t = 0; t < 10; t++) {
			inCommingLinks.keySet().forEach(k -> {
//			1=[4, 5, 6]
				List<Integer> links = new ArrayList<Integer>(inCommingLinks.get(k));
				double sum = 0.0;
				for(int i=0; i<links.size(); i++) {
					int v = links.get(i);		// -> 4, 5, 6
					double pr = weight.get(v);	// -> weight of 4
					int l = outCommingLinks.get(v).size();
					sum += pr / l;
				}
				double values = ((1-d)/N) + (d*sum);
				weight.put(k, values);
			});
			System.out.println(weight);
		}


////		update perplexity
//		double per = getPerplexity();
//		perplexity.add(per);
	}
	
	
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
