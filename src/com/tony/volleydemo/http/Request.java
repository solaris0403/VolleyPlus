package com.tony.volleydemo.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tony.volleydemo.http.VolleyLog.MarkerLog;

/**
 * 所有请求的抽象类 因为有多种请求 所以使用泛型
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Jul 27, 2015 2:50:58 PM
 */
public abstract class Request<T> implements Comparable<Request<T>> {

	// 默认的参数编码
	private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

	// 支持的请求方法
	// TODO why to use interface
	public interface Method {
		int DEPRECATED_GET_OR_POST = -1;
		int GET = 0;
		int POST = 1;
		int PUT = 2;
		int DELETE = 3;
		int HEAD = 4;
		int OPTIONS = 5;
		int TRACE = 6;
		int PATCH = 7;
	}

	// 事件日志跟踪该请求的生命周期;调试
	private final MarkerLog mEventLog = MarkerLog.ENABLED ? new MarkerLog() : null;

	// 請求的方法
	private int mMethod;

	// 請求url
	private String mUrl;

	// 一個請求傳遞的TAG
	private int mDefaultTrafficStatsTag;

	/** Listener interface for errors. */
	private final Response.ErrorListener mErrorListener;

	// 一個記錄該請求的順序號，FIFO
	private Integer mSequence;

	/** 该请求相关联的请求队列. */
	private RequestQueue mRequestQueue;

	// 該請求結果是否需要緩存 默認需要
	private boolean mShouldCache = true;

	// 該請求是否被取消 默認沒有
	private boolean mCanceled = false;

	// 是否有响应已交付该请求呢。
	private boolean mResponceDelivered = false;

	// 重试这一请求。
	private RetryPolicy mRetryPolicy;

	// 当一个请求可以从高速缓存检索而必须从刷新网络时，高速缓存条目将被存储在这里，
	// 以便在发生一个“Not Modified”的响应，我们可以肯定它并没有被驱逐出缓存。
	private Cache.Entry mCacheEntry = null;

	// 一个不透明的令牌标记这个请求;用于批量取消
	private Object mTag;

	// 创建具有给定的URL和错误监听一个新的请求。注意
	// 未提供此处作为输送响应的正常响应听者
	// 由子，谁拥有更好的主意如何提供一个提供
	// 已解析的响应。
	@Deprecated
	public Request(String url, Response.ErrorListener listener) {
		this(Method.DEPRECATED_GET_OR_POST, url, listener);
	}

	// 方法調用
	public Request(int method, String url, Response.ErrorListener listener) {
		mMethod = method;
		mUrl = url;
		mErrorListener = listener;
		setRetryPolicy(new DefaultRetryPolicy());
		// 默認的一個tag 通過url來標識
		mDefaultTrafficStatsTag = findDefaultTrafficStatsTag(url);
	}

	// 返回當前請求的方法
	public int getMethod() {
		return mMethod;
	}

	// Set a tag on this request. Can be used to cancel all requests with this
	// tag by {@link RequestQueue#cancelAll(Object)}.
	public Request<?> setTag(Object tag) {
		mTag = tag;
		return this;
	}

	// Returns this request's tag.
	public Object getTag() {
		return mTag;
	}

	/**
	 * @return this request's {@link com.android.volley.Response.ErrorListener}.
	 */
	public Response.ErrorListener getErrorListener() {
		return mErrorListener;
	}

	// A tag for use with {@link TrafficStats#setThreadStatsTag(int)}
	public int getTrafficStatsTag() {
		return mDefaultTrafficStatsTag;
	}

	// The hashcode of the URL's host component, or 0 if there is none.
	public static int findDefaultTrafficStatsTag(String url) {
		if (!TextUtils.isEmpty(url)) {
			Uri uri = Uri.parse(url);
			if (uri != null) {
				String host = uri.getHost();
				if (host != null) {
					return host.hashCode();
				}
			}
		}
		return 0;
	}

