package com.app.design.gcp.dataproc;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.dataproc.v1.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DataprocExample {

    private static final String PROJECT_ID = "your-project-id";
    private static final String REGION = "your-region";
    private static final String CLUSTER_NAME = "your-cluster-name";
    private static final String JOB_NAME = "your-job-name";
    private static final String JOB_FILE = "gs://your-bucket/job.jar";

    public static void main(String[] args) throws IOException {
        try (JobControllerClient jobControllerClient = JobControllerClient.create()) {
            createCluster();
            submitJob(jobControllerClient);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteCluster();
        }
    }

    private static void createCluster() throws IOException {
        try (ClusterControllerClient clusterControllerClient = ClusterControllerClient.create()) {
            RegionName regionName = RegionName.of(PROJECT_ID, REGION);

            ClusterConfig clusterConfig = ClusterConfig.newBuilder()
                    .setGceClusterConfig(GceClusterConfig.newBuilder()
                            .setZoneUri("your-zone")
                            .build())
                    .build();

            Cluster cluster = Cluster.newBuilder()
                    .setClusterName(CLUSTER_NAME)
                    .setConfig(clusterConfig)
                    .build();

            CreateClusterRequest createClusterRequest = CreateClusterRequest.newBuilder()
                    .setRegion(regionName.toString())
                    .setCluster(cluster)
                    .build();

            OperationFuture<Cluster, ClusterOperationMetadata> operation =
                    clusterControllerClient.createClusterAsync(createClusterRequest);
            operation.get();
            System.out.println("Cluster created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void submitJob(JobControllerClient jobControllerClient) throws IOException, InterruptedException, ExecutionException {
        JobPlacement jobPlacement = JobPlacement.newBuilder()
                .setClusterName(CLUSTER_NAME)
                .build();

        SparkJob sparkJob = SparkJob.newBuilder()
                .setMainJarFileUri(JOB_FILE)
                .build();

        Job job = Job.newBuilder()
                .setPlacement(jobPlacement)
                .setSparkJob(sparkJob)
                .build();

        SubmitJobRequest submitJobRequest = SubmitJobRequest.newBuilder()
                .setRegion(REGION)
                .setJob(job)
                .build();

        OperationFuture<Job, JobMetadata> operation =
                jobControllerClient.submitJobAsOperationAsync(submitJobRequest);
        operation.get();
        System.out.println("Job submitted.");
    }

    private static void deleteCluster() throws IOException {
        try (ClusterControllerClient clusterControllerClient = ClusterControllerClient.create()) {
            RegionName regionName = RegionName.of(PROJECT_ID, REGION);

            String clusterId = String.format("projects/%s/regions/%s/clusters/%s", PROJECT_ID, REGION, CLUSTER_NAME);

            DeleteClusterRequest deleteClusterRequest = DeleteClusterRequest.newBuilder()
                    .setClusterName(clusterId)
                    .build();

            clusterControllerClient.deleteClusterAsync(deleteClusterRequest);
            System.out.println("Cluster deleted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
