import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import com.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.Date;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class App {

    public static void main(final String[] args) {

        final TinkerGraph g = TinkerFactory.createModern();
        final Vertex marko = g.V(1).next();
        final Vertex vadas = g.V(2).next();
        final Model model = new Model();

        marko.property("date", System.currentTimeMillis());

        marko.<Long>values("date").tryNext().map(Date::new).ifPresent(model::setDate);
        vadas.<Long>values("date").tryNext().map(Date::new).ifPresent(model::setDate);
    }

    static class Model {

        public void setDate(final Date date) {
            System.out.println("Setting date in model: " + date);
        }
    }
}
