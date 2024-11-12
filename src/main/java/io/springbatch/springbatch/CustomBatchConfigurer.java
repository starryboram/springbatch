package io.springbatch.springbatch;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CustomBatchConfigurer extends DefaultBatchConfiguration {
    private final DataSource dataSource;

    public CustomBatchConfigurer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManager());
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("SYSTEM_");

        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
