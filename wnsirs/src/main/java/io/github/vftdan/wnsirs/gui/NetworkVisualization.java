package io.github.vftdan.wnsirs.gui;

import java.util.*;
import java.util.concurrent.*;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
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
			canvas = new ResizeableCanvas(480, 320);
		this.canvas = canvas;
		InvalidationListener listener = (o) -> {dirty = true;};
		canvas.widthProperty().addListener(listener);
		canvas.heightProperty().addListener(listener);
	}

	public NetworkVisualization(int width, int height) {
		this(new ResizeableCanvas(width, height));
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
		updateWorldTransformation();
		var ctx = getContext();
		var savedTransform = ctx.getTransform();
		ctx.setTransform(new Affine());
		ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ctx.setTransform(savedTransform);
		for (var el: elements) {
			el.draw(this, ctx);
		}
	}

	protected void updateWorldTransformation() {
		double minX = Double.POSITIVE_INFINITY,
		       minY = Double.POSITIVE_INFINITY,
		       maxX = Double.NEGATIVE_INFINITY,
		       maxY = Double.NEGATIVE_INFINITY;
		for (var el: elements) {
			var x = el.getX();
			var y = el.getY();
			if (Double.isNaN(x) || Double.isNaN(y))
				continue;
			if (minX > x)
				minX = x;
			if (minY > y)
				minY = y;
			if (maxX < x)
				maxX = x;
			if (maxY < y)
				maxY = y;
		}
		double clientPadding = 40;
		double clientWidth = canvas.getWidth();
		double clientHeight = canvas.getHeight();
		double clientPaddedWidth = clientWidth - 2 * clientPadding;
		if (clientPaddedWidth - clientPadding < 0)
			clientPaddedWidth = clientWidth;
		double clientPaddedHeight = clientHeight - 2 * clientPadding;
		if (clientPaddedHeight - clientPadding < 0)
			clientPaddedHeight = clientHeight;
		double xScale = clientPaddedWidth / (maxX - minX);
		double yScale = clientPaddedHeight / (maxY - minY);
		boolean xPositiveFinite = xScale > 0 && Double.isFinite(xScale);
		boolean yPositiveFinite = yScale > 0 && Double.isFinite(yScale);
		if (!xPositiveFinite && !yPositiveFinite) {
			xScale = 1;
			yScale = 1;
		} else if (!xPositiveFinite) {
			xScale = yScale;
		} else if (!yPositiveFinite) {
			yScale = xScale;
		}
		double scale = Math.min(xScale, yScale);
		if (!Double.isFinite(minX))
			minX = 0;
		if (!Double.isFinite(minY))
			minY = 0;
		worldTransformation.setToIdentity();
		worldTransformation.appendTranslation((clientWidth - clientPaddedWidth) / 2, (clientWidth - clientPaddedWidth) / 2);
		worldTransformation.appendScale(scale, scale);
		worldTransformation.appendTranslation(-minX, -minY);
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

	public static class ResizeableCanvas extends Canvas {
		public ResizeableCanvas(double w, double h) {
			super(w, h);
		}

		public ResizeableCanvas() {
			super();
		}

		@Override
		public boolean isResizable() {
			return true;
		}
	}
}
