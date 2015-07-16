package br.com.ritcher.caleido;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class Main implements ActionListener, MouseListener, ComponentListener, FocusListener {
	
	Desenho desenho;
	
	Traco traco;

	private int pontoRaio = 10;

	private JLabel statusLabel;
	
	JScrollPane scrollPane;
	JFrame frame;


	private Color pontoCor = Color.black;

	public static void main(String[] args) {
		Main m = new Main();
		m.go();
		if(args.length > 0){
			m.abrirArquivo(new File(args[0]));
		}
		
	}

	public void go() {
		
		frame = new JFrame("Caleido");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		frame.setLayout(new BorderLayout());
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600,600));
		
		desenho = criarDesenho(scrollPane);
		//scrollPane.add
		
		frame.add(scrollPane, BorderLayout.CENTER);
		scrollPane.addComponentListener(this);
		
		
		JMenuBar menuBar = new JMenuBar();
		setupMenuBar(menuBar);
		frame.setJMenuBar(menuBar);
		
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		frame.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("status");
		
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		JToolBar toolBar = new JToolBar("Still draggable");
		addButtons(toolBar);
		frame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		frame.setVisible(true);
		
		frame.pack();
		//frame.setSize(800,600);
	    
	    
		//resizeScrollBar();
	}
	
	

	private void setupMenuBar(JMenuBar menuBar) {
		JMenu menu = new JMenu("Arquivo");
		menu.setMnemonic(KeyEvent.VK_A);
		
		menu.add(createMenuItem("Novo", "novo", KeyEvent.VK_N));
		menu.addSeparator();
		menu.add(createMenuItem("Abrir", "abrir", KeyEvent.VK_A));
		menu.add(createMenuItem("Salvar", "salvar", KeyEvent.VK_S));
		menu.add(createMenuItem("Salvar Como", "salvar_como", KeyEvent.VK_C));
		
		
		
		
		menu.addSeparator();
		menu.add(createMenuItem("Exportar Imagem", "salvar_png", KeyEvent.VK_E));
		menu.add(createMenuItem("Exportar SVG", "salvar_svg", KeyEvent.VK_V));
		
		menuBar.add(menu);

	}

	private JMenuItem createMenuItem(String string, String string2, int mnemonic) {
		JMenuItem menuItem = new JMenuItem(string, mnemonic);
		menuItem.setActionCommand(string2);
		menuItem.addActionListener(this);
		return menuItem;
	}

	private Desenho criarDesenho(JScrollPane scrollPane) {
		desenho = new Desenho();
		desenho.repeticoes = 8;
		
		traco = new Traco();
		desenho.addTraco(traco);

		desenho.addMouseListener(this);
		
		scrollPane.setViewportView(desenho);
		
		//frame.pack();
		frame.setSize(frame.getSize());
		frame.invalidate();
		resizeScrollBar();
		
		userFiles = new HashMap<String, File>();
		
		return desenho;
		
		
	}
	
	private void abrirDesenho(){
		traco = new Traco();
		
		
		desenho.addTraco(traco);
		desenho.addMouseListener(this);
		
		scrollPane.setViewportView(desenho);
		((JTextField) fieldNames.get("Repetições")).setText(Integer.toString(desenho.repeticoes)); 
		
		//frame.pack();
		frame.setSize(frame.getSize());
		frame.revalidate();
		resizeScrollBar();
	}

	protected void addButtons(JToolBar toolbar) {
	    JButton button = null;
	    
	    //first button
	    button = makeNavigationButton("Traco", "traco", "Traco", null);
	    toolbar.add(button);
	    
	    button = makeNavigationButton("Cor", "cor", "Cor", null);
	    toolbar.add(button);
	    
	    button = makeNavigationButton("Fundo", "fundo", "Fundo", null);
	    toolbar.add(button);
	    
	    
	    makeField(toolbar, "Raio", Integer.toString(pontoRaio));
	    makeField(toolbar, "Repetições", Integer.toString(desenho.repeticoes));
	    
	    String[] field = {"AX", "AY", "BX", "BY"};
	    String[] values = {"0", "0", "0", "0"};
	    
	    toolbar.add(new JLabel("    Curvas"));
	    
	    for (int i = 0; i < field.length; i++) {
	    	makeField(toolbar, field[i], values[i]);
		}
	}
	
	private HashMap<Object, String> fields = new HashMap<Object, String>();
	private HashMap<String, Object> fieldNames = new HashMap<String, Object>();
	
	protected void makeField(JToolBar toolbar, String name, String value){

		
		JLabel label = new JLabel(name + " ");
		JTextField field = new JTextField();
		
		field.setText(value);
		field.setToolTipText(name);
		field.setActionCommand(name);
		field.addFocusListener(this);
		field.setColumns(5);
		
		toolbar.addSeparator();
		toolbar.add(label);
		toolbar.add(field);
		
		fields.put(field, name);
		fieldNames.put(name, field);
	}

	protected JButton makeNavigationButton(String toolTipText, String actionCommand, String altText, String imageName) {
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if(imageName != null){
			String imgLocation = "images/" + imageName + ".gif";
			URL imageURL = Main.class.getResource(imgLocation);
			button.setIcon(new ImageIcon(imageURL, altText));	
		} else { // no image found
			button.setText(toolTipText);
		}
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if("traco".equals(e.getActionCommand())){
			addTraco();
		}
		if("cor".equals(e.getActionCommand())){
			pontoCor = JColorChooser.showDialog(null, "Cor", pontoCor);
			addTraco();
		}
		if("fundo".equals(e.getActionCommand())){
			desenho.corFundo = JColorChooser.showDialog(null, "Cor", desenho.corFundo);
		}
		if("novo".equals(e.getActionCommand())){
			criarDesenho(scrollPane);
			
		}
		if("salvar_png".equals(e.getActionCommand())){
			File fileToSave = getFileFromUser("salvar_png", "Selecione arquivo para salvar", ".png", false);
			if(fileToSave == null){
				return;
			}
		    
		    BufferedImage image = new BufferedImage(desenho.getWidth(), desenho.getHeight(), BufferedImage.TYPE_INT_ARGB);
		    desenho.paint(image.getGraphics());
			try {
				ImageIO.write(image, "png", fileToSave);
			} catch (IOException ex) {
				showError(ex.getMessage());
			}
		}
		if("salvar_svg".equals(e.getActionCommand())){
			File fileOut = getFileFromUser("salvar_svg", "Selecione arquivo para salvar",  ".svg", false);
			if(fileOut == null){
				return;
			}
			
			 DOMImplementation domImpl =
		      GenericDOMImplementation.getDOMImplementation();

		    String svgNS = "http://www.w3.org/2000/svg";
		    Document document = domImpl.createDocument(svgNS, "svg", null);

		    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		    desenho.paint(svgGenerator); 

		    boolean useCSS = true; // we want to use CSS style attributes
		    Writer out;
			try {
				out = new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8");
				svgGenerator.stream(out, useCSS);
				
			} catch (UnsupportedEncodingException e1) {
				showError(e1.getMessage());
				
			} catch (SVGGraphics2DIOException e1) {
				showError(e1.getMessage());
				
			} catch (FileNotFoundException e1) {
				showError(e1.getMessage());
			}
		}
		if("salvar".equals(e.getActionCommand())){
			File fileOut = getFileFromUser("salvar", "Selecione arquivo para salvar",  ".cld", false);
			if(fileOut == null){
				return;
			}
		    salvarArquivo(fileOut);
		}
		if("salvar_como".equals(e.getActionCommand())){
			File fileOut = getFileFromUser("salvar", "Selecione arquivo para salvar",  ".cld", true);
			if(fileOut == null){
				return;
			}
		    salvarArquivo(fileOut);
		}
		if("abrir".equals(e.getActionCommand())){
			File fileOut = getFileFromUser("salvar", "Selecione arquivo para abrir",  ".cld", true, true);
			if(fileOut == null){
				return;
			}
		    
			abrirArquivo(fileOut);
			
		    
		}
		desenho.repaint();
	}

	private void salvarArquivo(File fileOut) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileOut));
		    out.writeObject(desenho);
		    out.close();
		    
		} catch (IOException ex) {
			showError(ex.getMessage());
		}
	}
	
	
	public void abrirArquivo(File fileOut){
		try {
	    	ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileOut));
	    	desenho = (Desenho) in.readObject();
	    	abrirDesenho();
	    	in.close();
	    	
		} catch (IOException ex) {
			showError(ex.getMessage());
			
		} catch (ClassNotFoundException e1) {
			showError(e1.getMessage());
		}
	}
	
	private HashMap<String, File> userFiles = new HashMap<String, File>(); 
	
	File getFileFromUser(String key, String userText, String extension, boolean newFile){
		return getFileFromUser(key, userText, extension, newFile, false);
	}
	
	File getFileFromUser(String key, String userText, String extension, boolean newFile, boolean open){
		if(userFiles.get(key) != null && !newFile){
			return userFiles.get(key);
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(userText);   
		
		int userSelection = 0; 
		if(open){
			userSelection = fileChooser.showOpenDialog(desenho);
		}
		else {
			userSelection = fileChooser.showSaveDialog(desenho);
		}
		
		if(userSelection != JFileChooser.APPROVE_OPTION){
			return null;
		}
		
		File fileToSave = fileChooser.getSelectedFile();
	    if (!fileToSave.getName().endsWith(extension)) {
	    	fileToSave = new File(fileToSave.getAbsoluteFile() + extension);
	    }
	    
	    userFiles.put(key, fileToSave);
	    return fileToSave;
	}
	
	void addTraco(){
		if(traco.pontos.size() > 0){
			Traco ultimo = traco;
			traco = new Traco();
			traco.curvas = ultimo.curvas;
			desenho.addTraco(traco);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Ponto p = new Ponto();
		p.raio = pontoRaio;
		p.x = e.getX();
		p.y = e.getY();
		p.cor = pontoCor;
		
		traco.addPonto(p);
		desenho.repaint();
		desenho.revalidate();
		//System.out.println(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		resizeScrollBar();
	}
	
	void resizeScrollBar(){
		JScrollBar bar;
		bar = scrollPane.getVerticalScrollBar();
		bar.setValue((bar.getMaximum() - bar.getMinimum() - scrollPane.getHeight())/2);
		
		bar = scrollPane.getHorizontalScrollBar();
		bar.setValue((bar.getMaximum() - bar.getMinimum()- scrollPane.getWidth())/2);
		scrollPane.revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		System.out.println(e);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() instanceof JTextField){
			try {
				JTextField field = (JTextField)e.getSource();
				String action = fields.get(field);
				int value = Integer.parseInt(field.getText());
				if("Raio".equals(action)){
					pontoRaio = value;
				}
				if("Repetições".equals(action)){
					desenho.repeticoes = value;
				}
				else if("AX".equals(action)){
					traco.curvas[0] = value;
				}
				else if("AY".equals(action)){
					traco.curvas[1] = value;
				}
				else if("BX".equals(action)){
					traco.curvas[2] = value;
				}
				else if("BY".equals(action)){
					traco.curvas[3] = value;
				}
				
				desenho.repaint();
			} catch (Exception e2) {
				showError(e2.getMessage());
			}
		}
			
	}
	
	private void showError(String error) {
		JOptionPane.showMessageDialog(desenho, error, "Erro", JOptionPane.ERROR_MESSAGE);
	}
}
