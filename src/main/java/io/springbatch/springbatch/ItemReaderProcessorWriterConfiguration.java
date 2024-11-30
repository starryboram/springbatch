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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class ItemReaderProcessorWriterConfiguration {

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
                .<Customer, Customer>chunk(3, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> itemWriter() {
        return new CustomItemWriter();
    }


    @Bean
    public ItemProcessor<? super Customer,? extends Customer> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemReader<Customer> itemReader() {
        return new CustomItemReader(Arrays.asList(
                new Customer("user1"),
                new Customer("user2"),
                new Customer("user3")
        ));
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

