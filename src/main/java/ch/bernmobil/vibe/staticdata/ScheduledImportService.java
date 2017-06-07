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

@Service
@EnableScheduling
public class ScheduledImportService {
    private final static Logger logger = Logger.getLogger(ScheduledImportService.class);
    private JobLauncher jobLauncher;
    private Job staticImportJob;

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

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setStaticImportJob(Job staticImportJob) {
        this.staticImportJob = staticImportJob;
    }
}
