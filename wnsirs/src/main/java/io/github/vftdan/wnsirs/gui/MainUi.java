package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Node;
import io.github.vftdan.wnsirs.methods.*;
import io.github.vftdan.wnsirs.util.*;

import java.util.*;
import java.util.function.*;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.stage.*;

public class MainUi extends Application {
	NetworkVisualization networkVisualization = new NetworkVisualization(null);
	GridPane mainPane = new GridPane();
	Button addParameterButton = new Button("Add parameter");
	Button runSimulationButton = new Button("Run simulation");
	ParameterList parameters = new ParameterList();
	Map<String, Supplier<ParameterChooser> > parameterFactories = new HashMap<String, Supplier<ParameterChooser> >() {{
		put("generalAntBehavior", () -> new GeneralAntBehaviorParameterChooser());
		put("deltaPheromoneFormula", () -> new DeltaPheromoneParameterChooser());
	}};
	Thread schedulingThread = null;
	Thread spawnThread = null;
	Scheduler scheduler;

	{
		mainPane.add(networkVisualization.getCanvas(), 1, 0, 1, 3);
		mainPane.add(parameters.getUiNode(), 0, 0);
		mainPane.add(addParameterButton, 0, 1);
		mainPane.add(runSimulationButton, 0, 2);
		addParameterButton.setOnAction((e) -> {addParameter();}); 
		runSimulationButton.setOnAction((e) -> {runSimulation();}); 
		networkVisualization.getWorldTransformation().appendScale(80, 80);
		networkVisualization.getWorldTransformation().appendTranslation(.5, .5);
	}

	public void addParameter() {
		parameters.addFrom(parameterFactories);
	}

	public void runSimulation() {
		if (spawnThread != null) {
			if (spawnThread.isAlive())
				spawnThread.interrupt();
			try {
				spawnThread.join(5000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			spawnThread = null;
		}
		spawnThread = new Thread(() -> {
			for (var ctx: parameters.applyAll(() -> {
				scheduler = new Scheduler();
				var ctx = new Context(scheduler);
				var part = new SimpleAlgorithmPart() {{
					defaultRole = "demoInitialization";
					registerImplementation(new MethodImplementation<Void, Void>() {
						@Override
						public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
							return InitializeSimulation.getInstance();
						}

						@Override
						public Void call(AlgorithmPart root, Void args) {
							var ctx = (Context) root;
							var nodes = new ArrayList<Node>(16);
							var sink = new Node(new double[] {.5, .5});
							var net = root.callMethod(GetNetwork.getInstance());
							var nodeSet = net.getNodes();
							for (int x = 0; x < 4; ++x) {
								for (int y = 0; y < 4; ++y) {
									var node = new Node(new double[] {x + Math.sin(y * 2) / 8, y + Math.sin(x * 2.5) / 9});
									nodes.add(node);
									nodeSet.add(node);
								}
							}
							nodes.add(sink);
							nodeSet.add(sink);
							for (var node: nodes) {
								var visElement = new NetworkVisualization.Element<Node>(node) {
									@Override
									protected void draw(NetworkVisualization visualization, GraphicsContext ctx) {
										ctx.setFill(Color.BLACK);
										var screenPos = visualization.getWorldTransformation().transform(getX(), getY());
										double r = 8;
										ctx.fillOval(screenPos.getX() - r, screenPos.getY() - r, r * 2, r * 2);
									}

									@Override
									public double getX() {
										return node.getPosition()[0];
									}

									@Override
									public double getY() {
										return node.getPosition()[1];
									}
								};
								node.getValueStorage().setValue("visualization", visElement);
								networkVisualization.addElement(visElement);
							}
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
					registerImplementation(new MethodImplementation<Edge, Void>() {
						@Override
						public MethodDescriptor<? extends Edge, ? super Void> implementationFor() {
							return EdgeCreated.getInstance();
						}

						@Override
						public Void call(AlgorithmPart root, Edge edge) {
							var visElement = new NetworkVisualization.Element<Edge>(edge) {
								@Override
								protected void draw(NetworkVisualization visualization, GraphicsContext ctx) {
									ctx.setStroke(new Color(0, 0, 0, 0.1));
									var method = GetPheromone.getInstance();
									double pheromone;
									var lock = new CompositeLock(edge.getMethodImplementation(method).getLocks(edge, null));
									lock.lock();
									try {
										pheromone = edge.callMethod(method);
									} finally {
										lock.unlock();
									}
									var lineWidth = 1 + Math.log1p(pheromone) * 2;
									ctx.setLineWidth(lineWidth);
									var screenPosStart = visualization.getWorldTransformation().transform(getX(), getY());
									var screenPosEnd = visualization.getWorldTransformation().transform(getEndX(), getEndY());
									ctx.strokeLine(screenPosStart.getX(), screenPosStart.getY(), screenPosEnd.getX(), screenPosEnd.getY());
								}

								@Override
								public double getX() {
									return edge.getStart().getPosition()[0];
								}

								@Override
								public double getY() {
									return edge.getStart().getPosition()[1];
								}

								public double getEndX() {
									return edge.getEnd().getPosition()[0];
								}

								public double getEndY() {
									return edge.getEnd().getPosition()[1];
								}
							};
							edge.getValueStorage().setValue("visualization", visElement);
							networkVisualization.addElement(visElement);
							return null;
						}
					});
				}};
				var net = new Network();
				var algo = new Algorithm();
				algo.setPart(part);
				ctx.setPart(algo);
				ctx.setPart(net);
				ctx.callMethod(SetDefaultPheromone.getInstance(), 1.0);
				return ctx;
			})) {
				scheduler.reset();
				scheduler.scheduleTask(0, ctx, InitializeSimulation.getInstance(), null);
				if (schedulingThread != null) {
					if (schedulingThread.isAlive())
						schedulingThread.interrupt();
					try {
						schedulingThread.join(5000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					schedulingThread = null;
				}
				var schedulingThread = new Thread(() -> {
					try {
						scheduler.schedulingLoop();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
				schedulingThread.start();
				try {
					schedulingThread.join();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				scheduler.terminate();
			}
		});
		spawnThread.start();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("WNSIRS");
		var root = new Group();
		root.getChildren().add(mainPane);
		var scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		ChangeListener<Object> listener = (o, oldValue, newValue) -> {networkVisualization.redraw();};
		scene.widthProperty().addListener(listener);
		scene.heightProperty().addListener(listener);
		networkVisualization.getTimer().start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}