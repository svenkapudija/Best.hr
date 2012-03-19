package com.svenkapudija.best.hr.internet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SimpleHttpClient {
	/**
		Use it as:
		
		SimpleHttpClient httpClient = new SimpleHttpClient(context, url, SimpleHttpClient.HTTP_GET, false);
		httpClient.addHeader("If-None-Match", eTag);
		httpClient.performRequest();
		
		String result = httpClient.getResultAsString();
		if (result != null) {
			...
		}
	 */
	
	public static final int HTTP_GET = 1;
	public static final int HTTP_POST = 2;
	private static final String EXPIRES_FORMAT = "EEE, dd MMM yyyy HH:mm:ss";
	private static final String SSL_KEYSTORE_PASSWORD = "your_password";
	private static final int SSL_CERTIFICATE_PATH = 0; // R.raw.mystore
	
	private Context context;
	
	private HttpUriRequest request;
	private HttpResponse response;
	private InputStream stream;
	
	private List<NameValuePair> httpNameValuePairs = new ArrayList<NameValuePair>();
	
	private int type;
	private String result;
	private boolean enableSSL = false;
	private int statusCode;
	
	/**
	 * Simple HTTP client that supports GET/POST requests, expires/E-tag headers, SSL support, GZip decompression.
	 * 
	 * @param context An activity context.
	 * @param url Complete URL of the resource.
	 * @param type Select the type of request, available GET/POST.
	 * @param enableSSL <code>TRUE</code> if you want to use SSL (https). Requires certificate in .raw folder.
	 */
	public SimpleHttpClient(Context context, String url, int type, boolean enableSSL) {
		this.context = context;
		this.type = type;
		
		if (type == HTTP_GET) {
			request = new HttpGet(url);
		} else if (type == HTTP_POST) {
			request = new HttpPost(url);
		}
		
		this.enableSSL = enableSSL;
	}
	
	/**
	 * Simple HTTP client that supports GET/POST requests, expires/E-tag headers, SSL support, GZip decompression.
	 * 
	 * @param context An activity context.
	 * @param url Complete URL of the resource.
	 * @param type Select the type of request, available GET/POST.
	 */
	public SimpleHttpClient(Context context, String url, int type) {
		this.context = context;
		this.type = type;
		
		if (type == HTTP_GET) {
			request = new HttpGet(url);
		} else if (type == HTTP_POST) {
			request = new HttpPost(url);
		}
	}
	
	public HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * 
	 * @param name Header name.
	 * @param value Header value.
	 */
	public void addHeader(String name, String value) {
		if (value != null) {
			request.addHeader(name, value);
		}
	}
	
	/**
	 * Use if your request type is POST.
	 * @param nameValuePair
	 */
	public void addParameter(String name, String value) {
		BasicNameValuePair pair = new BasicNameValuePair(name, value);
		httpNameValuePairs.add(pair);
	}
	
	private Header getHeader(String name) {
		return response.getFirstHeader(name);
	}
	
	/**
	 * 
	 * @return Get "ETag" header as a String.
	 */
	public String getETag() {
		Header eTag = getHeader("ETag");
		if (eTag == null) {
			return null;
		}
		
		return eTag.getValue();
	}
	
	/**
	 * 
	 * @return Get "Expires" header as a long integer - time is in miliseconds. Parsing date format is a static constant.
	 */
	public long getExpiresInMiliseconds() {
		if (getExpires() == null)
			return 0;
		
		SimpleDateFormat format = new SimpleDateFormat(EXPIRES_FORMAT);
		try {
			Date date = format.parse(getExpires());
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			
			return c.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @return Get "Expires" header as a String.
	 */
	public String getExpires() {
		Header expiresHeader = getHeader("Expires");
		if (expiresHeader == null) {
			return null;
		}
		
		return expiresHeader.getValue();
	}
	
	/**
	 * Request (GET/POST) is performed, InputStream is created and result can be returned.
	 */
	public void performRequest() {
		addGzipEncoding();
		
		if (enableSSL) {
			addParameter("api_key", "mRgtRry4OrOsP6B6BrbOlin2rApHsTEI0u");
		}
		
		Log.d("SimpleHttpClient", "Request - " + request.getURI().toString());
		for (NameValuePair pair : httpNameValuePairs) {
			Log.d("SimpleHttpClient", "Params - " + pair.getName() + ":" + pair.getValue());
		}
		
		try {
			if ((type == HTTP_POST) && (httpNameValuePairs.size() > 0))
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(httpNameValuePairs, "UTF-8"));
			
			if (enableSSL && SSL_CERTIFICATE_PATH != 0) {
				response = new SSLClient().execute(request);
			} else {
				response = getHttpClient().execute(request);
			}
			
			statusCode = response.getStatusLine().getStatusCode();
			
			if (response.getStatusLine().getStatusCode() == 200)
				stream = response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			stream = null;
			e.printStackTrace();
		} catch (IllegalStateException e) {
			stream = null;
			e.printStackTrace();
		} catch (UnknownHostException e) {
			stream = null;
			e.printStackTrace();
		} catch (SocketException e) {
			stream = null;
			e.printStackTrace();
		} catch (IOException e) {
			stream = null;
			e.printStackTrace();
		}
	}
	
	private void addGzipEncoding() {
		request.addHeader("Accept-Encoding", "gzip");
	}
	
	/**
	 * 
	 * @return Result as a string (GZIP automatically decompressed).
	 */
	public String getResultAsString() {
		if (stream == null) return null;
		
		try {
			extractGzipIfExists();

			String line = null;
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}

			result = total.toString();
			Log.d("SimpleHttpClient", "Response " + statusCode + " - " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}

		closeInputStream();
		return this.result;
	}
	
	/**
	 * 
	 * @return Result as a Bitmap image (GZIP automatically decompressed).
	 */
	public Bitmap getResultAsBitmap() {
		if (stream == null) return null;
		
		extractGzipIfExists();
		
		Bitmap image = BitmapFactory.decodeStream(stream);
		
		closeInputStream();
		return image;
	}
	
	private void extractGzipIfExists() {
		try {
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			if ((contentEncoding != null) && (contentEncoding.getValue().equalsIgnoreCase("gzip"))) {
				stream = new GZIPInputStream(stream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeInputStream() {
		try {
			if (stream != null)
				stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class SSLClient extends DefaultHttpClient {
		 
	    public SSLClient() {
	    	
	    }
	 
	    @Override
	    protected ClientConnectionManager createClientConnectionManager() {
	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        // Register for port 443 our SSLSocketFactory with our keystore to the ConnectionManager
	        registry.register(new Scheme("https", newSslSocketFactory(), 443));
	        return new SingleClientConnManager(getParams(), registry);
	    }
	 
	    private SSLSocketFactory newSslSocketFactory() {
	        try {
	            // Get an instance of the Bouncy Castle KeyStore format
	            KeyStore trusted = KeyStore.getInstance("BKS");
	            // Get the raw resource, which contains the keystore with
	            // your trusted certificates (root and any intermediate certs)
	            InputStream in = context.getResources().openRawResource(SSL_CERTIFICATE_PATH);
	            try {
	                // Initialize the keystore with the provided trusted certificates
	                // Also provide the password of the keystore
	                trusted.load(in, SSL_KEYSTORE_PASSWORD.toCharArray());
	            } finally {
	                in.close();
	            }
	            // Pass the keystore to the SSLSocketFactory. The factory is responsible
	            // for the verification of the server certificate.
	            SSLSocketFactory sf = new SSLSocketFactory(trusted);
	            // Hostname verification from certificate
	            // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
	            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
	            return sf;
	        } catch (CertificateException e) {
	            throw new AssertionError(e);
	        } catch (NoSuchAlgorithmException e) {
	            throw new AssertionError(e);
	        } catch (UnrecoverableEntryException e) {
	            throw new AssertionError(e);
	        } catch (KeyStoreException e) {
	            throw new AssertionError(e);
	        } catch (KeyManagementException e) {
	            throw new AssertionError(e);
	        } catch (IOException e) {
	            throw new AssertionError(e);
	        }
	    }
	}
	
	public static boolean haveConnection(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
}