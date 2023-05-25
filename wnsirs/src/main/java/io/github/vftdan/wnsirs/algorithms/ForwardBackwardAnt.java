package io.github.vftdan.wnsirs.algorithms;

import java.util.ArrayList;
import java.util.List;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Context.GetScheduler;
import io.github.vftdan.wnsirs.methods.*;
import io.github.vftdan.wnsirs.util.BareLinkedList;

import javafx.util.Pair;

public class ForwardBackwardAnt extends SimpleAlgorithmPart {
	{
		defaultRole = "generalAntBehavior";
		registerImplementation(new LaunchAntImplementation());
		registerImplementation(new TransitionAntImplementation());
		registerImplementation(new AntArrivedImplementation());
		registerImplementation(new UpdateHistoryImplementation());
		registerImplementation(new IncreasePheromoneImplementation());
	}

	@Override
	public String toString() {
		return "Forward and backward ant movement";
	}

	public static class LaunchAntImplementation extends LaunchAnt.Implementation {
		@Override
		protected Ant createAnt(Context ctx) {
			var ant = new Ant();
			ant.callMethod(SetAntBackward.getInstance(), false);
			return ant;
		}

		{
			var oldDependencies = dependencies;
			dependencies = new Dependency[oldDependencies.length + 1];
			for (int i = 0; i < oldDependencies.length; ++i)
				dependencies[i] = oldDependencies[i];
			dependencies[oldDependencies.length] = Dependency.fromDescriptor(SetAntBackward.getInstance());
		}
	}

	public static class TransitionAntImplementation extends MethodImplementation<Void, Void> {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return TransitionAnt.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			if (root.callMethod(GetAntBackward.getInstance(), null)) {
				root.callMethod(IncreasePheromone.Emitter.getInstance(), null);
				var node = root.callMethod(GetPreviousNode.getInstance(), null);
				if (node == null) {
					root.callMethod(DestroyAnt.Emitter.getInstance(), null);
				} else {
					root.callMethod(SendAntTo.getInstance(), node);
				}
			} else {
				var neighbors = new ArrayList<Node>(root.callMethod(GetNeighbors.getInstance(), null));
				var edges = new ArrayList<Edge>(neighbors.size());
				var node = root.callMethod(GetNode.getInstance());
				for (int i = 0; i < neighbors.size(); ++i) {
					edges.add(root.callMethod(GetEdgeBetween.getInstance(), new Pair<Node, Node>(node, neighbors.get(i))));
				}
				root.callMethod(SetConsideredEdges.getInstance(), edges);
				root.callMethod(GetScheduler.getInstance()).scheduleTask(0, (Context) root, TransitionAntForCandidates.getInstance(), neighbors);
			}
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetAntBackward.getInstance()),
				Dependency.fromDescriptor(IncreasePheromone.Emitter.getInstance()),
				Dependency.fromDescriptor(GetPreviousNode.getInstance()),
				Dependency.fromDescriptor(DestroyAnt.Emitter.getInstance()),
				Dependency.fromDescriptor(SendAntTo.getInstance()),
				Dependency.fromDescriptor(GetNeighbors.getInstance()),
				Dependency.fromDescriptor(GetNode.getInstance()),
				Dependency.fromDescriptor(GetEdgeBetween.getInstance()),
				Dependency.fromDescriptor(SetConsideredEdges.getInstance()),
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
		}
	}

	public static class AntArrivedImplementation extends MethodImplementation<Void, Void> {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return AntArrived.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			root.callMethod(GetScheduler.getInstance()).scheduleTask(0, (Context) root, WaitEvaluateAndSendBack.getInstance(), null);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
		}
	}

	public static class UpdateHistoryImplementation extends MethodImplementation<Node, Void> {
		@Override
		public MethodDescriptor<? extends Node, ? super Void> implementationFor() {
			return UpdateHistory.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Node args) {
			var history = root.callMethod(GetNodeHistory.getInstance());
			if (root.callMethod(GetAntBackward.getInstance())) {
				root.callMethod(SetNodeHistory.getInstance(), history.next);
				var ctx = (Context) root;  // TODO consider moving to another/separate method
				var edge = root.callMethod(GetEdgeBetween.getInstance(), new Pair<Node, Node>(ctx.callMethod(GetNextNode.getInstance()), root.callMethod(GetNode.getInstance())));
				ctx.setPart(edge);
			} else {
				root.callMethod(SetNodeHistory.getInstance(), new BareLinkedList<Node>(args, history));
			}
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(SetNodeHistory.getInstance()),
				Dependency.fromDescriptor(GetAntBackward.getInstance()),
			};
		}
	}

	public static class IncreasePheromoneImplementation extends MethodImplementation<Void, Void> {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return IncreasePheromone.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			double pheromone = root.callMethod(GetPheromone.getInstance());
			var history = root.callMethod(GetTotalNodeHistory.getInstance());
			double amount = root.callMethod(GetAntFitness.getInstance());
			if (history != null)
				amount /= history.length;
			pheromone += amount;
			root.callMethod(SetPheromone.getInstance(), pheromone);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetPheromone.getInstance()),
				Dependency.fromDescriptor(SetPheromone.getInstance()),
			};
		}
	}
}
