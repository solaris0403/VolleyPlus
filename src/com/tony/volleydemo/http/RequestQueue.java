package com.tony.volleydemo.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;

/**
 * 请求调度队列调度的线程池。 调用将排队给定的请求调度，无论从缓存或网络上的一个工作线程解析，然后输送在主线程解析后的响应。
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 4, 2015 11:56:38 AM
 */
public class RequestQueue {
	// Callback interface for completed requests.
	public static interface RequestFinishedListener<T> {
		// Called when a request has finished processing.
		public void onRequestFinished(Request<T> request);
	}

	// 用于生成单调递增的序列号的请求。
	 private AtomicInteger mSequenceGenerator = new AtomicInteger();

	// 对于已经在執行了重复的要求请求临时区域。
	private final Map<String, Queue<Request<?>>> mWaitingRequests = new HashMap<String, Queue<Request<?>>>();

	// 目前正由该请求队列处理的集合中的所有请求。请求将在这套如果在等待队列中的任何或正在被任何调度处理。
	private final Set<Request<?>> mCurrentRequests = new HashSet<Request<?>>();

	// 缓存队列分流.两个基于优先级的 Request 队列
	private final PriorityBlockingQueue<Request<?>> mCacheQueue = new PriorityBlockingQueue<Request<?>>();

	// 请求的队列被实际外出到网络。
	private final PriorityBlockingQueue<Request<?>> mNetworkQueue = new PriorityBlockingQueue<Request<?>>();

	// Number of network request dispatcher threads to start.
	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

	// 高速缓存接口，用于检索和存储的响应。
	private final Cache mCache;

	/** Network interface for performing requests. */
	 private final Network mNetwork;

	/** 响应传递机制. */
	 private final ResponseDelivery mDelivery;

	/** The network dispatchers. */
	 private NetworkDispatcher[] mDispatchers;

	/** The cache dispatcher. */
	 private CacheDispatcher mCacheDispatcher;

	private List<RequestFinishedListener> mFinishedListeners = new ArrayList<RequestFinishedListener>();

	/**
	 * Creates the worker pool. Processing will not begin until {@link #start()}
	 * is called.
	 *
	 * @param cache
	 *            A Cache to use for persisting responses to disk
	 * @param network
	 *            A Network interface for performing HTTP requests
	 * @param threadPoolSize
	 *            Number of network dispatcher threads to create
	 * @param delivery
	 *            A ResponseDelivery interface for posting responses and errors
	 */
	public RequestQueue(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
		mCache = cache;
		mNetwork = network;
		mDispatchers = new NetworkDispatcher[threadPoolSize];
		mDelivery = delivery;
	}

	/**
	 * Creates the worker pool. Processing will not begin until {@link #start()}
	 * is called.
	 *
	 * @param cache
	 *            A Cache to use for persisting responses to disk
	 * @param network
	 *            A Network interface for performing HTTP requests
	 * @param threadPoolSize
	 *            Number of network dispatcher threads to create
	 */
	public RequestQueue(Cache cache, Network network, int threadPoolSize) {
		this(cache, network, threadPoolSize, new ExecutorDelivery(new Handler(Looper.getMainLooper())));
	}

	/**
	 * Creates the worker pool. Processing will not begin until {@link #start()}
	 * is called.
	 *
	 * @param cache
	 *            A Cache to use for persisting responses to disk
	 * @param network
	 *            A Network interface for performing HTTP requests
	 * @param threadPoolSize
	 *            Number of network dispatcher threads to create
	 */
	public RequestQueue(Cache cache, Network network, int threadPoolSize) {
		this(cache, network, threadPoolSize, new ExecutorDelivery(new Handler(Looper.getMainLooper())));
	}

	/**
	 * Creates the worker pool. Processing will not begin until {@link #start()}
	 * is called.
	 *
	 * @param cache
	 *            A Cache to use for persisting responses to disk
	 * @param network
	 *            A Network interface for performing HTTP requests
	 */
	public RequestQueue(Cache cache, Network network) {
		this(cache, network, DEFAULT_NETWORK_THREAD_POOL_SIZE);
	}

