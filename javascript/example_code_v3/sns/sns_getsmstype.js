/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

This file is licensed under the Apache License, Version 2.0 (the "License").
You may not use this file except in compliance with the License. A copy of
the License is located at http://aws.amazon.com/apache2.0/

This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.

ABOUT THIS NODE.JS SAMPLE: This sample is part of the SDK for JavaScript Developer Guide topic
https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide//sns-examples-sending-sms.html

Purpose:
sns_getsmstype.js demonstrates how to retrieve the settings for sending Amazon SMS messages.

Inputs:
- REGION (in commmand line input below)
- ATTRIBUTE_NAME  (in commmand line input below)

Running the code:
node sns_getsmstype.js REGION ATTRIBUTE_NAME
*/
// snippet-start:[sns.JavaScript.SMS.getSMSAttributes]
async function run() {
  try {
    const {SNS, GetSMSAttributesCommand} = require("@aws-sdk/client-sns");
    const region = process.argv[2];
    const sns = new SNS(region);
    var params = {
      attributes: [
        'DefaultSMSType',
        proces.argv[3]
        /* more items */
      ]
    };
    const data = await sns.send(new GetSMSAttributesCommand(params));
    console.log(data);
  } catch (err) {
    console.error(err, err.stack);
  }
};
run();
// snippet-end:[sns.JavaScript.SMS.getSMSAttributes]
exports.run = run;
