package io.springbatch.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorForClassifier1 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        System.out.println("CustomItemProcessorForClassifier1");

        return item;
    }
}
