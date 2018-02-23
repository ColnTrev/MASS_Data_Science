package edu.uw.bothell.css.dsl.MASS.BestTool;
import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.MASS.logging.*;
import java.util.*;
public class BestTool {
    public static void main(String[] args){
      if(args.length < 7){
        System.out.println("Usage: java -jar <program name> nIter sizeX sizeY nAgents numDataPoints maxX maxY");
        System.exit(-1);
      }
      Vector<int[]> dataPoints;


      int nIter = Integer.parseInt(args[0]);
      int sizeX = Integer.parseInt(args[1]);
      int sizeY = Integer.parseInt(args[2]);
      int nAgents = Integer.parseInt(args[3]);
      int numDataPoints = Integer.parseInt(args[4]);
      int maxX = Integer.parseInt(args[5]);
      int maxY = Integer.parseInt(args[6]);
      long startTime;
      long endTime;

      dataPoints = initDataPoints(numDataPoints, maxX,maxY);
      MASS.getLogger().setLogLevel(LogLevel.DEBUG);

      MASS.init();
      Places storage = new Places(100, Storage.class.getName(), dataPoints, sizeX,sizeY);
      Agents tools   = new Agents(100, Tool.class.getName(), null, storage, nAgents);
      tools.callAll(Tool.init_);
      startTime = System.currentTimeMillis();
      for(int i = 0; i < nIter; i++){
        tools.callAll(Tool.cluster_);

        // if we are on the last iteration, don't compute new clusters
        if(i < (nIter - 1)){
          tools.callAll(Tool.recalculate_);
        }

      }
      endTime = System.currentTimeMillis();
      Object[] finalRes = (Object[])tools.callAll(Tool.collect_, new Object[tools.nAgents()]);

      MASS.finish();
      Centroid[] finalCents = Arrays.copyOf(finalRes,finalRes.length,Centroid[].class);
      long totalRunTime = endTime - startTime;
      System.out.println(totalRunTime);
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
}
