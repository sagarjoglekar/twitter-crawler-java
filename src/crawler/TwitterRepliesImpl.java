package crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 
public class TwitterRepliesImpl extends TwitterReplies {
 
    private final AtomicInteger counter = new AtomicInteger();

		private static BufferedWriter writer;
		private static BufferedWriter linksWriter;

		private static String fileName;
		private static String linksFileName;
		
		private String originalTweetContent;
		
    @Override
    public boolean saveTweets(List<Tweet> tweets, String originalTweet, TwitterReplyResponse response) {
        if(tweets!=null) {
						try {
							if (tweets.size() > 0) {
							  writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
							  writer.write(originalTweetContent + "|||null|||null\r\n");
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
							  linksWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linksFileName, true), "UTF-8"));
								for (String link : response.moreLinks) {
									linksWriter.write("more," + originalTweet + "," + link + "\r\n");
								}
								for (String link : response.viewOthers) {
									linksWriter.write("others," + originalTweet + "," + link + "\r\n");
								}
								linksWriter.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
        }
        return true;
    }
 
    public static void main(String[] args) throws InvalidQueryException {
		  try {
		  	fileName = "out/daily-suicides/suicides-daily-v3.replies";
		  	linksFileName = "out/daily-suicides/suicides-daily-v3.links";
		  	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
		  	writer.close();
		  	linksWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linksFileName), "UTF-8"));
		  	linksWriter.close();

			  BufferedReader reader = new BufferedReader(new FileReader("out/daily-suicides/suicides-all-filtered-v3.csv"));
			  String line = reader.readLine();
			  line = reader.readLine();
			  boolean skipBeforeId = false;
			  int i = 0;
			  while (line != null) {
			  	//This piece is for history clawler
//			  	String id = line.split("\\|\\|\\|")[0];
//			  	String account = line.split("\\|\\|\\|")[4];
			  	//This piece is from official crawler 
			  	String id = line.split(",")[0];
			  	String account = line.split(",")[2];
			  	if (!skipBeforeId) {
			  		System.out.println("[" + i + "] " + id + " " + account);
				  	TwitterRepliesImpl twitterReplies = new TwitterRepliesImpl();
//				  twitterReplies.originalTweet = line;
				  	twitterReplies.originalTweetContent = line;
				  	twitterReplies.search(id, account, 0);
//				  	break;
			  	}
			  	if (id.equals("470543344490528768")) {
			  		skipBeforeId = false;
			  	}			
			  	i++;
			  	line = reader.readLine();
//			  	break;
			  }
			  reader.close();

		  	
		  } catch (IOException e) {
				e.printStackTrace();
			}
   }
}