import java.awt.Color;

/**
 *  A class that will be the data for a node, and which holds location
 *  and color information as well as the actual node data.
 *
 *  @param <T>  the type of the node data
 */
public class PlacedData<T> {
    /** the node data */
    private T data;

    /** location x */
    private int x;

    /** location y */
    private int y;

    /** node rendering color */
    private Color color;

    private Color borderColor;

    private Color myColor = new Color(168, 219, 237);

    public PlacedData(T data, int x, int y) {
	this.data = data;
	this.x = x;
	this.y = y;
	this.color = myColor;
	this.borderColor = myColor;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Color getColor() { return color; }
    public Color getBorderColor() { return borderColor; }
    public T getData() { return this.data; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setColor(Color color) { this.color = color; }
    public void setBorderColor(Color color) { this.borderColor = color; }  
    public void setData(T data) { this.data = data; }

    public String toString() { return data.toString() + "@(" + x + "," + y + ")"; }
}
