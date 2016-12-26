import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.Scope.local;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.constant;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.count;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.label;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class App {

    public static double matchability(GraphTraversalSource g, Vertex a, Vertex b) {
        int matchedQues = 0;
        int matchedAns = 0;

        final List<String> list1 = g.V(a).outE().label().toList();
        System.out.println("total answered questions :" + list1);

        final List<String> list2 = g.V(b).outE().label().toList();
        System.out.println("total answered questions :" + list2);

        if (list1.size() > list2.size()) {
            for (int i = 0; i < list2.size(); i++) {
                if (list1.contains(list2.get(i))) {
                    matchedQues++;

                    if (g.V(a).outE(list2.get(i).toString()).next().inVertex().property("ans").toString().equals(g.V(b).outE(list2.get(i).toString()).next().inVertex().property("ans").toString())) {
                        matchedAns++;
                    }

                }
            }

            System.out.println("matched questions :" + matchedQues);

            System.out.println("matched ans :" + matchedAns);

        } else {
            for (int i = 0; i < list1.size(); i++) {
                if (list2.contains(list1.get(i))) {
                    matchedQues++;

                    if (g.V(a).outE(list1.get(i).toString()).next().inVertex().property("ans").toString().equals(g.V(b).outE(list1.get(i).toString()).next().inVertex().property("ans").toString())) {
                        matchedAns++;
                    }

                }
            }

            System.out.println("matched questions :" + matchedQues);

            System.out.println("matched ans :" + matchedAns);
        }

        double per = (matchedAns * 100) / matchedQues;
        return per;
    }

    public static double matchability2(GraphTraversalSource g, Vertex a, Vertex b) {
        /*
        return g.withSack(0).V(a).outE().aggregate("x").by(label).limit(1)
                .V(b).outE().as("e").label().as("l").where(select("x").unfold().as("l"))
                .store("matchedQues").by(constant(1))
                .V(a).outE().where(label().as("l")).inV().as("a1")
                .select("e").inV().as("a2").where("a1", eq("a2")).by("ans")
                .aggregate("matchedAns").limit(1)
                .sack(assign).by(select("matchedAns").count(Scope.local))
                .sack(mult).by(constant(100))
                .sack(div).by(select("matchedQues").count(Scope.local)).<Long>sack()
                .tryNext().orElse(0L);*/
        final Set<String> x = g.V(a).outE().label().toSet();
        final Map<String, Long> m = g.withSideEffect("x", x).V(b).outE().as("e").label().as("l")
                .where(select("x").unfold().as("l"))
                .store("matchedQues").by(constant(1))
                .constant(a).outE().where(label().as("l")).inV().as("a1")
                .select("e").inV().as("a2").filter(select("a1", "a2").by("ans").where("a1", eq("a2")))
                .aggregate("matchedAns").by(constant(1)).cap("matchedAns", "matchedQues")
                .<Long>select("matchedAns", "matchedQues").by(count(local)).next();
        return (m.get("matchedAns") * 100) / m.get("matchedQues");
    }

    public static void main(String[] args) throws Exception {

        final Graph graph = TinkerGraph.open();
        final GraphTraversalSource g = graph.traversal();

        final Vertex mayank = graph.addVertex("name", "mayank");
        final Vertex mque1 = graph.addVertex("ans", "yes");
        mayank.addEdge("que1", mque1);
        final Vertex mque2 = graph.addVertex("ans", "yes");
        mayank.addEdge("que2", mque2);
        final Vertex mque3 = graph.addVertex("ans", "yes");
        mayank.addEdge("que3", mque3);
        final Vertex mque5 = graph.addVertex("ans", "no");
        mayank.addEdge("que5", mque5);
        final Vertex mque6 = graph.addVertex("ans", "yes");
        mayank.addEdge("que6", mque6);
        final Vertex mque7 = graph.addVertex("ans", "yes");
        mayank.addEdge("que7", mque7);
        final Vertex mque8 = graph.addVertex("ans", "yes");
        mayank.addEdge("que8", mque8);
        final Vertex mque9 = graph.addVertex("ans", "yes");
        mayank.addEdge("que9", mque9);
        final Vertex mque10 = graph.addVertex("ans", "no");
        mayank.addEdge("que10", mque10);
        final Vertex mque12 = graph.addVertex("ans", "yes");
        mayank.addEdge("que12", mque12);
        final Vertex mque13 = graph.addVertex("ans", "yes");
        mayank.addEdge("que13", mque13);
        final Vertex mque14 = graph.addVertex("ans", "no");
        mayank.addEdge("que14", mque14);
        final Vertex mque15 = graph.addVertex("ans", "yes");
        mayank.addEdge("que15", mque15);

        final Vertex poonam = graph.addVertex("name", "poonam");
        final Vertex pque1 = graph.addVertex("ans", "yes");
        poonam.addEdge("que1", pque1);
        final Vertex pque2 = graph.addVertex("ans", "yes");
        poonam.addEdge("que2", pque2);
        final Vertex pque3 = graph.addVertex("ans", "yes");
        poonam.addEdge("que3", pque3);
        final Vertex pque4 = graph.addVertex("ans", "no");
        poonam.addEdge("que4", pque4);
        final Vertex pque5 = graph.addVertex("ans", "yes");
        poonam.addEdge("que5", pque5);
        final Vertex pque6 = graph.addVertex("ans", "yes");
        poonam.addEdge("que6", pque6);
        final Vertex pque7 = graph.addVertex("ans", "yes");
        poonam.addEdge("que7", pque7);
        final Vertex pque8 = graph.addVertex("ans", "no");
        poonam.addEdge("que8", pque8);
        final Vertex pque10 = graph.addVertex("ans", "no");
        poonam.addEdge("que10", pque10);
        final Vertex pque11 = graph.addVertex("ans", "yes");
        poonam.addEdge("que11", pque11);
        final Vertex pque12 = graph.addVertex("ans", "yes");
        poonam.addEdge("que12", pque12);
        final Vertex pque13 = graph.addVertex("ans", "yes");
        poonam.addEdge("que13", pque13);
        final Vertex bhoomi = graph.addVertex("name", "bhoomi");
        final Vertex bque2 = graph.addVertex("ans", "yes");
        bhoomi.addEdge("que2", bque2);
        final Vertex bque3 = graph.addVertex("ans", "yes");
        bhoomi.addEdge("que3", bque3);
        final Vertex bque4 = graph.addVertex("ans", "no");
        bhoomi.addEdge("que4", bque4);
        final Vertex bque5 = graph.addVertex("ans", "yes");
        bhoomi.addEdge("que5", bque5);
        final Vertex bque6 = graph.addVertex("ans", "no");
        bhoomi.addEdge("que6", bque6);
        final Vertex bque7 = graph.addVertex("ans", "yes");
        bhoomi.addEdge("que7", bque7);
        final Vertex bque8 = graph.addVertex("ans", "no");
        bhoomi.addEdge("que8", bque8);
        final Vertex bque9 = graph.addVertex("ans", "no");
        bhoomi.addEdge("que9", bque9);

        System.out.println("graph is " + graph);
        System.out.println("Graph created.....");
        System.out.println();

        double i = matchability(g, mayank, poonam);
        double i2 = matchability2(g, mayank, poonam);
        System.out.println("matchability between mayank and poonam is :" + i + "%");
        System.out.println("matchability between mayank and poonam is :" + i2 + "%");
        System.out.println();

        double j = matchability(g, mayank, bhoomi);
        double j2 = matchability2(g, mayank, bhoomi);
        System.out.println("matchability between mayank and bhoomi is :" + j + "%");
        System.out.println("matchability between mayank and bhoomi is :" + j2 + "%");
        System.out.println();

        double k = matchability(g, bhoomi, poonam);
        double k2 = matchability2(g, bhoomi, poonam);
        System.out.println("matchability between bhoomi and poonam is :" + k + "%");
        System.out.println("matchability between bhoomi and poonam is :" + k2 + "%");
        System.out.println();
    }
}
