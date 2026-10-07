# DataBase-Study

Oracle SQL Developer를 활용해 학습한 SQL 실습 코드와 프로젝트 데이터베이스 스키마를 정리한 저장소입니다.

## 사용 환경

- Oracle SQL Developer
- Oracle Database
- SQL

## 폴더 구조

```text
DataBase-Study/
└─ oracle/
   ├─ practice/
   │  ├─ 01_basic_select.sql
   │  ├─ 02_query_practice.sql
   │  └─ 03_ddl_dml_practice.sql
   │
   └─ jobradar/
      ├─ 01_schema.sql
      └─ 02_career_job.sql
```

## Oracle SQL Practice

### 01_basic_select.sql
기본적인 SELECT 조회문을 연습한 파일입니다.

### 02_query_practice.sql
BOOK, CUSTOMER, ORDERS 테이블을 기준으로 다양한 조회문을 연습했습니다.

주요 학습 내용:
- SELECT
- WHERE
- BETWEEN
- IN
- LIKE
- ORDER BY
- 집계 함수
- GROUP BY / HAVING
- DISTINCT
- 다중 테이블 조회 및 JOIN

### 03_ddl_dml_practice.sql
조회문에서 더 나아가 테이블 생성과 데이터 변경 관련 SQL을 연습한 파일입니다.

주요 학습 내용:
- LEFT OUTER JOIN
- Subquery
- CREATE TABLE
- PRIMARY KEY / FOREIGN KEY
- ALTER TABLE
- INSERT
- UPDATE
- DELETE
- COMMIT
- Boston Housing 데이터용 테이블 생성

## JobRadar Database

산업별 취업시장 분석 프로젝트인 JobRadar에서 사용한 Oracle 데이터베이스 스키마입니다.

### 01_schema.sql

다음과 같은 테이블을 정의합니다.

- `TB_INDUSTRY_VACANCY`
- `TB_INDUSTRY_EMPLOYMENT`
- `TB_INDUSTRY_INDEX`
- `TB_INDUSTRY_MAP`
- `TB_EMPLOYMENT_GENDER`
- `TB_BOARD_POST`

산업별 취업자 수, 빈일자리율, 증감률, 산업 분류 및 게시판 데이터를 관리하기 위한 구조입니다.

### 02_career_job.sql

CareerNet 직업 데이터를 저장하기 위한 `TB_CAREER_JOB` 테이블을 정의합니다.

주요 컬럼:
- 직업 코드
- 직업명
- 적성 유형
- 직업 분야
- 산업 코드
- 관련 자격
- 교육 및 훈련
- 준비 방법

## 정리 기준

기존 SQL 실습 파일의 내용을 유지하면서 용도에 따라 다음과 같이 구분했습니다.

- `practice`: Oracle SQL 학습 및 문법 실습
- `jobradar`: 프로젝트에서 실제 사용한 데이터베이스 스키마

> 일부 기존 SQL 파일에는 저장 당시 인코딩 문제로 한글 주석 및 데이터가 깨진 부분이 남아 있습니다. 원본 보존을 위해 임의로 수정하지 않았습니다.
