package twitter4j.api;

import twitter4j.Activity;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

public interface ActivityMethods {
	public ResponseList<Activity> getActivitiesAboutMe() throws TwitterException;
	public ResponseList<Activity> getActivitiesByFriends() throws TwitterException;
}
