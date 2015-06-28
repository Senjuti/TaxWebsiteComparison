import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BenchmarkTime {

	public ArrayList<String> page_list;
	public ArrayList<Double> average_time;
	public ArrayList<Double> minimum_time;
	public ArrayList<Double> maximum_time;
	public Set<String> outgoing_links;
	public ArrayList<String> page_content;
	public ArrayList<Integer> page_similar; 
	public double[][] profiles;
	public int count = 10; 
	
	/** 
	 * public constructor to initialize the page_list, average_time, min,max time
	 */ 
	
	public BenchmarkTime() { 

		page_list = new ArrayList<String>();
		average_time = new ArrayList<Double>();
		minimum_time = new ArrayList<Double>();
		maximum_time = new ArrayList<Double>();
		outgoing_links = new HashSet<String>(); 
		page_content = new ArrayList<String>();  
		page_similar = new ArrayList<Integer>(); 
		
		initPageList();
		profiles = new double[page_list.size()][count];
	}
	
	/** 
	 * public constructor to set the number of times a page has to be hit
	 * @param count Integer specifying the number times a page is hit
	 */ 
	
	public BenchmarkTime(int count) { 

		page_list = new ArrayList<String>();
		average_time = new ArrayList<Double>();
		minimum_time = new ArrayList<Double>();
		maximum_time = new ArrayList<Double>();
		this.count = count; 
		outgoing_links = new HashSet<String>(); 
		
		initPageList();
		profiles = new double[page_list.size()][count];
	}
	
	/** 
	 * Populates the page_list with names of the competitors
	 */ 
	
	public void initPageList() { 

		page_list.add("http://www.cleartax.in/");
		page_list.add("http://www.makemyreturns.com");
		page_list.add("http://www.taxchanakya.com");
		page_list.add("http://www.easyitfiling.com");
		page_list.add("https://myitreturn.com");
		page_list.add("https://incometaxindiaefiling.gov.in");
		page_list.add("https://www.hrblock.in");
		page_list.add("http://www.taxspanner.com");
	}
	
	/** 
	 * Returns the loading time of page in milliseconds 
	 * @param link String which contains the link of the to be opened
	 * @return Loading Time of Page in milliseconds
	 * @throws IOException
	 */ 
	
	@SuppressWarnings("unused")
	public double getLoadTime(String link) throws IOException { 

		double time = 0.0;
		long start_time;
		long end_time;
		URL url;
		BufferedReader br = null;
		String s = "";
		url = new URL(link);  

		try { 

			br = new BufferedReader(new InputStreamReader(url.openStream())); 

		} catch(Exception e) { 

			e.printStackTrace();
		}

		start_time = System.nanoTime();

		while ( (s = br.readLine()) != null) { 

		}

		end_time = System.nanoTime();
		br.close();
		time = (double)(end_time-start_time)*1.0; 

		return (time/1000000.0);
	}
	
	/** 
	 * Populates the profiles array with the loading times
	 * @throws IOException
	 */
	
	public void populateProfiles() throws IOException { 

		for (int i = 0; i < page_list.size(); i++) { 

			for (int j = 0; j < count; j++) { 

				profiles[i][j] = getLoadTime(page_list.get(i));
			}
		}
	}
	
	/** 
	 * Prints the profiles to standard output in a pretty fashion 
	 */ 
	
	public void printProfiles() {

		for (int i = 0; i < profiles.length; i++) { 

			System.out.print(page_list.get(i) + " "); 

			for (int j = 0; j < profiles[i].length; j++) { 

				System.out.print(profiles[i][j] + "\t");
			}

			System.out.println();
		}
	} 
	
	/** 
	 * Calculates the average download time for each page and stores it 
	 */ 
	
	public void averageTime() { 

		double average = 0.0;

		for (int i = 0; i < profiles.length; i++) { 

			for (int j = 0; j < profiles[i].length; j++) { 

				average += profiles[i][j];
			}

			average = average/profiles[i].length;
			average_time.add(average);
		}
	}
	
	/** 
	 * Calculates the minimum download time for each page and stores it 
	 */ 
	
	public void minimumTime() {  

		ArrayList<Double> temp = new ArrayList<Double>(); 

		for (int i = 0; i < profiles.length; i++) { 

			for (int j = 0; j < profiles[i].length; j++) {

				temp.add(profiles[i][j]);
			}

			minimum_time.add(Collections.min(temp));
			temp.clear();
		}
	} 
	
	/** 
	 * Calculates the maximum download time for each page 
	 */ 
	
	public void maximumTime() {  

		ArrayList<Double> temp = new ArrayList<Double>(); 

		for (int i = 0; i < profiles.length; i++) { 

			for (int j = 0; j < profiles[i].length; j++) {

				temp.add(profiles[i][j]);
			}

			maximum_time.add(Collections.max(temp));
			temp.clear();
		}
	}  
	
	/** 
	 * Prints out the results to a file
	 * @param filename file to which the results have to be printed
	 * @throws IOException
	 */ 
	
	public void commitToFile(String filename) throws IOException { 

		FileWriter fw;
		BufferedWriter bw;
		File f; 
		
		fw = new FileWriter(filename,true);
		bw = new BufferedWriter(fw);
		f = new File(filename); 
		
		if (!f.exists()) { 
			
			f.createNewFile();
		} 
		
		for (int i = 0; i < profiles.length; i++) { 

			bw.write(page_list.get(i) + "\t"); 

			for (int j = 0; j < profiles[i].length; j++) { 
				
				bw.write(String.valueOf(profiles[i][j]) + "\t");
			}
			
			bw.write(String.valueOf(average_time.get(i)) + "\t");
			bw.write(String.valueOf(minimum_time.get(i)) + "\t");
			bw.write(String.valueOf(maximum_time.get(i)));
			bw.newLine();
		}

		bw.close();
		fw.close();
	} 
	
	/** 
	 * Counts the number of outgoing links for a given url 
	 * @param link String containing the URL of the site
	 * @return Integer containing the number of outgoing links
	 * @throws IOException
	 */ 
	
	public int countLinks(String link) throws IOException { 
		
		int num_links = 0;
		URL url;
		BufferedReader br;
		String temp = ""; 
		
		url = new URL(link);
		br = new BufferedReader(new InputStreamReader(url.openStream()));
		int start_index = -1;
		int end_index = -1; 
		String out_link = "";
		while ((temp = br.readLine()) != null) { 
			
			if (temp.contains("a href=") && !temp.contains("a href=\"#\"")) { 
				
				start_index = temp.indexOf('"');
				
				if (start_index != -1) { 
					
					end_index = temp.indexOf('"',start_index+1);
				} 
				
				if (start_index != -1 && end_index != -1) { 
					
					out_link = temp.substring(start_index+1, end_index);
					
					if (!out_link.contains("#")) { 
						
						//System.out.println(out_link);
					}
				}
				
				//System.out.println(temp);
				num_links++;
			}
		}
		
		return num_links;
	} 
	
	/** 
	 * Given a URL retrieves the page and returns it as a string
	 * @param link String containing the URL to open
	 * @return String containing the page which is to be opened
	 * @throws IOException
	 */ 
	
	public String getPage(String link) throws IOException { 
		
		URL url = new URL(link);
		BufferedReader br = null; 
		String page = ""; 
		String temp = ""; 
		
		try { 
			 
			br = new BufferedReader(new InputStreamReader(url.openStream())); 
			
		} catch(Exception e) { 
			
			e.printStackTrace();
		} 
		
		while( (temp = br.readLine()) != null) { 
			
			page = page + temp;
		}
		
		return page;
	}  
	
	/** 
	 * Retrieves content of the page which is only between the <p> tags </p> 
	 * @param page String containing the web page source from which the content has to be extracted
	 * @return String containing only the content of the page between the <p> tags </p>
	 */ 
	
	public String getPageContent(String page) { 
		
		String content = "";
		int index_start = 0; 
		int index_end = 0;
		
		index_start = page.indexOf("<p>");
		
		while(index_start != -1) { 
			
			index_end = page.indexOf("</p>", index_start+1);
			
			if (index_end != -1 && index_start != -1) { 
				
				content = content + page.substring(index_start+3, index_end);
			}
			
			if (index_end != -1) { 
				
				page = page.substring(index_end+4);
			}
			
			index_start = page.indexOf("<p>");
		}
		
		return content;
	} 
	
	/** 
	 * Retrieves content of all the given pages and stores them in a ArrayList
	 * @throws IOException
	 */ 
	
	public void getAllPageContent() throws IOException {  
		
		String page = ""; 
		String content = ""; 
		
		for (String link : page_list) { 
			
			if (!link.contains("cleartax")) { 
			
				page = getPage(link); 
				
			} else { 
				
				page = loadClearTaxPage("cleartax.txt");
			} 
	
			content = getPageContent(page);
			content = Clean.cleanHTML(content);
			content = Clean.removeURL(content);
			content = Clean.removePunctuationAndJunk(content); 
			
			if (content.isEmpty()) {  
				
				content = getListTagContent(page);
			} 
			
			page_content.add(content);
		}
	} 
	
	/**
	 * Calculates the similarity between cleartax and its competitor websites in terms of the Levenstein distance
	 * @throws IOException  
	 */ 
	
	public void getPageSimilarityScore() throws IOException { 
		
		String clearTax_page = page_content.get(0);
		
		for (int i = 1; i < page_content.size(); i++) { 
			
			int distance = Levenstein.calculateDistance(clearTax_page, page_content.get(i));
			page_similar.add(distance);
		}
	} 
	
	/** 
	 * Loads the clearTax webpage from local repository since crawling is disable on it
	 * @param filename String containing the filename from which the webpage has to be read
	 * @return String containing the webpage
	 * @throws IOException
	 */ 
	
	public String loadClearTaxPage(String filename) throws IOException { 
		
		BufferedReader br; 
		FileReader fr; 
		String page = ""; 
		String temp = ""; 
		
		fr = new FileReader(filename);
		br = new BufferedReader(fr); 
		
		while( (temp = br.readLine()) != null) { 
			
			page = page + temp;
		}
		
		br.close();
		fr.close();
		
		return page;
	} 
	
	/** 
	 * Returns the content between a set of <li> tags
	 * @param page String containing the HTML page
	 * @return String which only contains the text between the <li> tags
	 */ 
	
	public String getListTagContent(String page) { 
		
		String list_tag = "";
		int start_index = -1;
		int end_index = -1;  
		
		String temp_page = Clean.removeNewLines(page);
		temp_page = Clean.cleanHTML(temp_page);
		temp_page = Clean.removePunctuationAndJunk(temp_page); 
		
		start_index = temp_page.indexOf("<li>"); 
		
		while(start_index != -1) { 
			
			end_index = temp_page.indexOf("</li>", start_index+1);
			
			if (end_index != -1 && start_index != -1) { 
				
				list_tag = list_tag + temp_page.substring(start_index+4, end_index); 
			}
			
			if ( (end_index+5) < temp_page.length()) { 
				
				temp_page = temp_page.substring(end_index+5);
			}
			
			start_index = temp_page.indexOf("<li>");
		}
		
		return list_tag;
	} 
	
	/** 
	 * Returns the contentPipeline for extracting the page content and measuring the similarity between clearTax and its competitor websites
	 * @throws IOException
	 */ 
	
	public void contentPipeline() throws IOException { 
		
		BenchmarkTime bt = new BenchmarkTime(); 
		bt.getAllPageContent();
		bt.getPageSimilarityScore();
		System.out.println("========== Content Similarity Score ===============\n");
		
		for (int i = 1; i < bt.page_similar.size(); i++) { 
			
			System.out.println(bt.page_list.get(i) + " : " + bt.page_similar.get(i));
		}
		
		System.out.println("======================================================\n");
	} 
	
	/** 
	 * Counts the total number of links on each page
	 * @throws IOException
	 */ 
	
	public void linkPipeline() throws IOException { 
		
		BenchmarkTime bt = new BenchmarkTime();  
		
		System.out.println("============== Number of Links on Each WebPage ================\n"); 
		
		for (String s : bt.page_list) { 
			
			System.out.println(s + " : " + bt.countLinks(s));
		}
		
		System.out.println("==================================================================\n");
	}
	
	/** 
	 * Defines the time profiling pipeline for each website 
	 * @throws IOException
	 */ 
	
	public void timeProfilePipeline() throws IOException { 
		
		BenchmarkTime bt = new BenchmarkTime(); 
		bt.populateProfiles();
		System.out.println("=========== Time Profiles of each Page =============================\n");
		bt.printProfiles();
		System.out.println("====================================================================\n");
		bt.averageTime();
		bt.minimumTime();
		bt.maximumTime();
		bt.commitToFile("timestats.txt"); // Uncomment this if you want the time stats written to an external file
	} 
	
	/** 
	 * Main function to test the functionality of the class
	 * @param arg
	 * @throws IOException
	 */ 
	
	public static void main(String arg[]) throws IOException { 

		BenchmarkTime bt = new BenchmarkTime(); 
		bt.linkPipeline(); 
		bt.timeProfilePipeline(); 
		bt.contentPipeline();  
	}
}
