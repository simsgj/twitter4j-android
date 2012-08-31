/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.internal.json;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getDate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.z_T4JInternalParseUtil;

/**
 * A data class representing Trends.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.2
 */
/* package */final class TrendsJSONImpl extends TwitterResponseImpl implements Trends {
	private Date asOf;
	private Date trendAt;
	private Trend[] trends;
	private Location location;

	/* package */TrendsJSONImpl(Date asOf, Location location, Date trendAt, Trend[] trends) {
		this.asOf = asOf;
		this.location = location;
		this.trendAt = trendAt;
		this.trends = trends;
	}

	TrendsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
		super(res);
		init(res.asString(), conf.isJSONStoreEnabled());
		if (conf.isJSONStoreEnabled()) {
			DataObjectFactoryUtil.clearThreadLocalMap();
			DataObjectFactoryUtil.registerJSONObject(this, res.asString());
		}
	}

	TrendsJSONImpl(String jsonStr) throws TwitterException {
		this(jsonStr, false);
	}

	TrendsJSONImpl(String jsonStr, boolean storeJSON) throws TwitterException {
		init(jsonStr, storeJSON);
	}

	@Override
	public int compareTo(Trends that) {
		return trendAt.compareTo(that.getTrendAt());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Trends)) return false;

		final Trends trends1 = (Trends) o;

		if (asOf != null ? !asOf.equals(trends1.getAsOf()) : trends1.getAsOf() != null) return false;
		if (trendAt != null ? !trendAt.equals(trends1.getTrendAt()) : trends1.getTrendAt() != null) return false;
		if (!Arrays.equals(trends, trends1.getTrends())) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getAsOf() {
		return asOf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Location getLocation() {
		return location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getTrendAt() {
		return trendAt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trend[] getTrends() {
		return trends;
	}

	@Override
	public int hashCode() {
		int result = asOf != null ? asOf.hashCode() : 0;
		result = 31 * result + (trendAt != null ? trendAt.hashCode() : 0);
		result = 31 * result + (trends != null ? Arrays.hashCode(trends) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "TrendsJSONImpl{" + "asOf=" + asOf + ", trendAt=" + trendAt + ", trends="
				+ (trends == null ? null : Arrays.asList(trends)) + '}';
	}

	void init(String jsonStr, boolean storeJSON) throws TwitterException {
		try {
			JSONObject json;
			if (jsonStr.startsWith("[")) {
				final JSONArray array = new JSONArray(jsonStr);
				if (array.length() > 0) {
					json = array.getJSONObject(0);
				} else
					throw new TwitterException("No trends found on the specified woeid");
			} else {
				json = new JSONObject(jsonStr);
			}
			asOf = z_T4JInternalParseUtil.parseTrendsDate(json.getString("as_of"));
			location = extractLocation(json, storeJSON);
			final JSONArray array = json.getJSONArray("trends");
			trendAt = asOf;
			trends = jsonArrayToTrendArray(array, storeJSON);
		} catch (final JSONException jsone) {
			throw new TwitterException(jsone.getMessage(), jsone);
		}
	}

	private static Location extractLocation(JSONObject json, boolean storeJSON) throws TwitterException {
		if (json.isNull("locations")) return null;
		ResponseList<Location> locations;
		try {
			locations = LocationJSONImpl.createLocationList(json.getJSONArray("locations"), storeJSON);
		} catch (final JSONException e) {
			throw new AssertionError("locations can't be null");
		}
		Location location;
		if (0 != locations.size()) {
			location = locations.get(0);
		} else {
			location = null;
		}
		return location;
	}

	private static Trend[] jsonArrayToTrendArray(JSONArray array, boolean storeJSON) throws JSONException {
		final Trend[] trends = new Trend[array.length()];
		for (int i = 0; i < array.length(); i++) {
			final JSONObject trend = array.getJSONObject(i);
			trends[i] = new TrendJSONImpl(trend, storeJSON);
		}
		return trends;
	}

	/* package */
	static ResponseList<Trends> createTrendsList(HttpResponse res, boolean storeJSON) throws TwitterException {
		final JSONObject json = res.asJSONObject();
		ResponseList<Trends> trends;
		try {
			final Date asOf = z_T4JInternalParseUtil.parseTrendsDate(json.getString("as_of"));
			final JSONObject trendsJson = json.getJSONObject("trends");
			final Location location = extractLocation(json, storeJSON);
			trends = new ResponseListImpl<Trends>(trendsJson.length(), res);
			@SuppressWarnings("unchecked")
			final Iterator<String> ite = trendsJson.keys();
			while (ite.hasNext()) {
				final String key = ite.next();
				final JSONArray array = trendsJson.getJSONArray(key);
				final Trend[] trendsArray = jsonArrayToTrendArray(array, storeJSON);
				if (key.length() == 19) {
					// current trends
					trends.add(new TrendsJSONImpl(asOf, location, getDate(key, "yyyy-MM-dd HH:mm:ss"), trendsArray));
				} else if (key.length() == 16) {
					// daily trends
					trends.add(new TrendsJSONImpl(asOf, location, getDate(key, "yyyy-MM-dd HH:mm"), trendsArray));
				} else if (key.length() == 10) {
					// weekly trends
					trends.add(new TrendsJSONImpl(asOf, location, getDate(key, "yyyy-MM-dd"), trendsArray));
				}
			}
			Collections.sort(trends);
			return trends;
		} catch (final JSONException jsone) {
			throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
		}
	}
}
