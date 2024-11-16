package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class StepBuilderConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job batchJob() {
        return new JobBuilder("batchJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // spring 제공하는 구현체
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Step step1(){
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

    // chunk 기반 Step 구성 (reader, processor, writer)

    @Bean
    public Step step2(){
        return new StepBuilder("step2", jobRepository)
                .chunk(3, transactionManager)
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor() {
                    @Override
                    public String process(Object item) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> chunk) throws Exception {

                    }
                })
                .build();
    }

    @Bean
    public Step step3(){
        return new StepBuilder("step3", jobRepository)
                .partitioner(step1()) // multi-thread 작업 시 여러 개의 step으로 분리해서 동시로 실행하고 싶을때 사용
                .gridSize(2)
                .build();
    }

    @Bean
    public Step step4(){
        return new StepBuilder("step4", jobRepository)
                .job(job()) // step 내에서 job호출
                .build();
    }

    @Bean
    public Step step5(){
        return new StepBuilder("step5", jobRepository)
                .flow(flow())
                .build();
    }

    @Bean
    public Job job(){
        return new JobBuilder("job", jobRepository)
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(step2()).end();
        return flowBuilder.build();
    }
}
