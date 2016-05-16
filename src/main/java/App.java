import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;

import java.util.Random;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.P.lt;
import static org.apache.tinkerpop.gremlin.process.traversal.P.neq;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.as;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.not;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;
import static org.apache.tinkerpop.gremlin.structure.T.id;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class App {

    public static void main(final String[] args) {

        final GraphTraversalSource g = TinkerFactory.createModern().traversal();
        final Random rand = new Random();

        System.out.println("\n-- extend vertex properties");
        g.V().sideEffect(t -> t.get().property("id", rand.nextInt(5))).valueMap().
                forEachRemaining(System.out::println);

        System.out.println("\n-- 1st iteration");
        g.V().as("a").aggregate("x").select("x").unfold().where(neq("a")).as("b").
                filter(select("a", "b").by(id).where("a", lt("b"))).
                filter(select("a", "b").by("id").where("a", eq("b"))).
                where(not(as("a").out("related").as("b"))).
                addOutE("a", "related", "b").forEachRemaining(System.out::println);

        System.out.println("\n-- 2nd iteration");
        g.V().as("a").aggregate("x").select("x").unfold().where(neq("a")).as("b").
                filter(select("a", "b").by(id).where("a", lt("b"))).
                filter(select("a", "b").by("id").where("a", eq("b"))).
                where(not(as("a").out("related").as("b"))).
                addOutE("a", "related", "b").forEachRemaining(System.out::println);
    }
}
