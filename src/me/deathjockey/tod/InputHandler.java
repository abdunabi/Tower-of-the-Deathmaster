package me.deathjockey.tod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

	public class Key {
		public int presses, absorbs;
		public boolean down, pressed;
		
		public Key() {
			keys.add(this);
		}
		
		public void toggle(boolean pressed) {
			if(pressed != down)  {
				down = pressed;
			}
			if(pressed) {
				presses++;
			}
		}
		
		public void tick() {
			if(absorbs < presses) {
				absorbs++;
				pressed = true;
			} else {
				pressed = false;
			}
		}
	}
	
	public List<Key> keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key enter = new Key();
	public Key use_rod = new Key();
	public Key cursor_up = new Key();
	public Key cursor_down = new Key();
	public Key confirm = new Key();
	public Key escape = new Key();

	public Key shift = new Key();
	
	public InputHandler(TowerComponent game) {
		game.addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		toggle(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggle(e, false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public void toggle(KeyEvent ke, boolean pressed) {
		int k = ke.getKeyCode();
		if(k == KeyEvent.VK_UP) up.toggle(pressed);
		if(k == KeyEvent.VK_DOWN) down.toggle(pressed);
		if(k == KeyEvent.VK_LEFT) left.toggle(pressed);
		if(k == KeyEvent.VK_RIGHT) right.toggle(pressed);
		
		if(k == KeyEvent.VK_W) up.toggle(pressed);
		if(k == KeyEvent.VK_S) down.toggle(pressed);
		if(k == KeyEvent.VK_A) left.toggle(pressed);
		if(k == KeyEvent.VK_D) right.toggle(pressed);
		
		if(k == KeyEvent.VK_2) cursor_up.toggle(pressed);
		if(k == KeyEvent.VK_8) cursor_down.toggle(pressed);
		if(k == KeyEvent.VK_SPACE) confirm.toggle(pressed);
		if(k == KeyEvent.VK_ESCAPE) escape.toggle(pressed);
		if(k == KeyEvent.VK_ENTER) enter.toggle(pressed);
		
		if(k == KeyEvent.VK_SHIFT) shift.toggle(pressed);
		
		if(k == KeyEvent.VK_L) use_rod.toggle(pressed);
	}
}
