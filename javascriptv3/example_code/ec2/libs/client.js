/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// snippet-start:[ec2.JavaScript.createclientv3]
const { EC2Client } = require("@aws-sdk/client-ec2");
const REGION = "us-east-1";
export const client = new EC2Client({ region: REGION });
// snippet-end:[ec2.JavaScript.createclientv3]
