# 專案: Microservice服務演練及佈署

## 簡介
本專案包含 `licens-server`、`organization-server`、`eureka-server`、`config-server`、`gateway-server` 等微服務，基於 Netflix Eureka 框架並搭配其他工具建構微服務架構。在測試階段使用 Docker 進行基本測試，並逐步改用 Kubernetes 進行佈署測試，最終在 AWS 的 EC2 與 EKS 上進行完整佈署。

## 使用工具
- **Kafka**：訊息隊列，用於事件驅動架構
- **Keycloak**：身份驗證與授權管理
- **ELK Stack**：包含 Elasticsearch、Logstash、Kibana，用於日誌收集與分析
- **Redis**：快取服務

## 範例概要

### EC2 使用服務
![EC2 使用服務](https://github.com/user-attachments/assets/42f4fcee-85f6-4f91-afe5-a4e243b0710f)

### EKS 服務
- `licens-server`
- `organization-server`
- `eureka-server`
- `config-server`
- `gateway-server`

## 操作步驟

### Step 1: 取得 Token

首先，通過身份驗證系統取得 Token，並使用該 Token 進行後續的 API 呼叫。

### Step 2: 操作 Organization Server 和 License Server

1. **Organization Server 新增資料**
   - API 呼叫及範例回應：
   ![Organization Server 新增資料](https://github.com/user-attachments/assets/9eec4841-3a20-4ddf-a0e8-520f9a2f077b)
   ![Organization Server 新增資料](https://github.com/user-attachments/assets/5fdd7f08-1ee3-43d0-b821-fe168b700797)

2. **License Server 新增資料**
   - API 呼叫及範例回應：
   ![License Server 新增資料](https://github.com/user-attachments/assets/a5bb8650-f9d4-4242-a674-bc8b860e130f)

### Step 3: 查詢 API 呼叫及影響的服務

使用多個工具查看服務之間的互動狀況，並追蹤各微服務之間的呼叫記錄：

- **Zipkin 查詢結果**：顯示微服務間的調用關係
  ![Zipkin 查詢結果](https://github.com/user-attachments/assets/568c0b31-1b62-4342-9872-d0505c05c4c8)

- **Kafka 查詢紀錄**：顯示訊息傳遞記錄
  ![Kafka 查詢紀錄](https://github.com/user-attachments/assets/225dac98-5269-4670-8aed-23c689c050e6)

- **Kibana 查詢 Log**：顯示日誌查詢結果
  ![Kibana 查詢 Log](https://github.com/user-attachments/assets/fd713e37-2f6b-49da-b1d8-c84517d88bb8)

---
