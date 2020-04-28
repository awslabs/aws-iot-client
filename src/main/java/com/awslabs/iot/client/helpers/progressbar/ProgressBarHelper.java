package com.awslabs.iot.client.helpers.progressbar;

import com.awslabs.iot.client.streams.interfaces.UsesStream;
import me.tongfei.progressbar.ProgressBar;

public interface ProgressBarHelper {
    <T> ProgressBar start(String name, UsesStream<T> usesStream);

    void step(long step);

    void next();

    void clearExtraMessageAndStep();

    void close();

    void throttled();
}
