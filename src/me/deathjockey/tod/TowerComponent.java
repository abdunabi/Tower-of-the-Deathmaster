package me.deathjockey.tod;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import me.deathjockey.tod.dy.DynamicsLoader;
import me.deathjockey.tod.level.Level;
import me.deathjockey.tod.level.Player;
import me.deathjockey.tod.screen.Screen;
import me.deathjockey.tod.screen.UI;
import me.deathjockey.tod.sound.AudioPlayer;

public class TowerComponent extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640, HEIGHT = 480;
	private boolean running = false;
	
	public static final String NAME = "Tower of the Deathmaster";
	
	private Screen screen;
	private AudioPlayer audioPlayer;
	private InputHandler input;
	
	private int floor = 1;
	
	public TowerComponent() {
		
	}
	
	public void upFloor(int tx, int ty) {
		floor++;
		Player p = level.player;
		level.player = null;
		level = Level.levels.get(floor);
		p.level = level;
		p.setPos(tx, ty);
		level.addEntity(p);
		audioPlayer.startBackgroundMusic(floor);
	}
	
	public void downFloor(int tx, int ty) {
		floor --;
		if(floor < 1) floor = 1;
		Player p = level.player;
		level.player = null;
		level = Level.levels.get(floor);
		p.level = level;
		p.setPos(tx, ty);
		level.addEntity(p);
		audioPlayer.startBackgroundMusic(floor);
	}
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		init();
		int fps = 0, tick = 0;
		double fpsTimer = System.currentTimeMillis();
		double nsPerTick = 1000000000.0 / 60;
		double then = System.nanoTime();
		double unp = 0;
		while(running) {
			double now = System.nanoTime();
			unp += (now - then) / nsPerTick;
			then = now;
			while(unp >= 1) {
				tick++;
				tick();
				--unp;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			fps++;
			render();
			if(System.currentTimeMillis() - fpsTimer > 1000) {
				System.out.printf("%d fps, %d tick%n", fps, tick);
				fps = 0; tick = 0;
				fpsTimer += 1000;
			}
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		UI.render(screen);
		level.render(screen);
		
		g.drawImage(screen.image, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
	}

	private void tick() {
		level.tick();
		UI.tick(floor);
	}
	
	Level level;

	private void init() {
		screen  = new Screen(WIDTH, HEIGHT);
		input = new InputHandler(this);
		DynamicsLoader.init(this, input);
		level = Level.levels.get(floor);
		audioPlayer = new AudioPlayer();
		audioPlayer.startBackgroundMusic(floor);
		UI.track(level.player);
	}

	public static void main(String[] args) {
		TowerComponent game = new TowerComponent();
		game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		game.setSize(new Dimension(WIDTH, HEIGHT));
		
		JFrame frame = new JFrame(NAME);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		game.start();
	}
}