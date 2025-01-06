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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class FlatFileFormattedConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job batchJob() {
        return new JobBuilder("batchJob", jobRepository)
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .chunk(5, transactionManager)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter customItemWriter() {

        return new FlatFileItemWriterBuilder<Customer>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("C:\\springbatch\\src\\main\\resource\\customer.txt")) // 파일 저장할 위치
                .formatted()
                .format("%-5s%-2d%-4s") // 문자열, int에 따라 %뒤에 오는 부분이 달라짐.
                .names(new String[]{"name", "age", "year"})
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> customItemReader() {
        List<Customer> customers = Arrays.asList(
                new Customer("user1", 11, "2012"),
                new Customer("user2", 12, "2014"),
                new Customer("user3", 13, "2016"),
                new Customer("user4", 14, "2017"));

        ListItemReader<Customer> reader = new ListItemReader<>(customers);
        return reader;
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" >> step1 was executed!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

}
