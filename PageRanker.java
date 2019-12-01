//Name(s): Rattanavaree Muangamai - Pantita Wang - Prach Yothaprasert
//ID: 6088092 - 6088219 - 6088234
//Section: 3

import java.util.Arrays;
import java.util.*;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File.*;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
	
	private static final double damping = 0.85;
	private HashMap<Integer, HashSet<Integer>> readDocuments = new HashMap<>();
	private HashMap<Integer, pageRank> weight = new HashMap<>();
	//private HashMap<Integer, PRank> Pagemap = new HashMap<>();
	//private HashMap<Integer, HashSet<Integer>> DatastoreGraph = new HashMap<>();
	private double perplexity;
	private int count = 1;
	private final int limit = 4;
	
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
					if(weight.containsKey(k)) {
						HashSet<Integer> values = new HashSet<>(readDocuments.get(k));
						values.add(v);
						readDocuments.put(k, values);
						weight.get(k).sumnodeout();
					} else {
						HashSet<Integer> values = new HashSet<>();
						values.add(v);
						readDocuments.put(k, values);
					}
				}
			}
			//System.out.println(readDocuments);
			
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
		
		// P is the set of all pages; |P| = N
		// S is the set of sink nodes, i.e., pages that have no out links
		// M(p) is the set of pages that link to page p
		// L(q) is the number of out-links from page q
		// d is the PageRank damping/teleportation factor; use d = 0.85 for this project
		
//		weight.entrySet().forEach(k -> {
//			double size = readDocuments.get(k).size();
//			double w = 1 / (double) weight.size();
//			weight.put(k, w);
			
			weight.entrySet().forEach((k) -> {
                k.getValue().setpageRank(1 / (double) weight.size());      
            });
//		});
//		System.out.println(weight);
		
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity(){
		
		double H = 0;
		
		H = weight.entrySet().stream().map((k) -> k.getValue()).map((PR) -> PR.getprank() * Math.log(PR.getprank()) / Math.log(2)).reduce(H, (accumulator, elem) -> accumulator + elem);
//		readDocuments.keySet().forEach(k -> {
//			int n = readDocuments.get(k).size();
//			pageRank w = weight.get(k);
//			int math = Math.log(2) * w;
//			double pow = -1.0 * ((w * (Math.log(2) * w)) * n);
////			double result = Math.pow(2, pow);
//			System.out.println(pow);
//		});

		return Math.pow(2, H * -1);
	
	}
	
	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores). 
	 */
	
	public boolean isConverge(){
		
		double curperplexity = getPerplexity();
		double preperplexity = Math.floor(perplexity) % 10;
        if(count == limit) {
            count = 1;
            return true;
        }
		this.perplexity = curperplexity;
        if(Math.floor(curperplexity) % 10 == preperplexity) {
            count++;
        }
        else {
            count = 1;
        }
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
	 * @throws IOException 
	 * 
	 */
	public void runPageRank(String perplexityOutFilename, String prOutFilename){
		
		int pcount = weight.size();
        List<String> perplexity = new ArrayList<>();
        List<String> pscore = new ArrayList<>();
        
        while(!isConverge())
        {
        	HashMap<Integer,Double> ranking = new HashMap<>();
        	double prank = 0;
        	prank = weight.keySet().stream().filter((PID) -> (weight.get(PID).sinknodes())).map((PID) -> weight.get(PID).getprank()).reduce(prank, (accumulator, _item) -> accumulator + _item);
        	
        	for(int pid : weight.keySet()) {
        		double newrank = (1 - damping) / (double) pcount;
        		newrank += damping * prank / (double) pcount;
        		
        		if(readDocuments.get(pid) != null && !readDocuments.get(pid).isEmpty()) {
        			newrank = readDocuments.get(pid).stream().map((page) -> weight.get(page)).map((newpage) -> damping  * newpage.getprank() / (double) newpage.countoutnodes()).reduce(newrank, (accumulator, elem) -> accumulator + elem);
        		}
        		ranking.put(pid, newrank);
          }
          weight.entrySet().forEach((P) -> {
              P.getValue().setpageRank(ranking.get(P.getKey()));
            });
          perplexity.add(String.valueOf(getPerplexity()));
        }
        weight.entrySet().forEach((P) -> {
        	pscore.add(P.getKey()+ " " + P.getValue().getprank());
        });
        try {
			WriteFileOut(perplexityOutFilename, perplexity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			WriteFileOut(prOutFilename, pscore);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void WriteFileOut(String file, List<String> line) throws IOException
    {
        Path pathout = Paths.get(file);
        File newfile = new File(file);
        
        if(newfile.exists())
        {
            newfile.delete();
        }
        Files.write(pathout, line, StandardCharsets.UTF_8);
    }
	
	
	/**
	 * Return the top K page IDs, whose scores are highest.
	 */
	public Integer[] getRankedPages(int k){
		
		ArrayList<pageRank> page = new ArrayList<>(weight.values());
        page.sort(pageRank::compareTo);
        int arrsize = Math.min(k, page.size());
        Integer[] topkpage = new Integer[arrsize];
        List<pageRank> sortout = page.subList(0, arrsize);
        for(int i = 0; i < arrsize;i++)
        {
            topkpage[i] = sortout.get(i).getpid();
        }
        return topkpage;
	
	}
	
	public static void main(String args[])
	{
	long startTime = System.currentTimeMillis();
		PageRanker pageRanker =  new PageRanker();
		//pageRanker.loadData("../p3_testcase/test.dat");
		pageRanker.loadData("/Users/doubleang/Documents/Year3_2019/Semester_1/IR/P3_6088092_6088219_6088234/p3_pagerank/test.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(100);
	double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		
		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
	
	// compare the values

	private static class pageRank implements Comparable {
		
		private int pid = 0;
		private int count = 0;
		private double pageRank;
		
		void setpageRank(double pageRank) {
			this.pageRank = pageRank;
		}
		
		boolean sinknodes() {
			return count == 0;
		}
		
		int getpid() {
			return pid;
		}
		
		double getprank() {
			return pageRank;
		}
		
		int countoutnodes() {
			return count;
		}
		
		pageRank(int pid) {
			this(pid, 0.0);
		}

		public pageRank(int pid, double d) {
			// TODO Auto-generated constructor stub
			this.pid = pid;
			this.pageRank = d;
		}
		
		void sumnodeout() {
			count++;
		}
		
		@Override
		public int compareTo(Object p) {
			if(p instanceof pageRank) {
				return 1;
			}
			int compare = Double.compare(((pageRank) p).pageRank, this.pageRank);
			if(compare != 0) {
				return 0;
			}
			else if(compare == 0) {
				return this.pid - ((pageRank) p).pid;
			}
			return compare;
		}
		
	}
}