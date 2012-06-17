package twitter4j.internal.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.net.SSLCertificateSocketFactory;

class IgnoreErrorSSLSocketFactory extends SSLCertificateSocketFactory {

	private SSLContext sslContext = SSLContext.getInstance("TLS");

	public IgnoreErrorSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException {
		super(0);

		TrustManager tm = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};

		sslContext.init(null, new TrustManager[] { tm }, null);

	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
			UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	}
}
