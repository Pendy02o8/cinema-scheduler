# Cinema Scheduler

電影院人力排班管理系統，用於整合員工、崗位需求、休假、可上班時段、週班表、缺員檢查及工時統計。

目前專案已內建編譯完成的前端頁面，啟動 Spring Boot 後即可直接使用完整系統。

## 主要功能

### 員工管理

- 新增、編輯及刪除員工
- 管理員工啟用狀態與顯示順序
- 支援員工類型：
  - 正職：`FULL_TIME`
  - 工讀生：`PART_TIME`
  - 清潔人員：`CLEANER`
- 支援固定班別：
  - 早班：`MORNING`
  - 晚班：`EVENING`
  - 無固定班別：`NONE`
- 設定員工是否需要指派崗位
- 設定員工是否使用每月休假管理

### 崗位與人力需求

- 新增、編輯及刪除工作崗位
- 設定崗位是否為必要崗位
- 設定各崗位的需求時段與需求人數
- 檢查指定日期或整週的缺員情況
- 檢查崗位超員情況
- 特殊「休」崗位用於表示員工排休，不列入工作時數

### 週班表管理

- 建立及管理每週班表
- 支援班表狀態：
  - 草稿：`DRAFT`
  - 已發布：`PUBLISHED`
- 依日期、員工及崗位安排班段
- 支援跨午夜班別
- 複製既有班段並貼到其他日期
- 自動產生固定班員工的班表：
  - 固定早班：08:50～17:30
  - 固定晚班：16:50～翌日 01:30
- 已發布班表再次修改時，記錄異動內容
- 可將班表匯出為 PNG 圖片

### 排班驗證

新增或修改班段時，系統會檢查：

- 同一員工的班段是否重疊
- 跨午夜班段是否與隔日班段衝突
- 當日排休與工作班段是否同時存在
- 工讀生班段是否符合可上班時段
- 正職或清潔人員當日是否已有休假
- 員工是否需要指定工作崗位
- 必要崗位是否缺員或超員

時間重疊、排休衝突等錯誤會阻止儲存；休假或可上班時段不符等情況則以警告顯示，可由排班人員確認是否仍要排班。

### 每月休假

- 管理需要月休追蹤的員工
- 支援休假類型：
  - 一般休假：`REGULAR_LEAVE`
  - 特休：`ANNUAL_LEAVE`
- 查詢指定日期區間的休假
- 查看指定月份的員工休假摘要
- 統計一般休假、特休及總休假天數
- 防止同一員工在同一天重複登記休假

### 工讀生可上班時段

- 手動新增、編輯及刪除可上班條件
- 支援以下類型：
  - 全天可上班：`ALL_DAY`
  - 指定時間前：`BEFORE`
  - 指定時間後：`AFTER`
  - 無法上班：`OFF`
- 可匯入 Google 表單匯出的 `.xlsx` 檔案

Excel 內容可使用：

| 內容 | 說明 |
| --- | --- |
| `整天可` | 當日全天可上班 |
| `1700前` | 17:00 前可上班 |
| `1700後` | 17:00 後可上班 |
| `休` | 當日無法上班 |

匯入時會讀取第一個工作表，並以員工姓名比對系統資料。匯入前會先刪除所選週班表原有的可上班資料，再寫入新內容。

### 工時統計

- 依自訂日期區間查詢
- 查詢單一員工或全部員工
- 自動處理跨午夜班別
- 「休」崗位不列入工時
- 單一班段滿 4 小時時扣除 30 分鐘休息時間
- 工時以 0.5 小時為單位無條件捨去

## 技術架構

### 後端

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- SQLite
- Hibernate Community Dialects
- Apache POI 5.2.5
- Springdoc OpenAPI 3.0.2
- Lombok
- Maven Wrapper

### 前端

目前儲存庫包含編譯完成的 React SPA 靜態檔案，並由 Spring Boot 提供服務。

主要技術包含：

- React
- Material UI
- React Router
- Axios
- dnd-kit
- html2canvas


## 專案結構

```text
Cinema-scheduler/
├─ src/
│  ├─ main/
│  │  ├─ java/com/pendy/cinema_scheduler/
│  │  │  ├─ config/          # SQLite 資料庫遷移
│  │  │  ├─ controller/      # REST API 與 SPA 路由
│  │  │  ├─ dto/             # API 請求及回應模型
│  │  │  ├─ entity/          # JPA 實體
│  │  │  ├─ repository/      # 資料存取層
│  │  │  └─ service/         # 商業邏輯與排班規則
│  │  └─ resources/
│  │     ├─ application.properties
│  │     ├─ data.sql         # 初始資料
│  │     └─ static/          # 編譯完成的前端
│  └─ test/                  # 後端測試
├─ mvnw
├─ mvnw.cmd
└─ pom.xml
```

## 執行需求

- Java 21


## 快速開始

### Windows PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

### macOS 或 Linux

```bash
./mvnw spring-boot:run
```

啟動完成後開啟：

```text
http://localhost:8080
```

Spring Boot 會同時提供前端頁面及 REST API。

## 資料庫

系統使用 SQLite，資料庫檔案預設建立在專案根目錄：

```
cinema-scheduler.db
```

相關設定位於：

```
src/main/resources/application.properties
```

系統啟動時會：

- 透過 Hibernate 更新資料表結構
- 執行 SQLite 相容性遷移
- 當主要資料表皆為空白時載入 `data.sql` 初始資料
- 確保系統具有特殊的「休」崗位

備份資料時，建議先停止應用程式，再複製 `cinema-scheduler.db`。刪除此檔案會遺失既有員工、休假及班表資料。

## API 文件

啟動系統後，可透過 Swagger UI 查看完整 API：

```text
http://localhost:8080/swagger-ui/index.html
```

主要 API 如下：

| API | 用途 |
| --- | --- |
| `/api/employees` | 員工管理與排序 |
| `/api/positions` | 崗位管理 |
| `/api/position-requirements` | 崗位人力需求 |
| `/api/weeklySchedule` | 週班表管理 |
| `/api/schedule-assignments` | 班段、排班驗證、缺員檢查與工時統計 |
| `/api/schedule-assignment-changes` | 已發布班表的異動紀錄 |
| `/api/availability` | 可上班時段與 Excel 匯入 |
| `/api/monthly-leaves` | 每月休假與統計 |
| `/api/businessHours` | 營業時間查詢 |

## 測試

### Windows

```powershell
.\mvnw.cmd test
```

### macOS 或 Linux

```bash
./mvnw test
```

目前測試涵蓋應用程式啟動、週班表服務及排班服務。

## 建置與執行

### Windows

```powershell
.\mvnw.cmd clean package

建置完成後執行：

```bash
java -jar target/cinema-scheduler-0.0.1-SNAPSHOT.jar
```

## 使用注意事項

- 刪除週班表時，相關班段、可上班資料、營業時間及異動紀錄會一併刪除。
- Excel 匯入會取代所選週班表原有的可上班資料。
- 已被班表使用的員工或崗位可能無法直接刪除。
