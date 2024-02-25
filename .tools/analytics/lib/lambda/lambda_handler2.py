import os
import boto3
import urllib3
from datetime import datetime
import json

# Environment variables
GITHUB_TOKEN = os.environ['GITHUB_TOKEN']  # GitHub token to authenticate API requests
TABLE_NAME = os.environ['TABLE_NAME']  # DynamoDB table name
REPO_OWNER = 'ford-at-aws'  # GitHub repository owner
REPO_NAME = 'aws-doc-sdk-examples'  # GitHub repository name

# Initialize DynamoDB
dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table(TABLE_NAME)

http = urllib3.PoolManager()

def fetch_page_views():
    """Fetch page view stats from GitHub using urllib3."""
    url = f'https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/traffic/views'
    headers = {'Authorization': f'Bearer {GITHUB_TOKEN}', "X-GitHub-Api-Version": '2022-11-28', "Accept": "application/vnd.github+json"}
    response = http.request('GET', url, headers=headers)
    breakpoint()
    if response.status == 200:
        return json.loads(response.data.decode('utf-8'))
    else:
        print(f'Failed to fetch data: {response.status}')
        return None

def store_data_in_dynamodb(data):
    """Store page view data into DynamoDB."""
    try:
        with table.batch_writer() as batch:
            for view in data['views']:
                timestamp = datetime.strptime(view['timestamp'], '%Y-%m-%dT%H:%M:%SZ')
                item = {
                    'Repo': f'{REPO_OWNER}/{REPO_NAME}',
                    'Timestamp': timestamp.isoformat(),
                    'Count': view['count'],
                    'Uniques': view['uniques']
                }
                batch.put_item(Item=item)
        print('Data stored successfully in DynamoDB')
    except Exception as e:
        print(f'Error storing data in DynamoDB: {e}')

def lambda_handler(event, context):
    """Lambda function entry point."""
    data = fetch_page_views()
    if data:
        store_data_in_dynamodb(data)
    else:
        print('No data fetched from GitHub API.')

    return {
        'statusCode': 200,
        'body': 'Successfully processed GitHub page views.'
    }


if __name__ == '__main__':
    lambda_handler(None, None)

