package game2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JPanel;

import utilities.Sprite;

public class ViewMainMenu extends JPanel{
	
	public Integer option = null;
	
	Image im = Sprite.GUI_MM; 
	AffineTransform bgTransf;
	
	public ViewMainMenu() {
	    double imWidth = im.getWidth(null); 
	    double imHeight = im.getHeight(null); 
	    double stretchx = Constants.FRAME_WIDTH/imWidth; 
	    double stretchy = Constants.FRAME_HEIGHT/imHeight;
	    bgTransf = new AffineTransform(); 
	    bgTransf.scale(stretchx, stretchy);
		
	    
	    this.setLayout(null);
	    
	    double iH = 3.7;
	    double iW = 17;
	    // Resume Game JButton
		JButton resumeGame = new JButton();  
		
		resumeGame.setOpaque(false);
		resumeGame.setContentAreaFilled(false);
		resumeGame.setBorderPainted(false);
		
        resumeGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	option = 1;
            }
        });
        resumeGame.setBounds((int) (Constants.FRAME_WIDTH/iW), (int)(Constants.FRAME_HEIGHT/iH), Constants.FRAME_WIDTH/20,Constants.FRAME_WIDTH/20);
      	this.add(resumeGame);
	
      	// New Game JButton
		JButton newGame = new JButton();
		newGame.setOpaque(false);
		newGame.setContentAreaFilled(false);
		newGame.setBorderPainted(false);
			
	    newGame.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	option = 2;
	        	}
	    });  	
	    newGame.setBounds((int) (Constants.FRAME_WIDTH/iW), (int)(Constants.FRAME_HEIGHT/iH +Constants.FRAME_HEIGHT*0.09), Constants.FRAME_WIDTH/20,Constants.FRAME_WIDTH/20);
		this.add(newGame, BorderLayout.CENTER);
		

      	// HighScore JButton
		JButton highScore = new JButton();
		highScore.setOpaque(false);
		highScore.setContentAreaFilled(false);
		highScore.setBorderPainted(false);
		
		highScore.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	option = 3;
	        	}
	    });  	
		highScore.setBounds((int) (Constants.FRAME_WIDTH/iW), (int)(Constants.FRAME_HEIGHT/iH +Constants.FRAME_HEIGHT*0.19), Constants.FRAME_WIDTH/20,Constants.FRAME_WIDTH/20);
		this.add(highScore, BorderLayout.CENTER);
	
	
      	// Option JButton
		JButton optionM = new JButton();
		optionM.setOpaque(false);
		optionM.setContentAreaFilled(false);
		optionM.setBorderPainted(false);
		
		optionM.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	option = null;
	        	}
	    });  	
		optionM.setBounds((int) (Constants.FRAME_WIDTH/iW), (int)(Constants.FRAME_HEIGHT/iH +Constants.FRAME_HEIGHT*0.29), Constants.FRAME_WIDTH/20,Constants.FRAME_WIDTH/20);
		this.add(optionM, BorderLayout.CENTER);
	
		
      	// Quit JButton
		JButton quit = new JButton();
		quit.setOpaque(false);
		quit.setContentAreaFilled(false);
		quit.setBorderPainted(false);
		
		quit.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	System.exit(0);
	        	}
	    });
		quit.setBounds((int) (Constants.FRAME_WIDTH/iW), (int)(Constants.FRAME_HEIGHT/iH +Constants.FRAME_HEIGHT*0.39), Constants.FRAME_WIDTH/20,Constants.FRAME_WIDTH/20);
		this.add(quit, BorderLayout.CENTER);
	
	
	}
	
	
	@Override
	public void paintComponent(Graphics g0) {
			
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.drawImage(im, bgTransf,null);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}