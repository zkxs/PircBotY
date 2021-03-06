package net.ae97.pircboty.managers;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import net.ae97.pircboty.PircBotY;
import net.ae97.pircboty.api.Event;
import net.ae97.pircboty.api.Listener;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ThreadedListenerManager implements ListenerManager {

    private static final AtomicInteger MANAGER_COUNT = new AtomicInteger();
    private final int managerNumber;
    private final ExecutorService pool;
    private final Set<Listener> listeners = Collections.synchronizedSet(new HashSet<Listener>());
    private final AtomicLong currentId = new AtomicLong();
    private final Multimap<PircBotY, ManagedFutureTask> runningListeners = MultimapBuilder.hashKeys().linkedListValues().build();

    public ThreadedListenerManager() {
        managerNumber = MANAGER_COUNT.getAndIncrement();
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("listenerPool" + managerNumber + "-thread%d")
                .daemon(true)
                .build();
        ThreadPoolExecutor defaultPool = (ThreadPoolExecutor) Executors.newCachedThreadPool(factory);
        defaultPool.allowCoreThreadTimeOut(true);
        this.pool = defaultPool;
    }

    public ThreadedListenerManager(ExecutorService pool) {
        managerNumber = MANAGER_COUNT.getAndIncrement();
        this.pool = pool;
    }

    @Override
    public boolean addListener(Listener listener) {
        synchronized (listeners) {
            return listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(Listener listener) {
        synchronized (listeners) {
            return listeners.remove(listener);
        }
    }

    @Override
    public Set<Listener> getListeners() {
        synchronized (listeners) {
            return new HashSet<>(listeners);
        }
    }

    @Override
    public boolean listenerExists(Listener listener) {
        return getListeners().contains(listener);
    }

    @Override
    public void dispatchEvent(Event event) {
        synchronized (listeners) {
            listeners.stream().forEach((curListener) -> {
                submitEvent(pool, curListener, event);
            });
        }
    }

    protected void submitEvent(ExecutorService es, final Listener listener, final Event event) {
        es.execute(new ManagedFutureTask(listener, event, () -> {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                PircBotY.getLogger().log(Level.SEVERE, "Exception encountered when executing event " + event + " on listener " + listener, e);
            }
            return null;
        }));
    }

    @Override
    public void setCurrentId(long currentId) {
        this.currentId.set(currentId);
    }

    @Override
    public long getCurrentId() {
        return currentId.get();
    }

    @Override
    public long incrementCurrentId() {
        return currentId.getAndIncrement();
    }

    public ExecutorService shutdown() {
        pool.shutdown();
        return pool;
    }

    public int getManagerNumber() {
        return managerNumber;
    }

    @Override
    public void shutdown(PircBotY bot) {
        synchronized (runningListeners) {
            for (ManagedFutureTask curFuture : runningListeners.get(bot)) {
                try {
                    PircBotY.getLogger().log(Level.FINE, "Waiting for listener " + curFuture.getListener() + " to execute event " + curFuture.getEvent());
                    curFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Cannot shutdown listener " + curFuture.getListener() + " executing event " + curFuture.getEvent(), e);
                }
            }
        }
    }

    private class ManagedFutureTask extends FutureTask<Void> {

        private final Listener listener;
        private final Event event;

        public ManagedFutureTask(Listener listener, Event event, Callable<Void> callable) {
            super(callable);
            this.listener = listener;
            this.event = event;
            if (event.getBot() != null) {
                synchronized (runningListeners) {
                    runningListeners.put(event.getBot(), this);
                }
            }
        }

        @Override
        protected void done() {
            if (event.getBot() != null) {
                synchronized (runningListeners) {
                    runningListeners.remove(event.getBot(), this);
                }
            }
        }

        public Listener getListener() {
            return listener;
        }

        public Event getEvent() {
            return event;
        }
    }
}
