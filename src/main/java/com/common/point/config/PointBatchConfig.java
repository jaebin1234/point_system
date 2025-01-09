package com.common.point.config;

import com.common.point.model.dto.Company;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class PointBatchConfig {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private long CHUNK_SIZE = 1;

	public PointBatchConfig(
			JobRepository jobRepository,
			PlatformTransactionManager transactionManager
	) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	public Job pointCalculationJob(Step pointCalculationMasterStep) throws Exception {
		System.out.println("pointCalculationJob >>> start");
		return new JobBuilder("pointCalculationJob", jobRepository)
				.start(pointCalculationMasterStep)
//				.next((jobExecution, stepExecution) -> FlowExecutionStatus.COMPLETED) // 첫 실행 후 종료
//            	.end()
				.build();
	}

	@Bean
	@JobScope
	public Step pointCalculationMasterStep(
			Partitioner partitioner,
			PartitionHandler partitionHandler
	) throws Exception {
		System.out.println("pointCalculationMasterStep >>> start");
		return new StepBuilder("pointCalculationMasterStep", jobRepository)
				.partitioner("pointCalculationStep", partitioner) // 파티셔너 설정
				.partitionHandler(partitionHandler)
				.build();
	}

	@Bean
	@JobScope
	public PartitionHandler partitionHandler(
			@Value("#{jobParameters['totalThreadCount']}") long totalThreadCount,
			@Value("#{jobParameters['chunkSize']}") long chunkSize,
			@Qualifier("pointTaskExecutor") TaskExecutor taskExecutor,
			@Qualifier("pointCalculationStep") Step pointCalculationStep
	) throws Exception {
		CHUNK_SIZE = chunkSize;

		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize((int) totalThreadCount);
		handler.setTaskExecutor(taskExecutor);
		handler.setStep(pointCalculationStep);
		handler.afterPropertiesSet();
		return handler;
	}


	@Bean
	@JobScope
	public Partitioner partitioner(
			@Value("#{jobParameters['totalThreadCount']}") Long totalThreadCount,
			@Value("#{jobParameters['limitSize']}") Long limitSize
	) {
		return gridSize -> {
			Map<String, ExecutionContext> partitions = new HashMap<>();
			for (int i = 0; i < gridSize; i++) {
				ExecutionContext context = new ExecutionContext();
				context.putInt("threadIndex", i);
				context.putLong("totalThreadCount", totalThreadCount);
				context.putLong("limitSize", limitSize);
				partitions.put("partition" + i, context);
			}
			return partitions;
		};
	}

	@Bean
	public Step pointCalculationStep(
			ItemReader<List<Company>> pointReader,
			ItemProcessor<List<Company>, List<HashMap<String, Object>>> pointProcessor,
			ItemWriter<List<HashMap<String, Object>>> pointWriter
	) throws Exception {
		return new StepBuilder("pointCalculationStep", jobRepository)
				.<List<Company>, List<HashMap<String, Object>>>chunk(Math.toIntExact(CHUNK_SIZE), transactionManager)
				.reader(pointReader)
				.processor(pointProcessor)
				.writer(pointWriter)
				.build();
	}

	@Bean
	@JobScope
	public TaskExecutor pointTaskExecutor(
			@Value("#{jobParameters['totalThreadCount']}") Long totalThreadCount
	) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(Math.toIntExact(totalThreadCount));
		executor.setMaxPoolSize(Math.toIntExact(totalThreadCount));
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("BatchThread-");
		executor.initialize();
		return executor;
	}
}
