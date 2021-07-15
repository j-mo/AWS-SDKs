﻿// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX - License - Identifier: Apache - 2.0

namespace CrossRegionReplicationExample
{
    using System;
    using System.Threading.Tasks;
    using Amazon;
    using Amazon.S3;
    using Amazon.S3.Model;

    /// <summary>
    /// This example shows how to configure replication for the objects in an
    /// Amazon Simple Storage Service (Amazon S3) bucket across AWS regions.
    /// It was created using AWS SDK for .NET version 3.7 and .NET Core 5.0.
    /// </summary>
    public class CrossRegionReplication
    {
        public static async Task Main()
        {
            string sourceBucket = "doc-example-bucket1";

            // The Amazon Resource Name (ARN) of the Amazon S3 destination
            // bucket. The included value is only an example. Be sure to change
            // this to the ARN of your destination bucket.
            //
            // The role ARN should be the ARN of a role that can read and
            // write to an S3 bucket.
            //
            // The examples below are not valid ARNs and must be replaced with
            // valid ARNS from your account.
            string destinationBucketArn = "arn:aws:s3:::doc-example-bucket2";
            string roleArn = "arn:aws:iam::704925961347:role/s3-replication-example";

            // Specify the AWS Region for your source bucket.
            RegionEndpoint sourceBucketRegion = RegionEndpoint.USWest2;
            IAmazonS3 s3Client = new AmazonS3Client(sourceBucketRegion);

            await EnableReplicationAsync(s3Client, sourceBucket, destinationBucketArn, roleArn);

            // Now verify that the configuration was set by retrieving it.
            await RetrieveReplicationConfigurationAsync(s3Client, sourceBucket);
        }

        /// <summary>
        /// This method adds replication rules to the S3 source bucket.
        /// </summary>
        /// <param name="client">The initialized S3 client object used to
        /// create and add the replication configuration to the source bucket.</param>
        /// <param name="sourceBucket">A string representing the name of the
        /// source S3 bucket.</param>
        /// <param name="destinationBucketArn">The ARN of the destination
        /// S3 bucket.</param>
        /// <param name="roleArn">The ARN of the role which will be used to
        /// replicate the contents of the source S3 bucket.</param>
        public static async Task EnableReplicationAsync(IAmazonS3 client, string sourceBucket, string destinationBucketArn, string roleArn)
        {
            try
            {
                ReplicationConfiguration replConfig = new ()
                {
                    Role = roleArn,
                    Rules =
                    {
                        new ReplicationRule
                        {
                            Filter = new ReplicationRuleFilter { Prefix = "Tax" },
                            Status = ReplicationRuleStatus.Enabled,
                            Destination = new ReplicationDestination
                            {
                                BucketArn = destinationBucketArn,
                            },
                        },
                    },
                };

                PutBucketReplicationRequest putRequest = new ()
                {
                    BucketName = sourceBucket,
                    Configuration = replConfig,
                };

                PutBucketReplicationResponse putResponse = await client.PutBucketReplicationAsync(putRequest);
            }
            catch (AmazonS3Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
        }

        /// <summary>
        /// This method retrieves the replication configuration for the S3
        /// source bucket and displays the information in the console.
        /// </summary>
        /// <param name="client">The initialized S3 client object used to call
        /// GetBucketReplicationAsync.</param>
        /// <param name="sourceBucket">A string representing the name of the
        /// source bucket.</param>
        private static async Task RetrieveReplicationConfigurationAsync(IAmazonS3 client, string sourceBucket)
        {
            // Retrieve the configuration for the source bucket.
            GetBucketReplicationRequest getRequest = new ()
            {
                BucketName = sourceBucket,
            };

            GetBucketReplicationResponse getResponse = await client.GetBucketReplicationAsync(getRequest);

            // Display the details of the configuration on the console.
            Console.WriteLine("Printing replication configuration information...");
            Console.WriteLine($"Role ARN: {getResponse.Configuration.Role}");
            foreach (var rule in getResponse.Configuration.Rules)
            {
                Console.WriteLine($"ID: {rule.Id}");
                Console.WriteLine($"Prefix: {rule.Filter}");
                Console.WriteLine($"Status: {rule.Status}");
            }
        }
    }
}
