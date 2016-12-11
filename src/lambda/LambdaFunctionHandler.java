package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	@Override
	public String handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);
		return HTML;
	}

	private static final String HTML = 
			"<!DOCTYPE html>\n" +
			"<html>\n" +
			"<head>\n" +
			"<meta charset=\"utf-8\">\n" +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
			"<title>Insert title here</title>\n" +
			"<script\n" +
			"	src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script>\n" +
			"<link rel=\"stylesheet\"\n" +
			"	href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\"\n" +
			"	integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\"\n" +
			"	crossorigin=\"anonymous\">\n" +
			"<script\n" +
			"	src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"\n" +
			"	integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\"\n" +
			"	crossorigin=\"anonymous\"></script>\n" +
			"</head>\n" +
			"<body>\n" +
			"	<div>\n" +
			"		<table class=\"table table-hover table-condensed\">\n" +
			"			<thead>\n" +
			"				<tr class=\"info\">\n" +
			"					<th>#</th>\n" +
			"					<th>First Name</th>\n" +
			"					<th>Last Name</th>\n" +
			"					<th>Username</th>\n" +
			"					<th>Action</th>\n" +
			"				</tr>\n" +
			"			</thead>\n" +
			"			<tbody>\n" +
			"				<tr>\n" +
			"					<th scope=\"row\">1</th>\n" +
			"					<td>Mark</td>\n" +
			"					<td>Otto</td>\n" +
			"					<td>@mdo</td>\n" +
			"					<td><button type=\"button\" class=\"btn btn-info btn-xs\">Info</button></td>\n" +
			"				</tr>\n" +
			"				<tr>\n" +
			"					<th scope=\"row\">2</th>\n" +
			"					<td>Jacob</td>\n" +
			"					<td>Thornton</td>\n" +
			"					<td>@fat</td>\n" +
			"					<td><button type=\"button\" class=\"btn btn-info btn-xs\">Info</button></td>\n" +
			"				</tr>\n" +
			"				<tr>\n" +
			"					<th scope=\"row\">3</th>\n" +
			"					<td>Larry</td>\n" +
			"					<td>the Bird</td>\n" +
			"					<td>@twitter</td>\n" +
			"					<td><button type=\"button\" class=\"btn btn-info btn-xs\">Info</button></td>\n" +
			"				</tr>\n" +
			"			</tbody>\n" +
			"		</table>\n" +
			"	</div>\n" +
			"	<div>\n" +
			"\n" +
			"		<!-- Nav tabs -->\n" +
			"		<ul class=\"nav nav-tabs\" role=\"tablist\">\n" +
			"			<li role=\"presentation\" class=\"active\"><a href=\"#home\"\n" +
			"				aria-controls=\"home\" role=\"tab\" data-toggle=\"tab\">Home</a></li>\n" +
			"			<li role=\"presentation\"><a href=\"#profile\"\n" +
			"				aria-controls=\"profile\" role=\"tab\" data-toggle=\"tab\">Profile</a></li>\n" +
			"			<li role=\"presentation\"><a href=\"#messages\"\n" +
			"				aria-controls=\"messages\" role=\"tab\" data-toggle=\"tab\">Messages</a></li>\n" +
			"			<li role=\"presentation\"><a href=\"#settings\"\n" +
			"				aria-controls=\"settings\" role=\"tab\" data-toggle=\"tab\">Settings</a></li>\n" +
			"		</ul>\n" +
			"\n" +
			"		<!-- Tab panes -->\n" +
			"		<div class=\"tab-content\">\n" +
			"			<div role=\"tabpanel\" class=\"tab-pane active\" id=\"home\">...</div>\n" +
			"			<div role=\"tabpanel\" class=\"tab-pane\" id=\"profile\">...</div>\n" +
			"			<div role=\"tabpanel\" class=\"tab-pane\" id=\"messages\">...</div>\n" +
			"			<div role=\"tabpanel\" class=\"tab-pane\" id=\"settings\">...</div>\n" +
			"		</div>\n" +
			"\n" +
			"	</div>\n" +
			"</body>\n" +
			"</html>";
}