	// Starts the dispatchers in this queue.
	public void start() {
		stop();// Make sure any currently running dispatchers are stopped.
		// Create the cache dispatcher and start it.
		mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
		mCacheDispatcher.start();
		// Create network dispatchers (and corresponding threads) up to the pool
		// size.
		for (int i = 0; i < mDispatchers.length; i++) {
			NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork, mCache, mDelivery);
			mDispatchers[i] = networkDispatcher;
			networkDispatcher.start();
		}
	}

	/**
	 * Stops the cache and network dispatchers.
	 */
	public void stop() {
		if (mCacheDispatcher != null) {
			mCacheDispatcher.quit();
		}
		for (int i = 0; i < mDispatchers.length; i++) {
			if (mDispatchers[i] != null) {
				mDispatchers[i].quit();
			}
		}
	}

	/**
	 * Gets a sequence number.
	 */
	public int getSequenceNumber() {
		return mSequenceGenerator.incrementAndGet();
	}

	/**
	 * Gets the {@link Cache} instance being used.
	 */
	public Cache getCache() {
		return mCache;
	}

	/**
	 * A simple predicate or filter interface for Requests, for use by
	 * {@link RequestQueue#cancelAll(RequestFilter)}.
	 */
	public interface RequestFilter {
		public boolean apply(Request<?> request);
	}

	/**
	 * Cancels all requests in this queue for which the given filter applies.
	 * 
	 * @param filter
	 *            The filtering function to use
	 */
	public void cancelAll(RequestFilter filter) {
		synchronized (mCurrentRequests) {
			for (Request<?> request : mCurrentRequests) {
				if (filter.apply(request)) {
					request.cancel();
				}
			}
		}
	}

	/**
	 * Cancels all requests in this queue with the given tag. Tag must be
	 * non-null and equality is by identity.
	 */
	public void cancelAll(final Object tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Cannot cancelAll with a null tag");
		}
		cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return request.getTag() == tag;
			}
		});
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * 
	 * @param request
	 *            The request to service
	 * @return The passed-in request
	 */
	public <T> Request<T> add(Request<T> request) {
		// Tag the request as belonging to this queue and add it to the set of
		// current requests.
		request.setRequestQueue(this);
		synchronized (mCurrentRequests) {
			mCurrentRequests.add(request);
		}

		// Process requests in the order they are added.
		request.setSequence(getSequenceNumber());
		request.addMarker("add-to-queue");

		// If the request is uncacheable, skip the cache queue and go straight
		// to the network.
		if (!request.shouldCache()) {
			mNetworkQueue.add(request);
			return request;
		}

		// Insert request into stage if there's already a request with the same
		// cache key in flight.
		synchronized (mWaitingRequests) {
			String cacheKey = request.getCacheKey();
			if (mWaitingRequests.containsKey(cacheKey)) {
				// There is already a request in flight. Queue up.
				Queue<Request<?>> stagedRequests = mWaitingRequests.get(cacheKey);
				if (stagedRequests == null) {
					stagedRequests = new LinkedList<Request<?>>();
				}
				stagedRequests.add(request);
				mWaitingRequests.put(cacheKey, stagedRequests);
				if (VolleyLog.DEBUG) {
					VolleyLog.v("Request for cacheKey=%s is in flight, putting on hold.", cacheKey);
				}
			} else {
				// Insert 'null' queue for this cacheKey, indicating there is
				// now a request in
				// flight.
				mWaitingRequests.put(cacheKey, null);
				mCacheQueue.add(request);
			}
			return request;
		}
	}

	/**
	 * Called from {@link Request#finish(String)}, indicating that processing of
	 * the given request has finished.
	 *
	 * <p>
	 * Releases waiting requests for <code>request.getCacheKey()</code> if
	 * <code>request.shouldCache()</code>.
	 * </p>
	 */
	<T> void finish(Request<T> request) {
		// Remove from the set of requests currently being processed.
		synchronized (mCurrentRequests) {
			mCurrentRequests.remove(request);
		}
		synchronized (mFinishedListeners) {
			for (RequestFinishedListener<T> listener : mFinishedListeners) {
				listener.onRequestFinished(request);
			}
		}

		if (request.shouldCache()) {
			synchronized (mWaitingRequests) {
				String cacheKey = request.getCacheKey();
				Queue<Request<?>> waitingRequests = mWaitingRequests.remove(cacheKey);
				if (waitingRequests != null) {
					if (VolleyLog.DEBUG) {
						VolleyLog.v("Releasing %d waiting requests for cacheKey=%s.", waitingRequests.size(), cacheKey);
					}
					// Process all queued up requests. They won't be considered
					// as in flight, but
					// that's not a problem as the cache has been primed by
					// 'request'.
					mCacheQueue.addAll(waitingRequests);
				}
			}
		}
	}

	public <T> void addRequestFinishedListener(RequestFinishedListener<T> listener) {
		synchronized (mFinishedListeners) {
			mFinishedListeners.add(listener);
		}
	}

	/**
	 * Remove a RequestFinishedListener. Has no effect if listener was not
	 * previously added.
	 */
	public <T> void removeRequestFinishedListener(RequestFinishedListener<T> listener) {
		synchronized (mFinishedListeners) {
			mFinishedListeners.remove(listener);
		}
	}
}
