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

package twitter4j.conf;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import twitter4j.auth.AuthorizationConfiguration;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpClientWrapperConfiguration;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Configuration extends HttpClientConfiguration, HttpClientWrapperConfiguration,
		AuthorizationConfiguration, Serializable {

	int getAsyncNumThreads();

	String getClientName();

	String getClientURL();

	String getClientVersion();

	String getDispatcherImpl();

	@Override
	int getHttpConnectionTimeout();

	// methods for HttpClientConfiguration

	@Override
	int getHttpDefaultMaxPerRoute();

	@Override
	int getHttpMaxTotalConnections();

	@Override
	String getHttpProxyHost();

	@Override
	String getHttpProxyPassword();

	@Override
	int getHttpProxyPort();

	@Override
	String getHttpProxyUser();

	@Override
	int getHttpReadTimeout();

	@Override
	int getHttpRetryCount();

	@Override
	int getHttpRetryIntervalSeconds();

	int getHttpStreamingReadTimeout();

	String getMediaProvider();

	// oauth related setter/getters

	String getMediaProviderAPIKey();

	Properties getMediaProviderParameters();

	@Override
	String getOAuthAccessToken();

	@Override
	String getOAuthAccessTokenSecret();

	String getOAuthAccessTokenURL();

	String getOAuthAuthenticationURL();

	String getOAuthAuthorizationURL();

	String getOAuthBaseURL();

	@Override
	String getOAuthConsumerKey();

	@Override
	String getOAuthConsumerSecret();

	String getOAuthRequestTokenURL();

	@Override
	String getPassword();

	@Override
	Map<String, String> getRequestHeaders();

	String getRestBaseURL();

	String getSearchBaseURL();

	String getSigningOAuthAccessTokenURL();

	String getSigningOAuthAuthenticationURL();

	String getSigningOAuthAuthorizationURL();

	String getSigningOAuthBaseURL();

	String getSigningOAuthRequestTokenURL();

	String getSigningRestBaseURL();

	String getSiteStreamBaseURL();

	String getStreamBaseURL();

	String getUploadBaseURL();

	@Override
	String getUser();

	String getUserAgent();

	String getUserStreamBaseURL();

	boolean isDebugEnabled();

	boolean isIncludeEntitiesEnabled();

	boolean isIncludeRTsEnabled();

	boolean isJSONStoreEnabled();

	@Override
	boolean isSSLErrorIgnored();

	boolean isUserStreamRepliesAllEnabled();
}
