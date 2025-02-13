package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class ChunkOrientedTaskletConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job job() {
        return new JobBuilder("batchJob", jobRepository)
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    @JobScope
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(2, transactionManager)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6")))
                .processor(new ItemProcessor<String, String>() { // 하나씩 처리해야 한다.
                    @Override
                    public String process(String item) throws Exception {
                        return "my_" + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> chunks) throws Exception {
                        chunks.forEach(chunk -> System.out.println(chunk));
                    }
                })
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(tasklet1(), transactionManager)
                .build();
    }
    @Bean
    public Tasklet tasklet1() {
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

