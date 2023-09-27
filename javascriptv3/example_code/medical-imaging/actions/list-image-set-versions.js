/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

import {fileURLToPath} from "url";

// snippet-start:[medical-imaging.JavaScript.datastore.listImageSetVersionsV3]
import {ListImageSetVersionsCommand} from "@aws-sdk/client-medical-imaging";
import {medicalImagingClient} from "../libs/medicalImagingClient.js";

/**
 * @param {string} datastoreId - The ID of the data store.
 * @param {string} imageSetId - The ID of the image set.
 */
export const listImageSetVersions = async (datastoreId = "xxxxxxxxxxxx", imageSetId = "xxxxxxxxxxxx") => {
    const response = await medicalImagingClient.send(
        new ListImageSetVersionsCommand({datastoreId : datastoreId, imageSetId : imageSetId})
    );
    console.log(response);
    // {
    //     '$metadata': {
    //         httpStatusCode: 200,
    //         requestId: '74590b37-a002-4827-83f2-3c590279c742',
    //         extendedRequestId: undefined,
    //         cfId: undefined,
    //         attempts: 1,
    //         totalRetryDelay: 0
    //     },
    //     imageSetPropertiesList: [
    //         {
    //             ImageSetWorkflowStatus: 'CREATED',
    //             createdAt: 2023-09-22T14:49:26.427Z,
    //             imageSetId: 'xxxxxxxxxxxxxxxxxxxxxxx',
    //             imageSetState: 'ACTIVE',
    //             versionId: '1'
    //         }]
    // }
    return response;
};
// snippet-end:[medical-imaging.JavaScript.datastore.listImageSetVersionsV3]

// Invoke main function if this file was run directly.
if (process.argv[1] === fileURLToPath(import.meta.url)) {
    await listImageSetVersions("728f13a131f748bf8d87a55d5ef6c5af", "22b8ce38456a11bfb8e16ff6bf037dd0");
 }
