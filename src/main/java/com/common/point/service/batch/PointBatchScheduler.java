package com.example.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 배치 스케줄러 클래스.
 * 일정에 따라 배치 작업을 실행합니다.
 */
@Component
public class PointBatchScheduler {

	private final JobLauncher jobLauncher;
	private final Job pointCalculationJob;

	public PointBatchScheduler(JobLauncher jobLauncher, Job pointCalculationJob) {
		this.jobLauncher = jobLauncher;
		this.pointCalculationJob = pointCalculationJob;
	}

	/**
	 * 매일 새벽 1시에 배치 작업을 실행합니다.
	 */
	@Scheduled(cron = "0 0 1 * * ?") // CRON 표현식
	public void runBatchJob() {
		try {
			// Job 파라미터 생성
			JobParameters params = new JobParametersBuilder()
					.addString("JobID", String.valueOf(System.currentTimeMillis()))
					.addLong("totalThreadCount", 1L)
					.addLong("limitSize", 10L)
                    .addLong("chunkSize", 1L)
					.toJobParameters();

			// 배치 작업 실행
			JobExecution execution = jobLauncher.run(pointCalculationJob, params);
			System.out.println("Job Status : " + execution.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
