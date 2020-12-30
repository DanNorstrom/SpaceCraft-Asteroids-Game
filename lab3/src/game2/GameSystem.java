package game2;

import static game2.Constants.DELAY;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class GameSystem {
	
	static int copyScore;
	
	// JComponents
	private static ViewMainMenu mainMenu;
	private static Game game;
	private static ViewGame view;
	private static GameWindow window;
	private static ViewPause viewPause;
	private static ViewAssert assertQ;
	private static ViewScoreScreen VSS;
	private static ViewMenuScore VMS;
	
	// menu booleans
	private static boolean returnToGame = false;
	private static boolean newGame = false;
	private static boolean GoMenu = true;
	private static boolean scoreScreenAlive;
	private static boolean menuScoreAlive;
	
	// initialize score object
	private static ReportScore RP = new ReportScore();
	
	public static void main(String[] args) throws Exception {
		// create menu JPanel
		mainMenu = new ViewMainMenu();
		// put it into the JFrame
		window = new GameWindow(mainMenu, "Dn18657 Asteroid     Game");
		// instantiate a game 
		game = new Game();
		game.alive = false;
		
		//presentation
		System.out.println("Dev: [dn18657] \nWelcome to Dan's Game! \nPlease enjoy yourself and beat my highscore!");
		
		// System loop, exits by shutting down application
		while(true) {
			
			if (GoMenu) {
				GoMenu = false;				
				window.changePanel(mainMenu);
				mainMenu.repaint();
				while (true) {
					Integer response = mainMenu.option;
					Thread.sleep(50);
					
					if (response == null) {
						continue;
					}

					// Resume game
					if ((response == (Integer) 1) && (game.alive)) {
						System.out.println("Main menu option 1! Sending you back ingame!");
						returnToGame = true;
						game.pause = false; 		// remove paus on game loop
						mainMenu.option = null; 	// reset menu choice
						break;
					}

					// New game
					if (response == (Integer) 2) {
						
						// Ascertain new game while game is running
						if (game.alive) {

							assertQ = new ViewAssert();
							window.changePanel(assertQ);
							window.requestFocusInWindow();
							assertQ.revalidate();
						
							while(assertQ.option == null) {
								Thread.sleep(50);
							}

							// yes!
							if (assertQ.option == 1) {
								System.out.println("Main menu option 2! New game!");
								newGame = true;
							}
							else {
								GoMenu = true;
							}
							mainMenu.option = null; 	// reset menu choice
							break;
						}
						// no game is running, safe to start
						else {
							System.out.println("Main menu option 2! New game!");
							newGame = true;
							mainMenu.option = null; 	// reset menu choice
							break;
						}
					}
					
					if (response == 3) {
						
						// get score and pass it to score screen
						ArrayList<Integer> scores = ReportScore.ReturnScore();
						
						// show score board
						VMS = new ViewMenuScore(scores); 				
						window.changePanel(VMS);
						window.requestFocusInWindow();
						//VMS.revalidate();
						
						
						menuScoreAlive = true;
					    window.addKeyListener(new KeyListener() {
				            @Override
				            public void keyTyped(KeyEvent e) {
				            	System.out.println("Pressed " + e.getKeyChar());
				            }
				            @Override
				            public void keyReleased(KeyEvent e) {
				            	System.out.println("Pressed " + e.getKeyChar());
				            }
				            @Override
				            public void keyPressed(KeyEvent e) {
				                System.out.println("Pressed " + e.getKeyChar());
					    		
				                // go to menu
				                menuScoreAlive = false;
				            }
				        });
						
						// show score screen until its exited
						while (menuScoreAlive) {
							Thread.sleep(50);
						}	
						
						mainMenu.option = null; 	// reset menu choice
						GoMenu = true;
						break;
					}
				}
			}
			
			// initialize a new game
			else if (newGame) {
				newGame = false;
				
				game = new Game();
				view = new ViewGame(game);
				window.addKeyListener(game.ctrl);
			
				returnToGame = true; // start GameLoop
			}
			

			// game loop
			else if (returnToGame && game.alive) {
				System.out.println("Game Initialised! ");
				
				window.changePanel(view);
				window.requestFocusInWindow();
				
				// Start game loop
        	    while (game.alive && !game.pause) {
        	    	game.update();
        	    	view.repaint();
        	    	Thread.sleep(DELAY);
        	    }
        	    returnToGame = false;
			}
			
			// pause game
			else if (game.pause) {
				
				//instantiate some JPanels
				viewPause = new ViewPause();
				window.changePanel(viewPause);
				window.requestFocusInWindow();
				viewPause.revalidate();
							
				while(viewPause.option == null) {
					Thread.sleep(50);
				}
				
				// return to game
				if (viewPause.option == 1) {
					returnToGame = true;
					game.pause = false;
				}
				else{
					GoMenu = true;
				}
				// reset
				viewPause.option = null;
			}
			
			
			// end of game ScoreScreen
			else if (!game.alive) {
				// save final score of this round
				ReportScore.addScore(Game.score);
				// get score and pass it to score screen
				ArrayList<Integer> scores = ReportScore.ReturnScore();
				
				// show score board
				VSS = new ViewScoreScreen(scores, Game.score); 				
				window.changePanel(VSS);
				window.requestFocusInWindow();
				VSS.revalidate();
				
				// clean game data (Removes any excess sounds etc) from static structs
				Game.GO.clear();
				Game.ParticleList.clear();
				Game.score = 0;
				
				Thread.sleep(2000); // hinder spam from game to skip Score screen
				scoreScreenAlive = true;
			    window.addKeyListener(new KeyListener() {

		            @Override
		            public void keyTyped(KeyEvent e) {
		            	System.out.println("Pressed " + e.getKeyChar());
		            }

		            @Override
		            public void keyReleased(KeyEvent e) {
		            	System.out.println("Pressed " + e.getKeyChar());
		            }

		            @Override
		            public void keyPressed(KeyEvent e) {
		                System.out.println("Pressed " + e.getKeyChar());
			    		
		                // go to menu
		                scoreScreenAlive = false;
		            }
		        });
				
				// show score screen until its exited
				while (scoreScreenAlive) {
					Thread.sleep(50);
				}	
				
				// return to menu
				GoMenu = true;			
			}
			
		}	
	}
}
