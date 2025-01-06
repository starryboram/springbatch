package io.springbatch.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorForClassifier2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        System.out.println("CustomItemProcessorForClassifier2");

        return item;
    }
}
