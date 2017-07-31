import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.Rectangle;

class DrawCH extends JFrame{

	int point_size = 10;
	double size, min_size, max_size;
	int max_x,min_x;
	int window_size = 750;
	int margin = 60;

	int[] id,x,y;
	int[] polyline_x;
	int[] polyline_y;

	Field F;

	DrawCH(int[] id, int[] x, int[] y){
		this.x = x;
		this.y = y;
		this.id = id;
		initialize();
	}

	//Legacy constructor
	DrawCH(Oblig3 d, IntList CoHull){
		x = d.x;
		y = d.y;
		id = new int[CoHull.size()];
		for(int i = 0; i < CoHull.size(); i++){
			id[i] = CoHull.get(i);
		}
		initialize();
	}

	private void initialize(){
		frameInit();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setSize(window_size+20,window_size+margin);

		updateMaxMin();
		updateSize();
		updatePolyline();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();

		JPanel JP = new JPanel();
		JP.setLayout(null);
		JP.setBounds(0,0,width,height);
		add(JP);

		F = new Field();
		F.setBounds(0,0,window_size+20,window_size+margin);
		JP.add(F);

		DrawMouseListener DML = new DrawMouseListener();
		JP.addMouseListener(DML);
		JP.addMouseMotionListener(DML);
		JP.addMouseWheelListener(DML);

		setVisible(true);
	}

	private void updateMaxMin(){
		max_x = x[0];
		min_x = x[0];
		for(int i = 1; i < x.length; i++){
			if(x[i] > max_x){
				max_x = x[i];
			}
			else if(x[i] < min_x){
				min_x = x[i];
			}
		}
	}

	private void updateSize(){
		size = window_size/(double)(max_x-min_x);
		max_size = size*256;
		min_size = size/2;
	}

	private void updatePolyline(){
		polyline_x = new int[id.length+1];
		polyline_y = new int[id.length+1];
		for(int i = 0; i < id.length; i ++){
			polyline_x[i] = (int)(x[id[i]]*size)+(point_size>>>1);
			polyline_y[i] = (int)(y[id[i]]*size)+(point_size>>>1);
		}
		polyline_x[id.length] = (int)(x[id[0]]*size)+(point_size>>>1);
		polyline_y[id.length] = (int)(y[id[0]]*size)+(point_size>>>1);

	}

	class Field extends JPanel{

		public void paintComponent(Graphics g){
			g.setColor(Color.BLACK);
			for(int i = 0; i < x.length; i++){
				g.fillOval((int)(x[i]*size),(int)(y[i]*size),point_size,point_size);
			}
			g.setColor(Color.GREEN);
			for(int i = 0; i < id.length; i++){
				g.fillOval((int)(x[id[i]]*size),(int)(y[id[i]]*size),point_size,point_size);
			}
			g.setColor(Color.RED);
			g.drawPolyline(polyline_x,polyline_y,polyline_x.length);
			g.setColor(Color.BLACK);
			if(size >= max_size/16){
				for(int i = 0; i < id.length; i++){
					g.drawString(id[i]+"",(int)((x[id[i]])*size),(int)((y[id[i]])*size+point_size*2));
				}
			}
		}
	}

	class DrawMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener{
	
		int x,y;
		boolean active = false;
	
		public void mouseDragged(MouseEvent e){
			if(!active) return;
			Rectangle r = F.getBounds();
			int new_x = r.x+(e.getX()-x);
			int new_y = r.y+(e.getY()-y);
			x = e.getX();
			y = e.getY();
			F.setBounds(new_x,new_y,r.width,r.height);
			repaint();
		}
	
		public void mouseMoved(MouseEvent e){}	
		public void  mouseClicked(MouseEvent e){}
	
		public void  mousePressed(MouseEvent e){
			if(e.getButton() == e.BUTTON1){
				x = e.getX();
				y = e.getY();
				active = true;
			}
		}
	
		public void  mouseReleased(MouseEvent e){
			if(e.getButton() == e.BUTTON1){
				active = false;
			}
		}
	
		public void  mouseEntered(MouseEvent e){}
		public void  mouseExited(MouseEvent e){}
	
		public void mouseWheelMoved(MouseWheelEvent e){
			int mouse_x = e.getX();
			int mouse_y = e.getY();
			int rotation = e.getWheelRotation();
			Rectangle r = F.getBounds();

			//zoom in
			if(rotation < 0 && size < max_size){
				size *= 2;
				F.setBounds(r.x*2-mouse_x,r.y*2-mouse_y,r.width*2,r.height*2);
			}

			//zoom out
			if(rotation > 0 && size > min_size){
				size /= 2;
				F.setBounds((r.x+mouse_x)/2,(r.y+mouse_y)/2,r.width/2,r.height/2);
			}
			updatePolyline();
			repaint();
		}
	}
}