//Name(s): Rattanavaree Muangamai - Pantita Wang - Prach Yothaprasert
//ID: 6088092 - 6088219 - 6088234
//Section: 3
import java.util.*;
import java.io.*;
import java.math.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Collectors.*;

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
	private double curPer = 0.0;
	private double prevPer = 0.0;
	private int countPer = 0;
	private HashSet<Integer> hs = new HashSet<Integer>();

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

//				Find P
//				P.add(firstElem);
				IntStream
						.range(0, splitLine.length)
						.forEach(i -> {
							int v = Integer.parseInt(splitLine[i]);
							P.add(v);
						});


				if (splitLine.length == 1) S.add(firstElem);

				List<Integer> mv =  new ArrayList<Integer>();
				for(int i=1; i<splitLine.length; i++) mv.add(Integer.parseInt(splitLine[i]));
				M.put(firstElem, mv);

				for(int i=1; i<splitLine.length; i++) {
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

			hs.addAll(P);
			P.clear();
			P.addAll(hs);

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
		double cal = 0.0;
		for(Integer p : PR.keySet()) {
			cal += PR.get(p) * (Math.log(PR.get(p)) / Math.log(2));
		}
		cal = -1 * cal;
		double per = Math.pow(2, cal);
		return per;
	}

	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores).
	 */
	public boolean isConverge(){
		int cPer = (int) curPer % 10;
		int pPer = (int) prevPer % 10;

		if (cPer == pPer) {
			countPer += 1;
		} else {
			countPer = 0;
			return false;
		}

		if (countPer == 3) return true;
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
		List<Double> listPer = new ArrayList<Double>();
		while(!isConverge()) {
			double N = P.size();
			HashMap<Integer, Double> newPR = new HashMap<Integer, Double>();
			double e = (1-d)/N;

			double sinkPR = 0.0;
			for(int i=0; i<S.size(); i++) {
				int p = S.get(i);
				sinkPR += PR.get(p);
			}

			for(int i=0; i<P.size(); i++) {
				int p = P.get(i);
				double cal = e + d * (sinkPR / N);
				if(M.containsKey(p)) {
					List<Integer> lm = new ArrayList<Integer>(M.get(p));
					for(int j=0; j<lm.size(); j++) {
						int q = lm.get(j);
						if(PR.containsKey(q)) {
							cal += d * PR.get(q) / L.get(q).size();
						}
					}
				}

				newPR.put(p, cal);
			}

			PR.keySet().forEach(p -> {
				PR.put(p, newPR.get(p));
			});

			double per = getPerplexity();
			prevPer = curPer;
			curPer = per;

			listPer.add(per);
		}

		try {
			FileWriter fileWriter = new FileWriter(prOutFilename);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			PR.keySet().forEach(k -> {
				printWriter.print(k + " " + PR.get(k) + "\n");
			});
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter fileWriter = new FileWriter(perplexityOutFilename);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for(int i=0; i<listPer.size(); i++) {
				printWriter.print(listPer.get(i) + "\n");
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the top K page IDs, whose scores are highest.
	 */
	public Integer[] getRankedPages(int K){
		LinkedList<Map.Entry<Integer, Double>> list = new LinkedList<>(PR.entrySet());
		Comparator<Map.Entry<Integer, Double>> comparator = Comparator.comparing(Map.Entry::getValue);
		Collections.sort(list, comparator.reversed());

		Integer[] rank = new Integer[K];

		if(K <= list.size()) {
			for(int i=0; i<K; i++) {
				rank[i] = list.get(i).getKey();
			}
		} else {
			for(int i=0; i<list.size(); i++) {
				rank[i] = list.get(i).getKey();
			}
		}
		return rank;
	}

	public static void main(String args[])
	{
		long startTime = System.currentTimeMillis();
		PageRanker pageRanker =  new PageRanker();
		pageRanker.loadData("citeseer.dat");
//		pageRanker.loadData("../p3_testcase/test.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(100);
		double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;

		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
}
