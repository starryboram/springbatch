package io.springbatch.springbatch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {


    private Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();

    @Override
    public T classify(C classifiable) { // 최종적으로 T에 반환되는 값은 ProcessorInfo다.
        return (T) processorMap.get(((ProcessorInfo) classifiable).getId());
    }

    // 1, 2, 3 에 해당하는 ItemProcessor가 반환되게끔 한다.
    public void setProcessorMap(Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap) {
        this.processorMap = processorMap;
    }
}
