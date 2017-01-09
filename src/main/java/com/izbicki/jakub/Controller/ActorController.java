package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ActorController {

    @Autowired @Qualifier("ActorService")
    private ActorService as;

    @RequestMapping(value = "/actors", method = GET)
    public List<Actor> selectAllActors(){

        return as.selectAll();
    }

    @RequestMapping(value = "/actors/{id}", method = GET)
    public Actor selectActorWhereId(@PathVariable("id") long id){

        return as.select(id);
    }

    @RequestMapping(value = "admin/actors/insert", method = PUT)
    public Actor insertActor(@RequestParam(value = "name") String name  ){

        return as.insert(name);
    }

    @RequestMapping(value = "admin/actors/remove/{id}", method = DELETE)
    public List<Actor> removeActorWhereId(@PathVariable("id") long id){

        return as.remove(id);
    }

    @RequestMapping(value = "admin/actors/update/{id}", method = POST)
    public Actor updateActorWhereId(@PathVariable("id") long id,
                                    @RequestParam(value="name") String name){

        return as.update(id, name);
    }
}
