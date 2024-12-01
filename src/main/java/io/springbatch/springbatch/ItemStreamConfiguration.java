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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class ItemStreamConfiguration {

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
                .<String, String>chunk(5, transactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super String> itemWriter() {
        return new CustomItemStreamWriter();
    }


    @Bean
    public CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>(10);

        for (int i = 0 ; i < 10 ; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);

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
                System.out.println("contribution: " + contribution);
                System.out.println("chunkContext: " + chunkContext);
                return RepeatStatus.FINISHED;
            }
        };
    }

}

