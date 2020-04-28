package com.awslabs.iot.client.helpers.progressbar;

import com.awslabs.iot.client.streams.interfaces.UsesStream;
import io.vavr.control.Try;
import me.tongfei.progressbar.ProgressBar;

import javax.inject.Inject;

public class BasicProgressBarHelper implements ProgressBarHelper {
    private ProgressBar progressBar;

    @Inject
    public BasicProgressBarHelper() {
    }

    @Override
    public <T> ProgressBar start(String name, UsesStream<T> usesStream) {
        // If there's no stream use an empty stream, get the stream, get the count, and rethrow all exceptions
        long count = Try.of(() -> usesStream.getStream().count()).get();

        if (progressBar != null) {
            progressBar.close();
            progressBar = null;
        }

        progressBar = new ProgressBar(name, count);

        return progressBar;
    }

    @Override
    public void step(long step) {
        progressBar.stepBy(step);
    }

    @Override
    public void next() {
        progressBar.step();
    }

    @Override
    public void clearExtraMessageAndStep() {
        progressBar.setExtraMessage("");
        progressBar.step();
    }

    @Override
    public void close() {
        progressBar.close();
        progressBar = null;
    }

    @Override
    public void throttled() {
        progressBar.setExtraMessage("Throttled");
    }
}
