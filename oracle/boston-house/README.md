# Boston House Price - Oracle JDBC Practice

Kaggle의 Boston House Price CSV 데이터를 Java로 읽어 Oracle Database에 적재하고,
DB에서 다시 조회한 뒤 각 컬럼의 값 분포를 10개 구간으로 나누어 콘솔에 출력하는 실습입니다.

## 구성

```text
boston-house/
├─ BOSTANHOUSE.java
├─ boston_house_price.csv
└─ schema.sql
```

## 처리 흐름

```text
CSV 읽기
  ↓
Oracle JDBC 연결
  ↓
PreparedStatement Batch INSERT
  ↓
DB 데이터 조회
  ↓
컬럼별 최소값/최대값 계산
  ↓
10개 구간으로 분포 집계
  ↓
별표 형태로 콘솔 출력
  ↓
실습 데이터 삭제
```

## 실행 전 준비

1. Oracle에서 `schema.sql`을 실행합니다.
2. Oracle JDBC Driver를 프로젝트에 추가합니다.
3. 환경변수에 DB 계정을 설정합니다.

```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

Windows PowerShell 예시:

```powershell
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
```

원본 최종 코드의 DB 접속 계정/비밀번호는 공개 저장소에 노출되지 않도록 환경변수 방식으로 변경했습니다.

## 주요 학습 내용

- Java 파일 입출력
- CSV 파싱
- Oracle JDBC 연결
- `PreparedStatement`
- Batch INSERT
- `ResultSet`
- 트랜잭션 / `commit()`
- DB 조회 결과 기반 간단한 데이터 분포 시각화
