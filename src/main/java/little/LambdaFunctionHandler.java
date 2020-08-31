package little;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Event;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();

        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getObjectContent()));
            while ((csvOutput = br.readLine()) != null) {
                context.getLogger().log("I read a line");
            }
        } catch (IOException ioe) {
            context.getLogger().log(ioe.getMessage());
        }
    }
}
