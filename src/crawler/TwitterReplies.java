package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class TwitterReplies {
	 
  public TwitterReplies() {

  }

  public abstract boolean saveTweets(List<Tweet> tweets, String originalTweet, TwitterReplyResponse response);

  public void search(final String tweetId, final String accountId, final long rateDelay) throws InvalidQueryException {
  	search(tweetId, accountId, rateDelay, null);
  }

  public void search(final String tweetId, final String accountId, final long rateDelay, final String originalTweet) throws InvalidQueryException {
	  TwitterReplyResponse response;
	//  String scrollCursor = null;
	  URL url = constructURL(tweetId, accountId, "");
	  boolean continueSearch = true;
	//  && response.isHas_more_items() 
	  boolean hasMore = true;
	  String previousId = "";
	  String currentId = "fdfsd";
	  while((response = executeSearch(url)) !=null && continueSearch && hasMore && !previousId.equals(currentId)) {
  		  if (!tweetId.equals(originalTweet)) {
  		  	response.parentTweetId = tweetId;
  		  }
	  		List<Tweet> tweets = response.getTweets();
	  		previousId = currentId; 
	  		if (tweets.size() > 0) {
	  			currentId = tweets.get(tweets.size() - 1).getId();
	  		}
	  		if (originalTweet == null) {
		      continueSearch = saveTweets(tweets, tweetId, response);
	  		} else {
		      continueSearch = saveTweets(tweets, originalTweet, response);
	  		}
	//      scrollCursor = response.getScroll_cursor();
	      try {
	          Thread.sleep(rateDelay);
	      } catch (InterruptedException e) {
	          e.printStackTrace();
	      }
	      hasMore = response.has_more;
	      url = constructURL(tweetId, accountId, response.min_position);
	  }
  }

  public static TwitterReplyResponse executeSearch(final URL url) {
  	BufferedReader reader = null;
    try {
        reader = new BufferedReader(new InputStreamReader(setCookies(url.openConnection()).getInputStream(), "UTF-8"));
        String line = reader.readLine();
        String string = "";
        while (line != null) {
        	string += line + "\r\n";
        	line = reader.readLine();
        }
        JSONObject jsonObject = new JSONObject(string);   
//        
//        Gson gson = new Gson();
//        return gson.fromJson(reader, TwitterReplyResponse.class);
        return new TwitterReplyResponse(jsonObject);
    } catch(IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
			e.printStackTrace();
		} finally {
        try {
            reader.close();
        } catch(NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
    return null;
  }

//  public final static String TYPE_PARAM = "f";
//  public final static String QUERY_PARAM = "q";
  public final static String SCROLL_CURSOR_PARAM = "max_position";
//  https://twitter.com/i/comma_com_ua/conversation/604928517479010304?include_available_features=1&include_entities=1&max_position=605117091063152640
//  https://twitter.com/i/comma_com_ua/conversation/604928517479010304?include_available_features=1&include_entities=1&max_position=606093682278809602
  public final static String TWITTER_REPLIES_URL = "https://twitter.com/i/";
  public final static String TWITTER_REPLIES_URL_SECOND_PART = "/conversation/";
     
  public static URL constructURL(final String tweetId, final String accountId, final String cursor) throws InvalidQueryException {
          if(tweetId==null || tweetId.isEmpty()) {
              throw new InvalidQueryException(tweetId);
          }
          try {

          	String url = TWITTER_REPLIES_URL + accountId + TWITTER_REPLIES_URL_SECOND_PART;
          	url += tweetId + "?include_available_features=1&include_entities=1&max_position=" + cursor;
	          System.out.println(url);
	          return new URL(url);
          } catch(MalformedURLException e) {
              e.printStackTrace();
              throw new InvalidQueryException(tweetId	);
          } 
  }
  
	public static URLConnection setCookies(URLConnection conn) throws IOException {
		try {
			conn.setRequestProperty("Cookie", "auth_token=3130fad824d1eab384a663978a640f872fb89544");
			return conn;
		} catch (java.lang.IllegalStateException ise) {
			IOException ioe = new IOException(
					"Illegal State! Cookies cannot be set on a URLConnection that is already connected. "
							+ "Only call setCookies(java.net.URLConnection) AFTER calling java.net.URLConnection.connect().");
			throw ioe;
		}
	}
}