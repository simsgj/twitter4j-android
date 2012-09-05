package twitter4j;

import java.util.Date;

public interface Activity extends TwitterResponse {

	public Action getAction();
	
	public Date getCreatedAt();

	public User[] getSources();
	
	public User[] getTargets();
	
	public Status[] getTargetObjects();
	
	public static enum Action {
		FOLLOW, MENTION, REPLY, RETWEET;
		
		public static Action fromString(String string) {
			if ("follow".equals(string)) return FOLLOW;
			if ("mention".equals(string)) return MENTION;
			if ("reply".equals(string)) return REPLY;
			if ("retweet".equals(string)) return RETWEET;
			return null;
		}
	}
}
