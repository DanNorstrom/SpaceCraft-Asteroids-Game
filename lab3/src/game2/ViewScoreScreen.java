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
public class ViewScoreScreen extends JPanel{
	
	Image im;
	Image imLeft; 
	Image imCenter;
	AffineTransform bgTransf;
	AffineTransform bgTransfLeft;
	AffineTransform bgTransfCenter;
	
	ArrayList<Integer> scores;
	int cScore;
	
	public ViewScoreScreen(ArrayList<Integer> scores, Integer currentScore) {

		// this works in tandem with the ImageLoaders scaling, might need some recoding
		
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
	    this.cScore = currentScore;
	}

	
	
	@Override
	public void paintComponent(Graphics g0) {
			
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.drawImage(im, bgTransf,null);
		g.drawImage(imLeft, bgTransfLeft,null);
		g.drawImage(imCenter, bgTransfCenter,null);
		
		
		g.setColor(Color.white);
		
		// screen resolution scaling
		double scaleFont = FRAME_WIDTH/2560.0;
		
		// top 5
		int i = -1;
		for (int score: scores) {
			
			Font currentFont = g.getFont();
			
			Font newFont = g.getFont().deriveFont(Font.BOLD, (float) (scaleFont*currentFont.getSize() * 1F));
			g.setFont(newFont);
			g.drawString("Rank "+(i+1),(int)(0.708*FRAME_WIDTH + 0.044*i*FRAME_WIDTH), (int)(0.680*FRAME_HEIGHT));
			
			Font newFont2 = g.getFont().deriveFont(Font.BOLD, (float)(scaleFont*currentFont.getSize() * 2F));
			g.setFont(newFont2);
			g.drawString(""+score,(int)(0.705*FRAME_WIDTH + 0.044*i*FRAME_WIDTH), (int)(0.702*FRAME_HEIGHT));
			
			i++;
			g.setFont(currentFont);
		}
		
		// current score
		g.setColor(Color.black);
		Font currentFont = g.getFont();
		Font newFont3 = g.getFont().deriveFont(Font.BOLD, (float)(scaleFont*currentFont.getSize() * 3F));
		g.setFont(newFont3);
		g.drawString("Score",(int)(0.747*FRAME_WIDTH), (int)(0.36*FRAME_HEIGHT));
		
		Font newFont4 = g.getFont().deriveFont(Font.BOLD, (float)(scaleFont*currentFont.getSize() * 2.2F));
		g.setFont(newFont4);
		g.drawString(""+cScore,((int)(0.745*FRAME_WIDTH)), ((int)(0.38*FRAME_HEIGHT)));	
		
		// draw middle message	
		g.drawString("Well done Pilot!",(int)(0.45*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(30*scaleFont));
		g.drawString("Your Mission Score can be found",(int)(0.415*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(60*scaleFont));
		g.drawString("on the right side of your helmets",(int)(0.415*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(90*scaleFont));
		g.drawString("Display. Other pilots HighScores",(int)(0.415*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(120*scaleFont));
		g.drawString("can be seen at the bottom as well.",(int)(0.415*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(150*scaleFont));
		g.drawString("-------------------------------------------",(int)(0.415*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(180*scaleFont));
		g.drawString("Press any key after 2sec",(int)(0.44*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(210*scaleFont));
		g.drawString("To return to main Menu.",(int)(0.44*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(240*scaleFont));
		g.drawString("Thank you for playing!",(int)(0.44*FRAME_WIDTH), (int)(0.4*FRAME_HEIGHT) +(int)(270*scaleFont));
		
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}