package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExecutionContextConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ExecutionContextTasklet1 tasklet1;
    private final ExecutionContextTasklet2 tasklet2;
    private final ExecutionContextTasklet3 tasklet3;
    private final ExecutionContextTasklet4 tasklet4;

    @Bean
    public Job executionContextJobTest(){
        return new JobBuilder("executionContextJobTest", jobRepository)
                .start(stepA())
                .next(stepB())
                .next(stepC())
                .next(stepD())
                .build();
    }

    @Bean
    public Step stepA(){
        System.out.println("----------------step1 has executed----------------");
        return new StepBuilder("STEP1", jobRepository)
                .tasklet(tasklet1, transactionManager)
                .build();
    }

    @Bean
    public Step stepB(){
        System.out.println("----------------step2 has executed----------------");
        return new StepBuilder("STEP2", jobRepository)
                .tasklet(tasklet2, transactionManager)
                .build();
    }

    @Bean
    public Step stepC(){
        System.out.println("----------------step3 has executed----------------");
        return new StepBuilder("STEP3", jobRepository)
                .tasklet(tasklet3, transactionManager)
                .build();
    }

    @Bean
    public Step stepD(){
        System.out.println("----------------step4 has executed----------------");
        return new StepBuilder("STEP4", jobRepository)
                .tasklet(tasklet4, transactionManager)
                .build();
    }
}