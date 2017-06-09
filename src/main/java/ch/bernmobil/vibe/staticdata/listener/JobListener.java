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

/**
 * Provides listener to handle execution results of the import job.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
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

    /**
     * Ensures that no other instance is importing static schedule data when before this instance is starting.
     * It detects other running instances by checking the update history.
     * @param jobExecution control object to handle error cases before running the job.
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        int beforeCount = updateManager.getRowCount();
        if(updateManager.hasUpdateCollision()) {
            logger.warn("Another instance of this import component is running. Abort execution.");
            jobExecution.stop();
        } else {
            Timestamp now;
            try {
                now = updateManager.prepareUpdate();
            } catch (DuplicateKeyException e) {
                logger.error("Another entry with the same timestamp has been found in the update history.", e);
                jobExecution.stop();
                return;
            }
            int afterCount = updateManager.getRowCount();
            if(afterCount != beforeCount + 1) {
                jobExecution.stop();
                updateManager.removeUpdateByTimestamp(now);
                logger.warn("Another job is running. There can be only one running instance at the same time.");
            } else {
                updateManager.startUpdate(now);
            }
        }
    }

    /**
     * Method handles error which might occur while importing static data. If import was successful it cleans old
     * database versions. The amount of remaining versions is specified in "bernmobil.history.size".
     * @param jobExecution to control the resulting status of the job.
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(status.equals(BatchStatus.COMPLETED)) {
            logger.info("Data import successful. Starting cleanup stale versions of schedule data.");
            updateManager.setStatus(Status.SUCCESS);
            updateManager.cleanOldData();
            updateNotificationSender.sendNotification();
            logger.debug("Cleanup finished.");
        } else if (status.isUnsuccessful()){
            logger.warn(String.format("Job execution failed. %s", jobExecution.getAllFailureExceptions()));
            logger.warn("Trying to repair database. This may take some time.");
            updateManager.repairFailedUpdate();
            updateManager.setStatus(Status.FAILED);
            logger.info("Database repair successfully finished. ");
        } else if (status.equals(BatchStatus.STOPPED)){
            logger.error("Import of static data has been aborted. Either there is another instance running or has failed " +
                    "while importing. Wait until \"bernmobil.history.timeout\" duration is over or check \"update_history\" " +
                    "table in schedule database to resolve this error.");
        }
    }
}
