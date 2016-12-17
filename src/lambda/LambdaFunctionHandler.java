package lambda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	@Override
	public String handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);
		return getFileContents();
	}
	
	private String getFileContents() {
		InputStream in = null;
		try{
			final AmazonS3 s3Client = new AmazonS3Client();
			final S3Object s3object = s3Client.getObject(new GetObjectRequest("dgallagher-bucket", "HTMLTEMPLATE.html"));
			in = s3object.getObjectContent();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			final StringBuilder sb = new StringBuilder();
	        while (true) {
	            line = reader.readLine();
	            sb.append(line).append("\r\n");
	            if (line == null){
	            	break;
	            }
	        }
			return sb.toString();
		} catch(Exception e){
			return e.toString();
		} finally{
			if(in != null){
				try{
					in.close();
				} catch(Exception e){
				}
			}
		}
	}

	private static final String HTML = 
			"<!DOCTYPE html>\n" +
					"<html ng-app=\"s3RedShiftApp\">\n" +
					"<head>\n" +
					"<meta charset=\"utf-8\">\n" +
					"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
					"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
					"<title>S3-RedShift Loader</title>\n" +
					"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script>\n" +
					"<script src=\"https://ajax.googleapis.com/ajax/libs/angularjs/1.6.0/angular.min.js\"></script>\n" +
					"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
					"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
					"<script type=\"text/javascript\">\n" +
					"	angular.module('s3RedShiftApp', []).controller(\n" +
					"			's3RedShiftAppController',\n" +
					"			function($scope, $window) {\n" +
					"				var mydata = this;\n" +
					"				\n" +
					"				// filter\n" +
					"				mydata.filterText = null;\n" +
					"				mydata.clearFilter = function(){\n" +
					"					mydata.filterText = null;\n" +
					"					this.filterProcess();\n" +
					"				}\n" +
					"				// filter processor\n" +
					"				mydata.filterProcess = function() {\n" +
					"					console.log('Filter:'+mydata.filterText);\n" +
					"					if(mydata.filterText == null || mydata.filterText == ''){\n" +
					"						mydata.filteredData = mydata.data;\n" +
					"						return;\n" +
					"					}\n" +
					"					mydata.filteredData = [];\n" +
					"					var parts = mydata.filterText.toUpperCase().split(' ');\n" +
					"					angular.forEach(mydata.data, function(row) {\n" +
					"						var passed = false;\n" +
					"						for (i = 0; i < parts.length; ++i) {\n" +
					"							var part = parts[i];\n" +
					"							if (row.bucket.toUpperCase().includes(part)){\n" +
					"								passed = true;\n" +
					"								continue;\n" +
					"							} else if (row.redshift.toUpperCase().includes(part)){\n" +
					"								passed = true;\n" +
					"								continue;\n" +
					"							}\n" +
					"							passed = false;\n" +
					"						}\n" +
					"						if(passed){\n" +
					"							mydata.filteredData.push(row);\n" +
					"						}\n" +
					"					});\n" +
					"				};\n" +
					"				\n" +
					"				// form data\n" +
					"				mydata.selectedBucket = null;\n" +
					"				mydata.buckets = ['dgallagher.surplus','dgallagher.files','dgallagher.lambda','dgallagher.store','dgallagher.glacier'];\n" +
					"				mydata.selectedRedshift = null;\n" +
					"				mydata.redshift = ['harvest.surplus','ds.backup','harvest.logs'];\n" +
					"				mydata.selectedStatus = null;\n" +
					"				mydata.status = ['No', 'Yes'];\n" +
					"				mydata.selectedFiles = null;\n" +
					"				mydata.selectedBatch = null;\n" +
					"				\n" +
					"				// form alert\n" +
					"				mydata.showAlert = false;\n" +
					"				mydata.alertMessage = null;\n" +
					"				// close alert\n" +
					"				mydata.closeAlert = function(){\n" +
					"					mydata.showAlert = false;\n" +
					"					mydata.alertMessage = null;\n" +
					"				}\n" +
					"				\n" +
					"				// form validation\n" +
					"				mydata.validateForm = function(){\n" +
					"					console.log('in validation');\n" +
					"					if(mydata.selectedBucket == null){\n" +
					"						mydata.alertMessage = 'Please select a bucket';\n" +
					"						mydata.showAlert = true;\n" +
					"						return false;\n" +
					"					}\n" +
					"					if(mydata.selectedStatus == null){\n" +
					"						mydata.alertMessage = 'Please select if live';\n" +
					"						mydata.showAlert = true;\n" +
					"						return false;\n" +
					"					}\n" +
					"					if(mydata.selectedFiles == null){\n" +
					"						mydata.alertMessage = 'Please select a number of file';\n" +
					"						mydata.showAlert = true;\n" +
					"						return false;\n" +
					"					}\n" +
					"					if(mydata.selectedBatch == null){\n" +
					"						mydata.alertMessage = 'Please select a batch size';\n" +
					"						mydata.showAlert = true;\n" +
					"						return false;\n" +
					"					}\n" +
					"					if(mydata.selectedRedshift == null){\n" +
					"						mydata.alertMessage = 'Please select a RedShift table';\n" +
					"						mydata.showAlert = true;\n" +
					"						return false;\n" +
					"					}\n" +
					"					return true;\n" +
					"				}\n" +
					"				\n" +
					"				// new record validation\n" +
					"				mydata.validateNew = function(){\n" +
					"					var result = true;\n" +
					"					angular.forEach(mydata.data, function(item){\n" +
					"						if(item.bucket == mydata.selectedBucket){\n" +
					"							mydata.alertMessage = 'Loader already exists for bucket: '+mydata.selectedBucket;\n" +
					"							mydata.showAlert = true;\n" +
					"							result = false;\n" +
					"						}\n" +
					"					});\n" +
					"					if(!result){\n" +
					"						return false;\n" +
					"					}\n" +
					"					angular.forEach(mydata.data, function(item){\n" +
					"						if(item.redshift == mydata.selectedRedshift){\n" +
					"							mydata.alertMessage = 'Loader already exists for RedShift: '+mydata.selectedRedshift;\n" +
					"							mydata.showAlert = true;\n" +
					"							result = false;\n" +
					"						}\n" +
					"					});\n" +
					"					if(!result){\n" +
					"						return false;\n" +
					"					}\n" +
					"					return true;\n" +
					"				}\n" +
					"\n" +
					"				// table header\n" +
					"				mydata.header = [\n" +
					"					{name: 'Bucket', setting : ''},\n" +
					"					{name: 'Live', setting : ''},\n" +
					"					{name: 'Files', setting : ''},\n" +
					"					{name: 'Batch', setting : 'hidden-xs'},\n" +
					"					{name: 'RedShift', setting : 'hidden-xs'},\n" +
					"					{name: '', setting : ''}\n" +
					"					];\n" +
					"\n" +
					"				// table data\n" +
					"				mydata.data = [ {\n" +
					"					bucket : 'dgallagher.surplus',\n" +
					"					live : true,\n" +
					"					files : 10000,\n" +
					"					batch : 1000,\n" +
					"					redshift : 'harvest.surplus'\n" +
					"				}, {\n" +
					"					bucket : 'dgallagher.files',\n" +
					"					live : true,\n" +
					"					files : 10000,\n" +
					"					batch : 1000,\n" +
					"					redshift : 'ds.backup'\n" +
					"				}, {\n" +
					"					bucket : 'dgallagher.store',\n" +
					"					live : false,\n" +
					"					files : 250000,\n" +
					"					batch : 20000,\n" +
					"					redshift : 'harvest.logs'\n" +
					"				}, ];\n" +
					"				\n" +
					"				mydata.filteredData = mydata.data;\n" +
					"				\n" +
					"				// display selected table row, based on bucket name\n" +
					"				mydata.edit = function(bucketName){\n" +
					"					angular.forEach(mydata.data, function(item){\n" +
					"						if(item.bucket == bucketName){\n" +
					"							//console.log(bucketName);\n" +
					"							mydata.showAlert = false;\n" +
					"							mydata.selectedBucket = bucketName;\n" +
					"							mydata.selectedRedshift = item.redshift;\n" +
					"							if(item.live){\n" +
					"								mydata.selectedStatus = 'Yes';\n" +
					"							} else{\n" +
					"								mydata.selectedStatus = 'No';\n" +
					"							}\n" +
					"							mydata.selectedFiles = item.files;\n" +
					"							mydata.selectedBatch = item.batch;\n" +
					"						}\n" +
					"						\n" +
					"					});\n" +
					"				}\n" +
					"				\n" +
					"				// clear bucket\n" +
					"				mydata.clear = function(bucketName){\n" +
					"					mydata.showAlert = false;\n" +
					"					mydata.selectedBucket = null;\n" +
					"					mydata.selectedRedshift = null;\n" +
					"					mydata.selectedStatus = null;\n" +
					"					mydata.selectedFiles = null;\n" +
					"					mydata.selectedBatch = null;\n" +
					"				}\n" +
					"				\n" +
					"				mydata.update = function(){\n" +
					"					console.log('in update');\n" +
					"					if(!this.validateForm()){\n" +
					"						console.log('validation failed');\n" +
					"						return;\n" +
					"					}\n" +
					"				}\n" +
					"				\n" +
					"				mydata.addNew = function(){\n" +
					"					console.log('in update');\n" +
					"					if(!this.validateForm()){\n" +
					"						console.log('validation failed');\n" +
					"						return;\n" +
					"					}\n" +
					"					if(!this.validateNew()){\n" +
					"						console.log('validation failed dup');\n" +
					"						return;\n" +
					"					}\n" +
					"				}\n" +
					"				\n" +
					"				mydata.delete = function(){\n" +
					"					console.log('in update');\n" +
					"					if(!this.validateForm()){\n" +
					"						console.log('validation failed');\n" +
					"						return;\n" +
					"					}\n" +
					"				}\n" +
					"				\n" +
					"				\n" +
					"			});\n" +
					"</script>\n" +
					"<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
					"<script type=\"text/javascript\">\n" +
					"	google.charts.load(\"current\", {\n" +
					"		packages : [ \"corechart\" ]\n" +
					"	});\n" +
					"	google.charts.setOnLoadCallback(drawChart);\n" +
					"	function drawChart() {\n" +
					"		var data = google.visualization.arrayToDataTable([\n" +
					"				[ 'Task', 'Hours per Day' ], [ 'Work', 11 ], [ 'Eat', 2 ],\n" +
					"				[ 'Commute', 2 ], [ 'Watch TV', 2 ], [ 'Sleep', 7 ] ]);\n" +
					"\n" +
					"		var options = {\n" +
					"			title : 'My Daily Activities',\n" +
					"			is3D : true,\n" +
					"		};\n" +
					"\n" +
					"		//var chart = new google.visualization.PieChart(document\n" +
					"		//		.getElementById('piechart_3d'));\n" +
					"		//chart.draw(data, options);\n" +
					"	}\n" +
					"</script>\n" +
					"</head>\n" +
					"<body ng-controller=\"s3RedShiftAppController as mydata\">\n" +
					"	<div class=\"container\">\n" +
					"		<div>\n" +
					"			<h3 class=\"page-header\">S3-RedShift Loader</h3>\n" +
					"		</div>\n" +
					"		<div class=\"form-group has-feedback input-group-sm\">\n" +
					"			<input ng-model=\"mydata.filterText\" ng-change=\"mydata.filterProcess()\" type=\"search\" class=\"form-control\" aria-describedby=\"inputError2Status\" placeholder=\"filter...\">\n" +
					"			<span ng-click=\"mydata.clearFilter()\" class=\"glyphicon glyphicon-remove form-control-feedback\" style=\"pointer-events: initial; color: #31b0d5; cursor: pointer;\"></span>\n" +
					"		</div>\n" +
					"		<div>\n" +
					"			<table class=\"table table-hover table-condensed\">\n" +
					"				<thead>\n" +
					"					<tr class=\"info\">\n" +
					"						<th class=\"{{head.setting}}\" ng-repeat=\"head in mydata.header\">{{head.name}}</th>\n" +
					"					</tr>\n" +
					"				</thead>\n" +
					"				<tbody>\n" +
					"					<tr ng-repeat=\"row in mydata.filteredData\">\n" +
					"						<td>{{row.bucket}}</td>\n" +
					"						<td ng-if=\"row.live == true\"><span class=\"label label-success\">Yes</span></td>\n" +
					"						<td ng-if=\"row.live == false\"><span class=\"label label-danger\">No</span></td>\n" +
					"						<td>{{row.files}}</td>\n" +
					"						<td class=\"hidden-xs\">{{row.batch}}</td>\n" +
					"						<td class=\"hidden-xs\">{{row.redshift}}</td>\n" +
					"						<td><a href=\"#myForm\"><button type=\"button\" ng-click=\"mydata.edit(row.bucket)\" class=\"btn btn-info btn-xs\">Edit</button></a></td>\n" +
					"					</tr>\n" +
					"				</tbody>\n" +
					"			</table>\n" +
					"		</div>\n" +
					"		<div>\n" +
					"			<form id=\"myForm\">\n" +
					"				<div class=\"alert alert-warning\" ng-show=\"mydata.showAlert\">\n" +
					"					<button type=\"button\" class=\"close\" data-ng-click=\"mydata.closeAlert()\" >×</button>\n" +
					"					<strong>{{mydata.alertMessage}}</strong>\n" +
					"				</div>\n" +
					"				<div class=\"form-group\">\n" +
					"					<label for=\"exampleInputBucket\">Bucket</label>\n" +
					"					<select class=\"form-control\" id=\"exampleInputBucket\" ng-model=\"mydata.selectedBucket\" ng-options=\"opt as opt for opt in mydata.buckets\">\n" +
					"						<option disabled selected></option>\n" +
					"					</select>\n" +
					"				</div>\n" +
					"				<div class=\"form-group\">\n" +
					"					<label for=\"exampleInputBucket\">Live</label>\n" +
					"					<select class=\"form-control\" id=\"exampleInputBucket\" ng-model=\"mydata.selectedStatus\" ng-options=\"opt as opt for opt in mydata.status\">\n" +
					"						<option disabled selected></option>\n" +
					"					</select>\n" +
					"				</div>\n" +
					"				<div class=\"form-group\">\n" +
					"					<label for=\"exampleInputPassword1\">Files</label>\n" +
					"					<input type=\"number\" ng-model=\"mydata.selectedFiles\" class=\"form-control\" id=\"exampleInputPassword1\" placeholder=\"Number 1 - 1,000,000\">\n" +
					"				</div>\n" +
					"				<div class=\"form-group\">\n" +
					"					<label for=\"exampleInputPassword1\">Batch</label>\n" +
					"					<input type=\"number\" ng-model=\"mydata.selectedBatch\" class=\"form-control\" id=\"exampleInputPassword1\" placeholder=\"Number 1 - 1,000\">\n" +
					"				</div>\n" +
					"				<div class=\"form-group\">\n" +
					"					<label for=\"exampleInputBucket\">RedShift</label> \n" +
					"					<select class=\"form-control\" id=\"exampleInputBucket\" ng-model=\"mydata.selectedRedshift\" ng-options=\"opt as opt for opt in mydata.redshift\">\n" +
					"						<option disabled selected></option>\n" +
					"					</select>\n" +
					"				</div>\n" +
					"				<button type=\"submit\" ng-click=\"mydata.update()\" class=\"btn btn-info\">Update</button>\n" +
					"				<button type=\"submit\" ng-click=\"mydata.clear()\" class=\"btn btn-warning\">Clear</button>\n" +
					"				<button type=\"submit\" ng-click=\"mydata.addNew()\" class=\"btn btn-success\">Add New</button>\n" +
					"				<button type=\"submit\" ng-click=\"mydata.delete()\" class=\"btn btn-danger\">Delete</button>\n" +
					"			</form>\n" +
					"		</div>\n" +
					"		<!-- <div id=\"piechart_3d\"></div>-->\n" +
					"		<div style=\"height: 60px\">\n" +
					"		</div>\n" +
					"	</div>\n" +
					"</body>\n" +
					"</html>";
}
