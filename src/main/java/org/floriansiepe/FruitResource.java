package org.floriansiepe;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

    // In-Memory "database"
    private final Set<Fruit> fruits = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public FruitResource() {
        // We are mocking a database
        fruits.add(new Fruit(1L, "Apple", "Winter fruit"));
        fruits.add(new Fruit(2L, "Pineapple", "Tropical fruit"));
        fruits.add(new Fruit(3L, "Strawberry", "Summer fruit"));
    }

    @GET
    public Set<Fruit> list() {
        return fruits;
    }

    @GET
    @Path("{id}")
    public Fruit byId(@PathParam("id") Long id) {
        return fruits.stream()
                .filter(fruit -> fruit.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No fruit with id " + id + "found"));
    }

    @PUT
    @Path("{id}")
    public Fruit update(@PathParam("id") Long id, Fruit fruit) {
        if (id > 1000) {
            throw new BadRequestException("Ids too big");
        }
        fruit.id = id;
        delete(id);
        fruits.add(fruit);
        return fruit;
    }

    @POST
    public Fruit add(Fruit fruit) {
        fruit.id = (long) (Math.random() * 1000);
        fruits.add(fruit);
        return fruit;
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        fruits.removeIf(existingFruit -> existingFruit.id.equals(id));
    }

}
