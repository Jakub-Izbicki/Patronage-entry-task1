package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Service.CastService;
import com.izbicki.jakub.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class MovieController {

    @Autowired @Qualifier("MovieService")
    private MovieService ms;

    @Autowired @Qualifier("CastService")
    private CastService cs;

    @RequestMapping(value = "/admin/movies", method = GET)
    public ResponseEntity selectAllMovies(){

        return ms.selectAll();
    }

    @RequestMapping(value = "/movies/{id}", method = GET)
    public ResponseEntity selectMovieWhereId(@PathVariable("id") long id){

        return ms.selectMovie(id);
    }

    @RequestMapping(value = "/admin/movies", method = POST)
    public ResponseEntity insertMovie(@RequestParam(value = "title") String title,
                             @RequestParam(value = "desc") String desc,
                             @RequestParam(value = "type") int type,
                             @RequestParam(value = "price") BigDecimal price){



        return ms.insert(title, desc, type, price);
    }

    @RequestMapping(value = "/admin/movies/{id}", method = DELETE)
    public ResponseEntity removeMovieWhereId(@PathVariable("id") Long id){

        return ms.remove(id);
    }

    @RequestMapping(value = "/admin/movies/{id}", method = PUT)
    public ResponseEntity updateMovie(@PathVariable("id") int id,
                             @RequestParam(value="title", required = false) String title,
                             @RequestParam(value="desc", required = false) String desc,
                             @RequestParam(value="type", required = false) Integer type,
                             @RequestParam(value="price", required = false) Float price){

        return ms.update(id, title, desc, type, price);
    }

    @RequestMapping(value = "/movies/{id}/actors", method = GET)
    public ResponseEntity updateMovie(@PathVariable("id") Long id){

        return cs.getActorsForMovie(id);
    }

    @RequestMapping(value = "/admin/movies/{id}/actors/{actorId}", method = POST)
    private ResponseEntity insertCast(@PathVariable(value = "id")Long movieId,
                            @PathVariable(value = "actorId")Long actorId){

        return cs.insert(movieId, actorId);
    }

    @RequestMapping(value = "/admin/movies/{id}/actors/{actorId}", method = DELETE)
    private ResponseEntity removeCast(@PathVariable(value = "id") Long movieId,
                                  @PathVariable(value = "actorId")Long actorId){

        return cs.remove(movieId, actorId);
    }

    @RequestMapping(value = "/movies/newest", method = GET)
    private ResponseEntity selectNewest(){

        return ms.selectNewest();
    }

    @RequestMapping(value = "/movies/hits", method = GET)
    private ResponseEntity selectHits(){

        return ms.selectHits();
    }

    @RequestMapping(value = "/movies/other", method = GET)
    private ResponseEntity selectOther(){

        return ms.selectOther();
    }

    @RequestMapping(value = "/movies/available", method = GET)
    private ResponseEntity selectAvaliable(){

        return ms.selectAvaliable();
    }
}
