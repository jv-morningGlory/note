![alt text](./imgs/04-001.png)

- 在TCP三次握手后，先利用非对称加密得到工作密钥，然后会话都是用工作密钥来对称加密。
- 服务端和客户端都要生成一个随机数，是为了防止重放攻击：通过引入随机数，确保每次会话的密钥生成都是唯一的，攻击者无法重用之前的会话数据来进行攻击。