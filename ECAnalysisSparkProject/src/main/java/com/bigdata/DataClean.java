package com.bigdata;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Data Cleaning:
 *
 *         Remove the comma in actual_price and change it to int type.
 *         Change average_rating data to float type.
 *         Change the date format of crawled_at to yyyy-MM-dd HH:mm:ss.
 *         Remove the comma in selling_price and change it to int type.
 *     After cleaning, encapsulate the data into a JSON format and output it to a JSON file
 */
public class DataClean {

    public static void main(String[] args) {

        // Database Connection
        String url = "jdbc:mysql://node03:3306/gcandecdb?useSSL=FALSE&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "123456";

        // SQL Query
        String query = "SELECT id,\n" +
                "\tCAST(REPLACE(actual_price, ',', '') AS UNSIGNED) AS actual_price,\n" +
                "\tCAST(average_rating AS DECIMAL(10,1)) AS average_rating,\n" +
                "\tbrand,\n" +
                "\tcategory,\n" +
                "\tDATE_FORMAT(STR_TO_DATE(crawled_at, '%d/%m/%Y, %H:%i:%s'), '%Y-%m-%d %H:%i:%s') AS crawled_at,\n" +
                "\tdiscount,\n" +
                "\tdescription,\n" +
                "\timages,\n" +
                "\tout_of_stock,\n" +
                "\tpid,\n" +
                "\tproduct_details,\n" +
                "\tseller,\n" +
                "\tCAST(REPLACE(selling_price, ',', '') AS UNSIGNED) AS selling_price,\n" +
                "\tsub_category,\n" +
                "\ttitle,\n" +
                "\turl FROM flipkart_fashion_products";

        // output file path
        String outputFile = "C:\\Users\\Lili\\Desktop\\portfolio\\projects\\output\\flipkart_fashion_products.json";

        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        ) {
            // iterate
            while (rs.next()) {
                // turn each line of data into a JSONObject
                JSONObject json = new JSONObject();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    json.put(columnName, value);
                }

                // convert JSONObject to JSON string
                String jsonString = json.toString();

                // write to file
                writer.write(jsonString);
                writer.newLine();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
