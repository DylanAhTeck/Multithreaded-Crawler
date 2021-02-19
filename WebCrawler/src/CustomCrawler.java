import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CustomCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			 + "|png|mp3|mp3|zip|gz))$");
	
	private static FileWriter fetchWriter;
	private static FileWriter visitWriter;
	public CustomCrawler () {
		
		
		  
		try {
			fetchWriter =  new FileWriter("fetch_FoxNews.csv");
			fetchWriter.append("URL,Status\n");
			
			visitWriter = new FileWriter("visit_FoxNews.csv");
			visitWriter.append("URL,File Size,Number of Links Found,Content-Type \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	 public boolean shouldVisit(Page referringPage, WebURL url) {
	 String href = url.getURL().toLowerCase();
	 return !FILTERS.matcher(href).matches()
	 && href.startsWith("https://www.foxnews.com/");
		
	}
	
	public void appendToCSV(List<String> rowData, FileWriter writer) {
		
		  try {
			writer.append(String.join(",", rowData));
			writer.append("\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	@Override 
	public void onBeforeExit () {
		try {
			fetchWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 /**
	  * This function is called when a page is fetched and ready
	  * to be processed by your program.
	  */
	  @Override
	  public void visit(Page page) {
		  
	 
	 
		
	  String url = page.getWebURL().getURL();
//	  System.out.println("URL: " + url);
	  System.out.println(url + ", " + page.getStatusCode());
	  String statusCode = String.valueOf(page.getStatusCode());
	  appendToCSV(Arrays.asList(url, statusCode), fetchWriter);
	  
//	  try {
//		FileWriter csvWriter = new FileWriter("/Users/dylanahteck/eclipse-workspace/new.csv");
//		csvWriter.append("TEST");
//
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	  if (page.getParseData() instanceof HtmlParseData) {
		  
	  HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	  String text = htmlParseData.getText();
	  String html = htmlParseData.getHtml();
	  Set<WebURL> links = htmlParseData.getOutgoingUrls();
	  
	  String fileSize = String.valueOf(page.getContentData().length);
	  String numOfOutgoingLinks = String.valueOf(links.size());
	  String contentType = page.getContentType();
	  appendToCSV(Arrays.asList(url, fileSize, numOfOutgoingLinks, contentType), visitWriter);
	

	  System.out.println("Text length: " + text.length());
	  System.out.println("Html length: " + html.length());
	  System.out.println("Number of outgoing links: " + links.size());
	  }
	  
	  }
	}
	
