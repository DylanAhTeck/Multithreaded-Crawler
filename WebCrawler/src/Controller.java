import java.util.List;

import edu.uci.ics.crawler4j.*;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	
 public static void main(String[] args) throws Exception {
 String crawlStorageFolder = "/Users/dylanahteck/eclipse-workspace/WebCrawler/data/crawl";
 int numberOfCrawlers = 5;
 CrawlConfig config = new CrawlConfig();
 config.setCrawlStorageFolder(crawlStorageFolder);
 
 //Settings
 config.setPolitenessDelay(200);
 config.setMaxPagesToFetch(500);
 config.setMaxDepthOfCrawling(16);
 config.setIncludeBinaryContentInCrawling(true);
 
 /*
 * Instantiate the controller for this crawl.
 */
 PageFetcher pageFetcher = new PageFetcher(config);
 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
 /*
 * For each crawl, you need to add some seed urls. These are the first
 * URLs that are fetched and then the crawler starts following links
 * which are found in these pages
 */
 //https://www.nytimes.com/
 controller.addSeed("https://www.nytimes.com/");

 //controller.addSeed("https://www.foxnews.com/");
 /*
 * Start the crawl. This is a blocking operation, meaning that your code
 * will reach the line after this only when crawling is finished.
 */
 controller.start(CustomCrawler.class, numberOfCrawlers);
 
 List<Object> crawlersLocalData = controller.getCrawlersLocalData();
 long totalLinks = 0;
 long totalTextSize = 0;
 int totalProcessedPages = 0;
 long totalPages = 0;
 for (Object localData : crawlersLocalData) {
     CrawlStat stat = (CrawlStat) localData;
     totalPages += stat.getTotalPagesFetched();
     totalLinks += stat.getTotalLinks();
     totalTextSize += stat.getTotalTextSize();
     totalProcessedPages += stat.getTotalProcessedPages();
 }

 System.out.println("Aggregated Statistics:");
 System.out.println(totalPages);
 System.out.println(totalProcessedPages);
 System.out.println(totalLinks);
 System.out.println(totalTextSize);
 }
}

