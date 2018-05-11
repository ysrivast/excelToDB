package com.example.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.example.demo.listener.JobCompletionNotificationListener;
import com.example.demo.model.Person;
import com.example.demo.processor.PersonItemProcessor;

@Configuration
@EnableBatchProcessing
public class ExcelFileToDatabaseJobConfig {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;

	@Bean
	ItemReader<Person> reader() {
		PoiItemReader<Person> reader = new PoiItemReader<>();
		//reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("person.xlsx"));
		reader.setRowMapper(excelRowMapper());
		System.out.println("ItemReader");
		return reader;
	}

	private RowMapper<Person> excelRowMapper() {
		/*BeanWrapperRowMapper<User> rowMapper = new BeanWrapperRowMapper<>();
		rowMapper.setTargetType(User.class);
		System.out.println("RawMapper" + rowMapper);
		return rowMapper;*/
		return new UserExcelRowMapper();
	}
	

	@Bean
	PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	JdbcBatchItemWriter<Person> writer() {
		System.out.println("Writer");
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
		System.out.println("Writer--1");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		System.out.println("Writer--2");
		
		  writer.setSql(
		 "INSERT INTO person (first_name, last_name,email,age) VALUES (:firstName, :lastName,:email,:age)"
		  );
		 
		/*writer.setSql("INSERT INTO User (firstname, lastname) VALUES (:firstName, :lastName)");
		System.out.println("Writer");*/
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(2);
		return taskExecutor;
	}
	

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) {
		System.out.println("importUserJob");
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		System.out.println("Step");
		return stepBuilderFactory.get("step1").<Person, Person>chunk(500).reader(reader()).processor(processor())
				.writer(writer())
			//	.taskExecutor(taskExecutor())
			//	.throttleLimit(2)
				.build();
	}
}