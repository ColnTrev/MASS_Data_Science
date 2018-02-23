package edu.uw.bothell.css.dsl.MASS.BestTool;
import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.MASS.logging.*;
import java.util.*;
public class Tool extends Agent {
  public static final int cluster_ = 0;
  public static final int init_ = 1;
  public static final int collect_ = 2;
  public static final int recalculate_ = 3;

  private Centroid cents;

  // This should never be called
  public Tool() {
    super();
  }

  public Tool(Object args){
    super();
    cents = new Centroid();
  }
  public Object callMethod(int functionId, Object args){
    switch(functionId){
      case cluster_: return cluster();
      case init_: return init();
      case collect_: return collect(args);
      case recalculate_: return newCentroid();
    }
    return null;
  }


  public Object cluster(){
    Vector<int[]> data = ((Storage)getPlace()).getDataPoints();
    int[] member = new int[3];
    for(int[] arr : data){
      member[0] = (int)findCluster(arr);
      member[1] = arr[0];
      member[2] = arr[1];
      cents.addMember(member);
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

  public Object init(){
    Vector<int[]> data = ((Storage)getPlace()).getDataPoints();
    cents.initRandom(data);
    return null;
  }

  public Object newCentroid(){
    cents.computeNewCentroids();
    cents.clearMembership();
    return null;
  }

  public Centroid collect(Object args){
    return cents;
  }
}
