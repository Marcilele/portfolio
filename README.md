# portfolio

Real-time Data Processing Workflow (Flipkart):

    File Data: The process starts with raw data that is stored in files. Here I use json format file.

    Flume: Apache Flume is employed as a distributed service to efficiently collect, aggregate, and transport large amounts of log data. In this step, Flume sources the data from files and channels it towards Kafka.

    Kafka: Apache Kafka, a distributed streaming platform, receives the data from Flume. It acts as a buffer and message broker, ensuring that data is reliably transmitted to the next processing stage, which is SparkStreaming.

    SparkStreaming: Apache Spark's streaming module processes the data in real-time. It can handle high-velocity data and perform transformations, aggregations, or any other operations as required. 

    Redis: After processing, the data is then pushed to Redis, an in-memory data structure store. Redis ensures rapid data retrieval and acts as a cache, holding the processed data ready for visualization.

    Ajax for Real-time Data Visualization: With the data stored in Redis, asynchronous JavaScript (Ajax) requests are used to fetch the data in real-time. This ensures that the latest processed data is always available for visualization.

    Visualization Technologies (SpringBoot, JSP, Echarts):
        SpringBoot: A Java-based framework is used for building stand-alone, production-ready applications. In this context, it's likely handling the web server responsibilities and back-end functionalities.
        JSP (JavaServer Pages): Helps in creating dynamically generated web pages based on the processed data.
        Echarts: A comprehensive charting library that provides a wide array of visualization options, making it easier to represent the data in visually appealing and understandable formats."

Batch Processing Data Pipeline(Game Recommendtions On Steam):

    File Data Ingestion: The journey of the data commences from files which could encompass multiple files like CSVs

    Hive for Data Storage and Analysis: Apache Hive comes into play as the next stop in our workflow. Primarily designed for data query and analysis, Hive provides a mechanism for projecting structure onto large datasets and enables querying data using a SQL-like language called HiveQL. Here, data from files is ingested, stored, and managed.

    Sqoop for Data Transfer: With data being ready and structured in Hive, Apache Sqoop takes charge of efficiently transferring bulk data between Hive and relational databases, in this case, MySQL. Sqoop ensures that data migrations are quick and maintain the integrity of the data.

    MySQL for RDBMS Storage: Once the data lands into MySQL, it is stored in relational tables. MySQL, being a reliable and widely-used relational database management system, offers flexibility for further querying and organizing the data.

    Data Visualization Stack:
        SpringBoot: Serving as the backbone for the application layer, SpringBoot facilitates the creation of stand-alone applications, possibly acting as a web server and managing back-end processes in this context.
        JSP (JavaServer Pages): Here, JSP assists in dynamically crafting web pages based on the data present in MySQL, ensuring the information is presented in a user-friendly manner.
        Echarts: This potent charting library illuminates the data, offering a rich assortment of visualization options. Through Echarts, data is presented in a series of intuitive charts and graphs, ensuring users can derive insights effectively.
