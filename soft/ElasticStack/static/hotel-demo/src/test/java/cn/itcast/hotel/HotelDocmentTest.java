package cn.itcast.hotel;


import cn.itcast.hotel.constants.HotelConstants;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;


import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HotelDocmentTest {


    public RestHighLevelClient client;

    @Resource
    private IHotelService hotelService;

    @Test
    void testBulk() throws IOException {
        List<Hotel> list = hotelService.list();
        BulkRequest bulkRequest = new BulkRequest();
        for(Hotel hotel : list){
            HotelDoc hotelDoc = new HotelDoc(hotel);
            bulkRequest.add(
                    new IndexRequest("hotel")
                            .id(hotelDoc.getId().toString())
                            .source(JSON.toJSONString(hotelDoc), XContentType.JSON)
            );
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


    @Test
    void testDeleteDocById() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("hotel", "36934");
        client.delete(deleteRequest, RequestOptions.DEFAULT);

    }

    @Test
    void testUpdateDocById() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("hotel", "36934");
        updateRequest.doc("age", 18, "name", "Rose");
        client.update(updateRequest, RequestOptions.DEFAULT);
    }



    @Test
    void testGetDocumentById() throws IOException {
        //1.准备Request对象
        GetRequest request = new GetRequest("hotel", "36934");
        //2.发送请求，得到响应
        GetResponse response   = client.get(request, RequestOptions.DEFAULT);
        //3. 解析响应结果
        String json = response.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.out.println(hotelDoc);
    }


    @Test
    void testCreateDoc() throws IOException {
        Hotel hotel = hotelService.getById("36934");
        HotelDoc hotelDoc = new HotelDoc(hotel);
        //1.准备Request对象
        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        //2.准备JSON文档
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        //3.发送请求
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
    }


    @BeforeEach
    void setUp() {
        RestClient httpClient  = RestClient.builder(
                HttpHost.create("http://117.72.16.114:9200")
        ).build();
        client = new RestHighLevelClientBuilder(httpClient)
                //这个地方需要设置兼容 https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.12/migrate-hlrc.html
                .setApiCompatibilityMode(true)
                .build();
    }

    @AfterEach
    void teardown() throws IOException {
        this.client.close();
    }

}
