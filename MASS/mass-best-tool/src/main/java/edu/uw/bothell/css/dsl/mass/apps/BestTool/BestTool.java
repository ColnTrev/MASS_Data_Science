package edu.uw.bothell.css.dsl.MASS.BestTool;
import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.MASS.logging.*;
import java.util.*;
public class BestTool {
    public static void main(String[] args){
      if(args.length < 5){
        System.exit(-1);
      }
      Vector<int[]> dataPoints;
      Centroid centroids = new Centroid();

      int nIter = 4;
      int sizeX = 5;
      int sizeY = 4;
      int nAgents = 20;
      dataPoints = initDataPoints(20, 10,10);

      centroids.initRandom(dataPoints); // assigns random centroids from dataPoints
      MASS.getLogger().setLogLevel(LogLevel.DEBUG);

      MASS.init();

      Places storage = new Places(100, Storage.class.getName(), dataPoints, sizeX,sizeY);
      Agents tools   = new Agents(100, Tool.class.getName(), centroids, storage, nAgents);

      for(int i = 0; i < nIter; i++){
        tools.callAll(Tool.cluster_);
        Object[] pass1 = new Object[tools.nAgents()];
        Object[] res = (Object[])tools.callAll(Tool.collect_, pass1);
        Centroid[] solutions = Arrays.copyOf(res,res.length,Centroid[].class);

        // if we are on the last iteration, don't compute new clusters
        if(i < (nIter - 1)){
          newCentroids(solutions);
          tools.callAll(Tool.distribute_,(Object[])solutions);
        }

      }
      Object[] finalRes = (Object[])tools.callAll(Tool.collect_, new Object[tools.nAgents()]);

      MASS.finish();
      Centroid[] finalCents = Arrays.copyOf(finalRes,finalRes.length,Centroid[].class);
      for(Centroid cent : finalCents){
        cent.printCentroids();
      }
    }

    public static Vector<int[]> initDataPoints(int numPoints, int maxX, int maxY){
      Vector<int[]> d = new Vector<>();
      int x;
      int y;
      Random rand = new Random();
      for(int i = 0; i < numPoints; i++){
          x = rand.nextInt(maxX);
          y = rand.nextInt(maxY);
        d.add(new int[]{x,y});
      }
      return d;
    }

    public static void newCentroids(Centroid[] solutions){
      for(Centroid cent: solutions){
        cent.computeNewCentroids();
        cent.clearMembership();
      }
    }

}
