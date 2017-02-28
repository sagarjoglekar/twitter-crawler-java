package crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.atomic.AtomicInteger;
 
public class TwitterLinksImpl extends TwitterReplies {
 
    private final AtomicInteger counter = new AtomicInteger();

		private static BufferedWriter writer;
//		private static BufferedWriter linksWriter;

		private static LinkedList<SimpleEntry<String, String>> linksQueue = new LinkedList<SimpleEntry<String, String>>(); 
		private static HashSet<String> alreadyTests = new HashSet<String>(); 

		private static String fileName;
//		private static String linksFileName;
		
//		private String originalTweetContent;
		
    @Override
    public boolean saveTweets(List<Tweet> tweets, String originalTweet, TwitterReplyResponse response) {
        if(tweets!=null) {
						try {
							if (tweets.size() > 0) {
							  writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
//							  writer.write(originalTweetContent + "|||null|||null\r\n");
							  for (Tweet tweet : tweets) {
	                System.out.println(counter.getAndIncrement() + 1 + "[" + tweet.getCreatedAt() + "] - " + tweet.getText());
	                writer.write(tweet + "|||" + originalTweet +"\r\n");
	//                if (counter.get() >= 500) {
	//                    return false;
	//                }
		            }
	//						  writer.write(originalTweet + "\r\n\r\n\r\n\r\n");
								writer.close();
							}
							if (response.moreLinks.size() > 0 || response.viewOthers.size() > 0) {
//							  linksWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linksFileName, true), "UTF-8"));
//								for (String link : response.moreLinks) {
//									linksWriter.write("more," + originalTweet + "," + link + "\r\n");
//								}
//								for (String link : response.viewOthers) {
//									linksWriter.write("others," + originalTweet + "," + link + "\r\n");
//								}
//								linksWriter.close();
								for (String link : response.viewOthers) {
									if (!alreadyTests.contains(link)) {
										if (link.contains("?")) {
											link = link.substring(0, link.indexOf("?"));
										}
										linksQueue.add(new SimpleEntry<String, String>(originalTweet, link));
										alreadyTests.add(link);
									}
								}
								for (String link : response.moreLinks) {
									if (link.contains("?")) {
										link = link.substring(0, link.indexOf("?"));
									}
									if (!alreadyTests.contains(link)) {
										linksQueue.add(new SimpleEntry<String, String>(originalTweet, link));
										alreadyTests.add(link);
									}
								}
//								linksQueue.addAll(response.moreLinks);
//								linksQueue.addAll(response.viewOthers);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
        }
        return true;
    }
 
    public static void main(String[] args) throws InvalidQueryException {
		  try {
		  	fileName = "out/daily-suicides-refined/suicides-daily-v3-replies.replies";
//		  	linksFileName = "out/missingpeople/missingpeople-replies.links";
		  	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
		  	writer.close();
//		  	linksWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linksFileName), "UTF-8"));
//		  	linksWriter.close();

			  BufferedReader reader = new BufferedReader(new FileReader("out/daily-suicides-refined/suicides-daily-v3.links"));
			  String line = reader.readLine();
			  line = reader.readLine();
			  int i = 0;
			  while (line != null) {
			  	String id = line.split(",")[2].split("/")[3];
			  	String account = line.split(",")[2].split("/")[1];
			  	String originalTweet = line.split(",")[1];
		  		System.out.println("[" + i + "] " + id + " " + account);
			  	TwitterLinksImpl twitterReplies = new TwitterLinksImpl();
//			  	twitterReplies.originalTweetContent = line;
			  	twitterReplies.search(id, account, 0, originalTweet);
			  	i++;
			  	line = reader.readLine();
			  }
			  System.out.println("Finished with the original links, going recursive...");
			  while (!linksQueue.isEmpty()) {
			  	SimpleEntry<String, String> entry = linksQueue.poll(); 
			  	String url = entry.getValue();
			  	String originalTweet = entry.getKey();
			  	String id = url.split("/")[3];
			  	if (id.contains("?")) {
			  		id = id.substring(0, id.indexOf("?"));
			  	}
			  	String account = url.split("/")[1];
		  		System.out.println("Recursive [" + i + "/" + linksQueue.size() + "] " + id + " " + account);
			  	TwitterLinksImpl twitterReplies = new TwitterLinksImpl();
			  	twitterReplies.search(id, account, 0, originalTweet);
			  	i++;
			  }
			  reader.close();
		  } catch (IOException e) {
				e.printStackTrace();
			}
   }
}