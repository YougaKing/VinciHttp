package youga.vincihttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import youga.vincihttp.RealCall.AsyncCall;
import youga.vincihttp.internal.Util;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/9 0009-16:29
 */

public class Dispatcher {


    private final Deque<RealCall> mRunningSyncCalls = new ArrayDeque<>();
    private final Deque<AsyncCall> mRunningASyncCalls = new ArrayDeque<>();
    private final Deque<AsyncCall> mReadyASyncCalls = new ArrayDeque<>();
    private final int mMaxRequests = 64;
    private final int mMaxRequestsPerHost = 5;
    private Runnable mIdleCallback;
    private ExecutorService mExecutorService;


    public synchronized ExecutorService executorService() {
        if (mExecutorService == null) {
            mExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return mExecutorService;
    }

    public synchronized void setIdleCallback(Runnable idleCallback) {
        this.mIdleCallback = idleCallback;
    }

    public synchronized void executed(RealCall call) {
        mRunningSyncCalls.add(call);
    }

    public synchronized void enqueue(AsyncCall call) {
        if (mRunningASyncCalls.size() < mMaxRequests && runningCallsForHost(call) < mMaxRequestsPerHost) {
            mRunningASyncCalls.add(call);
            executorService().execute(call);
        } else {
            mReadyASyncCalls.add(call);
        }

    }

    private int runningCallsForHost(AsyncCall call) {
        int result = 0;
        for (AsyncCall c : mRunningASyncCalls) {
            if (c.host().equals(call.host())) result++;
        }
        return result;
    }

    public void finished(AsyncCall call) {
        finished(mRunningASyncCalls, call, true);
    }

    private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
        int runningCallsCount;
        Runnable idleCallback;
        synchronized (this) {
            if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
            if (promoteCalls) promoteCalls();
            runningCallsCount = runningCallsCount();
            idleCallback = mIdleCallback;
        }

        if (runningCallsCount == 0 && idleCallback != null) {
            idleCallback.run();
        }
    }

    private void promoteCalls() {
        if (mRunningASyncCalls.size() >= mMaxRequests) return; // Already running max capacity.
        if (mReadyASyncCalls.isEmpty()) return; // No ready calls to promote.

        for (Iterator<AsyncCall> i = mReadyASyncCalls.iterator(); i.hasNext(); ) {
            AsyncCall call = i.next();

            if (runningCallsForHost(call) < mMaxRequestsPerHost) {
                i.remove();
                mRunningASyncCalls.add(call);
                executorService().execute(call);
            }

            if (mRunningASyncCalls.size() >= mMaxRequests) return; // Reached max capacity.
        }
    }

    public synchronized int runningCallsCount() {
        return mRunningASyncCalls.size() + mRunningSyncCalls.size();
    }
}
