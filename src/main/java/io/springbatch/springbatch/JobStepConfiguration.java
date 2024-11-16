package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class JobStepConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job parentJob() {
        return new JobBuilder("parentJob", jobRepository)
                .start(jobStep(null))
                .next(step2())
                .build();
    }

    @Bean
    public Step jobStep(JobLauncher jobLauncher) {
        return new StepBuilder("jobStep", jobRepository)
                .job(childJob())
                .launcher(jobLauncher)
                .parametersExtractor(jobParameterExtractor())
                .listener(new StepExecutionListener() {
                    // step이 수행되기 전에 사전 작업을 정의한다. 즉 미리 파라미터를 key:value 값을 넣어준다.
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        StepExecutionListener.super.beforeStep(stepExecution);
                        stepExecution.getExecutionContext().putString("name", "user1");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return StepExecutionListener.super.afterStep(stepExecution);
                    }
                })
                .build();
    }
// Bean으로 등록할 필요는 없다. ExecuteContext에 있는 key를 찾아서 값을 가지고 올 수가 있다. 그 key값을 "name"으로 설정했다.
    private DefaultJobParametersExtractor jobParameterExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }


    @Bean
    public Job childJob() {
        return new JobBuilder("childJob", jobRepository)
                .start(step1())
                .build();
    }


    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" >> step1 was executed!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(" >> step2 was executed!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}

