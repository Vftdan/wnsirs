package io.github.vftdan.wnsirs;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;

/**
 * Hello world!
 *
 */
public class App {
	public static void main( String[] args ) {
		System.out.println("Hello World!");
		System.out.println(GetEdge.getInstance());
		System.out.println(SetPheromone.getInstance());
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
					return null;
				}
			});
		}};
		var algo = new Algorithm();
		algo.setPart(part);
		var ctx = new Context();
		ctx.setPart(algo);
		ctx.setPart(new Edge());
		System.out.println("part.getMethods().size() = " + part.getMethods().size());
		System.out.println("algo.getMethods().size() = " + algo.getMethods().size());
		System.out.println("ctx.getMethods().size() = " + ctx.getMethods().size());
		System.out.println("part.collectAllMethodImplementations(InitializeSimulation) -> {");
		part.collectAllMethodImplementations(InitializeSimulation.getInstance(), (imp) -> {
			System.out.println(imp);
		});
		System.out.println("}");
		System.out.println("algo.collectAllMethodImplementations(InitializeSimulation) -> {");
		algo.collectAllMethodImplementations(InitializeSimulation.getInstance(), (imp) -> {
			System.out.println(imp);
		});
		System.out.println("}");
		System.out.println("ctx.collectAllMethodImplementations(InitializeSimulation) -> {");
		ctx.collectAllMethodImplementations(InitializeSimulation.getInstance(), (imp) -> {
			System.out.println(imp);
		});
		System.out.println("}");
		ctx.callMethod(ctx, InitializeSimulation.Emitter.getInstance(), null);
		var edge = ctx.callMethod(ctx, GetEdge.getInstance(), null);
		System.out.println("edge = " + edge);
		System.out.println("edge.storedValues = " + edge.getValueStorage());
		System.out.println("getPheromone() = " + ctx.callMethod(ctx, GetPheromone.getInstance(), null));
		System.out.println("setPheromone(1) = " + ctx.callMethod(ctx, SetPheromone.getInstance(), (Double) 1.0));
		System.out.println("edge.storedValues[\"pheromone\"] = " + edge.getValueStorage().getStored("pheromone", false));
		System.out.println("getPheromone() = " + ctx.callMethod(ctx, GetPheromone.getInstance(), null));
	}
}
