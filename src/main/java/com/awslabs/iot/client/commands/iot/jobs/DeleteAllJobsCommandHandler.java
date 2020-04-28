package com.awslabs.iot.client.commands.iot.jobs;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import io.vavr.control.Try;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.model.DeleteJobResponse;
import software.amazon.awssdk.services.iot.model.JobStatus;
import software.amazon.awssdk.services.iot.model.JobSummary;
import software.amazon.awssdk.services.iot.model.LimitExceededException;

import javax.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class DeleteAllJobsCommandHandler implements IotCommandHandler, UsesStream<JobSummary> {
    private static final String DELETE_ALL_JOBS = "delete-all-jobs";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllJobsCommandHandler.class);
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    IotClient iotClient;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllJobsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all IoT jobs", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(jobSummary -> progressBarHelper.next())
                // Delete the job with the retry policy
                .forEach(jobSummary -> Failsafe.with(getRetryPolicy()).run(() -> v2IotHelper.forceDelete(jobSummary)));

        return null;
    }

    private RetryPolicy<DeleteJobResponse> getRetryPolicy() {
        return new RetryPolicy<DeleteJobResponse>()
                .handle(LimitExceededException.class)
                .withBackoff(500, 4000, ChronoUnit.MILLIS)
                .withMaxRetries(20)
                .onRetry(failure -> progressBarHelper.throttled())
                .onRetriesExceeded(failure -> log.error("Exceeded rate limit too many times. Cannot continue."));
    }

    @Override
    public String getCommandString() {
        return DELETE_ALL_JOBS;
    }

    @Override
    public String getHelp() {
        return "Deletes all jobs.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    @Override
    public Stream<JobSummary> getStream() {
        return v2IotHelper.getJobs()
                // Do not try to delete jobs that are already in being deleted
                .filter(jobSummary -> !jobSummary.status().equals(JobStatus.DELETION_IN_PROGRESS));
    }
}
