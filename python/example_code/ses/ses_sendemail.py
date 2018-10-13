# Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License"). You
# may not use this file except in compliance with the License. A copy of
# the License is located at
#
# http://aws.amazon.com/apache2.0/
#
# or in the "license" file accompanying this file. This file is
# distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
# ANY KIND, either express or implied. See the License for the specific
# language governing permissions and limitations under the License.

import boto3

# Create SES client
ses = boto3.client('ses')

response = ses.send_email(
  Source          = 'SENDER_EMAIL_ADDRESS',
  Destination     = {
    'CcAddresses' : [
      'EMAIL_ADDRESS',
    ],
    'ToAddresses' : [
      'EMAIL_ADDRESS',
    ],
    'BccAddresses': [
      'EMAIL_ADDRESS',
    ]
  },

  Message = {
    'Subject'    : {
      'Data'     : 'TEST_EMAIL',
      'Charset'  : 'UTF-8'
    },
    'Body'       : {
      'Text'     : {
        'Data'   : 'TEXT_FORMAT_BODY',
        'Charset': 'UTF-8'
      },
      'Html'     : {
        'Data'   : 'HTML_FORMAT_BODY',
        'Charset': 'UTF-8'
      }
    }
  },

  ReplyToAddresses = [
    'EMAIL_ADDRESS',
  ],
)

print(response)
 

#snippet-sourcedescription:[<<FILENAME>> demonstrates how to ...]
#snippet-keyword:[Python]
#snippet-keyword:[Code Sample]
#snippet-service:[Amazon Simple Email Service]
#snippet-sourcetype:[full-example]
#snippet-sourcedate:[]
#snippet-sourceauthor:[AWS]

