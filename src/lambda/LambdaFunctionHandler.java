package lambda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;

import lambda.file.Record;
import lambda.file.Records;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	private static final String bucketReplace = "'dgallagher-bucket','dgallagher.file-dump','dgallagher.lambda-store','dgallagher.reporting','dgallagher.reporting-service'";
	private static final String redShiftReplace = "'harvest.surplus','ds.backup','harvest.logs', 'pu.table'";
	private static final String loaderData = "{id : 0,bucket : 'dgallagher-bucket',live : true,files : 10000,batch : 1000,redShift : 'harvest.surplus'}, {id : 1,bucket : 'dgallagher.file-dump',live : true,files : 10000,batch : 1000,redShift : 'ds.backup'}, {id : 2,bucket : 'dgallagher.lambda-store',live : false,files : 250000,batch : 20000,redShift : 'harvest.logs'}";

	@Override
	public String handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);
		try {
			final AmazonS3 s3Client = new AmazonS3Client();
			String html = getFileContents(s3Client, "dgallagher-bucket", "HTMLTEMPLATE.html");
			html = html.replace(bucketReplace, getBucketsLists(s3Client));
			html = html.replace(redShiftReplace, getRedShiftList());
			html = html.replace(loaderData, getRecords(s3Client));
			return html;
		} catch (Exception e) {
			return e.toString();
		}
	}

	public static void main(final String[] args) {
		try {
			final AmazonS3 s3Client = new AmazonS3Client();
			getRecords(s3Client) ;
			final String loaderJson = getFileContents(s3Client, "dgallagher-bucket", "loader-data.json");
			final Records records = new ObjectMapper().readValue(loaderJson, Records.class);
			int w = 0;
		} catch (Exception e) {
			int z = 0;
		}
	}
	
	public static String getRecords(final AmazonS3 s3Client) throws Exception {
		final StringBuilder sb = new StringBuilder();
		final String loaderJson = getFileContents(s3Client, "dgallagher-bucket", "loader-data.json");
		if(loaderJson == null || loaderJson.equals("")){
			return sb.toString();
		}
		final ObjectMapper mapper = new ObjectMapper();
		final Records records = mapper.readValue(loaderJson, Records.class);
		boolean first = true;
		int id = 0;
		for(final Record record : records.getRecords()){
			record.setId(id);
			if(!first){
				sb.append(",");
			} else{
				first = false;
			}
			sb.append(mapper.writeValueAsString(record));
			id++;
		}
		return sb.toString();
	}

	private static String getRedShiftList() throws Exception {
		return "'harvest.surplus','ds.backup','harvest.logs', 'pu.table'";
	}

	private static String getBucketsLists(final AmazonS3 s3Client) throws Exception {
		final List<Bucket> buckets = s3Client.listBuckets();
		Collections.sort(buckets, new Comparator<Bucket>() {
			@Override
			public final int compare(final Bucket a, final Bucket b) {
				return a.getName().toUpperCase().compareTo(b.getName().toUpperCase());
			};
		});
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final Bucket b : buckets) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append("'").append(b.getName()).append("'");
		}
		return sb.toString();
	}

	private static String getFileContents(final AmazonS3 s3Client, final String bucketName, final String fileName)
			throws Exception {

		InputStream in = null;
		try {
			final S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
			in = s3object.getObjectContent();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			final StringBuilder sb = new StringBuilder();
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				sb.append(line).append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
