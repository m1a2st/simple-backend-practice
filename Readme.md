## 🗻 做一個會員, 錢包的 API Service
* 👤 會員
    * C -> 註冊, 登入
    * R -> 查詢會員資料
    * U -> 更新資料
    * D -> 停用, 凍結
* 💰 錢包
    * C -> 開戶
    * R -> 查詢餘額, 交易紀錄
    * U -> 存款, 提款, 轉帳
    * D -> 停用, 凍結

## 括號內以 Java Spring 為例，其他語言或框架可自行代換
| 是否有使用 | 能力 | 範例技術 |
|:------| :--- | :--- |
| Y     | 📝 撰寫 RESTFul API 文件 | [springdoc-openapi](https://springdoc.org/) |
| Y     | ✍️ 設計 RESTFul API 端點             | [Spring Web MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html) |
| Y     | 🔑 管理 RESTFul API 權限             | [Spring Security](https://spring.io/projects/spring-security) |
| Y     | ⚡️ 為 API Endpoint 加 Cache        | [Spring Cache](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache) |
| Y     | 🧬 連接 Relational DB 資料庫          | [Spring Data JPA](https://spring.io/projects/spring-data-jpa) |
| Y     | 🕹️ 操作 SQL 語法                    | [Spring Data JPA](https://spring.io/projects/spring-data-jpa) |
| Y     | 💫 正確使用 Transaction              | [Spring Data JPA](https://spring.io/projects/spring-data-jpa) |
| Y     | 🔒 正確使用 Lock                     | [Spring Data JPA](https://spring.io/projects/spring-data-jpa) |
| Y     | 📖 查詢資料分頁                        | [Spring Data](https://spring.io/projects/spring-data) |
| Y     | 🔗 連接 NoSQL 資料庫                  | [Spring Data](https://spring.io/projects/spring-data) |
|       | 🗣️ 調用 RESTFul API Client 呼叫其他服務 | [Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-client-access) |
|       | ⏱️ 排程定期執行任務 | [Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling) |
|       | 🧰 整合測試    | [Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html) |
|       | 🧱 部署系統    | [jib](https://github.com/GoogleContainerTools/jib), [Docker Compose](https://docs.docker.com/compose/) |
|       | 📃 輸出系統日誌  | [Loki in Grafana Stack](https://grafana.com/products/enterprise/) |
|       | 👁️ 觀測系統狀態 | [Spring Actuator](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator), [Grafana Stack](https://grafana.com/products/enterprise/) |
|       | ♻️ 持續集成/佈署 | [GitHub Action](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven) |

## 目前類別圖
![使用者跟錢包的類別圖.png](%E4%BD%BF%E7%94%A8%E8%80%85%E8%B7%9F%E9%8C%A2%E5%8C%85%E7%9A%84%E9%A1%9E%E5%88%A5%E5%9C%96.png)