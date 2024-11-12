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
public class JobExecutionConfiguration {

    @Bean
    public Job jobExecutionConfigurationJob(JobRepository jobRepository, Step step1, Step step2){
        return new JobBuilder("jobExecutionConfigurationJob", jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, Tasklet task1, PlatformTransactionManager platformTransactionManager){
        System.out.println("----------------step1 has executed----------------");
        return new StepBuilder("STEP1", jobRepository)
                .tasklet(task1, platformTransactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, Tasklet task2, PlatformTransactionManager platformTransactionManager){
        System.out.println("----------------step2 has executed----------------");
        return new StepBuilder("STEP2", jobRepository)
                .tasklet(task2, platformTransactionManager)
                .build();
    }


    @Bean
    public Tasklet task1(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("tasklet1===========================");
                System.out.println("contribution: "+ contribution);
                System.out.println("chunkContext: "+ chunkContext);
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Tasklet task2(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("tasklet2===========================");
                System.out.println("contribution: "+ contribution);
                System.out.println("chunkContext: "+ chunkContext);
                return RepeatStatus.FINISHED;
            }
        };
    }

}
