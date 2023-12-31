# Setup
To run the code, you need to setup your cluster, install and configure the following applications:
setup 3 virtual machines, make sure you configure the network and SSH access, so each VM can communicate with each other, install the following applications on each machine, make sure they are compatible with each other. 
   ![structure](https://github.com/Marcilele/portfolio/assets/68567431/552d2a8f-d8d2-42ff-8d43-49c5ef053ab4)

# Project Description
## Stream Processing Analysis on Flipkart Fashion Products
### Overview
Leveraging a comprehensive dataset of Flipkart fashion products from Kaggle, I conducted an in-depth stream processing analysis.
This project was designed to emulate a real-time data processing pipeline, converting raw data streams into actionable insights and engaging visualizations, with a focus on understanding product trends and user preferences within the online fashion retail space.
### Data Pipeline

![Stream](https://github.com/Marcilele/portfolio/assets/68567431/44974721-0ac9-4998-9e80-a1d7162e0ae9)



### Scope & Responsibilities

#### Data Ingestion & Cleanup

Imported the dataset, originally in JSON format, into MySQL. Conducted thorough data cleaning to ensure accuracy and consistency.
Real-time Data Streaming

Utilized Flume to write data efficiently to Kafka, setting up a seamless pipeline for real-time data ingestion.
Data Analysis with Spark Streaming

Employed Spark Streaming to consume the Kafka data feeds, performing real-time analysis to extract meaningful insights from the continuous flow of product data.

### Result Storage

Depending on the specific requirements of the analysis, results were either stored in Redis for rapid retrieval or persisted in MySQL for structured storage.

### Visualization & Application Deployment

Developed an interactive dashboard using Springboot, incorporating echarts for a dynamic display of the analyzed data, highlighting trends, and insights related to Flipkart's fashion products. 
![Flipkart E-commerce on Big Data Dashboard](https://github.com/Marcilele/portfolio/assets/68567431/eb109480-e0e4-44a2-a80c-06ab81204e21)



### Technologies Used

Databases & Storage: MySQL, Redis.
Streaming & Processing: Flume, Kafka, Spark Streaming.
Application & Visualization: Springboot, echarts.

#### Key Achievements
Processed over 30,000 product updates per five second.

# Batch Processing Data Pipeline (Game Recommendtions On Steam)

## Overview

Utilizing a rich dataset on Steam game recommendations from Kaggle, I conducted a comprehensive batch processing analysis. The project's objective was to simulate an end-to-end data pipeline, transforming raw data into actionable insights and visual representations, highlighting user game preferences, and potential trends within the gaming community.
Scope & Responsibilities

### Data Pipeline


![batch](https://github.com/Marcilele/portfolio/assets/68567431/8ad65c51-60a3-4792-a15b-17f803585911)


### Data Ingestion
Imported the dataset into HDFS, laying the groundwork for further processing and ensuring data integrity.

### Data Layering with Hive
Implemented a three-tier data structure

#### Operational Data Store (ODS)
Served as the raw data layer.

#### Data Warehouse (DW)
Included the dwd (detailed data layer), dim (dimensional data layer), and dws (summary data layer), transforming raw data into a structured, query-optimized format.
#### Application Data Store (ADS)
The final processed data tier, optimized for application use and visualization.

### Data Export
Leveraged Sqoop to migrate the analyzed results from HDFS to a MySQL database efficiently.

### Visualization & Application Development

Utilized FineBI for generating insightful visualizations. Additionally, built an interactive application with Springboot, integrating echarts for dynamic data representations.
![Comprehensive Analysis Steam Game Recommendations on Big Data Dashboard](https://github.com/Marcilele/portfolio/assets/68567431/9637b23c-8a94-4944-ab99-8915168688bf)


### Technologies Used

Big Data Storage & Processing:
HDFS, Hive, Mapreduce
Data Transfer:
Sqoop
Database:
MySQL
Visualization & App Development:
FineBI, Springboot, echarts.
Version Control:
Github

### Key Achievements
#### Growth Trajectory Insight

Identified a momentous growth in the gaming industry spanning 25 years. Beginning with a mere 2 games in 1997, the industry witnessed an impressive surge, with 7,270 games released in 2022.

#### Game Tag Analysis

Delved deep into game preferences, revealing "Indie", "Singleplayer", and "Action" as the top three most prevalent tags throughout the years. This underscores a consistent inclination towards independent games, solitary gaming experiences, and action-driven gameplay in the gaming community.
