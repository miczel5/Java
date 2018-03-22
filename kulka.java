package kulka;


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class akcja extends Ellipse2D.Float
{
   Planszad p;
   int dx,dy;
   Belka b;
   Cegla[] cegly;
      
 
   akcja(Planszad p, Belka b, Cegla[] cegly, int x,int y,int dx,int dy) 
   {                                          
      this.x=x;                               
      this.y=y;                               
      this.width=10;                          
      this.height=10; 
      this.b=b;
      this.cegly = cegly;
      this.p=p;                               
      this.dx=dx;                             
      this.dy=dx; 
   }                                          
 
   void nextKrok()                                        
   {                                                     
      x+=dx;                                             
      y+=dy;                                             
      if(getMinX()<0 || getMaxX()>p.getWidth())  dx=-dx; 
      if( getMinY()>p.getHeight()) dy=-dy; 
      if(y==350) {
    	  for(int i=0; i < cegly.length; i++)
          {
                  dy =- dy;
                  cegly[i].x = 1000;
                  cegly[i].y = 1000;}
    	  x=200;
    	  y=200;
    	  SilnikKulki.licznik = 0;
              
      };
      if(b.intersects(x, y, width, height))
      {
        dy=-dy;
        }
      for(int i=0; i < cegly.length; i++)
      {
          if(cegly[i].intersects(x, y, width, height))
          {
              dy =- dy;
              cegly[i].x = 1000;
              cegly[i].y = 1000;
              
          }
      }
      p.repaint();                                       
   }
   
   
}
class SilnikKulki extends Thread
{
   public static int licznik=2;
   akcja a;
 
   SilnikKulki(akcja a) 
   {               
      this.a=a;         
      start();          
   }                    
 
   public void run()                   
   {                                  
      try                             
      {                               
         while(licznik > 0)                  
         {                            
            a.nextKrok();             
            sleep(15);                
         }
         
      }                               
      catch(InterruptedException e){} 
   }                                  
}
class Cegla extends Rectangle2D.Float
{
    Cegla(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.width =60;
        this.height =10;
    }
    void setX(int x)
    {
        this.x=x;
    }
    void setY(int y)
    {
        this.y=y;
    }
}
class Belka extends Rectangle2D.Float
{
   Belka(int x)       
   {                  
      this.x=x;       
      this.y=330;     
      this.width=60;  
      this.height=10; 
   }                  
 
   void setX(int x) 
   {                
      this.x=x;     
   }                
}
class Planszad extends JPanel implements MouseMotionListener
{
   Belka b;
   akcja a;
   SilnikKulki s;
   Cegla[] cegly={new Cegla(10,5), new Cegla(80,5), new Cegla(150,5), new Cegla(220,5), new Cegla(290,5), new Cegla(360,5), new Cegla(10,20), new Cegla(80,20), new Cegla(150,20), new Cegla(220,20), new Cegla(290,20), new Cegla(360,20)};
 
   Planszad()                         
   {                                 
      super();                       
      addMouseMotionListener(this);  
      
      b=new Belka(100);              
      a=new akcja(this, b, cegly, 100,100,1,1); 
      s=new SilnikKulki(a);   
   }                                 
 
   public void paintComponent(Graphics g) 
   {                                      
      super.paintComponent(g);            
      Graphics2D g2d=(Graphics2D)g;       
 
      g2d.fill(a);                        
      g2d.fill(b);
      g2d.setColor(Color.red);
      for(int i=0; i<cegly.length; i++)
      {
          g2d.fill(cegly[i]);
      }
      if(SilnikKulki.licznik<=0)
      { 
        g2d.clearRect(0,0,getWidth(),getHeight());
        BufferedImage bsrc = null;
          try {
              bsrc = ImageIO.read(new File("wiadro.jpg"));
          } catch (IOException ex) {
              System.out.print(ex);
          }
        Image imgOut = bsrc.getScaledInstance(390,345,Image.SCALE_DEFAULT);
        g2d.drawImage(imgOut, 0,0,null);
        
      }
   }                                      
 
   public void mouseMoved(MouseEvent e) 
   {                                    
      b.setX(e.getX()-50);              
      repaint();                        
   }                                    
 
   public void mouseDragged(MouseEvent e) 
   {
	   b.setX(e.getX()-50);              
	      repaint(); 
   }                                      
}
public class kulka {

   public static void main(String[] args)                       
   {                                                           
      javax.swing.SwingUtilities.invokeLater(new Runnable()    
      {                                                        
         public void run()                                     
         {                                                     
            Planszad p;                                         
            p=new Planszad();                                   
 
            JFrame jf=new JFrame();                            
            jf.add(p);                                         
            jf.setTitle("GRA");  
            jf.setSize(450,400); 
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            jf.setVisible(true);                               
         }                                                     
      });                                                      
   }                                   
}