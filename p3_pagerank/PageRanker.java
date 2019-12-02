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
	private List<Integer> P = new ArrayList<Integer>();
	private List<Integer> S = new ArrayList<Integer>();
	private HashMap<Integer, List<Integer>> M = new HashMap<Integer, List<Integer>>();
	private HashMap<Integer, List<Integer>> L = new HashMap<Integer, List<Integer>>();
	private HashMap<Integer, Double> PR = new HashMap<Integer, Double>();

	private List<Double> perplexity = new ArrayList<Double>();

	public void loadData(String inputLinkFilename){
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(inputLinkFilename));
			String line = null;
			while(reader.ready()) {
				line = reader.readLine();
				String[] splitLine = line.split(" ");

				int firstElem = Integer.parseInt(splitLine[0]);
				P.add(firstElem);

				if (splitLine.length == 1) S.add(firstElem);

				List<Integer> mv =  new ArrayList<Integer>();
				for(int i=1; i<splitLine.length; i++) mv.add(Integer.parseInt(splitLine[i]));
				M.put(firstElem, mv);

				for(int i=0; i<splitLine.length; i++) {
					int k = Integer.parseInt(splitLine[i]);
					if(L.containsKey(k)) {
						List<Integer> v = new ArrayList<Integer>(L.get(k));
						v.add(firstElem);
						L.put(k, v);
					} else {
						List<Integer> v = new ArrayList<Integer>();
						v.add(firstElem);
						L.put(k, v);
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
	public void initialize() {
		double N = P.size();
		P.forEach(p -> {
			double w = 1 / N;
			PR.put(p, w);
		});
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity() {
		return 0;
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
	// P is the set of all pages; |P| = N
	// S is the set of sink nodes, i.e., pages that have no out links
	// M(p) is the set of pages that link to page p
	// L(q) is the number of out-links from page q
	// d is the PageRank damping/teleportation factor; use
		double N = P.size();
		for(int i=0; i<1; i++) {
			var obj = new Object(){
				Double sinkPR = 0.0;
			};

			S.forEach(p -> {
				obj.sinkPR += PR.get(p);
			});

			P.forEach(p -> {
				var obj2 = new Object(){
					Double newPR = 0.0;
				};

				obj2.newPR = (1 - d) / N;
				obj2.newPR += d * obj.sinkPR / N;
				M.keySet().forEach(q -> {
					double cal = d * PR.get(q) / L.get(q).size();
					obj2.newPR += cal;
				});
				PR.put(p, obj2.newPR);
			});
		}

		System.out.println(PR);



//		double N = inCommingLinks.size();
//		for(int t = 0; t < 10; t++) {
//			inCommingLinks.keySet().forEach(k -> {
//				List<Integer> links = new ArrayList<Integer>(inCommingLinks.get(k));
//				double sum = 0.0;
//				for(int i=0; i<links.size(); i++) {
//					int v = links.get(i);		// -> 4, 5, 6
//					double pr = weight.get(v);	// -> weight of 4
//					int l = outCommingLinks.get(v).size();
//					sum += pr / l;
//				}
//				double values = (1-d)/N + d*sum;
//				weight.put(k, values);
//			});
//			System.out.println(weight);
//		}
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
