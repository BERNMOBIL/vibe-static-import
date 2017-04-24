package ch.bernmobil.vibe.staticdata.listener;

import ch.bernmobil.vibe.staticdata.UpdateManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomJobListener implements JobExecutionListener {
    private final UpdateManager updateManager;

    @Autowired
    public CustomJobListener(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        //TODO: Create Timestamp for update Table
        //TODO:
        updateManager.createUpdateTimestamp();

        Logger logger = Logger.getLogger(CustomJobListener.class);
        logger.info("mattu-before-job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Logger logger = Logger.getLogger(CustomJobListener.class);
        logger.info("mattu-start-cleaning");
        updateManager.cleanOldData();
        logger.info("mattu-after-job");
    }
}
