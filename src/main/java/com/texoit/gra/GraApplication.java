package com.texoit.gra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import com.texoit.gra.entity.Movie;
import com.texoit.gra.entity.Producer;
import com.texoit.gra.repository.MovieRepository;
import com.texoit.gra.repository.ProducerRepository;

@SpringBootApplication
public class GraApplication {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ProducerRepository producerRepository;

	public static void main(String[] args) {
		SpringApplication.run(GraApplication.class, args);
	}
	
	@PostConstruct
	private void postConstruct() throws FileNotFoundException, IOException {
		
		List<String[]> csvData = readCsv();
		
		Set<Producer> producers = mapProducers(csvData);
		
		producerRepository.saveAll(producers);
		
		List<Movie> records = csvData
				.stream()
				.map(row -> {
					Movie movie = new Movie();
			        movie.setYear(Long.parseLong(row[0]));
			        movie.setTitle(row[1]);
			        movie.setStudios(row[2]);
			        movie.setProducers(
			        		Arrays.asList(row[3].split(","))
			        		.stream().map((p) -> new Producer(p))
			        		.collect(Collectors.toList()));
			        movie.setWinner(row.length > 4);
			        return movie;
				}).collect(Collectors.toList());
		
		
		movieRepository.saveAll(records);
		
		System.out.println(records);
	}
	
	private Set<Producer> mapProducers(List<String[]> csvData) {
		return csvData.stream()
			.map(row -> row[3].split(","))
			.flatMap(Stream::of)
			.distinct()
	        .map(Producer::new)
	        .collect(Collectors.toSet());
	}
	
	private Set<Movie> mapMovies(List<String[]> csvData) {
		return null;
	}
	
	private List<String[]> readCsv() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:movielist.csv")))) {
			return br.lines().map(l -> l.split(";")).skip(1).collect(Collectors.toList());
		}
	}
}
