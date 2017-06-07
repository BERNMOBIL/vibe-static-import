package ch.bernmobil.vibe.staticdata.listener;

import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.UpdateManager.Status;
import ch.bernmobil.vibe.staticdata.communication.UpdateNotificationSender;
import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

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
        int beforeCount = updateManager.getRowCount();
        if(updateManager.hasUpdateCollision()) {
            logger.warn("Update-Collision detected: Update aborted");
            jobExecution.stop();
        } else {
            Timestamp now;
            try {
                now = updateManager.prepareUpdate();
            } catch (DuplicateKeyException e) {
                logger.error(e);
                jobExecution.stop();
                return;
            }
            int afterCount = updateManager.getRowCount();
            if(afterCount != beforeCount + 1) {
                jobExecution.stop();
                updateManager.removeUpdateByTimestamp(now);
                logger.warn("Another job is running while this job started");
            } else {
                UpdateManager.startUpdate(now);
            }
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(status.equals(BatchStatus.COMPLETED)) {
            logger.info("Success - start update cleanup");
            updateManager.setStatus(Status.SUCCESS);
            updateManager.cleanOldData();
            updateNotificationSender.sendNotification();
            logger.info("Finished - everything up to date");
        } else if (status.isUnsuccessful()){
            logger.warn(String.format("Job execution failed. %s", jobExecution.getAllFailureExceptions()));
            logger.info("Reparing database");
            updateManager.repairFailedUpdate();
            updateManager.setStatus(Status.FAILED);
            logger.info("Reparing database finished");
        } else if (status.equals(BatchStatus.STOPPED)){
            logger.warn("Updates skipped because of collisions");
        }
    }
}
