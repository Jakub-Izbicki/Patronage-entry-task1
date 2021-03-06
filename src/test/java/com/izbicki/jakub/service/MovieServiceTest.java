package com.izbicki.jakub.service;


import com.izbicki.jakub.entity.Actor;
import com.izbicki.jakub.entity.Cast;
import com.izbicki.jakub.entity.Movie;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.error.ApiNotFoundException;
import com.izbicki.jakub.repository.ActorRepository;
import com.izbicki.jakub.repository.CastRepository;
import com.izbicki.jakub.repository.MovieRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MovieServiceTest {



    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CastRepository castRepository;

    @Autowired
    private MovieService movieService;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        movieRepository.save(new Movie("Atak pająków", "Oskar za fabułę", MovieType.newest, new BigDecimal("10"), true));
        movieRepository.save(new Movie("Atak pająków 2", "Zmutowane pająki", MovieType.newest, new BigDecimal("10"), true));
        movieRepository.save(new Movie("Atak pająków 3", "Zakończenie epickiej trylogii.", MovieType.newest, new BigDecimal("10"), true));

        actorRepository.save(new Actor("Bruce Willis"));
        actorRepository.save(new Actor("Jackie Chan"));

        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(1L)));
        castRepository.save(new Cast(movieRepository.findOne(1L), actorRepository.findOne(2L)));
    }

    @Test
    public void testFindAll(){

        List<Movie> movieList = (List<Movie>) movieService.selectAll().getBody();

        Assert.assertNotNull("Failure - expected not null", movieList);
        Assert.assertEquals("Failure - expected list size", 30,  movieList.size());
    }

    @Test
    public void testFindOne(){

        Long id = 1L;

        Movie movie = (Movie) movieService.selectMovie(id).getBody();

        Assert.assertNotNull("failure - expected not null", movie);
        Assert.assertEquals("failure - expected id attribute match", id,
                movie.getId());
    }

    @Test(expected = ApiNotFoundException.class)
    public void testFindOneNotFound() {

        Long id = Long.MAX_VALUE;

        Movie movie = (Movie) movieService.selectMovie(id).getBody();

        Assert.assertNull("failure - expected null", movie);
    }

    @Test
    public void testInsert() {

        Movie createdMovie = (Movie) movieService.insert("title1", "desc1", 0, new BigDecimal("10")).getBody();

        Assert.assertNotNull("failure - expected not null", createdMovie);
        Assert.assertNotNull("failure - expected id attribute not null", createdMovie.getId());
        Assert.assertEquals("failure - expected text attribute match", "title1", createdMovie.getTitle());

        List<Movie> movieList = (List<Movie>) movieService.selectAll().getBody();

        Assert.assertEquals("failure - expected size", 31, movieList.size());
    }

    @Test
    public void testUpdate() {

        Long id = 1L;

        Movie movie = (Movie) movieService.selectMovie(id).getBody();

        Assert.assertNotNull("failure - expected not null", movie);

        Movie updatedMovie = (Movie) movieService.update(id, "title2", null, null, null).getBody();

        Assert.assertNotNull("failure - expected not null", updatedMovie);
        Assert.assertEquals("failure - expected id attribute match", id,
                updatedMovie.getId());
    }

    @Test(expected = ApiNotFoundException.class)
    public void testDelete() {

        Long id = 1L;

        Movie movie = (Movie) movieService.selectMovie(id).getBody();

        Assert.assertNotNull("failure - expected not null", movie);

        movieService.remove(id);

        List<Movie> movieList = (List<Movie>) movieService.selectAll().getBody();

        Assert.assertEquals("failure - expected size", 29, movieList.size());

        Movie deletedMovie = (Movie) movieService.selectMovie(id).getBody();

        Assert.assertNull("failure - expected null", deletedMovie);

    }
}
