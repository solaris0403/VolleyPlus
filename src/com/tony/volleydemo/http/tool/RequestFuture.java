package com.tony.volleydemo.http.tool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tony.volleydemo.http.core.Request;
import com.tony.volleydemo.http.core.Response;
import com.tony.volleydemo.http.core.VolleyError;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 10, 2015 8:47:08 AM
 */
public class RequestFuture<T> implements Future<T>, Response.Listener<T>, Response.ErrorListener {
	private Request<?> mRequest;
	private boolean mResultReceived = false;
	private T mResult;
	private VolleyError mException;

	public static <E> RequestFuture<E> newFuture() {
		return new RequestFuture<E>();
	}

	private RequestFuture() {
	}

	public void setRequest(Request<?> request) {
		mRequest = request;
	}

	@Override
	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		if (mRequest == null) {
			return false;
		}

		if (!isDone()) {
			mRequest.cancel();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		try {
			return doGet(null);
		} catch (TimeoutException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
	}

	private synchronized T doGet(Long timeoutMs) throws InterruptedException, ExecutionException, TimeoutException {
		if (mException != null) {
			throw new ExecutionException(mException);
		}

		if (mResultReceived) {
			return mResult;
		}

		if (timeoutMs == null) {
			wait(0);
		} else if (timeoutMs > 0) {
			wait(timeoutMs);
		}

		if (mException != null) {
			throw new ExecutionException(mException);
		}

		if (!mResultReceived) {
			throw new TimeoutException();
		}

		return mResult;
	}

	@Override
	public boolean isCancelled() {
		if (mRequest == null) {
			return false;
		}
		return mRequest.isCanceled();
	}

	@Override
	public synchronized boolean isDone() {
		return mResultReceived || mException != null || isCancelled();
	}

	@Override
	public synchronized void onResponse(T response) {
		mResultReceived = true;
		mResult = response;
		notifyAll();
	}

	@Override
	public synchronized void onErrorResponse(VolleyError error) {
		mException = error;
		notifyAll();
	}
}
