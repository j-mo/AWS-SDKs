<?php
/**
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 *  ABOUT THIS PHP SAMPLE: This sample is part of the SDK for PHP Developer Guide topic at
 * https://docs.aws.amazon.com/sdk-for-php/v3/developer-guide/cw-examples-work-with-alarms.html
 *
 */
require 'vendor/autoload.php';

use Aws\CloudWatch\CloudWatchClient;
use Aws\Exception\AwsException;

/**
 * Describe Alarms in CloudWatch
 *
 * This code expects that you have AWS credentials set up per:
 * https://docs.aws.amazon.com/sdk-for-php/v3/developer-guide/guide_credentials.html
 */

$client = new CloudWatchClient([
    'profile' => 'default',
    'region' => 'us-west-2',
    'version' => '2010-08-01'
]);

try {
    $result = $client->describeAlarms([
    ]);
    foreach ($result['MetricAlarms'] as $alarm) {
        echo $alarm['AlarmName'] . "\n";
    }
} catch (AwsException $e) {
    // output error message if fails
    error_log($e->getMessage());
}
 

//snippet-sourcedescription:[DescribeAlarms.php demonstrates how to list all cloudwatch alarm names.]
//snippet-keyword:[PHP]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon Cloudwatch]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[]
//snippet-sourceauthor:[AWS]

