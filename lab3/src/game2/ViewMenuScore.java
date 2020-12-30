package game2;

import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;
import static game2.Constants.WORLD_SIZE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import utilities.Sprite;

@SuppressWarnings("serial")
public class ViewMenuScore extends JPanel{
	
	Image im;
	Image imLeft;
	Image imCenter;
	AffineTransform bgTransf;
	AffineTransform bgTransfLeft;
	AffineTransform bgTransfCenter;
	
	ArrayList<Integer> scores;
	
	public ViewMenuScore(ArrayList<Integer> scores) {

		// scale position Left
	    bgTransf = new AffineTransform();
	    im = Sprite.resize(Sprite.GUI_VSS,4*(Sprite.GUI_VSS.getWidth(null)/(double)FRAME_WIDTH),1.15*(Sprite.GUI_VSS.getHeight(null)/(double)FRAME_HEIGHT)).getImage();
	    bgTransf.translate(0.1*FRAME_WIDTH, 0.06*FRAME_HEIGHT);
	    
	    // scale position right
	    bgTransfLeft = new AffineTransform();
	    imLeft = Sprite.resize(Sprite.GUI_VSSLEFT,4*(Sprite.GUI_VSSLEFT.getWidth(null)/(double)FRAME_WIDTH),1.17*(Sprite.GUI_VSSLEFT.getHeight(null)/(double)FRAME_HEIGHT)).getImage();
	    bgTransfLeft.translate(0.64*FRAME_WIDTH, 0.065*FRAME_HEIGHT);
	
	    // scale position center
	    bgTransfCenter = new AffineTransform();
	    imCenter = Sprite.resize(Sprite.GUI_VSSTEXT,5*(Sprite.GUI_VSSTEXT.getWidth(null)/(double)FRAME_WIDTH),4*(Sprite.GUI_VSSTEXT.getHeight(null)/(double)FRAME_HEIGHT)).getImage();
	    bgTransfCenter.translate(0.395*FRAME_WIDTH, 0.37*FRAME_HEIGHT);
	    
	    
	    this.scores = scores;
	}

	
	
	@Override
	public void paintComponent(Graphics g0) {
			
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.drawImage(im, bgTransf,null);
		g.drawImage(imLeft, bgTransfLeft,null);
		g.drawImage(imCenter, bgTransfCenter,null);		
		
		g.setColor(Color.white);
		
		// top 5
		int i = -1;
		for (int score: scores) {
			
			Font currentFont = g.getFont();
			
			Font newFont = g.getFont().deriveFont(Font.BOLD, currentFont.getSize() * 1F);
			g.setFont(newFont);
			g.drawString("Rank "+(i+1),(int)(0.177*FRAME_WIDTH*WORLD_SIZE + 0.011*i*FRAME_WIDTH*WORLD_SIZE), (int)(0.170*FRAME_HEIGHT*WORLD_SIZE));
			
			Font newFont2 = g.getFont().deriveFont(Font.BOLD, currentFont.getSize() * 2F);
			g.setFont(newFont2);
			g.drawString(""+score,(int)(0.176*FRAME_WIDTH*WORLD_SIZE + 0.011*i*FRAME_WIDTH*WORLD_SIZE), (int)(0.1755*FRAME_HEIGHT*WORLD_SIZE));
			
			i++;
			g.setFont(currentFont);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}