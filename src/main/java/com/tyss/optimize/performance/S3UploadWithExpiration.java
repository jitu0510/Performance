package com.tyss.optimize.performance;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.File;
import java.time.Duration;

public class S3UploadWithExpiration {

    public static void main(String[] args) {
        // Define the bucket and folder path
        String bucketName = "acoe-1";
        String folderName = "folder1/";
        String fileName = "your-file.txt";
        String filePath = "/path/to/your/file.txt";

        // Region and S3 Client
        Region region = Region.AP_SOUTH_1; // Use your region
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Create the "folder" (simply an empty object with a folder path)
        createFolder(s3, bucketName, folderName);

        // Upload file with a pre-signed URL valid for 2 days
       // uploadFileWithExpiry(s3, bucketName, folderName + fileName, filePath, region);
        
        // Close the client
        s3.close();
    }

    // Function to create a folder in S3
    private static void createFolder(S3Client s3, String bucketName, String folderName) {
        PutObjectRequest folderRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName)
                .build();

        PutObjectResponse folderResponse = s3.putObject(folderRequest, RequestBody.empty());
        System.out.println("Folder created: " + folderName);
    }

    // Function to upload a file with expiration (2 days)
    private static void uploadFileWithExpiry(S3Client s3, String bucketName, String key, String filePath, Region region) {
        // Create the presigner
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Create a pre-signed PUT request
        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(r -> r.signatureDuration(Duration.ofDays(2))
                .putObjectRequest(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()));

        // Output the pre-signed URL
        System.out.println("Pre-signed URL to upload the file: " + presignedRequest.url());

        // Upload the file
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

      //  PutObjectResponse response = s3.putObject(objectRequest, new File(filePath));
        System.out.println("File uploaded successfully to: " + key);
    }
}

