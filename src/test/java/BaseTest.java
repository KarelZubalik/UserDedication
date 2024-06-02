import org.userDedication.UserDedication;
import org.testng.annotations.*;

import java.sql.SQLException;



public class BaseTest {
    public BaseTest() {
        userDedication=new UserDedication();
    }

    String user;
    UserDedication userDedication;
    @BeforeTest
    void before() throws Exception {
        user=userDedication.alocateTechnician(getClass().getName());
    }
    @AfterTest
    void afterTest() throws SQLException {
        userDedication.freeTechnician(user);
        System.out.println("K testu " + getClass().getName() + " uvolňuji technika:" + user);
    }
}
