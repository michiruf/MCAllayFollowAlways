package de.michiruf.allayfollowalways.testhelper;

import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.gametest.framework.GameTestHelper;

public class TestExecutor {

    private static boolean isLocked = false;
    private boolean ownsLock = false;

    private final GameTestHelper context;
    private int defaultTickDelay;
    private final ArrayList<Step> entries = new ArrayList<>();

    public TestExecutor(GameTestHelper context) {
        this(context, 1);
    }

    public TestExecutor(GameTestHelper context, int defaultTickDelay) {
        this.context = context;
        this.defaultTickDelay = defaultTickDelay;
    }

    public TestExecutor defaultTickDelay(int defaultDelay) {
        this.defaultTickDelay = defaultDelay;
        return this;
    }

    public TestExecutor then(int delay, Runnable runnable) {
        entries.add(new DelayedStep(runnable, delay));
        return this;
    }

    public TestExecutor then(Runnable runnable) {
        return then(defaultTickDelay, runnable);
    }

    public TestExecutor immediate(Runnable runnable) {
        return then(0, runnable);
    }

    public TestExecutor wait(int ticks) {
        return then(ticks, () -> {
        });
    }

    public TestExecutor waitUntil(int delay, Supplier<Boolean> condition) {
        return wait(delay).waitUntil(condition);
    }

    public TestExecutor waitUntil(Supplier<Boolean> condition) {
        entries.add(new WaitUntilStep(condition));
        return this;
    }

    public void run() {
        if (entries.isEmpty()) {
            releaseLock();
            return;
        }

        var entry = entries.remove(0);
        entry.schedule(context, this::run, this::releaseLock);
    }

    private void releaseLock() {
        if (ownsLock) {
            ownsLock = false;
            isLocked = false;
        }
    }

    public void runSync() {
        synchronized (TestExecutor.class) {
            if (isLocked) {
                context.runAfterDelay(1, this::runSync);
                return;
            }

            isLocked = true;
            ownsLock = true;
            this.run();
        }
    }

    private interface Step {
        void schedule(GameTestHelper context, Runnable next, Runnable onError);
    }

    private record DelayedStep(Runnable runnable, int delay) implements Step {
        @Override
        public void schedule(GameTestHelper context, Runnable next, Runnable onError) {
            context.runAfterDelay(delay, () -> {
                try {
                    runnable.run();
                } catch (Throwable t) {
                    onError.run();
                    throw t;
                }
                next.run();
            });
        }
    }

    private record WaitUntilStep(Supplier<Boolean> condition) implements Step {
        @Override
        public void schedule(GameTestHelper context, Runnable next, Runnable onError) {
            context.runAfterDelay(1, () -> {
                try {
                    if (condition.get()) {
                        next.run();
                    } else {
                        schedule(context, next, onError);
                    }
                } catch (Throwable t) {
                    onError.run();
                    throw t;
                }
            });
        }
    }
}
