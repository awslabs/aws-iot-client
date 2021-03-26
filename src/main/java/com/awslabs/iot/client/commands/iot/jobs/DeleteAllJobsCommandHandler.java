package com.awslabs.iot.client.commands.iot.jobs;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import software.amazon.awssdk.services.iot.IotClient;
import software.amazon.awssdk.services.iot.model.DeleteJobResponse;
import software.amazon.awssdk.services.iot.model.JobStatus;
import software.amazon.awssdk.services.iot.model.JobSummary;
import software.amazon.awssdk.services.iot.model.LimitExceededException;

import javax.inject.Inject;
import java.time.temporal.ChronoUnit;

public class DeleteAllJobsCommandHandler implements IotCommandHandler, UsesStream<JobSummary> {
    private static final String DELETE_ALL_JOBS = "delete-all-jobs";
    @Inject
    IotHelper iotHelper;
    @Inject
    IotClient iotClient;
    @Inject
    ParameterExtractor parameterExtractor;
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
                .forEach(jobSummary -> Failsafe.with(getRetryPolicy()).run(() -> iotHelper.forceDelete(jobSummary)));

        return null;
    }

    private RetryPolicy<DeleteJobResponse> getRetryPolicy() {
        return new RetryPolicy<DeleteJobResponse>()
                .handle(LimitExceededException.class)
                .withBackoff(500, 4000, ChronoUnit.MILLIS)
                .withMaxRetries(20)
                .onRetry(failure -> progressBarHelper.throttled())
                .onRetriesExceeded(failure -> Logger.error(this, "Exceeded rate limit too many times. Cannot continue."));
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

    @Override
    public Stream<JobSummary> getStream() {
        return iotHelper.getJobs()
                // Do not try to delete jobs that are already in being deleted
                .filter(jobSummary -> !jobSummary.status().equals(JobStatus.DELETION_IN_PROGRESS));
    }
}
