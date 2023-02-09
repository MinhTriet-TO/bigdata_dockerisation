#!/usr/bin/python3

from pyspark.sql import SparkSession

# Load mongo data
input_uri = "mongodb://127.0.0.1/anagrams.words"
output_uri = "mongodb://127.0.0.1/anagrams.words"

# Create a SparkSession
my_spark = SparkSession\
    .builder\
    .appName("AnagramsApp")\
    .config("spark.mongodb.input.uri", input_uri)\
    .config("spark.mongodb.output.uri", output_uri)\
    .config('spark.jars.packages','org.mongodb.spark:mongo-spark-connector_2.12:2.4.2')\
    .getOrCreate()

# Create a DataFrame
words = my_spark.createDataFrame([("test",  "estt"), ("listen", "eilnst"), ("silent", "eilnst"), ("raptor", "aoprrt"), ("parrot", "aoprrt")], ["Word", "Anagram"])

print(words)

# Write the data into MongoDB
words.write.format("com.mongodb.spark.sql.DefaultSource").mode("append").save()

# Read it back from MongoDB into a new Dataframe
df = my_spark.read.format('com.mongodb.spark.sql.DefaultSource').load()

print(df.show())
# print(df.printSchema())
# print(df.first())