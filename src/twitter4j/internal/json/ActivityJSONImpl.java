package twitter4j.internal.json;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getBoolean;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getInt;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getLong;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getRawString;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Activity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;

public final class ActivityJSONImpl extends TwitterResponseImpl implements Activity {

	public Status[] getTargetObjects() {
		return targetObjects;
	}

	public User[] getSources() {
		return sources;
	}

	public User[] getTargets() {
		return targets;
	}

	public Activity.Action getAction() {
		return action;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	private Action action;
	private Date createdAt;
	private User[] sources, targets;
	private Status[] targetObjects;


	/* package */ActivityJSONImpl(JSONObject json) throws TwitterException {
		super();
		init(json);
	}

	final void init(JSONObject json) throws TwitterException {
		try {
			action = Action.fromString(getRawString("action", json));
			createdAt = getDate("created_at", json, "EEE MMM dd HH:mm:ss z yyyy");
			final JSONArray sources_array = json.getJSONArray("sources");
			final int sources_size = sources_array.length();
			sources = new User[sources_size];
			for (int i = 0;i < sources_size;i++) {
				sources[i] = new UserJSONImpl(sources_array.getJSONObject(i));
			}
			final JSONArray targets_array = json.getJSONArray("targets");
			final int targets_size = targets_array.length();
			targets = new User[targets_size];
			for (int i = 0;i < targets_size;i++) {
				targets[i] = new UserJSONImpl(targets_array.getJSONObject(i));
			}
			final JSONArray target_objects_array = json.getJSONArray("target_objects");
			final int target_objects_size = target_objects_array.length();
			targetObjects = new Status[target_objects_size];
			for (int i = 0;i < target_objects_size;i++) {
				targetObjects[i] = new StatusJSONImpl(targets_array.getJSONObject(i));
			}
		} catch (TwitterException te) {
			throw te;
		} catch (JSONException jsone) {
			throw new TwitterException(jsone);
		}
	}


	/* package */
	static ResponseList<Activity> createActivityList(HttpResponse res, Configuration conf) throws TwitterException {
		return createActivityList(res.asJSONArray(), res, conf);
	}

	/* package */
	static ResponseList<Activity> createActivityList(JSONArray list, HttpResponse res, Configuration conf)
	throws TwitterException {
		try {
			if (conf.isJSONStoreEnabled()) {
				DataObjectFactoryUtil.clearThreadLocalMap();
			}
			final int size = list.length();
			final ResponseList<Activity> users = new ResponseListImpl<Activity>(size, res);
			for (int i = 0; i < size; i++) {
				final JSONObject json = list.getJSONObject(i);
				final Activity activity = new ActivityJSONImpl(json);
				users.add(activity);
				if (conf.isJSONStoreEnabled()) {
					DataObjectFactoryUtil.registerJSONObject(activity, json);
				}
			}
			if (conf.isJSONStoreEnabled()) {
				DataObjectFactoryUtil.registerJSONObject(users, list);
			}
			return users;
		} catch (final JSONException jsone) {
			throw new TwitterException(jsone);
		} catch (final TwitterException te) {
			throw te;
		}
	}
}
