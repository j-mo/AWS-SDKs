//snippet-sourcedescription:[DescribeVault.kt demonstrates how to describe an Amazon Glacier vault.]
//snippet-keyword:[AWS SDK for Kotlin]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon Glacier]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[11/04/2021]
//snippet-sourceauthor:[scmacdon-aws]
/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

package com.kotlin.glacier

// snippet-start:[glacier.kotlin.describe.import]
import aws.sdk.kotlin.services.glacier.GlacierClient
import aws.sdk.kotlin.services.glacier.model.DescribeVaultRequest
import aws.sdk.kotlin.services.glacier.model.GlacierException
import kotlin.system.exitProcess
// snippet-end:[glacier.kotlin.describe.import]

suspend fun main(args:Array<String>) {

    val usage = """
        Usage: 
            <vaultName>

        Where:
            vaultName - the name of the vault.
        """

    if (args.size != 1) {
        println(usage)
        exitProcess(1)
    }

    val vaultName = args[0]
    val glacierClient = GlacierClient { region = "us-east-1" }
    describeGlacierVault(glacierClient, vaultName)
    glacierClient.close()
}

// snippet-start:[glacier.kotlin.describe.main]
suspend fun describeGlacierVault(glacier: GlacierClient, vaultNameVal: String) {
    try {
        val describeVaultRequest = DescribeVaultRequest{
            vaultName = vaultNameVal
        }
        val desVaultResult = glacier.describeVault(describeVaultRequest)
        println("Describing the vault: $vaultNameVal")
        print(
            """
                CreationDate: ${desVaultResult.creationDate.toString()}
                LastInventoryDate: ${desVaultResult.lastInventoryDate.toString()}
                NumberOfArchives: ${desVaultResult.numberOfArchives.toString()}
                SizeInBytes: ${desVaultResult.sizeInBytes.toString()}
                VaultARN: ${desVaultResult.vaultArn.toString()}
                VaultName: ${desVaultResult.vaultName}
                """.trimIndent()
        )
    } catch (e: GlacierException) {
        println(e.message)
        glacier.close()
        exitProcess(0)
    }
}
// snippet-end:[glacier.kotlin.describe.main]