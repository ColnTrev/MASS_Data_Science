package edu.uw.bothell.css.dsl.MASS.BestTool;
import java.util.*;
import java.io.Serializable;
public class Centroid implements Serializable {
  private  Vector<int[]> centroids;
  private HashMap<Integer,Vector<int[]>> membership;
  private int numClusters;

  public Centroid(){
    centroids = new Vector<>();
    membership = new HashMap<>();
    numClusters = 3;
    initMembership();
  }

  public Centroid(int nc){
    numClusters = nc;
    centroids = new Vector<>();
    membership = new HashMap<>();
    initMembership();
  }
  public Centroid(Centroid cent){
    this.centroids = cent.centroids;
    this.membership = cent.membership;
  }
  //picks random centroids from a set of dataPoints
  public void initRandom(Vector<int[]> dataPoints){
    int i = 0;
    Random rand = new Random();
    while(i < numClusters){
      int idx = rand.nextInt(dataPoints.size());
      int[] point = dataPoints.elementAt(idx);
      centroids.add(point);
      i++;
    }
  }

  //initializes centroids at the given points
  public void initChosen(Vector<int[]> centers){
    if(centers.size() > numClusters){
      numClusters = centers.size();
    }

    if(!centroids.isEmpty()){
      centroids = new Vector<>();
    }
    for(int[] center : centers){
      centroids.add(center);
    }
  }

  //computes new centroids based on data in membership
  public void computeNewCentroids(){
    Vector<int[]> newCenters = new Vector<>();
    int sumX = 0;
    int sumY = 0;
    int total;

    for(int i = 0; i < centroids.size(); i++){
      Vector<int[]> points = membership.get(i);
      total = points.size();
      for(int[] point : points){
        sumX += point[0];
        sumY += point[1];
      }
      if(total > 0){
        int newX = sumX / total;
        int newY = sumY / total;
        newCenters.add(new int[]{newX, newY});
      } else {
        newCenters.add(new int[]{0,0});
      }
    }

    initChosen(newCenters);
  }

  //returns a centroid at a given index
  public int[] getCentroid(int idx){
    return centroids.elementAt(idx);
  }

  // returns the vector of centroids
  public Vector<int[]> getCentroids(){
    return centroids;
  }

  // adds a point to the membership of a specific cluster
  public void addMember(int[] data){
    if(data.length != 3){
      //error
    }

    membership.get(data[0]).add(new int[]{data[1], data[2]});
  }

  public void printCentroids(){
    if(centroids == null){
      System.out.println("woops");
      return;
    }
    for(int[] arr : centroids){
      System.out.println("Centroid: " + Arrays.toString(arr));
    }
  }
  // outputs membership
  public void printMembership(){
    if(membership == null){
      System.out.println("oops");
      return;
    }
    for(Map.Entry<Integer,Vector<int[]>> entry : membership.entrySet()){
      System.out.print("Cluster: " + entry.getKey() + " ");
      for(int[] arr : entry.getValue()){
        System.out.print(Arrays.toString(arr) + " ");
      }
      System.out.println();
    }
  }

  //clears membership for next cycle
  public void clearMembership(){
    membership = new HashMap<>();
    initMembership();
  }

  // initializes membership
  private void initMembership(){
    for(int i = 0; i < numClusters; i++){
      membership.put(new Integer(i), new Vector<>());
    }
  }
}
