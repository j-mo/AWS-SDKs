/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

import com.example.cloudwatch.DeleteAlarm;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatchevents.CloudWatchEventsClient;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import com.example.cloudwatch.*;
import java.io.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudWatchTest {

    private static CloudWatchClient cw ;
    private static CloudWatchLogsClient cloudWatchLogsClient ;
    private static CloudWatchEventsClient cwe;
    private static String logGroup="";
    private static String alarmName="";
    private static String streamName ="";
    private static String metricId = "";
    private static String instanceId="";
    private static String ruleResource = "";
    private static String filterName="";
    private static String destinationArn="";
    private static String roleArn ="";
    private static String ruleArn ="";
    private static String namespace ="";
    private static String filterPattern = "";
    private static String ruleName = "";
    private static String myDateSc = "";
    private static String costDateWeekSc = "";
    private static String dashboardNameSc = "";
    private static String dashboardJsonSc = "";
    private static String dashboardAddSc = "";
    private static String settingsSc = "";
    private static String metricImageSc = "";


    @BeforeAll
    public static void setUp() throws IOException {

        Region region = Region.US_EAST_1;
        cw = CloudWatchClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        cloudWatchLogsClient = CloudWatchLogsClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        cwe = CloudWatchEventsClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        try (InputStream input = CloudWatchTest.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            // Populate the data members required for all tests
            logGroup = prop.getProperty("logGroup");
            alarmName = prop.getProperty("alarmName");
            streamName = prop.getProperty("streamName");
            ruleResource = prop.getProperty("ruleResource");
            metricId = prop.getProperty("metricId");
            filterName = prop.getProperty("filterName");
            destinationArn = prop.getProperty("destinationArn");
            roleArn= prop.getProperty("roleArn");
            filterPattern= prop.getProperty("filterPattern");
            instanceId= prop.getProperty("instanceId");
            ruleName= prop.getProperty("ruleName");
            ruleArn= prop.getProperty("ruleArn");
            namespace= prop.getProperty("namespace");
            myDateSc= prop.getProperty("myDateSc");
            costDateWeekSc= prop.getProperty("costDateWeekSc");
            dashboardNameSc= prop.getProperty("dashboardNameSc");
            dashboardJsonSc= prop.getProperty("dashboardJsonSc");
            dashboardAddSc= prop.getProperty("dashboardAddSc");
            settingsSc= prop.getProperty("settingsSc");
            metricImageSc= prop.getProperty("metricImageSc");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void whenInitializingAWSCWService_thenNotNull() {
        assertNotNull(cw);
        assertNotNull(cloudWatchLogsClient);
        System.out.printf("\n Test 1 passed");
    }

    @Test
    @Order(2)
    public void CreateAlarm() {
        PutMetricAlarm.putMetricAlarm(cw, alarmName,instanceId );
        System.out.printf("\n Test 2 passed");
    }

    @Test
    @Order(3)
    public void DescribeAlarms() {
       DescribeAlarms.desCWAlarms(cw);
       System.out.printf("\n Test 3 passed");
    }

    @Test
    @Order(4)
    public void CreateSubscriptionFilters() {
      PutSubscriptionFilter.putSubFilters(cloudWatchLogsClient, filterName, filterPattern, logGroup, destinationArn);
       System.out.printf("\n Test 4 passed");
    }

    @Test
    @Order(5)
    public void DescribeSubscriptionFilters() {
       DescribeSubscriptionFilters.describeFilters(cloudWatchLogsClient,logGroup);
       System.out.printf("\n Test 5 passed");
    }

    @Test
    @Order(6)
    public void DisableAlarmActions() {

      DisableAlarmActions.disableActions(cw, alarmName);
      System.out.println("\n Test 6 passed");
    }

    @Test
    @Order(7)
    public void EnableAlarmActions() {

      EnableAlarmActions.enableActions(cw, alarmName) ;
      System.out.println("\n Test 7 passed");
    }

    @Test
    @Order(8)
    public void GetLogEvents() {

        GetLogEvents.getCWLogEvents(cloudWatchLogsClient,logGroup,streamName);
        System.out.println("\n Test 8 passed");
    }

    @Test
    @Order(9)
    void PutCloudWatchEvent() {
       PutEvents.putCWEvents(cwe,ruleResource );
       System.out.println("\n Test 9 passed");
    }

    @Test
    @Order(10)
    public void GetMetricData() {

      GetMetricData.getMetData(cw);
      System.out.println("\n Test 10 passed");
    }

    @Test
    @Order(11)
    public void DeleteSubscriptionFilter() {

        DeleteSubscriptionFilter.deleteSubFilter(cloudWatchLogsClient, filterName,logGroup );
        System.out.println("\n Test 11 passed");
    }

    @Test
    @Order(12)
   public void PutRule() {
       PutRule.putCWRule(cwe, ruleName, ruleArn);
        System.out.println("\n Test 12 passed");
   }

    @Test
    @Order(13)
   public void ListMetrics() {
       ListMetrics.listMets(cw, namespace);
       System.out.println("\n Test 13 passed");
   }

    @Test
    @Order(14)
   public void PutLogEvents() {
       PutLogEvents.putCWLogEvents(cloudWatchLogsClient, logGroup, streamName);
        System.out.println("\n Test 14 passed");
   }

    @Test
    @Order(15)
    public void DeleteAlarm() {
      DeleteAlarm.deleteCWAlarm(cw, alarmName);
      System.out.println("\n Test 15 passed");
    }

    @Test
    @Order(16)
    public void TestScenario() throws IOException {
        Double dataPoint = Double.parseDouble("10.0");
        System.out.println(CloudWatchScenario.DASHES);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. List at least 5 available unique namespaces from Amazon CloudWatch. Select a CloudWatch Namespace from the list.");
        ArrayList<String> list = CloudWatchScenario.listNameSpaces(cw);
        for (int z=0; z<5; z++) {
            System.out.println("    " +(z+1) +". " +list.get(z));
        }
        String option = reader.readLine();
        String selectedNamespace = "";
        String selectedMetrics = "";
        switch (option) {
            case "1":
                selectedNamespace= list.get(0);
                break;
            case "2":
                selectedNamespace= list.get(1);
                break;
            case "3":
                selectedNamespace= list.get(2);
                break;
            case "4":
                selectedNamespace= list.get(3);
                break;
            case "5":
                selectedNamespace= list.get(4);
                break;
            default:
                System.out.println("You did not select a valid option.");
                System.exit(1);
        }
        System.out.println("You selected "+selectedNamespace);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("2. List available metrics within the selected Namespace and select one from the list.");
        ArrayList<String> metList = CloudWatchScenario.listMets(cw, selectedNamespace);
        for (int z=0; z<5; z++) {
            int index = z+1;
            System.out.println("    " +index +". " +metList.get(z));
        }
        String metOption = reader.readLine();
        switch (metOption) {
            case "1":
                selectedMetrics= metList.get(0);
                break;
            case "2":
                selectedMetrics= metList.get(1);
                break;
            case "3":
                selectedMetrics= metList.get(2);
                break;
            case "4":
                selectedMetrics= metList.get(3);
                break;
            case "5":
                selectedMetrics= metList.get(4);
                break;
            default:
                System.out.println("You did not select a valid option.");
                System.exit(1);
        }
        System.out.println("You selected "+selectedMetrics);
        Dimension myDimension = CloudWatchScenario.getSpecificMet( cw, selectedNamespace, selectedMetrics);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("3. Get statistics for the selected metric over the last day.");
        String metricOption="";
        ArrayList<String> statTypes = new ArrayList<>();
        statTypes.add("SampleCount");
        statTypes.add("Average");
        statTypes.add("Sum");
        statTypes.add("Minimum");
        statTypes.add("Maximum");

        for (int t=0; t<5; t++){
            System.out.println("    " +(t+1) +". "+statTypes.get(t));
        }
        System.out.println("Select a metric statistic by entering a number from the preceding list:");
        String myOption = reader.readLine();
        switch (myOption) {
            case "1":
                metricOption= statTypes.get(0);
                break;
            case "2":
                metricOption= statTypes.get(1);
                break;
            case "3":
                metricOption= statTypes.get(2);
                break;
            case "4":
                metricOption= statTypes.get(3);
                break;
            case "5":
                metricOption= statTypes.get(4);
                break;
            default:
                System.out.println("You did not select a valid option.");
                System.exit(1);
        }
        System.out.println("You selected "+metricOption);
        CloudWatchScenario.getAndDisplayMetricStatistics(cw, selectedNamespace, selectedMetrics, metricOption, myDateSc, myDimension);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("4. Get CloudWatch estimated billing for the last week.");
        CloudWatchScenario.getMetricStatistics(cw, costDateWeekSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("5. Create a new CloudWatch dashboard with metrics.");
        CloudWatchScenario.createDashboardWithMetrics(cw, dashboardNameSc, dashboardJsonSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("6. List dashboards using a Paginator.");
        CloudWatchScenario.listDashboards(cw);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("7. Create a new custom metric by adding data to it.");
        CloudWatchScenario.createNewCustomMetric(cw, dataPoint);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("8. Add additional metric to the dashboard.");
        CloudWatchScenario.addMetricToDashboard(cw, dashboardAddSc, dashboardNameSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("9. Create an alarm for the custom metric.");
        String alarmName = CloudWatchScenario.createAlarm(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("10. Describe 10 current alarms.");
        CloudWatchScenario.describeAlarms(cw);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("11. Get current data for new custom metric.");
        CloudWatchScenario.getCustomMetricData(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("12. Push data into the custom metric to trigger the alarm.");
        CloudWatchScenario.addMetricDataForAlarm(cw, settingsSc) ;
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("13. Check the alarm state using the action DescribeAlarmsForMetric.");
        CloudWatchScenario.checkForMetricAlarm(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("14. Get alarm history for the new alarm.");
        CloudWatchScenario.getAlarmHistory(cw, settingsSc, myDateSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("15. Add an anomaly detector for the custom metric.");
        CloudWatchScenario.addAnomalyDetector(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("16. Describe current anomaly detectors");
        CloudWatchScenario.describeAnomalyDetectors(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("17. Get a metric image for the custom metric.");
        CloudWatchScenario.getAndOpenMetricImage(cw, metricImageSc);
        System.out.println(CloudWatchScenario.DASHES);

        System.out.println(CloudWatchScenario.DASHES);
        System.out.println("18. Clean up the Amazon CloudWatch resources.");
        CloudWatchScenario.deleteDashboard(cw, dashboardNameSc);
        CloudWatchScenario.deleteCWAlarm(cw, alarmName);
        CloudWatchScenario.deleteAnomalyDetector(cw, settingsSc);
        System.out.println(CloudWatchScenario.DASHES);
    }
}





