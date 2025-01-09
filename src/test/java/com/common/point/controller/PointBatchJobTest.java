package com.common.point.controller;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@EnableBatchProcessing
public class PointBatchJobTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testPointCalculationJob() throws Exception {
		long startTime = System.nanoTime();

		JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
//                .addLong("totalThreadCount", 1L)
//                .addLong("limitSize", 10L)
//                .addLong("chunkSize", 1L)
				.addLong("totalThreadCount", 5L)
                .addLong("limitSize", 2L)
                .addLong("chunkSize", 1L)
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1_000_000;
		System.out.println("Execution time: " + duration + " ms");
	}
}