	/**
	 * Sets the retry policy for this request.
	 *
	 * @return This Request object to allow for chaining.
	 */
	public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
		mRetryPolicy = retryPolicy;
		return this;
	}

	// Adds an event to this request's event log; for debugging.
	public void addMarker(String tag) {
		if (MarkerLog.ENABLED) {
			mEventLog.add(tag, Thread.currentThread().getId());
		}
	}

	/**
	 * Notifies the request queue that this request has finished (successfully
	 * or with error).
	 *
	 * <p>
	 * Also dumps all events from this request's event log; for debugging.
	 * </p>
	 */
	void finish(final String tag) {
		if (mRequestQueue != null) {
			mRequestQueue.finish(this);
		}
		if (MarkerLog.ENABLED) {
			final long threadId = Thread.currentThread().getId();
			if (Looper.myLooper() != Looper.getMainLooper()) {
				// If we finish marking off of the main thread, we need to
				// actually do it on the main thread to ensure correct ordering.
				Handler mainThread = new Handler(Looper.getMainLooper());
				mainThread.post(new Runnable() {
					@Override
					public void run() {
						mEventLog.add(tag, threadId);
						mEventLog.finish(this.toString());
					}
				});
				return;
			}
			mEventLog.add(tag, threadId);
			mEventLog.finish(this.toString());
		}
	}

	/**
	 * Associates this request with the given queue. The request queue will be
	 * notified when this request has finished.
	 *
	 * @return This Request object to allow for chaining.
	 */
	public Request<?> setRequestQueue(RequestQueue requestQueue) {
		mRequestQueue = requestQueue;
		return this;
	}

	// Sets the sequence number of this request
	public final Request<?> setSequence(int sequence) {
		mSequence = sequence;
		return this;
	}

	// Returns the sequence number of this request.
	public int getSequence() {
		if (mSequence == null) {
			throw new IllegalStateException("getSequence called before setSequence");
		}
		return mSequence;
	}

	// Return the URL of this request
	public String getUrl() {
		return mUrl;
	}

	// return this cache key, default this is URL
	public String getCacheKey() {
		return getUrl();
	}

	/**
	 * Annotates this request with an entry retrieved for it from cache. Used
	 * for cache coherency support.
	 *
	 * @return This Request object to allow for chaining.
	 */
	public Request<?> setCacheEntry(Cache.Entry entry) {
		mCacheEntry = entry;
		return this;
	}

	/**
	 * Returns the annotated cache entry, or null if there isn't one.
	 */
	public Cache.Entry getCacheEntry() {
		return mCacheEntry;
	}

	// Mark this request as canceled. No callback will be delivered.
	public void cancel() {
		mCanceled = true;
	}

	// Returns true if this request has been canceled.
	public boolean isCanceled() {
		return mCanceled;
	}

	/**
	 * Returns a list of extra HTTP headers to go along with this request. Can
	 * throw {@link AuthFailureError} as authentication may be required to
	 * provide these values.
	 * 
	 * @throws AuthFailureError
	 *             In the event of auth failure
	 */
	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	/**
	 * Returns a Map of POST parameters to be used for this request, or null if
	 * a simple GET should be used. Can throw {@link AuthFailureError} as
	 * authentication may be required to provide these values.
	 *
	 * <p>
	 * Note that only one of getPostParams() and getPostBody() can return a
	 * non-null value.
	 * </p>
	 * 
	 * @throws AuthFailureError
	 *             In the event of auth failure
	 *
	 * @deprecated Use {@link #getParams()} instead.
	 */
	@Deprecated
	protected Map<String, String> getPostParams() throws AuthFailureError {
		return getParams();
	}

	/**
	 * Returns which encoding should be used when converting POST parameters
	 * returned by {@link #getPostParams()} into a raw POST body.
	 *
	 * <p>
	 * This controls both encodings:
	 * <ol>
	 * <li>The string encoding used when converting parameter names and values
	 * into bytes prior to URL encoding them.</li>
	 * <li>The string encoding used when converting the URL encoded parameters
	 * into a raw byte array.</li>
	 * </ol>
	 *
	 * @deprecated Use {@link #getParamsEncoding()} instead.
	 */
	@Deprecated
	protected String getPostParamsEncoding() {
		return getParamsEncoding();
	}

	/**
	 * @deprecated Use {@link #getBodyContentType()} instead.
	 */
	@Deprecated
	public String getPostBodyContentType() {
		return getBodyContentType();
	}

	/**
	 * Returns the raw POST body to be sent.
	 *
	 * @throws AuthFailureError
	 *             In the event of auth failure
	 *
	 * @deprecated Use {@link #getBody()} instead.
	 */
	@Deprecated
	public byte[] getPostBody() throws AuthFailureError {
		// Note: For compatibility with legacy clients of volley,
		// thisimplementation must remain
		// here instead of simply calling the getBody() function because
		// thisfunction must
		// call getPostParams() and getPostParamsEncoding() since legacy
		// clientswould have
		// overridden these two member functions for POST requests.
		Map<String, String> postParams = getPostParams();
		if (postParams != null && postParams.size() > 0) {
			return encodeParameters(postParams, getPostParamsEncoding());
		}
		return null;
	}

	/**
	 * Returns a Map of parameters to be used for a POST or PUT request. Can
	 * throw {@link AuthFailureError} as authentication may be required to
	 * provide these values.
	 *
	 * <p>
	 * Note that you can directly override {@link #getBody()} for custom data.
	 * </p>
	 *
	 * @throws AuthFailureError
	 *             in the event of auth failure
	 */
	protected Map<String, String> getParams() throws AuthFailureError {
		return null;
	}

	/**
	 * Returns which encoding should be used when converting POST or PUT
	 * parameters returned by {@link #getParams()} into a raw POST or PUT body.
	 *
	 * <p>
	 * This controls both encodings:
	 * <ol>
	 * <li>The string encoding used when converting parameter names and values
	 * into bytes prior to URL encoding them.</li>
	 * <li>The string encoding used when converting the URL encoded parameters
	 * into a raw byte array.</li>
	 * </ol>
	 */
	protected String getParamsEncoding() {
		return DEFAULT_PARAMS_ENCODING;
	}

	// Returns the content type of the POST or PUT body.
	public String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
	}

	/**
	 * Returns the raw POST or PUT body to be sent.
	 *
	 * <p>
	 * By default, the body consists of the request parameters in
	 * application/x-www-form-urlencoded format. When overriding this method,
	 * consider overriding {@link #getBodyContentType()} as well to match the
	 * new body format.
	 *
	 * @throws AuthFailureError
	 *             in the event of auth failure
	 */
	public byte[] getBody() throws AuthFailureError {
		Map<String, String> params = getParams();
		if (params != null && params.size() > 0) {
			return encodeParameters(params, getParamsEncoding());
		}
		return null;
	}

	/**
	 * Converts <code>params</code> into an application/x-www-form-urlencoded
	 * encoded string.
	 */
	private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	// Set whether or not responses to this request should be cached.
	public final Request<?> setShouldCache(boolean shouldCache) {
		mShouldCache = shouldCache;
		return this;
	}

	// Returns true if responses to this request should be cached.
	public boolean shouldCache() {
		return mShouldCache;
	}

	// Priority values. Requests will be processed from higher priorities to
	// lower priorities, in FIFO order.
	public enum Priority {
		LOW, NORMAL, HIGH, IMMEDIATE
	}

	// Returns the {@link Priority} of this request; {@link Priority#NORMAL} by
	// default.
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	/**
	 * Returns the socket timeout in milliseconds per retry attempt. (This value
	 * can be changed per retry attempt if a backoff is specified via
	 * backoffTimeout()). If there are no retry attempts remaining, this will
	 * cause delivery of a {@link TimeoutError} error.
	 */
	public final int getTimeoutMs() {
		return mRetryPolicy.getCurrentTimeout();
	}

	/**
	 * Returns the retry policy that should be used for this request.
	 */
	public RetryPolicy getRetryPolicy() {
		return mRetryPolicy;
	}

	/**
	 * Mark this request as having a response delivered on it. This can be used
	 * later in the request's lifetime for suppressing identical responses.
	 */
	public void markDelivered() {
		mResponceDelivered = true;
	}

	/**
	 * Returns true if this request has had a response delivered for it.
	 */
	public boolean hasHadResponseDelivered() {
		return mResponceDelivered;
	}

	/**
	 * Subclasses must implement this to parse the raw network response and
	 * return an appropriate response type. This method will be called from a
	 * worker thread. The response will not be delivered if you return null.
	 * 
	 * @param response
	 *            Response from the network
	 * @return The parsed response, or null in the case of an error
	 */
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	/**
	 * Subclasses can override this method to parse 'networkError' and return a
	 * more specific error.
	 *
	 * <p>
	 * The default implementation just returns the passed 'networkError'.
	 * </p>
	 *
	 * @param volleyError
	 *            the error retrieved from the network
	 * @return an NetworkError augmented with additional information
	 */
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		return volleyError;
	}

	/**
	 * Subclasses must implement this to perform delivery of the parsed response
	 * to their listeners. The given response is guaranteed to be non-null;
	 * responses that fail to parse are not delivered.
	 * 
	 * @param response
	 *            The parsed response returned by
	 *            {@link #parseNetworkResponse(NetworkResponse)}
	 */
	abstract protected void deliverResponse(T response);

	/**
	 * Delivers error message to the ErrorListener that the Request was
	 * initialized with.
	 *
	 * @param error
	 *            Error details
	 */
	public void deliverError(VolleyError error) {
		if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}
	}

	/**
	 * Our comparator sorts from high to low priority, and secondarily by
	 * sequence number to provide FIFO ordering.
	 */
	@Override
	public int compareTo(Request<T> another) {
		Priority left = this.getPriority();
		Priority right = another.getPriority();
		return left == right ? this.mSequence - another.mSequence : right.ordinal() - left.ordinal();
	}

	@Override
	public String toString() {
		String trafficStatsTag = "0x" + Integer.toHexString(getTrafficStatsTag());
		return (mCanceled ? "[X] " : "[ ] ") + getUrl() + " " + trafficStatsTag + " " + getPriority() + " " + mSequence;
	}
}
