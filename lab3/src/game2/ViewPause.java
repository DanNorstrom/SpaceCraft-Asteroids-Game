package game2;

import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import javax.swing.JButton;
import javax.swing.JPanel;

import utilities.Sprite;

@SuppressWarnings("serial")
public class ViewPause extends JPanel{
	
	public Integer option = null;
	
	Image im = Sprite.GUI_resume; 
	AffineTransform bgTransf;
	
	final static float dash1[] = {10.0f};
    final static BasicStroke dashed =
        new BasicStroke(2.0f);
    
	
	public ViewPause() {
     	
		JButton goResume = new JButton();
		goResume.setOpaque(false);
		goResume.setContentAreaFilled(false);
		goResume.setBorderPainted(false);
		goResume.setIcon(Sprite.resize(Sprite.GUI_resume,3,3));		


		goResume.removeMouseListener(goResume.getMouseListeners()[0]);
		goResume.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				option = 1;
			}			
		});	
        
	    this.add(goResume, BorderLayout.CENTER);
	    
	    
	
		JButton goMenu = new JButton();
		goMenu.setOpaque(false);
		goMenu.setContentAreaFilled(false);
		goMenu.setBorderPainted(false);
		goMenu.setIcon(Sprite.resize(Sprite.GUI_menu,3,3));	
		
		goMenu.removeMouseListener(goMenu.getMouseListeners()[0]);
		goMenu.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				option = 2;
			}			
		});	
	    this.add(goMenu, BorderLayout.CENTER);
	}

	
	
	@Override
	public void paintComponent(Graphics g0) {
			
		Graphics2D g = (Graphics2D) g0;
		
		double imWidth = im.getWidth(null); 
	    double imHeight = im.getHeight(null);
		int x1 = (int) ((Constants.FRAME_WIDTH/2) - imWidth/3.3); 
		g.setStroke(dashed);
		g.setColor(Color.cyan);
		g.drawRoundRect(x1, -1, (int) ((int)2*imWidth/3.3), (int)imHeight/2, 5, 5);
		
		// draw score 
		Color w = Color.white;
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(Font.BOLD, currentFont.getSize() * 2.6F);
		g.setColor(Color.white);
		g.setFont(newFont);
		g.drawString("PAUSE",(int)(Constants.FRAME_WIDTH/2 - 50), (int)(imHeight/2.2));

		
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}