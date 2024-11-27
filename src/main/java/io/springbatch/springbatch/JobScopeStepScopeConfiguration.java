package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class JobScopeStepScopeConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job job() {
        return new JobBuilder("batchJob", jobRepository)
                .start(step1(null))
                .next(step2())
                .listener(new JobListener())
                .build(); // Job 구성이 FlowJob 객체 생성
    }

// JobScope는 Step을 생성하는 쪽에 정의를 해야한다.
    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters['message']}") String message) {
        System.out.println("message = "+ message);

        return new StepBuilder("step1", jobRepository)
                .tasklet(tasklet1(null), transactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(tasklet2(null), transactionManager)
                .listener(new CustomStepListener())
                .build();
    }
    @Bean
    @StepScope
    public Tasklet tasklet1(@Value("#{jobExecutionContext['name']}") String name){
        System.out.println("name ===========" + name);
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
    @StepScope
    public Tasklet tasklet2(@Value("#{stepExecutionContext['name2']}") String name2){
        System.out.println("name2 ===========" + name2);
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

