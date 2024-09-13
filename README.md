# Keywordream Rest API Server
> Keywordream은 주식이 왜 오르고 내리는지 알고 싶어하는 주린이들에게 각 종목에 대한 `정보`와 `시세 변화에 대한 이해`를 제공한다.

## 🔗 Link
> https://github.com/Keywordream-PDA
### 리팩토링 전
> [Rest API Server](https://github.com/Keywordream-PDA/Server) - Express.js  
> [Crawling Server](https://github.com/Keywordream-PDA/Data) - Flask
### 리팩토링 후
> [Rest API Server](https://github.com/kkh0331/keywodream-api-server) - Spring Boot  
> [Crawling Server](https://github.com/kkh0331/keywordream-crawling-server) - Flask

## 🔨 리팩토링을 진행한 이유
> [Keywordream 리팩토링 계획](https://velog.io/@rlgus9301/Keywordream-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-%EA%B3%84%ED%9A%8D)

프로젝트를 당시 `DB 설계`, `API 구조 설계`, `폴더 구조 분리` 등 백엔드 개발의 핵심적인 부분에서 다소 미흡한 점을 발견  
코드의 유지 보수성과 확장성을 높이기 위한 `리팩토링`의 필요성을 느낌

## 📝 크롤링 서버 따로 구축한 이유
- 프로젝트 초기에는 실시간 크롤링을 Main Server에 붙일려고 했었음  
- But, 실시간 크롤링을 하는 도중에 에러가 발생할 경우 Main Server가 동작하지 않을 가능성이 있음  
- So 크롤링 서버를 따로 분리함  
- 크롤링, 키워드 추출, 뉴스 감정분석을 용이하게 하기 위해서 python을 이용한 Flask Server을 구축했음

## 🙋‍♂️ 담당한 기능
### 리팩토링 전
- 한국투자증권 API을 이용한 실시간 시세 받아오기
- [리팩토링 전 크롤링 서버](https://github.com/Keywordream-PDA/Data) 구축 & 뉴스 크롤링
- 뉴스 관련 API 설계 및 개발
- 뉴스 관련 UI 설계 및 개발
### 리팩토링 후
- [리팩토링 후 크롤링 서버](https://github.com/kkh0331/keywordream-crawling-server) 전체 코드 리팩토링
- [리팩토링 후 Rest API 서버](https://github.com/kkh0331/keywodream-api-server) 전체 코드 리팩토링

## 🗂️ Schema
### 리팩토링 전
<img src="https://github.com/user-attachments/assets/2b32f526-97da-4d0f-86ae-3f39d65c395f" width="600" height="auto">

### 리팩토링 후
<img src="https://github.com/user-attachments/assets/a1955503-06bc-44a9-b3d5-ee3c26b60dfd" width="600" height="auto">

## 📑 API 명세서
[Keywordream API 명세서](https://kkh0331.notion.site/Keywordream-API-9c8d284998854dc7b30cb6d4a2b4ab51)

## 😡 Trouble Issues
>[Keywordream 프로젝트 후기](https://velog.io/@rlgus9301/Keywordream-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9B%84%EA%B8%B0)
### 실시간 시세 조회
- 처음에는 Client와 한국투자증권 Web Socket Server을 직접 연결했으나 `동시 연결 불가능`이라는 제약사항 존재하여 자체 서버를 중간 경유지로 사용하여 해결
- 중간 경유지로 사용함으로써, 자체 서버에 각 클라이언트가 요청한 모든 주식 데이터가 수집됨 -> 자체 서버에서 각 클라이언트에게 필요한 주가 데이터만 보내기 위해서 socket.io의 room 기능을 활용함

### 크롤링 최소화
- 원래는 사용자가 요청할 때마다, 크롤링을 진행하려고 했었음
- But, 중복 크롤링 가능성도 있고 크롤링을 하는 네이버에서 막을 가능성이 존재
- So, 크롤링을 최소화하기 위해 DB에 저장함으로써 중복 크롤링을 막음
- 대략 뉴스 크롤링 및 키워드 추출 시간을 평균 1/3 줄임

## 🗂️ API Server Folder Structure 
### 리팩토링 전
```
├── README.md
├── app.js
├── bin
│   └── www
├── database
│   ├── connect
│   │   └── mariadb.js
│   └── news
│       ├── detail.js
│       ├── list.js
│       └── tag.js
├── package-lock.json
├── package.json
├── public
│   └── stylesheets
│       └── style.css
├── routes
│   ├── chart.js
│   ├── finstat.js
│   ├── flask.js
│   ├── index.js
│   ├── keyword.js
│   ├── main.js
│   ├── mypage.js
│   ├── news.js
│   ├── search.js
│   ├── stock.js
│   ├── stockInfo.js
│   └── users.js
├── socket.js
├── utils
│   ├── BatchStocks.js
│   ├── MarketOpen.js
│   ├── MyStock.js
│   ├── Shinhan.js
│   ├── Signup.js
│   ├── StockInfo.js
│   ├── TopKeyword.js
│   ├── time.js
│   └── token
│       ├── EbestToken.js
│       └── KOInvToken.js
└── views
    ├── error.ejs
    └── index.ejs
```
### 리팩토링 후
src/main 안의 폴더 구조
```├── java
│   └── pda
│       └── keywordream
│           ├── KeywordreamApplication.java
│           ├── chart
│           │   ├── ChartController.java
│           │   ├── dto
│           │   │   └── StockChartPrice.java
│           │   └── service
│           │       └── ChartService.java
│           ├── client
│           │   ├── FlaskApi.java
│           │   ├── GoogleApi.java
│           │   ├── KoInvSecApi.java
│           │   ├── LSSecApi.java
│           │   ├── ShinhanSecApi.java
│           │   ├── ThinkpoolApi.java
│           │   └── dto
│           │       ├── flask
│           │       │   ├── NewsSentimentAnalysis.java
│           │       │   └── NewsSentimentAnalysisRes.java
│           │       ├── google
│           │       │   └── TrendingSearch.java
│           │       ├── koinvsec
│           │       │   ├── StockDailyPrice.java
│           │       │   ├── StockDailyPriceRes.java
│           │       │   ├── StockFinancialRatio.java
│           │       │   ├── StockFinancialRatioRes.java
│           │       │   ├── StockIncomeState.java
│           │       │   ├── StockIncomeStateRes.java
│           │       │   ├── StockOtherMajorRatio.java
│           │       │   ├── StockOtherMajorRatioRes.java
│           │       │   ├── StockPrice.java
│           │       │   └── StockPriceRes.java
│           │       ├── lssec
│           │       │   ├── T8412Req.java
│           │       │   ├── T8412ReqBlock.java
│           │       │   ├── T8412Res.java
│           │       │   ├── T8412ResBlock.java
│           │       │   ├── T8430Req.java
│           │       │   ├── T8430ReqBlock.java
│           │       │   ├── T8430Res.java
│           │       │   └── T8430ResBlock.java
│           │       ├── shinhansec
│           │       │   ├── RankStock.java
│           │       │   ├── RankStockByViewsDataBody.java
│           │       │   ├── RankStockByViewsRes.java
│           │       │   └── RankStockRes.java
│           │       └── thinkpool
│           │           ├── TopKeyword.java
│           │           ├── TopKeywordRes.java
│           │           └── TopKeywordStock.java
│           ├── config
│           │   └── SchedulerConfig.java
│           ├── heart
│           │   ├── HeartController.java
│           │   ├── dto
│           │   │   ├── HeartStockResDto.java
│           │   │   └── RegisterHeartStockReqDto.java
│           │   ├── entity
│           │   │   └── HeartStock.java
│           │   ├── repository
│           │   │   └── HeartStockRepository.java
│           │   └── service
│           │       └── HeartStockService.java
│           ├── keyword
│           │   └── KeywordController.java
│           ├── news
│           │   ├── NewsController.java
│           │   ├── dto
│           │   │   ├── NewsDetailResDto.java
│           │   │   ├── NewsKeywordResDto.java
│           │   │   ├── NewsResDto.java
│           │   │   └── NewsSentimetAnalysisResDto.java
│           │   ├── entity
│           │   │   ├── News.java
│           │   │   ├── NewsKeyword.java
│           │   │   └── NewsStock.java
│           │   ├── repository
│           │   │   ├── NewsKeywordRepository.java
│           │   │   ├── NewsRepository.java
│           │   │   └── NewsStockRepository.java
│           │   └── service
│           │       └── NewsService.java
│           ├── rank
│           │   ├── RankController.java
│           │   ├── dto
│           │   │   ├── RankStockResDto.java
│           │   │   ├── TopKeywordResDto.java
│           │   │   ├── TopKeywordStockResDto.java
│           │   │   └── TrendingSearchReqDto.java
│           │   ├── service
│           │   │   └── RankService.java
│           │   └── type
│           │       └── Sorting.java
│           ├── statement
│           │   ├── StatementController.java
│           │   ├── dto
│           │   │   ├── PerPbrDto.java
│           │   │   └── StatementResDto.java
│           │   ├── entity
│           │   │   └── Statement.java
│           │   ├── repository
│           │   │   └── StatementRepository.java
│           │   └── service
│           │       └── StatementService.java
│           ├── stock
│           │   ├── StockController.java
│           │   ├── dto
│           │   │   ├── GetStockResDto.java
│           │   │   ├── GetStocksReqDto.java
│           │   │   ├── GetStocksResDto.java
│           │   │   ├── PageNation.java
│           │   │   ├── StockDailyPriceResDto.java
│           │   │   └── StockResDto.java
│           │   ├── entity
│           │   │   └── Stock.java
│           │   ├── repository
│           │   │   └── StockRepository.java
│           │   └── service
│           │       └── StockService.java
│           ├── user
│           │   ├── UserController.java
│           │   ├── dto
│           │   │   ├── NicknameReqDto.java
│           │   │   └── NicknameResDto.java
│           │   ├── entity
│           │   │   └── User.java
│           │   ├── repository
│           │   │   └── UserRepository.java
│           │   └── service
│           │       └── UserService.java
│           └── utils
│               ├── ApiUtils.java
│               ├── annotation
│               │   ├── DateValidator.java
│               │   └── ValidDate.java
│               ├── exceptions
│               │   ├── GlobalExceptionHandler.java
│               │   └── NoSaveElementException.java
│               ├── jwt
│               │   └── JWTUtil.java
│               └── token
│                   ├── KoInvSecToken.java
│                   └── LSSecToken.java
└── resources
    └── application.properties
```

## 🗂️ Crawling Server Folder Structure 
### 리팩토링 전
```
├── README.md
├── app.py
├── crawling
│   ├── api.py
│   ├── bs4.py
│   └── crawling.py
├── db
│   ├── check_insert_stock.py
│   ├── check_news_id.py
│   ├── connect.py
│   ├── get_title_content.py
│   ├── insert_isGood.py
│   ├── insert_keywords.py
│   ├── insert_news.py
│   └── insert_news_stock.py
├── mecab
│   ├── Mecab-ko-for-Google-Colab
│   ├── mecab-0.996-ko-0.9.2.tar.gz
│   └── mecab-ko-dic-2.1.1-20180720.tar.gz
└── requirements.txt
```
### 리팩토링 후
```
├── README.md
├── app.py
├── client
│   └── naver_api.py
├── crawling
│   ├── repository.py
│   └── service.py
├── database
│   └── connect.py
├── keywords
│   ├── repository.py
│   └── service.py
├── mecab
│   ├── Mecab-ko-for-Google-Colab
│   ├── mecab-0.996-ko-0.9.2.tar.gz
│   └── mecab-ko-dic-2.1.1-20180720.tar.gz
├── news
│   ├── controller.py
│   ├── repository.py
│   └── service.py
├── requirements.txt
├── stocks
│   ├── controller.py
│   ├── repository.py
│   └── service.py
└── utils
    ├── api_result.py
    └── json_app.py
```
