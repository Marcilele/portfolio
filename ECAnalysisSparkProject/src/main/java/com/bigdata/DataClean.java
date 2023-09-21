package com.bigdata;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * 清洗数据：
 * 1.把actual_price里面的,去掉并改为int类型
 * 2.把average_rating数据改为浮点类型
 * 3.把crawled_at的日期格式改为yyyy-MM-dd HH:mm:ss
 * 4.把selling_price里面的,去掉并改为int类型
 *
 * 清洗后把数据封装成json格式的，并输出到一个json文件中
 */
public class DataClean {

    public static void main(String[] args) {

        // 数据库连接信息
        String url = "jdbc:mysql://node03:3306/gcandecdb?useSSL=FALSE&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "123456";

        // SQL 查询语句
        String query = "SELECT id,\n" +
                "\tCAST(REPLACE(actual_price, ',', '') AS UNSIGNED) AS actual_price,\n" +
                "\tCAST(average_rating AS DECIMAL(10,1)) AS average_rating,\n" +
                "\tbrand,\n" +
                "\tcategory,\n" +
                "\tDATE_FORMAT(STR_TO_DATE(crawled_at, '%d/%m/%Y, %H:%i:%s'), '%Y-%m-%d %H:%i:%s') AS crawled_at,\n" +
                "\tdescription,\n" +
                "\tdiscount,\n" +
                "\timages,\n" +
                "\tout_of_stock,\n" +
                "\tpid,\n" +
                "\tproduct_details,\n" +
                "\tseller,\n" +
                "\tCAST(REPLACE(selling_price, ',', '') AS UNSIGNED) AS selling_price,\n" +
                "\tsub_category,\n" +
                "\ttitle,\n" +
                "\turl FROM flipkart_fashion_products";

        // 输出文件路径
        String outputFile = "C:\\Users\\Lili\\Desktop\\portfolio\\output\\result.json";

        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        ) {
            // 遍历结果集
            while (rs.next()) {
                // 将行数据转为 JSONObject
                JSONObject json = new JSONObject();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    json.put(columnName, value);
                }

                // 将 JSONObject 转为 JSON 字符串
                String jsonString = json.toString();

                // 写入文件并添加换行符
                writer.write(jsonString);
                writer.newLine();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
