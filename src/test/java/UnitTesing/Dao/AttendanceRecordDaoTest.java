/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTesing.Dao;

import alex.oopmotorphpayrollsystem.AttendanceRecord;
import dao.AttendanceRecordDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DbConnection;

/**
 *
 * @author Alex Resurreccion
 */
public class AttendanceRecordDaoTest {
    AttendanceRecordDao attendanceRecordDao = new AttendanceRecordDao();
    Connection connection;
    
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DbConnection.getConnectionWithTransaction();
        attendanceRecordDao = new AttendanceRecordDao(connection);
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback();  // Roll back everything done in the test
            connection.close();
        }
    }
    
    @Test
    public void testGetAttendanceRecordSuccess() {
        System.out.println("Test for getAttendanceRecord - Success");
        LocalDate localDate = LocalDate.of(2024, 6, 3);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AttendanceRecord attendance = attendanceRecordDao.getAttendanceRecord(10004, date);

        assertNotNull(attendance);
        System.out.println("Attendance for 10004 is not null");
        System.out.println("Time in: " + attendance.getTimeIn().toString());
        System.out.println("Time out: " + attendance.getTimeOut().toString());
    }
    
    @Test
    public void testGetAttendanceRecordNoLogsOnDate() {
        System.out.println("Test for getAttendanceRecord - No logs on Date");
        LocalDate localDate = LocalDate.of(2015, 12, 12);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AttendanceRecord attendance = attendanceRecordDao.getAttendanceRecord(10004, date);

        assertNotNull(attendance);
        assertNull(attendance.getTimeIn());
        assertNull(attendance.getTimeOut());
        System.out.println("Attendance for 10004 on 2015-12-12 "
                + "has been created with null time in and time out");
    }
    
    @Test
    public void testGetAttendanceRecordInvalidEmployee() {
        System.out.println("Test for getAttendanceRecord - Invalid Employee");
        LocalDate localDate = LocalDate.of(2024, 6, 3);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AttendanceRecord attendance = attendanceRecordDao.getAttendanceRecord(999999999, date);

        assertNull(attendance);
        System.out.println("Attendance does not exist.");
    }
}
