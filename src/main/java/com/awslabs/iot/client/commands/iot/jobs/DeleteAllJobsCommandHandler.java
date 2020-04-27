package com.awslabs.iot.client.commands.iot.jobs;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
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
import java.util.Comparator;
import java.util.Optional;

public class DeleteAllJobsCommandHandler implements IotCommandHandler {
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
    public DeleteAllJobsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        RetryPolicy<DeleteJobResponse> deleteJobResponseRetryPolicy = new RetryPolicy<DeleteJobResponse>()
                .handle(LimitExceededException.class)
                .withBackoff(500, 4000, ChronoUnit.MILLIS)
                .withMaxRetries(20)
                .onRetry(failure -> log.warn(System.currentTimeMillis() + ": Exceeded rate limit or too many jobs in deletion in progress status, backing off..."))
                .onRetriesExceeded(failure -> log.error("Exceeded rate limit too many times. Cannot continue."));

        log.info("Deleting " + v2IotHelper.getJobs().count() + " job(s)");

        long deletedJobCount = v2IotHelper.getJobs()
                // Do not try to delete jobs that are already in progress
                .filter(jobSummary -> !jobSummary.status().equals(JobStatus.DELETION_IN_PROGRESS))
                // Sort them by creation date to delete oldest ones first
                .sorted(Comparator.comparing(JobSummary::createdAt))
                // Delete the job with the retry policy
                .map(jobSummary -> deleteJob(jobSummary, Optional.of(deleteJobResponseRetryPolicy)))
                // Log the deleted job once it is successful
                .map(jobSummary -> String.join(" ", jobSummary.createdAt().toString(), jobSummary.jobId(), jobSummary.statusAsString()))
                .peek(log::info)
                // Throw away the job information and just sum up how many jobs we deleted
                .map(jobSummary -> 1L)
                .reduce(0L, Long::sum);

        log.info("Deleted " + deletedJobCount + " job(s)");
    }

    private JobSummary deleteJob(JobSummary jobSummary) {
        return deleteJob(jobSummary, Optional.empty());
    }

    private JobSummary deleteJob(JobSummary jobSummary, Optional<RetryPolicy<DeleteJobResponse>> optionalRetryPolicy) {
        Runnable deleteJobResponseRunnable = () -> v2IotHelper.delete(jobSummary);

        if (optionalRetryPolicy.isPresent()) {
            Failsafe.with(optionalRetryPolicy.get())
                    .run(deleteJobResponseRunnable::run);
        } else {
            // Rethrow all exceptions
            Try.run(deleteJobResponseRunnable::run).get();
        }

        return jobSummary;
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
}
