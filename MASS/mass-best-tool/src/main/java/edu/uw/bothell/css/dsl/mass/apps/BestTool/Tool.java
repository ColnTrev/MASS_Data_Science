package edu.uw.bothell.css.dsl.MASS.BestTool;
import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.MASS.logging.*;
import java.util.*;
public class Tool extends Agent {
  public static final int cluster_ = 0;
  public static final int distribute_ = 1;
  public static final int collect_ = 2;

  private Centroid cents;

  // This should never be called
  public Tool() {
    super();
  }

  public Tool(Object args){
    super();
    cents = new Centroid((Centroid)args);
  }
  public Object callMethod(int functionId, Object args){
    switch(functionId){
      case cluster_: return cluster();
      case distribute_: return distribute(args);
      case collect_: return collect(args);
    }
    return null;
  }


  public Object cluster(){
    Vector<int[]> data = ((Storage)getPlace()).getDataPoints();
    int[] frank = new int[3];
    for(int[] arr : data){
      int member = (int)findCluster(arr);
      frank[0] = member;
      frank[1] = arr[0];
      frank[2] = arr[1];
      cents.addMember(frank);
    }
    return null;
  }

  private int findCluster(int[] point){
    int current = dist(point, cents.getCentroid(0));
    int better;
    int key = 0;
    for(int i = 1; i < cents.getCentroids().size(); i++){
      better = dist(point, cents.getCentroid(i));
      if(better < current){
        current = better;
        key = i;
      }
    }
    return key;
  }

  private int dist(int[] point, int[] centroid){
    double xs = (double)(centroid[0] - point[0]);
    double ys = (double)(centroid[1] - point[1]);
    return (int)Math.sqrt((xs*xs) + (ys*ys));
  }

  public Object distribute(Object args){
    cents = new Centroid((Centroid)args);
    return null;
  }
  public Centroid collect(Object args){
    return cents;
  }
}
