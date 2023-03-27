package SlideGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JComponent;

public class HexagonUI extends JComponent {
    private int size, score;
    private Color color;
    private TileType type;
    private String displayText;

    public Color pink = new Color(255,192,203),
	pink2 = new Color(255,185,206),
	pink3 = new Color(255,200,200),
	C1aero = new Color(17, 181, 228),
	C2puce = new Color(206, 123, 145),
	C3tangerine = new Color(244, 159, 10),
	C4rust = new Color(191, 49, 0),
	C5moss = new Color(138, 155, 104),
	C6uv = new Color(92, 93, 141),
	C7salmon = new Color(255, 142, 114),
	C8mauvre = new Color(237, 201, 255),
	C9npblue = new Color(142, 220, 230),
	C10pistachio = new Color(188, 217, 121),
	C11plum = new Color(155, 80, 148),
	C12persiangreen = new Color(35, 150, 127),
	C13thistle = new Color(222, 197, 227),
	C14columbiablue = new Color(205, 237, 253),
	C15vanilla = new Color(252, 246, 177),
	C16cerise = new Color(230, 52, 98);




    public HexagonUI(int size, TileType type) {
        this.size = size;
        this.type = type;
		tileSetup(type);
    }
	
	public HexagonUI(int size, TileType type, int scoreValue)
	{
		this.size = size;
		this.type = type;
		this.score = scoreValue;
		tileSetup(type);
	}	

	public HexagonUI(int size)
	{
		this.size = size;
		tileSetup(TileType.BackgroundTile);

	}

    @Override	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(this.color);
        
        Point[] points = new Point[6];
		Point center = new Point(size / 2, size / 2);
		double rotationAngle = 30.0;

		for (int i = 0; i < 6; i++) {
			int x = (int) (size / 2.0 + size / 2.0 * Math.cos(i * Math.PI / 3.0));
			int y = (int) (size / 2.0 + size / 2.0 * Math.sin(i * Math.PI / 3.0));
			points[i] = rotatePoint(new Point(x, y), rotationAngle, center);
		}
        Polygon polygon = new Polygon();
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
			
		g.fillPolygon(polygon);

	
		if(this.getType() != TileType.BackgroundTile)
		{
			g.setColor(Color.BLACK);
        	g.drawPolygon(polygon);
			if(this.getType().equals(TileType.Bomb) 
			|| this.getType().equals(TileType.WildCard) 
			|| this.getType().equals(TileType.Shuffler))
			{
				g.setColor(Color.BLACK);
				for(Point p : points)
				{
					g.fillOval(p.x - 3, p.y - 3, 6, 6);
				}
			}
			
		}

		if(displayText != null)
		{
			int x,
			y = 34;
			if(this.getColor().equals(Color.BLACK))
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.BLACK);

			switch(displayText.length())
			{
				case 1:
					x = 28;
					break;
				case 2:
					x = 23;
					break;
				case 3:
					x = 20;
					break;
				case 4:
					x = 17;
					break;
				case 5:
					x = 15;
					break;
				case 6:
					x = 13;
					break;
				default:
					x = 30;
					break;
			

			}
			g.drawString(displayText, x,y);
			
			
			
		}
		
		

	}

	private Point rotatePoint(Point p, double angle, Point center) {
		double radians = Math.toRadians(angle);
		double x = center.x + (p.x - center.x) * Math.cos(radians) - (p.y - center.y) * Math.sin(radians);
		double y = center.y + (p.x - center.x) * Math.sin(radians) + (p.y - center.y) * Math.cos(radians);
		return new Point((int) x, (int) y);
	}
	
    
    private void tileSetup(TileType type) {
    	if (type == null)
		{
			setColor(Color.gray);
		}
		else switch(type)
    	{
		case BackgroundTile:
			setColor(Color.WHITE);
			break;
		case BasicObstacle:
			setColor(Color.black);
			setDisplayText("");
			break;
		case Hole:
			setColor(Color.WHITE);
			break;
		case PointTile:
			switch(getScore())
			{
			case 1:
				setColor(C1aero);
				setDisplayText(""+score);
				break;
			case 2:
				setColor(C2puce);
				setDisplayText(""+score);
				break;
			case 4:	
				setColor(C3tangerine);
				setDisplayText(""+score);
				break;
			case 8:	
				setColor(C4rust);
				setDisplayText(""+score);
				break;
			case 16:	
				setColor(C5moss);
				setDisplayText(""+score);
				break;
			case 32:
				setColor(C6uv);
				setDisplayText(""+score);
				break;
			case 64:	
				setColor(C7salmon);
				setDisplayText(""+score);
				break;
			case 128:	
				setColor(C8mauvre);
				setDisplayText(""+score);
				break;
			case 256:
				setColor(C9npblue);
				setDisplayText(""+score);
				break;
			case 512:
				setColor(C10pistachio);
				setDisplayText(""+score);
				break;
			case 1024:
				setColor(C11plum);
				setDisplayText(""+score);
				break;
			case 2048:
				setColor(C12persiangreen);
				setDisplayText(""+score);
				break;
			case 4096:
				setColor(C13thistle);
				setDisplayText(""+score);
				break;
			case 8192:
				setColor(C14columbiablue);
				setDisplayText(""+score);
				break;
			case 16384:
				setColor(C15vanilla);
				setDisplayText(""+score);
				break;
			case 32768:
				setColor(C16cerise);
				setDisplayText(""+score);
				break;
			default:
				setColor(C1aero);
				setScore(1);
				setDisplayText(""+score);

			}
			break;
		case Bomb:
			setColor(pink);
			setDisplayText("BOMB");
			break;
		case WildCard:
			setColor(pink2);
			setDisplayText("WILD");
			break;
		case Shuffler:
			setColor(pink3);
			setDisplayText("SHFL");
			break;
		default:
			break;
    	
    	}
    	
    	
    }


	public int getScore() {
		return score;
	}	
	public void setScore(int score) {
		this.score = score;
	}	

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
    
    
    
}



/*package SlideGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HexagonUI extends javax.swing.JPanel{
	
	int renderSize, numPoint = 6;
	public Image hexagon;
	public int[] xPoints = {100,50,-50,-100,-50,50,}, yPoints = {0,-87,-87,0,87,87};
	Polygon p;
	
	//100,0 50,-87 -50,-87 -100,-0 -50,87 50,87
	
	HexagonUI(double size, TileType t) throws IOException	
	{
		
		p = new Polygon(xPoints, xPoints, numPoint);
		
		size = 1;
		for(int i = 0; i < 6; i++)
		{
			
			p.addPoint(xPoints[i], xPoints[i]);
			
		}
		
		
	}
	
	   @Override
	   public void paint(Graphics g) {
		  
		   super.paintComponent(g);
		   g.drawPolygon(p);
		   
		   
       
	   }
	

}
*/