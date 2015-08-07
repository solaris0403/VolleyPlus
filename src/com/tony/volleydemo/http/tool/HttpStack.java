package com.tony.volleydemo.http.tool;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.tony.volleydemo.http.core.AuthFailureError;
import com.tony.volleydemo.http.core.Request;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 7, 2015 2:20:56 PM
 */
public interface HttpStack {
	/**
	 * Performs an HTTP request with the given parameters.
	 *
	 * <p>
	 * A GET request is sent if request.getPostBody() == null. A POST request is
	 * sent otherwise, and the Content-Type header is set to
	 * request.getPostBodyContentType().
	 * </p>
	 *
	 * @param request
	 *            the request to perform
	 * @param additionalHeaders
	 *            additional headers to be sent together with
	 *            {@link Request#getHeaders()}
	 * @return the HTTP response
	 */
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError;
}
