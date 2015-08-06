package com.tony.volleydemo.http;
/**
 * Retry policy for a request.
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 4, 2015 1:57:06 PM
 */
public interface RetryPolicy {
	 /**
     * Returns the current timeout (used for logging).
     */
    public int getCurrentTimeout();

    /**
     * Returns the current retry count (used for logging).
     */
    public int getCurrentRetryCount();
    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     * @throws VolleyError In the event that the retry could not be performed (for example if we
     * ran out of attempts), the passed in error is thrown.
     */
    public void retry(VolleyError error) throws VolleyError;
}
