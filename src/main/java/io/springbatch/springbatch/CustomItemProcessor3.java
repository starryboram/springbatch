package io.springbatch.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor3 implements ItemProcessor<String, String> {

    int cnt = 0;
    @Override
    public String process(String item) throws Exception {
         cnt ++;
        return item + cnt;
    }
}
