package io.francoisbotha.namazingserver.services.impl;

import java.io.File;
import java.io.IOException;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.francoisbotha.namazingserver.services.S3Service;
import io.francoisbotha.namazingserver.utility.Utility;

import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import io.francoisbotha.namazingserver.exceptions.S3Exception;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {
    
    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    private static final String PROFILE_PICTURE_FILE_NAME = "profilePicture";

    @Override
    public void downloadFile(String keyName) {

        try {

            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: "  + s3object.getObjectMetadata().getContentType());
            Utility.displayText(s3object.getObjectContent());
            log.debug("===================== Import File - Done! =====================");

        } catch (AmazonServiceException ase) {
            log.debug("Caught an AmazonServiceException from GET requests, rejected reasons:");
            log.debug("Error Message:    " + ase.getMessage());
            log.debug("HTTP Status Code: " + ase.getStatusCode());
            log.debug("AWS Error Code:   " + ase.getErrorCode());
            log.debug("Error Type:       " + ase.getErrorType());
            log.debug("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.debug("Caught an AmazonClientException: ");
            log.debug("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            log.debug("IOE Error Message: " + ioe.getMessage());
        }
    }

    @Override
    public void uploadFile(String keyName, String uploadFilePath) {

        try {

            File file = new File(uploadFilePath);
            s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
            log.debug("===================== Upload File - Done! =====================");

        } catch (AmazonServiceException ase) {
            log.debug("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            log.debug("Error Message:    " + ase.getMessage());
            log.debug("HTTP Status Code: " + ase.getStatusCode());
            log.debug("AWS Error Code:   " + ase.getErrorCode());
            log.debug("Error Type:       " + ase.getErrorType());
            log.debug("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.debug("Caught an AmazonClientException: ");
            log.debug("Error Message: " + ace.getMessage());
        }
    }

    @Override
    public void deleteFile(String keyName) {

        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            log.debug("===================== Delete File - Done! =====================");

        } catch (AmazonServiceException ase) {
            log.debug("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            log.debug("Error Message:    " + ase.getMessage());
            log.debug("HTTP Status Code: " + ase.getStatusCode());
            log.debug("AWS Error Code:   " + ase.getErrorCode());
            log.debug("Error Type:       " + ase.getErrorType());
            log.debug("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.debug("Caught an AmazonClientException: ");
            log.debug("Error Message: " + ace.getMessage());
        }
    }

    /********************************
     * Adapted from:
     * https://github.com/mtedone/devopsbuddy/blob/develop/src/main/java/com/devopsbuddy/backend/service/S3Service.java
     */

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param uploadedFile The multipart file uploaed by the user
     * @param username The username for which to upload this file
     * @return The URL of the uploaded image
     * @throws S3Exception If something goes wrong
     */
    public String storeProfileImage(MultipartFile uploadedFile, String username)  {

        String profileImageUrl = null;

        try {
            if (uploadedFile != null && !uploadedFile.isEmpty()) {
                byte[] bytes = uploadedFile.getBytes();

                // The root of our temporary assets. Will create if it doesn't exist
                File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);
                if (!tmpImageStoredFolder.exists()) {
                    log.debug("Creating the temporary root for the S3 assets");
                    tmpImageStoredFolder.mkdirs();
                }

                // The temporary file where the profile image will be stored
                File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath()
                        + File.separatorChar
                        + PROFILE_PICTURE_FILE_NAME
                        + "."
                        + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));

                log.debug("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

                try(BufferedOutputStream stream =
                            new BufferedOutputStream(
                                    new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))) {
                    stream.write(bytes);
                }

                profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

                // Clean up the temporary folder
                tmpProfileImageFile.delete();
            }
        } catch (IOException e) {
            throw new S3Exception(e);
        }

        return profileImageUrl;

    }

    /**
     * Returns the root URL where the bucket name is located.
     * <p>Please note that the URL does not contain the bucket name</p>
     * @param bucketName The bucket name
     * @return the root URL where the bucket name is located.
     * @throws S3Exception If something goes wrong.
     */
    private String ensureBucketExists(String bucketName) {

        String bucketUrl = null;

        try {
            if (!s3client.doesBucketExistV2(bucketName)) {
                log.debug("Bucket {} doesn't exists...Creating one");
                s3client.createBucket(bucketName);
                log.debug("Created bucket: {}", bucketName);
            }
           bucketUrl = "https://" + bucketName + ".s3.amazonaws.com";
        } catch (AmazonClientException ace) {
            log.debug("An error occurred while connecting to S3. Will not execute action" +
                    " for bucket: {}", bucketName, ace);
            throw new S3Exception(ace);
        }


        return bucketUrl;
    }

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param resource The file resource to upload to S3
     * @return The URL of the uploaded resource or null if a problem occurred
     *
     * @throws S3Exception If the resource file does not exist
     */
    private String storeProfileImageToS3(File resource, String username) {

        String resourceUrl = null;

        if (!resource.exists()) {
            log.debug("The file {} does not exist. Throwing an exception", resource.getAbsolutePath());
            throw new S3Exception("The file " + resource.getAbsolutePath() + " doesn't exist");
        }

        String rootBucketUrl = this.ensureBucketExists(bucketName);

        if (null == rootBucketUrl) {

            log.debug("The bucket {} does not exist and the application " +
                    "was not able to create it. The image won't be stored with the profile", rootBucketUrl);

        } else {

            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username + "/" + PROFILE_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(resource.getName());

            try {
                s3client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
            } catch (AmazonClientException ace) {
                log.debug("A client exception occurred while trying to store the profile" +
                        " image {} on S3. The profile image won't be stored", resource.getAbsolutePath(), ace);
                throw new S3Exception(ace);
            }
            resourceUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;
            log.debug(resourceUrl);
        }

        return resourceUrl;

    }

}