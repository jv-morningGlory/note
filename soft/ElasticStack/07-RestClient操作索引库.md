# 1 连接和销毁
```java
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
                //这个地方需要设置兼容 https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.12/migrate-hlrc.html
                .setApiCompatibilityMode(true)
                .build();
    }

    @AfterEach
    void teardown() throws IOException {
        this.client.close();
    }

```
.setApiCompatibilityMode(true) 这个很重要 因为我们


# 2 创建索引库
代码如下
```java
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


```
效果如下：
![img.png](img/07-001.png)

# 2 删除和判断是否存在
```java

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



```