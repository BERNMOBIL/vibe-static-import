package ch.bernmobil.vibe.staticdata.listener;

import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.communication.UpdateNotificationSender;
import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {
    private static Logger logger = Logger.getLogger(JobListener.class);
    private final UpdateManager updateManager;
    private final UpdateNotificationSender updateNotificationSender;

    @Autowired
    public JobListener(UpdateManager updateManager, UpdateNotificationSender updateNotificationSender) {
        this.updateManager = updateManager;
        this.updateNotificationSender = updateNotificationSender;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        updateManager.createUpdateTimestamp();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(!status.equals(BatchStatus.COMPLETED)) {
            logger.warn(String.format("Job execution failed. %s",
                    jobExecution.getAllFailureExceptions()));
            logger.info("Reparing database");
            updateManager.repairFailedUpdate();
            logger.info("Reparing database finished");
        } else {
            logger.info("Success - start update cleanup");
            updateNotificationSender.send(UpdateManager.getLatestUpdateTimestamp().toLocalDateTime().toString());
            updateManager.cleanOldData();
            logger.info("Finished - everything up to date");
        }
    }
}
