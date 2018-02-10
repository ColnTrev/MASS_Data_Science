package edu.uw.bothell.css.dsl.MASS.BestTool;
import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.MASS.logging.*;
import java.util.*;
public class Storage extends Place {
  public static final int init_    = 0;
  public static final int sizeGrid_ = 1;
  public static final int rept_    = 2;

  private Vector<int[]> dataPoints;
  private Log4J2Logger logger;

  // This should never be called
  public Storage(){
    super();
  }


  public Storage(Object args){
    super();
    logger = Log4J2Logger.getInstance();
    dataPoints = new Vector<>((Vector<int[]>)args);

    Vector<int[]> neighbors = new Vector<>();
    neighbors.add(new int[] {0,-1});
    neighbors.add(new int[] {1,0});
    neighbors.add(new int[] {0,1});
    neighbors.add(new int[] {-1,0});
    setNeighbors(neighbors);
  }

  public Object callMethod(int functionId, Object args) {
      switch(functionId) {
      case sizeGrid_:
        return sizeGrid();
      }
      return null;
  }

  protected int[] sizeGrid(){
    int maxX = 0;
    int maxY = 0;
    for(int[] arr : dataPoints){
      maxX = Math.max(arr[0], maxX);
      maxY = Math.max(arr[1], maxY);
    }
    return new int[] {maxX, maxY};
  }

  protected Vector<int[]> getDataPoints(){
    return dataPoints;
  }
}
