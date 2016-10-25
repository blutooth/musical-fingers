package ox.musicalfingers.instrument;

public class KeyPos {
	Float x;
	Float y;
	Float width;
	Float height;
	
public KeyPos(Float x, Float y, Float width, Float height ) {
		this.x= x; this.y=y; this.width=width; this.height=height; }
	
  public Boolean  isInRange( Float a, Float b) {
	return (x<=a && a<=x+width)&&(y<=b && b<=y+height);
  }
	
}