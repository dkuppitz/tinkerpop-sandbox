import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class App {

    public static void main(final String[] args) {

        final Graph g = new TinkerGraph();

        for (int i = 1; i <= 4; i++) {
            g.addVertex("S" + i).setProperty("type", "sender");
            g.addVertex("R" + i).setProperty("type", "receiver");
        }

        g.getVertex("S1").addEdge("tx", g.getVertex("R1"));
        g.getVertex("S2").addEdge("tx", g.getVertex("R2"));
        g.getVertex("S2").addEdge("tx", g.getVertex("R3"));
        g.getVertex("S3").addEdge("tx", g.getVertex("R4"));
        g.getVertex("S4").addEdge("tx", g.getVertex("R4"));

        System.out.println("\n== one sender to many receivers ==");
        new GremlinPipeline<>(g).V().has("type", "sender").as("s").cast(Vertex.class).
                filter(v -> new GremlinPipeline<>(v).outE("tx").range(0, 2).count() > 1).
                transform(v -> new GremlinPipeline<>(v).outE("tx").inV().path().cast(List.class).
                        transform(p -> p.subList(1, 3)).toList()).as("r").
                select(Arrays.asList("s", "r")).forEachRemaining(System.out::println);


        System.out.println("\n== many senders to one receiver ==");
        new GremlinPipeline<>(g).V().has("type", "receiver").as("r").cast(Vertex.class).
                filter(v -> new GremlinPipeline<>(v).inE("tx").range(0, 2).count() > 1).
                transform(v -> new GremlinPipeline<>(v).inE("tx").outV().path().cast(List.class).
                        transform(p -> p.subList(1, 3)).toList()).as("r").
                select(Arrays.asList("s", "r")).forEachRemaining(System.out::println);
    }
}
