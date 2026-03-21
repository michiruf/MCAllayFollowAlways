package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.test.TestContext;

import java.util.ArrayList;
import java.util.function.Supplier;

public class TestExecutor {

    private static boolean isLocked = false;
    private boolean ownsLock = false;

    private final TestContext context;
    private int defaultTickDelay;
    private final ArrayList<Step> entries = new ArrayList<>();

    public TestExecutor(TestContext context) {
        this(context, 1);
    }

    public TestExecutor(TestContext context, int defaultTickDelay) {
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
            if (ownsLock) {
                ownsLock = false;
                isLocked = false;
            }
            return;
        }

        var entry = entries.remove(0);
        entry.schedule(context, this::run);
    }

    public void runSync() {
        synchronized (TestExecutor.class) {
            if (isLocked) {
                context.waitAndRun(1, this::runSync);
                return;
            }

            isLocked = true;
            ownsLock = true;
            this.run();
        }
    }

    private interface Step {
        void schedule(TestContext context, Runnable next);
    }

    private record DelayedStep(Runnable runnable, int delay) implements Step {
        @Override
        public void schedule(TestContext context, Runnable next) {
            context.waitAndRun(delay, () -> {
                runnable.run();
                next.run();
            });
        }
    }

    private record WaitUntilStep(Supplier<Boolean> condition) implements Step {
        @Override
        public void schedule(TestContext context, Runnable next) {
            context.waitAndRun(1, () -> {
                if (condition.get()) {
                    next.run();
                } else {
                    schedule(context, next);
                }
            });
        }
    }
}
