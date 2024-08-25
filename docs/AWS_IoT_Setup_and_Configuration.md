# AWS IoT Setup and Configuration

AWS IoT is a cloud platform that enables devices to connect to the AWS cloud and interact with other devices or cloud application.<br>
[https://aws.amazon.com/iot-core/](https://aws.amazon.com/iot-core/)

This page is based on [Building an AWS IoT Core device using AWS Serverless and an ESP32](https://aws.amazon.com/blogs/compute/building-an-aws-iot-core-device-using-aws-serverless-and-an-esp32/).

## Creating an AWS IoT Device

1. In the [AWS IoT console](https://aws.amazon.com/iot-core/), choose Register a new thing in `Manage/All devices/Things`, Create a single thing.
2. Name of te new thing in **PLC**. Leave the remaining fields set to their defaults. Choose Next.
3. Choose `Auto-generate a new certificate (recommended)`.
4. Create a new policy, named **PLCPolicy**. In the policy document select JSON and paste the content below:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "*",
      "Resource": "*"
    }
  ]
}
```
5. Select the Policy created and choose Create thing.
6. Download `Device Cretificate`, `Public key file`, `Private key file`, and `Root CA certificate (Amazon Root CA1)`.
7. Choose Done. Copy your Endpoint address in the `Settings` page, you will need it in the next step.

## Configure the Ladder project

Open the `IoT Lamp` project example and change the `Project/Properties`.
1. Enter the Wifi `SSID` and `password`.
2. In the `Broker address` paste your Endpoint address from the AWS Iot account.
3. Set the `Broker port` to 8883.
4. The client ID must be the same thing name created on the AWS IoT, `PLC`.
5. On the sub-tab `User`, leave the fields Username and Password blanked.
6. On the sub-tab `SSL`, check `Enable SSH` and `Use Client Certificate`, opend the files `CA`, `Client Certificate`, and `Private Key`.
7. Save and Build the project.