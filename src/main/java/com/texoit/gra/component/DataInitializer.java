package com.texoit.gra.component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.texoit.gra.entity.Movie;
import com.texoit.gra.entity.Producer;
import com.texoit.gra.repository.MovieRepository;
import com.texoit.gra.repository.ProducerRepository;

@Component
public class DataInitializer implements InitializingBean{

	static final int YEAR = 0;
	static final int TITLE = 1;
	static final int STUDIOS = 2;
	static final int PRODUCERS = 3;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		List<String[]> csvData = readCsv();
		
		Set<Producer> producers = mapProducers(csvData);
		producerRepository.saveAll(producers);
		
		List<Movie> records = csvData
				.stream()
				.map(row -> {
					List<Producer> producerList = extractProducerNames(row[PRODUCERS])
			        		.stream()
			        		.map(n -> producers.stream().filter(p -> p.getName().equals(n)).findFirst())
			        		.map(Optional::get)
			        		.collect(Collectors.toList());
					
					Movie movie = new Movie();
			        movie.setYear(Long.parseLong(row[YEAR]));
			        movie.setTitle(row[TITLE]);
			        movie.setStudios(row[STUDIOS]);
			        movie.setProducers(producerList);
			        movie.setWinner(row.length > 4);
			        return movie;
				}).collect(Collectors.toList());
				
		movieRepository.saveAll(records);
		
		System.out.println(records);
	}
	
	private Set<Producer> mapProducers(List<String[]> csvData) {
		return csvData.stream()
			.map(row -> row[PRODUCERS])
			.map(this::extractProducerNames)
			.flatMap(List::stream)
			.distinct()
	        .map(Producer::new)
	        .collect(Collectors.toSet());
	}
	
	private List<String> extractProducerNames(String col){
		return Arrays.asList(col.replace(" and ", ", ").split(","))
				.stream()
				.map(String::trim)
				.filter(Predicate.not(String::isBlank))
				.collect(Collectors.toList());
	}

	private List<String[]> readCsv() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:movielist.csv")))) {
			return br.lines().map(l -> l.split(";")).skip(1).collect(Collectors.toList());
		}
	}

}
