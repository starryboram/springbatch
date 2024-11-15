package io.springbatch.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class JobLauncherController {
    /* Job 수행을 위해서는 JobLauncher가 필요하다. (Job, JobParameter)가 필요하다.
    - Job은 Bean으로 생성되기 때문에 @Autowired를 통해 의존성을 주입받으면 된다.
    - JobLaucher 또한 Job과 동일하게 @Autowired를 통해 의존성을 주입받으면 된다.*/

    @Qualifier("helloJob")
    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    private TaskExecutorJobLauncher taskExecutorJobLauncher;

    @PostMapping("/batch")
    public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

      // 스프링 배치가 초기화 되면서 프록시 객체를 만든 후, 실제 객체를 찾아가서 사용하는 방식

        taskExecutorJobLauncher.setTaskExecutor(new AsyncTaskExecutor() {
            @Override
            public void execute(Runnable task) {
                System.out.println("비동기식");
            }
        });
        jobLauncher.run(job, jobParameters);

        return "batch completed";
    }
}
