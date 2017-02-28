package crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class TwitterReplyResponse {
 
		String items_html = "";
		boolean has_more = true;
//		String cursorTweetId = "";
		String min_position = "";
		List<String> moreLinks = new LinkedList<String>();
		List<String> viewOthers = new LinkedList<String>();
		String parentTweetId = null;
		
    public TwitterReplyResponse(JSONObject json) {
    	try {
				items_html = json.getJSONObject("descendants").getString("items_html");
				has_more = json.getJSONObject("descendants").getBoolean("has_more_items");
				if (!json.getJSONObject("descendants").isNull("min_position")) {
					min_position = json.getJSONObject("descendants").getString("min_position");
				}
    		//System.out.println("MIN POSITION: " + min_position);
				System.out.println("NEW LATENT COUNT " + json.getJSONObject("descendants").getInt("new_latent_count"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
    }

    public List<Tweet> getTweets() {
//	  System.out.println(items_html);
	  final List<Tweet> tweets = new ArrayList<>();
	  Document doc = Jsoup.parse(items_html);
	  
	  Elements threads = doc.select("li.ThreadedConversation,div.ThreadedConversation--loneTweet");
	  //threads.addAll(doc.select("div.ThreadedConversation--loneTweet"));
	  
  	System.out.println("Number of threads " + threads.size());
	  for (Element thread : threads) {
	  	String replyToId = parentTweetId;
//    	System.out.println("Elements in the thread " + thread.select("div.js-stream-tweet").size());
		  for (Element el : thread.select("div.js-stream-tweet")) {
//	  for(Element el : doc.select("li.js-stream-item")) {
	      String id = el.attr("data-item-id");
//	      cursorTweetId = id;
	      String text = null;
	      String userId = null;
	      String userScreenName = null;
	      String userName = null;
	      Date createdAt = null;
	      int retweets = 0;
	      int favourites = 0;
	      try {
	          text = el.select("p.tweet-text").text();
	      } catch (NullPointerException e) {
	          e.printStackTrace();
	      }
	      try {
	          userId = el.select("div.tweet").attr("data-user-id");
	      } catch (NullPointerException e) {
	          e.printStackTrace();
	      }
	      try {
	          userName = el.select("div.tweet").attr("data-name");
	      } catch (NullPointerException e) {
	          e.printStackTrace();
	      }
	      try {
	          userScreenName = el.select("div.tweet").attr("data-screen-name");
	      } catch (NullPointerException e) {
	          e.printStackTrace();
	      }
	      try {
	          final String date = el.select("span._timestamp").attr("data-time-ms");
	          if (date != null && !date.isEmpty()) {
	              createdAt = new Date(Long.parseLong(date));
	          }
	      } catch (NullPointerException | NumberFormatException e) {
	          e.printStackTrace();
	      }
	      try {
	          retweets = Integer.parseInt(el.select("span.ProfileTweet-action--retweet > span.ProfileTweet-actionCount")
	                  .attr("data-tweet-stat-count"));
	      } catch(NumberFormatException | NullPointerException e) {
	          e.printStackTrace();
	      }
	      try {
	          favourites = Integer.parseInt(el.select("span.ProfileTweet-action--favorite > span.ProfileTweet-actionCount")
	                  .attr("data-tweet-stat-count"));
	      } catch (NumberFormatException | NullPointerException e) {
	          e.printStackTrace();
	      }
	      Tweet tweet = new Tweet(
	              id,
	              text,
	              userId,
	              userName,
	              userScreenName,
	              createdAt,
	              retweets,
	              favourites,
	              replyToId
	      );
	      if (tweet.getId() != null) {
	          tweets.add(tweet);
	      }
	      replyToId = id;
		  }
	  }
    try {
    	for (Element el : doc.select("a.show-more-link")) {
        System.out.println("!!!MORE LINKS!!! " + el.attr("href"));
    		moreLinks.add(el.attr("href"));
    	}
    } catch (NumberFormatException | NullPointerException e) {
        e.printStackTrace();
    }
    try {
    	for (Element el : doc.select("a.view-other-link")) {
        System.out.println("!!!VIEW OTHERS!!! " + el.attr("href"));
    		viewOthers.add(el.attr("href"));
    	}
    } catch (NumberFormatException | NullPointerException e) {
        e.printStackTrace();
    }
	  return tweets;
	}

		/**
		 * @return the items_html
		 */
		public String getItems_html() {
			return items_html;
		}

		/**
		 * @param items_html the items_html to set
		 */
		public void setItems_html(String items_html) {
			this.items_html = items_html;
		}

}
