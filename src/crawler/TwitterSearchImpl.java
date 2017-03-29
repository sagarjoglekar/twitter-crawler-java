package crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 
public class TwitterSearchImpl extends TwitterSearch {
 
    private final AtomicInteger counter = new AtomicInteger();

		private static BufferedWriter writer;

		private static String fileName;
		
    @Override
    public boolean saveTweets(List<Tweet> tweets) {
    	  System.out.println(tweets.size());
        if(tweets!=null) {
						try {
						  writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
	            for (Tweet tweet : tweets) {
                System.out.println(counter.getAndIncrement() + 1 + "[" + tweet.getCreatedAt() + "] - " + tweet.getText());
                writer.write(tweet + "\r\n");
//                if (counter.get() >= 500) {
//                    return false;
//                }
	            }
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
        }
        return true;
    }
 
    public static void main(String[] args) throws InvalidQueryException {
		  try {
		  	fileName = "westMinster.tweets";
		  	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
		  	writer.close();
		  } catch (IOException e) {
				e.printStackTrace();
			}
    	  TwitterSearch twitterSearch = new TwitterSearchImpl();
        //twitterSearch.search("#DroughtSelfie OR #KeepSavingCA OR #Waterscarcity OR #Flintwatercrisis OR #SaoPauloDrought", 2000);
    	  twitterSearch.search("#WestminsterAttack OR WestminsterAttack OR #WeStandTogether OR #pcpalmer OR #Westminster OR #WeAreNotAfraid until:2017-03-25 since:2015-03-21", 20000);
    }
}

//twitterSearch.search("blogomanija OR blogomania OR twitoslavija OR tvitoslavija OR tvitoslavia OR twitoslavia OR tvitomanija OR tvitomania OR twitomanija OR twitomania", 2000);

//writer = new BufferedWriter(new FileWriter("out/findmike.tweets"));
//TwitterSearch twitterSearch = new TwitterSearchImpl();
//twitterSearch.search("#findmike until:2014-01-31", 2000);
//writer.close();
//writer = new BufferedWriter(new FileWriter("out/bigtweet-2014.tweets"));
//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out/radar-samaritans.tweets"), "UTF-8"));
//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out/serbiafloods-2.tweets"), "UTF-8"));
//writer = new BufferedWriter(new FileWriter("out/serbiafloods.tweets"));
//twitterSearch.search("#bigtweet until:2013-05-25 since:2013-05-17", 2000);
//twitterSearch.search("radar samaritans", 2000);
//twitterSearch.search("#bosniafloods OR #srpskafloods OR #SerbianFloods OR #SerbiaFloods OR #SupportSerbiaandBosnia OR #PrayforSerbia OR #PrayforBosnia OR #SerbiaNeedsHelp", 2000);
//twitterSearch.search("#bosniafloods OR #srpskafloods OR #SerbianFloods OR #SerbiaFloods OR #SupportSerbiaandBosnia OR #PrayforSerbia OR #PrayforBosnia OR #SerbiaNeedsHelp #NDF OR #novakfoundation", 2000);
//twitterSearch.search("#sadgirl until:2015-04-01 since:2010-01-01", 2000);
////twitterSearch.search("\"Robin Williams\" OR #RobinWilliams until:2014-10-01 since:2014-07-31", 0);
//twitterSearch.search("\"Robin Williams\" OR #RobinWilliams until:2014-08-12 since:2014-07-31", 0);
//twitterSearch.search("\"Amanda Todd\" OR #AmandaTodd until:2012-12-01 since:2012-10-01", 0);
//twitterSearch.search("#facebookreport", 0);
//twitterSearch.search("suicide until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"Charlotte Dawson\" OR #CharlotteDawson until:2014-04-01 since:2014-02-15", 0);
//twitterSearch.search("#poplave OR #Serbiafloods OR #Srbija OR #Tvitoslavija OR #hrvatskapomaze", 0);
//twitterSearch.search("\"feel so empty\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"push everyone away\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"not good enough\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("feel depressed until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("feel drowning until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"so close to giving up\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"thought of suicide\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"I just want to sleep\" forever until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"I do not\" OR \"I dont\" OR \"I don't\" care anymore until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"if I kill myself\" OR \"if I killed myself\" until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("\"i might as well\" OR \"i may as well\" not exist until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("feel drowning until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("nobody OR \"no one\" OR noone OR anybody OR \"any body\" would care died until:2015-03-01 since:2015-01-01", 0);
//twitterSearch.search("leelah alcorn since:2015-02-01", 0);
//twitterSearch.search("ria.ru OR 1tv.ru OR vesti-ukr.com OR ura-inform.com OR vesti.ru OR kp.ru OR ntv.ru OR lifenews.ru OR politonline.ru OR itar-tass.com OR russian.rt.com OR business-gazeta.ru OR vz.ru OR antifashist.com OR rbcdaily.ru OR mir24.tv OR ulpressa.ru OR izvestia.ru OR kommersant.ru OR newsru.com since:2014-03-02 until:2014-04-02", 0);
//twitterSearch.search("#bosniafloods OR #srpskafloods OR #SerbianFloods OR #SerbiaFloods OR #SupportSerbiaandBosnia OR #PrayforSerbia OR #PrayforBosnia OR #SerbiaNeedsHelp OR #NDF OR #novakfoundation OR #poplave OR #Serbiafloods OR #Srbija OR #Tvitoslavija OR #hrvatskapomaze OR #pomoć OR #poplava", 2000);
//twitterSearch.search("ria.ru since:2014-03-02 until:2014-04-02", 0);

//fileName = "out/radar-samaritans-full.tweets";
//fileName = "out/serbiafloods-full.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/suicide-long.tweets";
//fileName = "out/suicides/suicide.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/sadgirl-long.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/feel-so-empty.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/push-everyone-away.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/not-good-enough.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/feel-depressed.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/feel-drowning.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/constant-anxiety.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/so-close-to-giving-up.tweets";
//fileName = "out/suicides/pretending-to-be-okay.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/do-not-know-how-to-be-happy.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/i-do-not-care-anymore.tweets";
//fileName = "/home/dmytro/mnt/dmytro/suicides/i-may-as-well-not-exist.tweets";
//fileName = "out/feel-drowning";
//fileName = "/home/dmytro/mnt/dmytro/suicides/nobody-would-care-died";
//fileName = "out/leelah-alcorn.tweets";
//fileName = "out/leelah-alcorn-afterjanuary.tweets";
//fileName = "out/aaron-swartz.tweets";
//fileName = "out/stopfake.org.tweets";
//fileName = "out/euromaidan-missing.tweets";
//fileName = "out/test.tweets";
//fileName = "out/stopfake.org.tweets.2";
