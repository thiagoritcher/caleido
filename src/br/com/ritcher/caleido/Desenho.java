package br.com.ritcher.caleido;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class Desenho extends JPanel implements Scrollable{
	private static final long serialVersionUID = 1L;
	
	int repeticoes = 2;
	List<Traco> tracos = new ArrayList<Traco>();

	Color corFundo = Color.white;

	private Dimension dimension = new Dimension(2500, 2500);
	
	
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHints(rh);
		
		g.setColor(corFundo);
		int w = getWidth(), h = getHeight();
		int w2 = w/2, h2 = h/2;
		g.fillRect(0, 0, w, h);
	
		//g.translate(getWidth()/2, getHeight()/2);
		//g.setColor(Color.black);
		g.fillOval(w2, h2, 5, 5);
		
		for(int i = 0; i < repeticoes; i++){
			g.rotate((2* Math.PI * i)/repeticoes, w2, h2);
			
			paintTracos(g);
			
			g.translate(w, 0);
			g.scale(-1 , 1);
			
			paintTracos(g);
			g.translate(0, 0);
			
			//g.scale(-1, 1);
			//paintTracos(g);
			//g.translate(0, 0);
		}
		
		//scrollRectToVisible(new Rectangle(dimension));
	    //setPreferredSize(dimension);
	}
	
	
	private void paintTracos(Graphics2D g){
		Iterator<Traco> tr = tracos.iterator();
		while(tr.hasNext()){
			Traco t = tr.next(); 
			t.paint(g);
		}
	}

	public void addTraco(Traco traco) {
		tracos.add(traco);
	}


	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return dimension;
	}


	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 10;
	}


	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}


	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}
}
