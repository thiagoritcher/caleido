package br.com.ritcher.caleido;

import java.awt.Graphics2D;

public class Ponto extends Grafico {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int raio;
	int x,y;
	
	public void paint(Graphics2D g) {
		int xl = x-(raio/2);
		int yl = y-(raio/2);
		
		g.setColor(cor);
        g.drawOval(xl, yl, raio, raio);
        g.fillOval(xl, yl, raio, raio);
	}  
}
