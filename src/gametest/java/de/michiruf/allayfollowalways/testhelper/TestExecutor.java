package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.test.TestContext;

import java.util.ArrayList;

public class TestExecutor {

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

    public TestExecutor then(Runnable runnable, int delay) {
        entries.add(new Entry(runnable, delay));
        return this;
    }

    public TestExecutor then(Runnable runnable) {
        return then(runnable, defaultTickDelay);
    }

    public TestExecutor immediate(Runnable runnable) {
        return then(runnable, 0);
    }

    public void run() {
        if (entries.isEmpty())
            return;

        var entry = entries.remove(0);

        context.waitAndRun(entry.delay, () -> {
            entry.runnable.run();
            run();
        });
    }

    private record Entry(Runnable runnable, int delay) {
    }
}
