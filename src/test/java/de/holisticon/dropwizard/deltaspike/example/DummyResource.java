package de.holisticon.dropwizard.deltaspike.example;

import com.google.common.base.Supplier;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {

    @Named
    public static class Foo implements Supplier<String> {

        @Override
        public String get() {
            return "foo";
        }
    }

    private Foo foo = new Foo();



    @GET
    public String hellWorld() {
        return "hello " + foo.get();
    }

}
