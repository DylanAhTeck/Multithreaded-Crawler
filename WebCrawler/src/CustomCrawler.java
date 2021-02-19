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
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CustomCrawler extends WebCrawler {

	//private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
	//		 + "|png|mp3|mp3|zip|gz))$");
	
	//private final static Pattern FILTERS = Pattern.compile(".*(\\.(doc|DOC|DOCX|docx|HTML|html|gif|GIF|jpg|JPG"
	//		 + "|png|PNG|PDF|pdf|))$");
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|print" +
 "|font-woff2|json|xml|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	private static FileWriter fetchWriter;
	private static FileWriter visitWriter;
	private static FileWriter urlWriter;
	
	CrawlStat myCrawlStat;
	
	public CustomCrawler () {
		myCrawlStat = new CrawlStat();
		
		try {
			fetchWriter =  new FileWriter("fetch_FoxNews.csv");
			fetchWriter.append("URL,Status\n");
			
			visitWriter = new FileWriter("visit_FoxNews.csv");
			visitWriter.append("URL,File Size/Bytes,Number of Links Found,Content-Type \n");
			
			urlWriter = new FileWriter("urls_FoxNews.csv");
			urlWriter.append("URL,Resides in Website \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	 public boolean shouldVisit(Page referringPage, WebURL url) {
	
	 this.myCrawlStat.incTotalPagesFetched();
	 
	 String href = url.getURL().toLowerCase();
	 
	 String residesInWebsite = href.startsWith("https://www.foxnews.com/") ? "OK" : "N_OK";
	 appendToCSV(Arrays.asList(href, residesInWebsite), urlWriter);
	 
	 //return !FILTERS.matcher(href).matches()
	 //&& href.startsWith("https://www.foxnews.com/");
	 
	 
	 return (!FILTERS.matcher(href).matches())
//      && href.startsWith("https://www.foxnews.com/");
			 && href.startsWith("https://www.nytimes.com/");
	 
	}
	
	public void appendToCSV(List<String> rowData, FileWriter writer) {
		try {
			writer.append(String.join(",", rowData));
			writer.append("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }
	
	@Override 
	public void onBeforeExit () {
		try {
			fetchWriter.close();
			urlWriter.close();
			visitWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public int handlePageStatusCode() {
//		
//	}
//	
	 /**
	  * This function is called when a page is fetched and ready
	  * to be processed by your program.
	  */
	  @Override
	  public void visit(Page page) {
		  
	  //If not a valid type, skip page
	  if(!page.getContentType().contains("image/") && 
			  !page.getContentType().contains("application/pdf") &&  
			  !page.getContentType().contains("text/html") && 
			  !page.getContentType().contains("application/doc")
			  ) {

		  return;
	  }
	  myCrawlStat.incProcessedPages();
	 
		
	  String url = page.getWebURL().getURL();
	  String statusCode = String.valueOf(page.getStatusCode());
	  appendToCSV(Arrays.asList(url, statusCode), fetchWriter);
	  

//	  if (page.getParseData() instanceof HtmlParseData) {
		  
	  ParseData parseData = page.getParseData();
//	  HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//	  String text = htmlParseData.getText();
//	  String html = htmlParseData.getHtml();
//	  Set<WebURL> links = htmlParseData.getOutgoingUrls();
	  
	  Set<WebURL> links = parseData.getOutgoingUrls();
	  
	  myCrawlStat.incTotalLinks(links.size());
	  String fileSize = String.valueOf(page.getContentData().length);
	  String numOfOutgoingLinks = String.valueOf(links.size());
	  String contentType = page.getContentType().split(";")[0];
	  appendToCSV(Arrays.asList(url, fileSize, numOfOutgoingLinks, contentType), visitWriter);
	
//	  System.out.println("Text length: " + text.length());
//	  System.out.println("Html length: " + html.length());
//	  System.out.println("Number of outgoing links: " + links.size());
//	  }
	  
	  }
	}
	
