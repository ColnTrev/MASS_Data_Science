// Collin Gordon
// Assignment 3 "Fun" with Hadoop
// 11/21/16
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

// This class generates an inverted index listing of 5 keywords in a given set of
// files using Hadoop MapReduce paradigm oriented programming
public class InvertedIndex {
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
	private JobConf conf;

	public void configure(JobConf job){
	    this.conf = job;
	}

	public void map(LongWritable docId, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	    FileSplit fileSplit = (FileSplit)reporter.getInputSplit();
	    String filename = "" + fileSplit.getPath().getName();
            String line = value.toString();
	    StringTokenizer tokens = new StringTokenizer(line);
            HashMap<String, Integer> results = new HashMap<>();
            int argc = Integer.parseInt(conf.get("argc"));
            
            ArrayList<String> keys = getKeys(conf, argc); // loading keywords
           
            while(tokens.hasMoreTokens()){ // checking tokens against keywords
              String tok = tokens.nextToken();
              if(keys.contains(tok)){
                //adding keywords to map
                results.put(tok, new Integer(results.getOrDefault(tok, 0) + 1));
              }
            }
            // gathering the map results in the output object
            for(HashMap.Entry<String, Integer> entry : results.entrySet()){
                output.collect(new Text(entry.getKey()), 
                               new Text(concat(filename, 
                                       entry.getValue().intValue())));
            }
	}
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
        
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException{  
          HashMap<String, Integer> docContainer = new HashMap<>(); 
          StringBuilder docList = new StringBuilder();
          
          while(values.hasNext()){ // cycling through the values
             String infostring = values.next().toString();
             String[] fileAndCount = infostring.split("_");
             String file = fileAndCount[0]; // file name
             int count = Integer.parseInt(fileAndCount[1]); // keyword count
             // adding the file name and keyword count associated with the keyword
             docContainer.put(file, new Integer(docContainer.getOrDefault(file, 0) + count));
          }
          
          // prepping map entries in desired output format
          for(HashMap.Entry<String, Integer> doc : docContainer.entrySet()){
              docList.append(doc.getKey());
              docList.append(' ');
              docList.append(doc.getValue());
              docList.append(' ');
          }
          output.collect(key, new Text(docList.toString())); // gathering outputs
	}
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(InvertedIndex.class);
        
        conf.setJobName("InvertedIndex");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        conf.set( "argc", String.valueOf( args.length - 2 ) );
        for ( int i = 0; i < args.length - 2; i++ )
            conf.set( "keyword" + i, args[i + 2] );
        
        long start = System.nanoTime();
        JobClient.runJob(conf);
        long end = System.nanoTime();
        System.out.println("Duration: " + (end - start) + " nanoseconds.");   
    }
    
    // This method concatenates the file name and count into the desired output
    // format for reduce using a StringBuilder
    public static String concat(String file, int value){
        StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append('_');
        sb.append(value);
        return sb.toString();
    }
    // This method loads the keywords from the configuration into an ArrayList
    // and returns that ArrayList to the map method for use
    public static ArrayList<String> getKeys(JobConf conf, int argc){
        ArrayList<String> keys = new ArrayList<>();
        for(int i = 0; i < argc; i++){
            keys.add(conf.get("keyword" + i));
        }
        return keys;
    }
}
    
    
