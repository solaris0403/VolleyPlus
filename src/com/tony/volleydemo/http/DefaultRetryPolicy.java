package com.tony.volleydemo.http;

/**
 * 默认重试政策的要求。
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 4, 2015 2:02:10 PM
 */
public class DefaultRetryPolicy implements RetryPolicy {
	// Default current timeout in milliseconds
	private int mCurrentTimeoutMs;
	// The current retry count
	private int mCurrentRetryCount;

	/** The maximum number of attempts. */
	private final int mMaxNumRetries;
	/** The backoff multiplier for the policy . 对于政策的乘数退避 */
	private final float mBackoffMultiplier;
	/** The default socket timeout in milliseconds */
	public static final int DEFAULT_TIMEOUT_MS = 2500;

	/** The default number of retries */
	public static final int DEFAULT_MAX_RETRIES = 1;

	/** The default backoff multiplier */
	public static final float DEFAULT_BACKOFF_MULT = 1f;

	/**
	 * Constructs a new retry policy using the default timeouts.
	 */
	public DefaultRetryPolicy() {
		this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
	}

	/**
	 * Constructs a new retry policy.
	 * 
	 * @param initialTimeoutMs
	 *            The initial timeout for the policy.
	 * @param maxNumRetries
	 *            The maximum number of retries.
	 * @param backoffMultiplier
	 *            Backoff multiplier for the policy.
	 */
	public DefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
		mCurrentTimeoutMs = initialTimeoutMs;
		mMaxNumRetries = maxNumRetries;
		mBackoffMultiplier = backoffMultiplier;
	}

	@Override
	public int getCurrentTimeout() {
		return mCurrentTimeoutMs;
	}

	@Override
	public int getCurrentRetryCount() {
		return mCurrentRetryCount;
	}

	/**
	 * Returns the backoff multiplier for the policy.
	 */
	public float getBackoffMultiplier() {
		return mBackoffMultiplier;
	}

	@Override
	public void retry(VolleyError error) throws VolleyError {
		mCurrentRetryCount++;
		mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
		if (!hasAttemptRemaining()) {
			throw error;
		}
	}

	/**
	 * Returns true if this policy has attempts remaining, false otherwise.
	 */
	protected boolean hasAttemptRemaining() {
		return mCurrentRetryCount <= mMaxNumRetries;
	}
}
