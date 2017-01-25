package com.izbicki.jakub.service;

import com.izbicki.jakub.entity.Movie;
import com.izbicki.jakub.error.ApiCustomException;
import com.izbicki.jakub.error.ApiNotFoundException;
import com.izbicki.jakub.error.ErrorCodes;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("MovieService")
public class MovieService {

    @Autowired
    private ResourceBundleMessageSource exceptionMessageSource, applicationConfigMessageSource;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    @Qualifier("CastService")
    private CastService cs;

    public ResponseEntity selectAll() {

        List<Movie> moviesList = new ArrayList<>();

        for (Movie movie : movieRepository.findAll()) {

            moviesList.add(movie);
        }
        return ResponseEntity.ok(moviesList);
    }

    public ResponseEntity selectMovie(long id) {

        Movie movie = movieRepository.findOne(id);

        if (movie == null)
            throw new ApiNotFoundException("movie");

        return ResponseEntity.ok(movie);
    }

    public ResponseEntity insert(String title, String desc, int movieType, BigDecimal price) {


        if (!Arrays.asList(0, 1, 2).contains(movieType))
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM),
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM_USER));

        MovieType type = MovieType.values()[movieType];

        Movie movie = new Movie(title, desc, type, price, true);

        movieRepository.save(movie);

        String location = "/movies/" + movie.getId().toString();

        System.out.println(location);

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "asa").body(movie);
    }

    public ResponseEntity remove(long id) {

        cs.removeCastOfMovie(id);

        movieRepository.delete(id);

        return selectAll();
    }

    public ResponseEntity update(long id, String title, String desc, Integer type, Float price) {

        if (movieRepository.findOne(id) == null)
            throw new ApiNotFoundException("movie");

        if (title != null)
            movieRepository.updateMovieTitle(id, title);
        if (desc != null)
            movieRepository.updateMovieDesc(id, desc);
        if (type != null)
            movieRepository.updateMovieType(id, type);
        if (price != null)
            movieRepository.updateMoviePrice(id, price);

        return ResponseEntity.ok(movieRepository.findOne(id));
    }

    public ResponseEntity selectNewest() {

        List<Movie> moviesList = new ArrayList<>();

        for (Movie movie : movieRepository.selectAvailableNewest())
            moviesList.add(movie);

        return ResponseEntity.ok(moviesList);
    }

    public ResponseEntity selectHits() {

        List<Movie> moviesList = new ArrayList<>();

        for (Movie movie : movieRepository.selectAvailableHits())
            moviesList.add(movie);

        return ResponseEntity.ok(moviesList);
    }

    public ResponseEntity selectOther() {

        List<Movie> moviesList = new ArrayList<>();

        for (Movie movie : movieRepository.selectAvailableOther())
            moviesList.add(movie);

        return ResponseEntity.ok(moviesList);
    }

    public ResponseEntity selectAvailable(Integer category) {

        List<Movie> moviesList = new ArrayList<>();

        if (category == null) {
            moviesList = movieRepository.selectAvailable();

        } else if (!Arrays.asList(0, 1, 2).contains(category)) {

            throw new ApiCustomException(HttpStatus.BAD_REQUEST,
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM),
                    getExceptionMsgSource(ErrorCodes.BAD_MOVIE_ENUM_USER));
        } else {

            MovieType movieType = MovieType.values()[category];

            if (movieType == MovieType.newest)
                moviesList = movieRepository.selectAvailableNewest();
            else if (movieType == MovieType.hits)
                moviesList = movieRepository.selectAvailableHits();
            else if (movieType == MovieType.other)
                moviesList = movieRepository.selectAvailableOther();

        }

        String maxAge = "max-age=" + applicationConfigMessageSource.getMessage(
                "CACHE_MAX_TIME", null, null, null);

        return ResponseEntity.status(HttpStatus.OK).header("Cache-Control", maxAge).body(moviesList);
    }

    /**
     * Retrives exception message from String's message source
     */
    private String getExceptionMsgSource(String msgCode) {

        return exceptionMessageSource.getMessage(
                msgCode, null, "Something went wrong.", null);
    }
}
