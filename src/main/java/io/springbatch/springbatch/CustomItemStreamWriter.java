package io.springbatch.springbatch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

public class CustomItemStreamWriter implements ItemStreamWriter<String> {
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        System.out.println("CustomIteamStreamWriter write start");
        chunk.forEach(item -> System.out.println(item));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("CustomItemStreamWriter open");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("CustomItemStreamWriter update");
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("CustomItemStreamWriter close");
    }
}
