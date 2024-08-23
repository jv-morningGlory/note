package cn.itcast.hotel;


import cn.itcast.hotel.constants.HotelConstants;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HotelIndexTest {


    public RestHighLevelClient client;


    @Test
    void testExistsHotelIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    @Test
    void testDeleteHotelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void testCreateHotelIndex() throws IOException {
        //1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        //2.准备请求的参数：DSL语句
        request.mapping(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
        //不推荐下面这种写法，如果有语法错误，在CreateIndexResponse中不能暴露出来
        //request.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
        //3.发送请求
        CreateIndexResponse createIndexResponse = this.client.indices().create(request, RequestOptions.DEFAULT);
        //4.处理响应结果
        System.out.println(createIndexResponse.isAcknowledged());
    }


    @Test
    void testInit() {
        System.out.println(client);
    }


    @BeforeEach
    void setUp() {
        RestClient httpClient  = RestClient.builder(
                HttpHost.create("http://117.72.16.114:9200")
        ).build();
        client = new RestHighLevelClientBuilder(httpClient)
                .setApiCompatibilityMode(true)
                .build();
    }

    @AfterEach
    void teardown() throws IOException {
        this.client.close();
    }

}
