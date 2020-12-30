package game2;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {

	public GameWindow (JComponent comp, String title) {
	    super(title);
	    
	    this.setLayout(new BorderLayout());
	    this.add(BorderLayout.SOUTH, comp);
  
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    this.setUndecorated(true);

	    pack();
	    this.setVisible(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    repaint();
	}
	
	
	public void changePanel(JPanel comp) {
		//
	    getContentPane().removeAll();
	    getContentPane().add(comp, BorderLayout.CENTER);
	    getContentPane().doLayout();
	    
	    //update(getGraphics());    
	    //this.revalidate();
	    //this.repaint();
	}
}

	
	
	
	  

