import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;
import static org.apache.tinkerpop.gremlin.structure.T.id;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class App {

    public static void main(final String[] args) {

        final GraphTraversalSource g = TinkerGraph.open().traversal();

        g.addV("person").property(id, 1).property("name", "bob").as("a").
                addV("person").property(id, 2).property("name", "robert").property("nick", "bob").as("b").
                addE("knows").from("a").to("b").iterate();

        g.V(1).as("t").out().as("r").
                filter(select("t").by("name").as("tn").select("r").by("nick").as("rn").where("tn", eq("rn"))).
                forEachRemaining(System.out::println);
    }
}
