package io.github.vftdan.wnsirs;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;
import io.github.vftdan.wnsirs.algorithms.*;

/**
 * Hello world!
 *
 */
public class App {
	public static void main( String[] args ) {
		System.out.println("Hello World!");
		System.out.println(GetEdge.getInstance());
		System.out.println(SetPheromone.getInstance());
		var scheduler = new Scheduler();
		var part = new SimpleAlgorithmPart() {{
			defaultRole = "demoInitialization";
			registerImplementation(new MethodImplementation<Void, Void>() {
				@Override
				public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
					return InitializeSimulation.getInstance();
				}

				@Override
				public Void call(AlgorithmPart root, Void args) {
					System.out.println("Initialize!");
					var ctx = (Context) root;
					var nodes = new ArrayList<Node>(16);
					var sink = new Node(new double[] {.5, .5});
					for (int x = 0; x < 4; ++x) {
						for (int y = 0; y < 4; ++y) {
							var node = new Node(new double[] {x, y});
							nodes.add(node);
						}
					}
					nodes.add(sink);
					for (int i = 0; i < nodes.size() - 1; ++i) {
						var node = nodes.get(i);
						var neighbors = new HashSet<Node>(nodes);
						neighbors.remove(node);
						node.neighbors = neighbors;
						int k = i;
						for (int j = 0; k > 0; k >>= 1, ++j) {
							if ((k & 1) != 0) {
								var newCtx = ctx.clone();
								newCtx.setPart(node);
								scheduler.scheduleTask(j, newCtx, LaunchAnt.getInstance(), sink);
							}
						}
					}
					return null;
				}
			});
		}};
		var net = new Network();
		var algo = new Algorithm();
		algo.setPart(new ForwardBackwardAnt());
		algo.setPart(part);
		var ctx = new Context(scheduler);
		ctx.setPart(algo);
		ctx.setPart(net);
		scheduler.scheduleTask(0, ctx, InitializeSimulation.getInstance(), null);
		try {
			scheduler.schedulingLoop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		scheduler.terminate();
		for (var edge: net.getEdges().values()) {
			var startPos = edge.getStart().getPosition();
			var endPos = edge.getEnd().getPosition();
			System.out.println("Edge (" + startPos[0] + ", " + startPos[1] + ") -> (" + endPos[0] + ", " + endPos[1] + ")");
			System.out.println("Pheromone = " + edge.callMethod(GetPheromone.getInstance()));
		}
	}
}
