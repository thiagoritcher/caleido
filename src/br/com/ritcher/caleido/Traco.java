package br.com.ritcher.caleido;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Traco implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<Ponto> pontos = new ArrayList<Ponto>();
	
	int[] curvas = {0,0,0,0};
	
	public void paint(Graphics2D g) {
		Iterator<Ponto> ip = pontos.iterator();
		Ponto last = null;
		
		GeneralPath line = new GeneralPath(GeneralPath.WIND_EVEN_ODD, pontos.size());
		while(ip.hasNext()){
			Ponto p = ip.next();
			p.paint(g);
			
			if(last == null){
				line.moveTo(p.x, p.y);
			}
			else {
				line.curveTo(last.x + curvas[0], last.y + curvas[1], p.x + curvas[2], p.y + curvas[3], p.x, p.y);
			}
			last = p;
		}
		g.draw(line);
	}
	
	
	public void addPonto(Ponto p) {
		pontos.add(p);
	}
}
