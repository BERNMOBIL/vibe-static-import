package ch.bernmobil.vibe.staticdata;

import org.apache.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Class configures scheduling of the import job.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Service
@EnableScheduling
public class ScheduledImportService {
    private final static Logger logger = Logger.getLogger(ScheduledImportService.class);
    private final JobLauncher jobLauncher;
    private final Job staticImportJob;

    @Autowired
    public ScheduledImportService(JobLauncher jobLauncher, Job staticImportJob) {
        this.jobLauncher = jobLauncher;
        this.staticImportJob = staticImportJob;
    }

    /**
     * Schedules the job to the defined value in "bernmobil.batch.schedule" and starts it.
     * @throws JobParametersInvalidException if job parameters are invalid.
     * @throws JobExecutionAlreadyRunningException if job is already running.
     * @throws JobRestartException if restarting the job is not possible.
     * @throws JobInstanceAlreadyCompleteException if the job trying to start is already completed.
     */
    @Scheduled(cron = "${bernmobil.batch.schedule}")
    public void run()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters param = new JobParametersBuilder()
                .addString(staticImportJob.getName(),String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        logger.info(String.format("Job started [%s, with params {%s}] at %s", staticImportJob.getName(),
                    param.toString(), LocalTime.now()));

        JobExecution execution = jobLauncher.run(staticImportJob, param);

        logger.info(String.format("Job finished [%s with status {%s}] at %s", staticImportJob.getName(),
                        execution.getExitStatus(), LocalTime.now()));
    }
}
