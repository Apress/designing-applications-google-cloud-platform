package com.app.design.gcp.dataflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;


public class DataflowPipeline {
    public static void main(String[] args) {
        // Create the pipeline options
        PipelineOptions options = PipelineOptionsFactory.create();

        // Create the pipeline
        Pipeline pipeline = Pipeline.create(options);

        // Read the input data from Cloud Storage
        PCollection<String> input = pipeline.apply(TextIO.read().from("gs://your-input-bucket/input.txt"));

        // Apply transformation on the input data
        PCollection<KV<String, Integer>> transformedData = input.apply(ParDo.of(new DoFn<String, KV<String, Integer>>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                String line = c.element();
                // Perform your transformation logic here
                // For example, split the line and count the words
                String[] words = line.split(" ");
                for (String word : words) {
                    c.output(KV.of(word, 1));
                }
            }
        }));

        // Write the transformed data to Cloud Storage
        transformedData.apply(ParDo.of(new DoFn<KV<String, Integer>, String>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                KV<String, Integer> element = c.element();
                String output = element.getKey() + "," + element.getValue();
                c.output(output);
            }
        })).apply(TextIO.write().to("gs://your-output-bucket/output.txt"));

	     // Write the transformed data to BigQuery
	        transformedData.apply(BigQueryIO.<KV<String, Integer>>write()
	                .to("your-project-id:your-dataset.your-table")
	                .withSchema(getTableSchema())
	                .withFormatFunction(element -> new TableRow()
	                        .set("word", element.getKey())
	                        .set("count", element.getValue()))
	                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
	                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
	                .withMethod(BigQueryIO.Write.Method.STREAMING_INSERTS));

        // Run the pipeline
        pipeline.run();
    }

    private static TableSchema getTableSchema() {
        // Define and return the schema for the BigQuery table
        // For example:
        TableSchema schema = new TableSchema();
        List<TableFieldSchema> fields = new ArrayList<>();
        fields.add(new TableFieldSchema().setName("word").setType("STRING"));
        fields.add(new TableFieldSchema().setName("count").setType("INTEGER"));
        schema.setFields(fields);
        return schema;
    }

}

