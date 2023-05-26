package io.github.vftdan.wnsirs.gui;

import java.util.*;
import java.util.concurrent.*;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.*;
import javafx.scene.transform.Affine;

public class NetworkVisualization {
	protected Canvas canvas;
	protected GraphicsContext context;
	protected volatile boolean dirty = false;
	protected Affine worldTransformation = new Affine();
	protected AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long now) {
			redrawIfDirty();
		}
	};

	private SortedSet<Element<?> > elements = new ConcurrentSkipListSet<Element<?> >();

	public NetworkVisualization(Canvas canvas) {
		if (canvas == null)
			canvas = new Canvas(480, 320);
		this.canvas = canvas;
	}

	public NetworkVisualization(int width, int height) {
		this(new Canvas(width, height));
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public GraphicsContext getContext() {
		if (context == null)
			context = canvas.getGraphicsContext2D();
		return context;
	}

	public Affine getWorldTransformation() {
		return worldTransformation;
	}

	public AnimationTimer getTimer() {
		return timer;
	}

	public void redrawIfDirty() {
		if (dirty)
			redraw();
	}

	public void redraw() {
		dirty = false;
		var ctx = getContext();
		var savedTransform = ctx.getTransform();
		ctx.setTransform(new Affine());
		ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ctx.setTransform(savedTransform);
		for (var el: elements) {
			el.draw(this, ctx);
		}
	}

	public void addElement(Element<?> element) {
		if (element == null)
			return;
		element.subscribers.add(this);
		this.elements.add(element);
		dirty = true;
	}

	public void removeElement(Element<?> element) {
		if (element == null)
			return;
		element.subscribers.remove(this);
		this.elements.remove(element);
		dirty = true;
	}

	public void clearElements() {
		for (var el: elements)
			removeElement(el);
	}

	public static abstract class Element<T> implements Comparable<Element<T> > {
		private Set<NetworkVisualization> subscribers = Collections.newSetFromMap(new WeakHashMap<NetworkVisualization, Boolean>());

		protected T representee;
		protected int zIndex = 0;
		protected double x = 0, y = 0;

		public Element(T representee) {
			this.representee = representee;
		}

		public T getRepresentee() {
			return representee;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		@Override
		public String toString() {
			return super.toString() + "{" + representee + "}";
		}

		@Override
		public int compareTo(Element<T> other) {
			if (zIndex == other.zIndex)
				return System.identityHashCode(this) - System.identityHashCode(other);
			return zIndex - other.zIndex;
		}

		public void invalidate() {
			for (var subscriber: subscribers) {
				subscriber.dirty = true;
			}
		}

		protected abstract void draw(NetworkVisualization visualization, GraphicsContext ctx);
	}
}
