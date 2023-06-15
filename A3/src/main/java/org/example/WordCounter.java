package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * References for Spark reduce algorithm
 * 1. https://stackoverflow.com/questions/38880956/spark-2-0-0-arrays-aslist-not-working-incompatible-types
 * 2. https://spark.apache.org/docs/latest/rdd-programming-guide.html
 * 3. https://www.tutorialkart.com/apache-spark/spark-rdd-flatmap/
 */


public class WordCounter {
    private List<String> keywords;

    public WordCounter(List<String> keywords) {
        this.keywords = keywords;
    }

    public void execute() throws IOException{
        SparkConf sparkConf = new SparkConf().setAppName("csci5408as").setMaster("34.125.100.196:8080"); //2
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf); //2
        File file = new File("WordCount");
        FileWriter fileWriter = new FileWriter(file);
        for (String item: keywords) {
            JavaRDD<String> lines = javaSparkContext.textFile(item + ".txt");
            //Map
            JavaRDD<String> words = lines.flatMap(content -> Arrays.asList(content.split(" ")).iterator()); //1
            //Reduce
            JavaPairRDD<String, Integer> wordCount = words.mapToPair(t -> new Tuple2<>(t, 1)).reduceByKey((x, y) -> x + (int) y); //3
            Map<String, Integer> wordCountMap = wordCount.collectAsMap();
            fileWriter.write(item + ": " + wordCountMap.get(item) + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
