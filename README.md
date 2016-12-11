# lambda-return-html
Lambda function to return html

## API Gateway API Request and Response Payload-Mapping Template Reference

http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-mapping-template-reference.html


## API Gateway Configuration

#### Configure GET response

![alt text](https://github.com/dgallagher-ire/lambda-return-html/blob/master/docs/get-method-reponse.jpg "get response")

#### Configure Integration response

```
{
$input.path('$')
}
```

![alt text](https://github.com/dgallagher-ire/lambda-return-html/blob/master/docs/integration-reponse.jpg "integration response")

