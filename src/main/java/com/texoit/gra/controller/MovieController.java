package com.texoit.gra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.gra.projection.PrizeIntervalProjection;
import com.texoit.gra.repository.MovieRepository;

@RestController
@RequestMapping("movies")
public class MovieController {

	@Autowired
	private MovieRepository repository;
	
	@GetMapping("")
    public List<PrizeIntervalProjection> list() {
        return repository.findAllTest();       
    }
}
