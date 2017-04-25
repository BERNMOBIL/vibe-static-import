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

    @Autowired
    public JobListener(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }
        Logger logger = Logger.getLogger(JobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Start static import");
        updateManager.createUpdateTimestamp();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if(!status.equals(BatchStatus.COMPLETED)) {
            logger.info("Something went wrong - start repair process");
            updateManager.repairFailedUpdate();
            logger.info("DB repaired");
        } else {
            logger.info("Success - start update cleanup");
            updateManager.cleanOldData();
            logger.info("Finished - everything up to date");
        }
    }
}
