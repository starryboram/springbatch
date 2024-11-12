package io.springbatch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class JobRepositoryConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepositoryListener jobRepositoryListener;

    public JobRepositoryConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobRepositoryListener jobRepositoryListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.jobRepositoryListener = jobRepositoryListener;
    }

    @Bean
    public Job batchJob(JobRepository jobRepository, JobRepositoryListener jobRepositoryListener, PlatformTransactionManager transactionManager){
        return new JobBuilder("batchJob", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .next(step2(jobRepository, transactionManager))
                .listener(jobRepositoryListener)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}