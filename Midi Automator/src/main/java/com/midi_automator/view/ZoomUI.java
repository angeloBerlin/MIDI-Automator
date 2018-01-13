package com.midi_automator.view;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.LayerUI;

public class ZoomUI extends LayerUI<JComponent> {

	private static final long serialVersionUID = 1L;
	private double zoom = 1;

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.scale(zoom, zoom);
		super.paint(g2, c);
		g2.dispose();
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public double getZoom() {
		return zoom;
	}
}
