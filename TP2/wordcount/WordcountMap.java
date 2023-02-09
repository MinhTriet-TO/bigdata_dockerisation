package bgd.hadoop.wordcount;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import java.util.StringTokenizer;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;


// The "mapper" class
// org.apache.hadoop.mapreduce.Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
public class WordcountMap extends Mapper<Object, Text, Text, IntWritable>
{

	// The "map" function
	// protected void map(KEYIN key,
	//                    VALUEIN value,
	//                    org.apache.hadoop.mapreduce.Mapper.Context context)
	//     throws IOException,
	//            InterruptedException
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException
	{  
		// StringTokenizer allows to iterate through each word of current line (given in "value" parameter) 
		StringTokenizer tok=new StringTokenizer(value.toString(), " ");
		while(tok.hasMoreTokens())
		{
			// Get the next word on current line
			Text word=new Text(tok.nextToken());
			
			// Return (key,value) tuple: (current word,1)
			context.write(word, new IntWritable(1));
		}
	}
}
