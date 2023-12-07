# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

"""
Stub functions that are used by the Agents for Amazon Bedrock unit tests.
"""

from test_tools.example_stubber import ExampleStubber


class BedrockAgentStubber(ExampleStubber):
    """
    A class that implements stub functions used by Amazon Bedrock Agent unit tests.
    """

    def __init__(self, client, use_stubs=True):
        """
        Initializes the object with a specific client and configures it for
        stubbing or AWS passthrough.

        :param client: A Boto3 Agents for Amazon Bedrock client.
        :param use_stubs: When True, uses stubs to intercept requests. Otherwise,
                          passes requests through to AWS.
        """
        super().__init__(client, use_stubs)

    def stub_list_agents(self, agents, error_code=None):
        expected_params = {}
        response = {"agentSummaries": agents}
        self._stub_bifurcator(
            "list_agents", expected_params, response, error_code=error_code
        )

    def stub_get_agent(self, agent_id, agent, error_code=None):
        expected_params = {"agentId": agent_id}
        response = {"agent": agent}
        self._stub_bifurcator(
            "get_agent", expected_params, response, error_code=error_code
        )

    def stub_create_agent(self, name, foundation_model, role_arn, instruction, error_code=None):
        expected_params = {
            "agentName": name,
            "foundationModel": foundation_model,
            "agentResourceRoleArn": role_arn,
            "instruction": instruction
        }
        response = {
            "agent": {
                "agentId": "ARANDOMAGENTID",
                "agentName": "fake_agent_name",
                "agentArn": "xxx",
                "foundationModel": "fake.model-id",
                "instruction": "fake instruction with a minimum of 40 characters",
                "agentVersion": "1.234.5",
                "agentStatus": "xxx",
                "idleSessionTTLInSeconds": 60,
                "agentResourceRoleArn": "xxx",
                "createdAt": "1970-01-01T00:00:00Z",
                "updatedAt": "1970-01-01T00:00:00Z"
            }
        }
        self._stub_bifurcator(
            "create_agent", expected_params, response, error_code=error_code
        )
