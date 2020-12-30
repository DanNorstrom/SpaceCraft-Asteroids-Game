package game2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class Keys extends KeyAdapter implements Controller  {
	Action action;
	// Set of currently pressed keys
	private final Set<Integer> pressed = new HashSet<Integer>();
	
	public Keys() {
		action = new Action();
	}

	public Action action() {
		 // this is defined to comply with the standard interface
		if (pressed.contains(KeyEvent.VK_UP)) action.thrust = 1;
		else action.thrust = 0;
				
		if (pressed.contains(KeyEvent.VK_RIGHT)) action.turn = 1;
		
		if (pressed.contains(KeyEvent.VK_LEFT)) action.turn = -1;
		
		if (!pressed.contains(KeyEvent.VK_LEFT) && !pressed.contains(KeyEvent.VK_RIGHT)) {
			action.turn = 0;
		}
		
		if (pressed.contains(KeyEvent.VK_SPACE)) action.shoot = true;
		else action.shoot = false;
		
		if (pressed.contains(KeyEvent.VK_ESCAPE)) action.pause = true;
	    return action;
	}

	public void keyPressed(KeyEvent e) {
		pressed.add(e.getKeyCode());
	}
//    int key = e.getKeyCode();
//    switch (key) {
//    case KeyEvent.VK_UP:
//    	action.thrust = 1;
//    	break;
//    case KeyEvent.VK_LEFT:
//    	action.turn = -1;
//    	break;
//    case KeyEvent.VK_RIGHT:
//    	action.turn = +1;
//    	break;
//    case KeyEvent.VK_SPACE:
//    	action.shoot = true;
//    	break;
//    case KeyEvent.VK_ESCAPE:
//    	action.pause = true;
//    }

	public void keyReleased(KeyEvent e) {
		// please add appropriate event handling code
		pressed.remove(e.getKeyCode());  
	}
//	int key = e.getKeyCode();
//    switch (key) {
//    case KeyEvent.VK_UP:
//      action.thrust = 0;
//      break;
//    case KeyEvent.VK_LEFT:
//      action.turn = 0;
//      break;
//    case KeyEvent.VK_RIGHT:
//      action.turn = 0;
//      break;
//    case KeyEvent.VK_SPACE:
//      action.shoot = false;
//      break;
//    }
}
