package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class ClassifierConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job batchJob() throws Exception {
        return new JobBuilder("batchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<ProcessorInfo, ProcessorInfo> chunk(10, transactionManager)
                .reader(new ItemReader<ProcessorInfo>() {
                    int i = 0;
                    @Override
                    public ProcessorInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        ProcessorInfo processorInfo = ProcessorInfo.builder()
                                .id(i)
                                .build();
                        return i > 3 ? null : processorInfo;
                    }
                })
                .processor(customeItemProcessor())
                .writer(items -> System.out.println(items))
                .build();
    }

    @Bean
    public ItemProcessor<? super ProcessorInfo,? extends ProcessorInfo> customeItemProcessor() {

        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor = new ClassifierCompositeItemProcessor<>();

        ProcessorClassifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();

        Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
        processorMap.put(1, new CustomItemProcessorForClassifier1());
        processorMap.put(2, new CustomItemProcessorForClassifier2());
        processorMap.put(3, new CustomItemProcessorForClassifier3());

        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);

        return processor;
    }

}
