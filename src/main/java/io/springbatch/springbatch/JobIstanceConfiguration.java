package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JobIstanceConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job jobInstanceConfigurationJob(){
        return new JobBuilder("jobInstanceConfigurationJob", jobRepository)
                .start(step_1())
                .next(step_2())
                .build();
    }

    @Bean
    public Step step_1(){
        return new StepBuilder("STEP_1", jobRepository)
                .tasklet(tasklet1(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step step_2(){
        return new StepBuilder("STEP_2", jobRepository)
                .tasklet(tasklet2(), platformTransactionManager)
                .build();
    }


    @Bean
    public Tasklet tasklet1(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("contribution: "+ contribution);
                System.out.println("chunkContext: "+ chunkContext);
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Tasklet tasklet2(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("contribution: "+ contribution);
                System.out.println("chunkContext: "+ chunkContext);
                return RepeatStatus.FINISHED;
            }
        };
    }

}
