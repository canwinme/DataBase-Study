import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BOSTANHOUSE {
    
    // CSV 파일
    private static final String CSV_FILE =
            "src/boston_house_price.csv";
    
    // Oracle DB 접속 정보
    private static final String DB_HOST =
            "127.0.0.1";

    private static final String DB_PORT =
            "1521";

    private static final String DB_SERVICE =
            "orcl";

    private static final String DB_URL =
            "jdbc:oracle:thin:@//"
                    + DB_HOST + ":"
                    + DB_PORT + "/"
                    + DB_SERVICE;

    private static final String DB_USER =
            System.getenv("DB_USERNAME");

    private static final String DB_PASSWORD =
            System.getenv("DB_PASSWORD");
    
    // INSERT SQL
    private static final String INSERT_SQL =
            "INSERT INTO BOOSTON_HOUSE_PRICE "
                    + "(crim, zn, indus, chas, nx, rm, age, dis, "
                    + "rad, tax, ptratio, b, lstat, medv) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



    public static void main(String[] args)
            throws ClassNotFoundException {
        
        // CSV 읽기
        List<double[]> rows =
                readCSV(CSV_FILE);

        System.out.println(
                "CSV에서 읽은 행의 수 : "
                        + rows.size()
        );
        
        // Oracle JDBC 드라이버 로드
        Class.forName("oracle.jdbc.OracleDriver");

        // Oracle DB 연결
        try (Connection conn =
                     DriverManager.getConnection(
                             DB_URL,
                             DB_USER,
                             DB_PASSWORD)) {

            conn.setAutoCommit(false);
            
            // CSV 데이터를 Oracle에 저장
            insertData(conn, rows);
            
            System.out.println(
                    "총 " + rows.size()
                            + "개의 데이터를 BOOSTON_HOUSE_PRICE 테이블에 저장했습니다."
            );
            
            //DB 데이터 분포 출력
            printDistribution(conn);
            
            //출력이 끝난 후 테이블 데이터 삭제
            deleteTableData(conn);


        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    // CSV 데이터를 Oracle에 INSERT
    private static void insertData(
            Connection conn,
            List<double[]> rows)
            throws SQLException {

        try (PreparedStatement pstmt =
                     conn.prepareStatement(INSERT_SQL)) {

            int batchCount = 0;
            
            for (double[] row : rows) {

                pstmt.setDouble(1, row[0]);   // CRIM
                pstmt.setDouble(2, row[1]);   // ZN
                pstmt.setDouble(3, row[2]);   // INDUS
                pstmt.setInt(4, (int) row[3]); // CHAS
                pstmt.setDouble(5, row[4]);   // NX
                pstmt.setDouble(6, row[5]);   // RM
                pstmt.setDouble(7, row[6]);   // AGE
                pstmt.setDouble(8, row[7]);   // DIS
                pstmt.setInt(9, (int) row[8]); // RAD
                pstmt.setDouble(10, row[9]);  // TAX
                pstmt.setDouble(11, row[10]); // PTRATIO
                pstmt.setDouble(12, row[11]); // B
                pstmt.setDouble(13, row[12]); // LSTAT
                pstmt.setDouble(14, row[13]); // MEDV

                pstmt.addBatch();

                batchCount++;


                // 100개씩 처리
                if (batchCount % 100 == 0) {

                    pstmt.executeBatch();
                }
            }


            // 100개 단위로 처리하고 남은 데이터 처리
            pstmt.executeBatch();
            
            // INSERT 확정
            conn.commit();
        }
    }



    // DB에서 데이터를 읽어서 분포 출력
    private static void printDistribution(
            Connection conn)
            throws SQLException {

        String sql =
                "SELECT * FROM BOOSTON_HOUSE_PRICE";


        // 컬럼 이름
        String[] columns = {

                "CRIM",
                "ZN",
                "INDUS",
                "CHAS",
                "NX",
                "RM",
                "AGE",
                "DIS",
                "RAD",
                "TAX",
                "PTRATIO",
                "B",
                "LSTAT",
                "MEDV"
        };


        // DB에서 읽은 데이터를 저장할 List
        List<double[]> data =
                new ArrayList<>();


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql);

             ResultSet rs =
                     pstmt.executeQuery()) {



            // DB 한 행을 double[] 하나로 저장
            while (rs.next()) {

                double[] row = {

                        rs.getDouble("CRIM"),
                        rs.getDouble("ZN"),
                        rs.getDouble("INDUS"),
                        rs.getDouble("CHAS"),
                        rs.getDouble("NX"),
                        rs.getDouble("RM"),
                        rs.getDouble("AGE"),
                        rs.getDouble("DIS"),
                        rs.getDouble("RAD"),
                        rs.getDouble("TAX"),
                        rs.getDouble("PTRATIO"),
                        rs.getDouble("B"),
                        rs.getDouble("LSTAT"),
                        rs.getDouble("MEDV")
                };


                data.add(row);
            }
        }



        // 분포 출력
        System.out.println();

        System.out.println(
                "========== BOSTON HOUSE PRICE 데이터 분포 =========="
        );

        System.out.println(
                "총 데이터 수 : " + data.size()
        );


        // 컬럼 하나씩 처리
        for (int i = 0;
             i < columns.length;
             i++) {


            System.out.println();
            System.out.println(
                    "[" + columns[i] + "]"
            );


            // 최소값 / 최대값 찾기
            double min =
                    Double.MAX_VALUE;

            double max =
                    -Double.MAX_VALUE;


            for (double[] row : data) {

                if (row[i] < min) {
                    min = row[i];
                }

                if (row[i] > max) {
                    max = row[i];
                }
            }


            // 데이터가 없는 경우
            if (data.isEmpty()) {

                System.out.println(
                        "데이터가 없습니다."
                );

                continue;
            }


         
            // 10개 구간으로 나누기
            int[] count =
                    new int[10];


            for (double[] row : data) {

                int index;


                if (max == min) {

                    index = 0;

                } else {

                    index =
                            (int) (
                                    (row[i] - min)
                                            / (max - min)
                                            * 10
                            );


                    // 최대값이 10번 구간으로
                    // 넘어가는 것을 방지

                    if (index == 10) {
                        index = 9;
                    }
                }


                count[index]++;
            }


            // 출력
            for (int j = 0;
                 j < 10;
                 j++) {


                double start =
                        min
                                + (max - min)
                                / 10
                                * j;


                double end =
                        min
                                + (max - min)
                                / 10
                                * (j + 1);


                int dataCount =
                        count[j];


                // 별표 최대 50개
                // 전체 데이터에서 해당 구간의 비율을 계산

                int starCount =
                        (int) (
                                (double) dataCount
                                        / data.size()
                                        * 50
                        );


                System.out.printf(
                        "%8.2f ~ %8.2f | ",
                        start,
                        end
                );


                for (int k = 0;
                     k < starCount;
                     k++) {

                    System.out.print("*");
                }


                System.out.println(
                        " (" + dataCount + ")"
                );
            }
        }
    }


 
    // 테이블 데이터 삭제
    private static void deleteTableData(
            Connection conn)
            throws SQLException {

        String sql =
                "DELETE FROM BOOSTON_HOUSE_PRICE";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {


            int count =
                    pstmt.executeUpdate();


            conn.commit();


            System.out.println();

            System.out.println(
                    "테이블의 데이터 "
                            + count
                            + "개를 삭제했습니다."
            );
        }
    }



    // CSV 읽기
    private static List<double[]> readCSV(
            String path) {

        // CSV 데이터를 임시로 저장
        List<double[]> rows =
                new ArrayList<>();


        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(path))) {


            // 첫 번째 줄은 컬럼명
            // 따라서 읽고 버림

            String line =
                    br.readLine();


            // 두 번째 줄부터 실제 데이터
            while ((line = br.readLine()) != null) {


                // 빈 줄 무시

                if (line.isBlank()) {
                    continue;
                }


                // 쉼표 기준으로 분리

                String[] cols =
                        line.split(",");


                // CSV 한 줄을 double[]로 변환

                double[] row = {

                        Double.parseDouble(cols[0]),
                        Double.parseDouble(cols[1]),
                        Double.parseDouble(cols[2]),
                        Double.parseDouble(cols[3]),
                        Double.parseDouble(cols[4]),
                        Double.parseDouble(cols[5]),
                        Double.parseDouble(cols[6]),
                        Double.parseDouble(cols[7]),
                        Double.parseDouble(cols[8]),
                        Double.parseDouble(cols[9]),
                        Double.parseDouble(cols[10]),
                        Double.parseDouble(cols[11]),
                        Double.parseDouble(cols[12]),
                        Double.parseDouble(cols[13])
                };


                // List에 추가

                rows.add(row);
            }


        } catch (FileNotFoundException e) {

            throw new RuntimeException(
                    "CSV 파일을 찾을 수 없습니다.",
                    e
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "CSV 파일을 읽을 수 없습니다.",
                    e
            );
        }


        return rows;
    }