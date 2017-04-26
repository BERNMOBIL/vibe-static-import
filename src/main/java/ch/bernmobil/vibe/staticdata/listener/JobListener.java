package ch.bernmobil.vibe.staticdata.listener;

import ch.bernmobil.vibe.staticdata.UpdateManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {
    private final UpdateManager updateManager;
    private final static Logger LOGGER = Logger.getLogger(JobListener.class);

    @Autowired
    public JobListener(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        updateManager.createUpdateTimestamp();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(!status.equals(BatchStatus.COMPLETED)) {
            LOGGER.warn(String.format("Job execution failed. %s",
                    jobExecution.getAllFailureExceptions()));
            LOGGER.info("Reparing database");
            updateManager.repairFailedUpdate();
            LOGGER.info("Reparing database finished");
        } else {
            LOGGER.info("Success - start update cleanup");
            updateManager.cleanOldData();
            LOGGER.info("Finished - everything up to date");
        }
    }
}
