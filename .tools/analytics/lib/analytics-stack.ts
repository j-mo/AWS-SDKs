import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb';
import * as events from 'aws-cdk-lib/aws-events';
import * as targets from 'aws-cdk-lib/aws-events-targets';

export class AnalyticsStack extends Stack {
  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    // DynamoDB table
    const table1 = new dynamodb.Table(this, 'GitHubPageViews', {
      partitionKey: { name: 'Repo', type: dynamodb.AttributeType.STRING },
      sortKey: { name: 'Timestamp', type: dynamodb.AttributeType.STRING },
      billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
    });

    // Lambda function
    const lambdaFunction1 = new lambda.Function(this, 'GitHubPageViewsFunction', {
      runtime: lambda.Runtime.PYTHON_3_8,
      handler: 'lambda_handler1.lambda_handler',
      code: lambda.Code.fromAsset('lib/lambda'),
      environment: {
        TABLE_NAME: table1.tableName,
      },
    });

    // DynamoDB table
    const table2 = new dynamodb.Table(this, 'GitHubTopReferrers', {
      partitionKey: { name: 'Repo', type: dynamodb.AttributeType.STRING },
      sortKey: { name: 'Timestamp', type: dynamodb.AttributeType.STRING },
      billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
    });

    // Lambda function
    const lambdaFunction2 = new lambda.Function(this, 'GitHubTopReferrersFunction', {
      runtime: lambda.Runtime.PYTHON_3_8,
      handler: 'lambda_handler2.lambda_handler',
      code: lambda.Code.fromAsset('lib/lambda'),
      environment: {
        TABLE_NAME: table2.tableName,
      },
    });

    // Grant the Lambda function read/write permissions to the DynamoDB table
    table1.grantReadWriteData(lambdaFunction1);
    table2.grantReadWriteData(lambdaFunction2);


    // Create a CloudWatch Event rule that triggers at 12:01 AM every day
    const rule = new events.Rule(this, 'Rule', {
      schedule: events.Schedule.cron({ minute: '1', hour: '0' }), // 12:01 AM UTC
    });

    // Add the Lambda function as the target of the rule
    rule.addTarget(new targets.LambdaFunction(lambdaFunction1));
    rule.addTarget(new targets.LambdaFunction(lambdaFunction2));
  }
}
