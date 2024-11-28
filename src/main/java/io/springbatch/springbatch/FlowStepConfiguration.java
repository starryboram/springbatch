package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
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
public class FlowStepConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job job() {
        return new JobBuilder("batchJob", jobRepository)
                .start(flowStep())
                .next(step2())
                .build(); // Job 구성이 FlowJob 객체 생성
    }


    private Step flowStep(){
        return new StepBuilder("flowStep", jobRepository)
                .flow(flow())
                .build();
    }


    public Flow flow() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
        builder.start(step1())
                .end();

        return builder.build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" >> step1 was executed!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" >> step2 was executed!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

}

