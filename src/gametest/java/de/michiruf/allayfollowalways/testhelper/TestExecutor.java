package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.entity.Entity;
import net.minecraft.test.TestContext;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestExecutor {

    private static boolean isLocked = false;
    private boolean ownsLock = false;

    private final TestContext context;
    private int defaultTickDelay;
    private final ArrayList<Entry> entries = new ArrayList<>();

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
        entries.add(new Entry(runnable, delay));
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
        return then(delay, () -> {
            var holder = new Object() {
                boolean condition;
            };

            do {
                new TestExecutor(context)
                        .wait(1)
                        .immediate((() -> holder.condition = condition.get()))
                        .run();
            } while (holder.condition);
        });
    }

    public TestExecutor waitUntil(Supplier<Boolean> condition) {
        return waitUntil(0, condition);
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

        context.waitAndRun(entry.delay, () -> {
            entry.runnable.run();
            run();
        });
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

    private record Entry(Runnable runnable, int delay) {
    }
}
