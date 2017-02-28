package crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.google.gson.Gson;

public abstract class TwitterSearch {
	 
  public TwitterSearch() {

  }

  public abstract boolean saveTweets(List<Tweet> tweets);

  public void search(final String query, final long rateDelay) throws InvalidQueryException {
    TwitterResponse response;
    String scrollCursor = null;
    URL url = constructURL(query, scrollCursor);
    boolean continueSearch = true;
//    && response.isHas_more_items() 
    String previousCursor = "NOT EMPTY";
    while((response = executeSearch(url)) !=null && continueSearch) {
        continueSearch = saveTweets(response.getTweets());
//        scrollCursor = response.getScroll_cursor();
        scrollCursor = response.getMin_position();
        System.out.println(scrollCursor);
        try {
            Thread.sleep(rateDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        url = constructURL(query, scrollCursor);
        if (previousCursor.equals(scrollCursor)) {
        	break;
        }
	      previousCursor = scrollCursor;  
    }
  }

  public static TwitterResponse executeSearch(final URL url) {
    BufferedReader reader = null;
    try {
    		for (int i = 0; i < 3; i++) {
      		try {
      			URLConnection connection = url.openConnection();
      			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
  	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
  	        Gson gson = new Gson();
  	        return gson.fromJson(reader, TwitterResponse.class);
      		} catch (FileNotFoundException e) {
      			e.printStackTrace();
      		}
    		}
    } catch(IOException e) {
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

  public final static String TYPE_PARAM = "f";
  public final static String QUERY_PARAM = "q";
//  public final static String SCROLL_CURSOR_PARAM = "scroll_cursor";
  public final static String SCROLL_CURSOR_PARAM = "max_position";//"max_position";
  public final static String TWITTER_SEARCH_URL = "https://twitter.com/i/search/timeline";
   
  public static URL constructURL(final String query, final String scrollCursor) throws InvalidQueryException {
          if(query==null || query.isEmpty()) {
              throw new InvalidQueryException(query);
          }
          try {
//              UriBuilder uriBuilder;
//              uriBuilder = new URIBuilder(TWITTER_SEARCH_URL);
//              uriBuilder.addParameter(QUERY_PARAM, query);
//              uriBuilder.addParameter(TYPE_PARAM, "realtime");
//              if (scrollCursor != null) {
//                  uriBuilder.addParameter(SCROLL_CURSOR_PARAM, scrollCursor);
//              }
//              return uriBuilder.build().toURL();
          	String url = TWITTER_SEARCH_URL;
          	url += "?f=tweets&vertical=default&src=typd&include_available_features=1&include_entities=1&reset_error_state=false&" + QUERY_PARAM + "=" + URLEncoder.encode(query, "UTF-8"); //+ "&" + TYPE_PARAM + "=realtime"
	          if (scrollCursor != null) {
	          	url += "&" + SCROLL_CURSOR_PARAM + "=" + URLEncoder.encode(scrollCursor, "UTF-8");
	          } else {
	          	url += "&" + SCROLL_CURSOR_PARAM + "=";
	          }
	          System.out.println(url);
//	          URI uRL = new URI(url);
////	          System.out.println(uRL.toURI());
//	          return uRL.toURL();
	          return new URL(url);
          } catch(MalformedURLException | UnsupportedEncodingException e) {
              e.printStackTrace();
              throw new InvalidQueryException(query);
          } 
  }
  
}