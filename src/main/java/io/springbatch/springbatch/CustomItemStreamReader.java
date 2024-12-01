package io.springbatch.springbatch;

import org.springframework.batch.item.*;

import java.util.List;

public class CustomItemStreamReader implements ItemStreamReader<String> {
    private final List<String> items;
    private int index = -1;
    private boolean restart = false;

    public CustomItemStreamReader(List<String> items) {
        this.items = items;
        this.index = 0;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        String item = null;

        // 인덱스가 아이템 사이즈보다 작을때까지 읽는다.
        if(this.index < this.items.size()) {
            item = this.items.get(index); // 0부터 읽고, index 더해가며 계속한다.
            index++;
        }

        // 재시작이 아닌상태에서 index가 6인 경우 실패하도록 구성
        if(this.index == 6 && !restart) {
            throw new RuntimeException("Restart is required");
        }
        System.out.println("CustomItemStreamReader read item========================" + item);

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // ExecutionContext내부에는 <Key, Value>로 저장하는 map이 있다.
        // 우리가 저장하려고 하는 index값이 key로 존재한다면 -> 이미 Database에 저장되어있다고 볼 수 있다.
        // 따라서 해당 index를 가지고 와서 index에 저장하고, 재시작 여부를 true로 반환한다.
        if(executionContext.containsKey("index")) {
            index = executionContext.getInt("index");
            this.restart = true;
            System.out.println("CustomItemStreamReader index" + index);

        } else { // 처음 시작하는 경우에는 index Key가 존재하지 않는다.
            index = 0; // 초기 index값 설정하고
            executionContext.put("index", index); // index key값을 넣어준다.
            System.out.println("CustomItemStreamReader first open");
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // chunk size의 데이터를 읽어서 itemWriter에게 전달하며, 한 사이클이 완료되면 그때마다 update 메소드가 호출된다.
        // 그렇기 때문에 항상 현재의 정보를 저장하면 된다. 마지막으로 저장된 값을 가져오면 된다.
        executionContext.put("index", index);
        System.out.println("CustomItemStreamReader update" + index);
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("CustomItemStreamReader closed");
    }
}
