# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX - License - Identifier: Apache - 2.0
require "aws-sdk-iam"
require "logger"

# snippet-start:[ruby.iam.ManageRoles]
# Manages IAM roles
class RoleManager
  # Initialize with an AWS IAM client
  #
  # @param iam_client [Aws::IAM::Client] An initialized IAM client
  def initialize(iam_client)
    @iam_client = iam_client
    @logger = Logger.new($stdout)
    @logger.progname = "RoleManager"
  end

  # snippet-start:[ruby.iam.ListRoles]
  # Lists IAM roles up to a specified count
  # @param count [Integer] the maximum number of roles to list
  # @return [Array<String>] the names of the roles
  def list_roles(count)
    role_names = []
    roles_counted = 0

    @iam_client.list_roles.each_page do |page|
      page.roles.each do |role|
        break if roles_counted >= count
        @logger.info("\t#{roles_counted + 1}: #{role.role_name}")
        role_names << role.role_name
        roles_counted += 1
      end
      break if roles_counted >= count
    end

    role_names
  rescue Aws::IAM::Errors::ServiceError => e
    @logger.error("Couldn't list roles for the account. Here's why:")
    @logger.error("\t#{e.code}: #{e.message}")
    raise
  end
  # snippet-end:[ruby.iam.ListRoles]

  # snippet-start:[ruby.iam.GetRole]
  # Gets data about a role.
  #
  # @param name [String] The name of the role to look up.
  # @return [Aws::IAM::Role] The retrieved role.
  def get_role(name)
    role = @iam_resource.role(name)
    puts("Got data for role '#{role.name}'. Its ARN is '#{role.arn}'.")
  rescue Aws::Errors::ServiceError => e
    puts("Couldn't get data for role '#{name}' Here's why:")
    puts("\t#{e.code}: #{e.message}")
    raise
  else
    role
  end
  # snippet-end:[ruby.iam.GetRole]

  # snippet-start:[ruby.iam.CreateRole]
  # Creates a role and attaches policies to it
  #
  # @param role_name [String] The name of the role
  # @param assume_role_policy_document [Hash] The trust relationship policy document
  # @param policy_arns [Array<String>] The ARNs of the policies to attach
  # @return [String, nil] The ARN of the new role if successful, or nil if an error occurred
  def create_role(role_name, assume_role_policy_document, policy_arns)
    response = @iam_client.create_role(
      role_name: role_name,
      assume_role_policy_document: assume_role_policy_document.to_json
    )
    role_arn = response.role.arn

    policy_arns.each do |policy_arn|
      @iam_client.attach_role_policy(
        role_name: role_name,
        policy_arn: policy_arn
      )
    end

    role_arn
  rescue Aws::IAM::Errors::ServiceError => e
    @logger.error("Error creating role: #{e.message}")
    nil
  end
  # snippet-end:[ruby.iam.CreateRole]

  # snippet-start:[ruby.iam.CreateServiceLinkedRole]
  # Creates a service-linked role.
  #
  # @param service_name [String] The name of the service that owns the role.
  # @param description [String] A description to give the role.
  # @return [Aws::IAM::Role] The newly created role.
  def create_service_linked_role(service_name, description)
    response = @iam_resource.client.create_service_linked_role(
      aws_service_name: service_name, description: description)
    role = @iam_resource.role(response.role.role_name)
    puts("Created service-linked role #{role.name}.")
  rescue Aws::Errors::ServiceError => e
    puts("Couldn't create service-linked role for #{service_name}. Here's why:")
    puts("\t#{e.code}: #{e.message}")
    raise
  else
    role
  end
  # snippet-end:[ruby.iam.CreateServiceLinkedRole]

  # snippet-start:[ruby.iam.DeleteRole]
  # Deletes a role and its attached policies
  #
  # @param role_name [String] The name of the role to delete.
  def delete_role(role_name)
    begin
      # Detach and delete attached policies
      @iam_client.list_attached_role_policies(role_name: role_name).each do |response|
        response.attached_policies.each do |policy|
          @iam_client.detach_role_policy({
                                    role_name: role_name,
                                    policy_arn: policy.policy_arn
                                  })
          @iam_client.delete_policy({ policy_arn: policy.policy_arn })
          @logger.info("Deleted policy #{policy.policy_name}.")
        end
      end

      # Delete the role
      @iam_client.delete_role({ role_name: role_name })
      @logger.info("Deleted role #{role_name}.")
    rescue Aws::IAM::Errors::ServiceError => e
      @logger.error("Couldn't detach policies and delete role #{role_name}. Here's why:")
      @logger.error("\t#{e.code}: #{e.message}")
      raise
    end
  end
  # snippet-end:[ruby.iam.DeleteRole]

  # snippet-start:[ruby.iam.DeleteServiceLinkedRole]
  # Deletes a service-linked role from the account.
  #
  # @param role [Aws::IAM::Role] The role to delete.
  def delete_service_linked_role(role)
    response = @iam_resource.client.delete_service_linked_role(role_name: role.name)
    task_id = response.deletion_task_id
    while true
      response = @iam_resource.client.get_service_linked_role_deletion_status(
        deletion_task_id: task_id)
      status = response.status
      puts("Deletion of #{role.name} #{status}.")
      if %w(SUCCEEDED FAILED).include?(status)
        break
      else
        sleep(3)
      end
    end
  rescue Aws::Errors::ServiceError => e
    # If AWS has not yet fully propagated the role, it deletes the role but
    # returns NoSuchEntity.
    if e.code != "NoSuchEntity"
      puts("Couldn't delete #{role.name}. Here's why:")
      puts("\t#{e.code}: #{e.message}")
      raise
    end
  end
  # snippet-end:[ruby.iam.DeleteServiceLinkedRole]
end
# snippet-end:[ruby.iam.ManageRoles]

# Example usage:
if __FILE__ == $PROGRAM_NAME
  iam_client = Aws::IAM::Client.new
  role_manager = RoleManager.new(iam_client)
  role_name = "my-ec2-s3-dynamodb-full-access-role"
  assume_role_policy_document = {
    Version: "2012-10-17",
    Statement: [
      {
        Effect: "Allow",
        Principal: { Service: "ec2.amazonaws.com" },
        Action: "sts:AssumeRole"
      }
    ]
  }
  policy_arns = %w[arn:aws:iam::aws:policy/AmazonS3FullAccess arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess]

  if (role_arn = role_manager.create_role(role_name, assume_role_policy_document, policy_arns))
    puts "Role created with ARN '#{role_arn}'."
  else
    puts "Could not create role."
  end

  puts "Attempting to delete role #{role_name}..."
  role_manager.delete_role(role_name)
end
