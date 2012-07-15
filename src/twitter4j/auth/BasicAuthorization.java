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

package twitter4j.auth;

import java.io.Serializable;

import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpRequest;

/**
 * An authentication implementation implements Basic authentication
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class BasicAuthorization implements Authorization, Serializable {

	private String userId;

	private String password;
	private String basic;
	private static final long serialVersionUID = -5861104407848415060L;

	public BasicAuthorization(String userId, String password) {
		this.userId = userId;
		this.password = password;
		basic = encodeBasicAuthenticationString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BasicAuthorization)) return false;

		final BasicAuthorization that = (BasicAuthorization) o;

		return basic.equals(that.basic);

	}

	@Override
	public String getAuthorizationHeader(HttpRequest req) {
		return basic;
	}

	public String getPassword() {
		return password;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		return basic.hashCode();
	}

	/**
	 * #{inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "BasicAuthorization{" + "userId='" + userId + '\'' + ", password='**********'\'" + '}';
	}

	private String encodeBasicAuthenticationString() {
		if (userId != null && password != null)
			return "Basic " + BASE64Encoder.encode((userId + ":" + password).getBytes());
		return null;
	}

}
