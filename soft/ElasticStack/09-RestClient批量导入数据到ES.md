# 思路

1. 利用mybatis-plus查询酒店数据
2. 将查询到的酒店数据（Hotel）转换为文档类型数据（HotelDoc）
3. 利用RestClient中的Bulk批处理，实现批量新增文档，示例代码如下

```java

@Test
void testBulk() throws IOException {
    List<Hotel> list = hotelService.list();
    BulkRequest bulkRequest = new BulkRequest();
    for (Hotel hotel : list) {
        HotelDoc hotelDoc = new HotelDoc(hotel);
        bulkRequest.add(
                new IndexRequest("hotel")
                        .id(hotelDoc.getId().toString())
                        .source(JSON.toJSONString(hotelDoc), XContentType.JSON)
        );
    }
    client.bulk(bulkRequest, RequestOptions.DEFAULT);
}

```