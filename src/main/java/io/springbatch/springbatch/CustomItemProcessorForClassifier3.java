package io.springbatch.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorForClassifier3 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        System.out.println("CustomItemProcessorForClassifier3");

        return item;
    }
}
