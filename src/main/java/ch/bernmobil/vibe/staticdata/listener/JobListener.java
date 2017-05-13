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
        while(!updateManager.checkUpdateCollision()) {
            logger.info("update collision, try again..");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }

        updateManager.createUpdateTimestamp();
        updateManager.setInProgressStatus();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(!status.equals(BatchStatus.COMPLETED)) {
            logger.warn(String.format("Job execution failed. %s", jobExecution.getAllFailureExceptions()));
            logger.info("Reparing database");
            updateManager.repairFailedUpdate();
            updateManager.setErrorStatus();
            logger.info("Reparing database finished");
        } else {
            logger.info("Success - start update cleanup");
            updateManager.cleanOldData();
            updateManager.setSuccessStatus();
            updateNotificationSender.sendNotification();
            logger.info("Finished - everything up to date");
        }
    }
}
