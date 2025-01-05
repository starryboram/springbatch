package io.springbatch.springbatch;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

public class DefaultLineMapper<T> implements LineMapper<T> {

    private LineTokenizer tokenizer;
    private FieldSetMapper<T> fieldSetMapper;

    // FieldSet을 반환받아야 한다.
    // tokenizer가 line을 받아서 토큰화를 진행하고,
    // 진행시킨 토큰을 fieldset에서 배열로 만들고, 가지고 있는 fieldSet을 return해주면 된다.
    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    }

    public void setLineTokenize(LineTokenizer lineTokenizer) {
        this.tokenizer = lineTokenizer;
    }

    public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
        this.fieldSetMapper = fieldSetMapper;
    }
}
