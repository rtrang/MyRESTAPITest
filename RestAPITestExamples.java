
package com.hp.ssmc.svt.testsecurity;

import com.hp.tpd.test.helpers.SessionToken;
import com.hp.tpd.test.helpers.Systems;
import com.hp.tpd.test.helpers.TestHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static com.hp.tpd.test.helpers.TestHelper.*;

/**
 * Created with IntelliJ IDEA.
 * User: Roger Trang
 * Date: 6/2/14
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserPasswordTestCases {
    private static final Systems TestSystem = Systems.S388;
    //private static String baseURL = "https://localhost:8443";
    //private static final Systems[] TestSystems = {Systems.S388};
    private static String wwn = TestSystem.getWwn();
    private static String URL = "https://localhost:8443/security/REST/userservice/systems/" +wwn+ "/users";

    @BeforeClass
    public static void before() throws Exception {
        try {
            TestHelper.startJetty();
            addTheseSystems(TestSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() throws Exception {
        Thread.sleep(100);
    }

    // 1. Verify admin should be able to create user
    @Test
    public void AdminCreatesSuperUser() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        System.out.println(session+ URL+ body);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        watch(URL, session);
        waitForChanges(session);
        DeleteUser(username, UserURI);
     }

    // 1. Create a user without confirmPassword parameter should fail
    @Test
    public void CreatesUserWithoutConfirmPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        //System.out.println(session+" "+URL+" "body);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // 1. admin should be able to change super user's password
    @Test
    public void SuperUserChangePassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        AdminChangeUsersPassword(username, currentpwd, newpwd, UserURI);
        DeleteUser(username, UserURI);
    }



    // AA user should be able to change its own password
    @Test
    public void AOUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "3PAR_AO");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // BB user should be able to change its own password
    @Test
    public void RMUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword", currentpwd);
        body.put("roleName", "3PAR_RM");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // CC user should be able to change its own password
    @Test
    public void BEUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "basic_edit");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // DD user should be able to change its own password
    @Test
    public void BrowseUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "browse");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Create role user should be able to change its own password
    @Test
    public void CreateRoleUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "create");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Edit user should be able to change its own password
    @Test
    public void EditUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "edit");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Service role user should be able to change its own password
    @Test
    public void ServiceRoleUserChangeOwnPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "service");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }


    // Create a user with 256 characters
    @Test
    public void CreateUserWith256Chars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(256, true, true);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify user should NOT be able to change password with less than 6 charecters
    @Test
    public void PasswordLessThanSixChars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(5, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "edit");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 1);
        DeleteUser(username, UserURI);
    }

    // 1. Edit user should NOT be able to change password more than 9 charecters
    @Test
    public void ChangePasswordWithNineChars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(9, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "edit");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 1);
        DeleteUser(username, UserURI);
    }

    // 2. Verify user can change its own passwrod with dashes
    @Test
    public void ChangePasswordWithDashes() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = "------", confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // 2. Verify user can change its own passwrod with dashes
    @Test
    public void ChangePasswordWithSpecialChars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = "*()_+{}";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
//        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        String nl = System.lineSeparator();
        System.out.println("URL: " +URL +nl+ "BODY: " +body);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        System.out.println("Response code: " +responseCode);
        String UserURI = GetUserURI(username);
        DeleteUser(username, UserURI);
    }

    // 2. Verify user can change its own passwrod with dashes
    @Test
    public void ChangePasswordWithSpecial1Chars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = "|:<>?~!";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
//        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        String nl = System.lineSeparator();
        System.out.println("URL: " +URL +nl+ "BODY: " +body);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        System.out.println("Response code: " +responseCode);
        String UserURI = GetUserURI(username);
        DeleteUser(username, UserURI);
    }

    // 1. Verify user cannot change password with wrong ConfirmPassword
    @Test
    public void WrongConfirmPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(7, true, true);
        String confirmpwd = RandomStringUtils.random(8, true, true);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 1);
        DeleteUser(username, UserURI);
    }

    // Verify user cannot change new password with blanks
    @Test
    public void BlanksNewPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = " ", confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        System.out.println("create session token from admin is "+session+" - PASS");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 1);
        DeleteUser(username, UserURI);
    }

    // Verify user can change ConfirmPassword with dots
    @Test
    public void ChangePasswordWithDots() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = "......", confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Verify user can change ConfirmPassword with underscore
    @Test
    public void ChangePasswordWithUnderscore() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = "______", confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Verify user can change password with all special chars
    @Test
    public void ChangePasswordWithDotsAndDashesChars() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = "___...--", confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        UserChangeOwnPassword(username, newpwd, currentpwd, confirmpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    // Verify user can change password with zeros
    @Test
    public void PasswordWithAllZeros() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        int currentpwd = 0000000;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify user password with symbols
    @Test
    public void UserPasswordWithSymbols() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = "!@#=%^&*";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        DeleteUser(username, UserURI);
    }

    // Verify user name with symbols
    @Test
    public void UserNameWithSymbols() throws Exception {
        //String username = RandomStringUtils.random(5, true, true);
        String username = "!@#=%^&*";
        String currentpwd = "!@#=%^&*";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify user name more than 31 chars
    @Test
    public void UserNameMoreThan31Chars() throws Exception {
        String username = RandomStringUtils.random(32, true, true);
        String currentpwd = "ssmssm";
        String confirmpwd = currentpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify user name with 1 chars
    @Test
    public void UserNameMoreThanOneChars() throws Exception {
        String username = RandomStringUtils.random(1, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        //System.out.println("username:"+username+" URL:"+URL+" session:"+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        String UserURI = GetUserURI(username);
        DeleteUser(username, UserURI);
    }

    // Verify user name with blanks
    @Test
    public void UserNameWithBlanks() throws Exception {
        String username = "    ";
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        //System.out.println("username:"+username+" URL:"+URL+" session:"+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify user name with 31 chars and 409 return code
    @Test
    public void VerifyCreateUserWithConflict() throws Exception {
        String username = RandomStringUtils.random(31, true, true);
        String currentpwd = "ssmssm";
        String confirmpwd = currentpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("confirmPassword",currentpwd);
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 0);
        Response Conflictresp = TestHelper.post(URL, session, body);
        int ConflictRespCode = Conflictresp.getStatus();
        System.out.println("Response Code should be 409 conflict");
        // 0 is happy path and 1 is unhappy path
        Results(ConflictRespCode, 1);
        String UserURI = GetUserURI(username);
        DeleteUser(username, UserURI);
    }

    // Verify incorrect username parameter
    @Test
    public void WrongUserNameParam() throws Exception {
        String username = RandomStringUtils.random(30, true, true);
        String currentpwd = "ssmssm";
        String confirmpwd = currentpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("username", username);      // incorrect userName parameter specified
        body.put("password", currentpwd);
        body.put("confirmPassword", confirmpwd);
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify incorrect password parameter
    @Test
    public void WrongPasswordParam() throws Exception {
        String username = RandomStringUtils.random(30, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("passWord", currentpwd);   // incorrect password parameter specified
        body.put("roleName", "super");
        //System.out.println("username: "+username+" URL: "+URL+" session: "+session);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify incorrect role name parameter
    @Test
    public void WrongRoleNameParam() throws Exception {
        String username = RandomStringUtils.random(10, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("rolename", "super");          // incorrect roleName parameter specified
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        Results(responseCode, 1);
    }

    // Verify invalid username parameter
    @Test
    public void UserNameParam() throws Exception {
        String username = RandomStringUtils.random(10, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");          // incorrect roleName parameter specified
        String jsonresp = TestHelper.post(URL, session, Response.Status.BAD_REQUEST, body);
        String[] ArrayList = jsonresp.split(":");
        for (int i=0; i<ArrayList.length; i++) {
            System.out.println(ArrayList[i]);
        }
        if (ArrayList[1].contains("Invalid user name") && ArrayList[2].contains("Invalid user name")) {
            System.out.println("PASS");
        } else {
            throw new RuntimeException("FAIL");
        }
    }

    // Verify invalid password parameter
    @Test
    public void PasswordNameParam() throws Exception {
        String username = RandomStringUtils.random(10, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("passWord", currentpwd);      // password field name should password instead of passWord
        body.put("roleName", "super");
        String jsonresp = TestHelper.post(URL, session, Response.Status.BAD_REQUEST, body);
        String[] ArrayList = jsonresp.split(":");
        for (int i=0; i<ArrayList.length; i++) {
            System.out.println(ArrayList[i]);
        }
        if (ArrayList[1].contains("Invalid password") && ArrayList[2].contains("Invalid password")) {
            System.out.println("PASS");
        } else {
            throw new RuntimeException("FAIL");
        }
    }
    // Verify invalid role name parameter
    @Test
    public void localizedMessageRoleNameParam() throws Exception {
        String username = RandomStringUtils.random(10, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("rolename", "super");          // incorrect roleName parameter specified
        System.out.println("url is " +URL+ " session is " +session);
        String jsonresp = TestHelper.post(URL, session, Response.Status.BAD_REQUEST, body);
    }

    // Verify without confirmPassword parameter
    @Test
    public void WithoutConfirmPassword() throws Exception {
        String username = RandomStringUtils.random(10, true, true);
        String currentpwd = "ssmssm";
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        Response jsonresp = TestHelper.post(URL, session, body);
        int response_status = jsonresp.getStatus();
        System.out.println("response body is " +response_status);

    }

    // Verify user can change password without confirm
    @Test
    public void ChangePasswordWithoutConfirmPassword() throws Exception {
        String username = RandomStringUtils.random(5, true, true);
        String currentpwd = RandomStringUtils.random(6, true, true);
        String newpwd = RandomStringUtils.random(6, true, true);
        String confirmpwd = newpwd;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("userName", username);
        body.put("password", currentpwd);
        body.put("roleName", "super");
        System.out.println("Create user URL: " +URL);
        System.out.println("Create user body: " +body);
        Response resp = TestHelper.post(URL, session, body);
        int responseCode = resp.getStatus();
        // 0 is happy path and 1 is unhappy path
        //Results(responseCode, 0);
        System.out.println("Create user status: " +responseCode);
        String UserURI = GetUserURI(username);
        System.out.println("Edit password URL: " +UserURI);
        UserChangePasswordNoConfirm(username, newpwd, currentpwd, UserURI, 0);
        DeleteUser(username, UserURI);
    }

    public String GetUserURI(String username) throws Exception {
        String UserURI = null;
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        //JSONObject resp = new JSONObject();
        JSONObject jsonresp = TestHelper.get(URL, session, Response.Status.OK);
        //System.out.println("resp is " + jsonresp.toString(2));
        JSONObject jsonObject = new JSONObject(jsonresp.toString(2));
        JSONArray members_resp = (JSONArray) jsonObject.get("members");
        ArrayList<String> urilist = new ArrayList<String>();
        ArrayList<String> namelist = new ArrayList<String>();
        for(int i=1; i<members_resp.length(); i++) {
            urilist.add(members_resp.getJSONObject(i).getString("uri"));
            namelist.add(members_resp.getJSONObject(i).getString("name"));
        }
        for (int j=0; j<namelist.size(); j++) {
            if (namelist.get(j).equals(username)) {
                UserURI = "https://localhost:8443"+urilist.get(j);
                break;
            }
        }
        return UserURI;
    }

    public void DeleteUser(String username, String UserURI) throws Exception {
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        Response resp = TestHelper.delete(UserURI, session);
        int responseCode = resp.getStatus();
        if (responseCode == 204) {
           System.out.println("User "+username+" deleted: "+responseCode+" - PASS");
           } else {
               throw new RuntimeException("User "+username+" deleted: "+responseCode+" - FAIL");
           }
        }

    public void AdminChangeUsersPassword(String username, String currentpwd, String newpwd,
                                         String UserURI) throws Exception {
        //String UserURI = GetUserURI(username);
        //System.out.println("user uri is " +UserURI);
        SessionToken session = TestHelper.createSessionToken("admin", "xxxxxxx");
        JSONObject body = new JSONObject();
        body.put("currentPassword", currentpwd);
        body.put("newPassword", newpwd);
        body.put("confirmPassword", newpwd);
        Response resp = TestHelper.put(UserURI, session, body);
        int responseCode = resp.getStatus();
        System.out.println("Verifying Admin user change password return status.....");
        if (responseCode < 205) {
            System.out.println("admin can change "+username+"'s password - PASS " +responseCode);
        } else {
            throw new RuntimeException("admin can change "+username+"'s password - FAIL " +responseCode);
        }
        SessionToken newsession = TestHelper.createSessionToken(username, newpwd);
        System.out.println("New session token is: "+newsession);
        TestHelper.deleteSessionToken(newsession);
        watch(URL, session);
        waitForChanges(session);
    }

    public void UserChangeOwnPassword(String username, String newpwd, String currpwd,
                                      String confpwd, String UserURI, int testtype) throws Exception {
        SessionToken session = TestHelper.createSessionToken(username, currpwd);
        JSONObject body = new JSONObject();
        body.put("currentPassword", currpwd);
        body.put("newPassword", newpwd);
        body.put("confirmPassword", confpwd);
        Response resp = TestHelper.put(UserURI, session, body);
        int responseCode = resp.getStatus();
        System.out.println("Verifying user change password return status.....");
        Results(responseCode, testtype);
        if (testtype == 0) {
            SessionToken newsession = TestHelper.createSessionToken(username, newpwd);
            System.out.println("New session token with new password is: "+newsession);
            TestHelper.deleteSessionToken(newsession);
        } else {
            SessionToken newsession = TestHelper.createSessionToken(username, currpwd);
            System.out.println("New session token current password is: "+newsession);
            TestHelper.deleteSessionToken(newsession);
        }
        watch(UserURI, session);
        waitForChanges(session);
    }

    public void UserChangePasswordNoConfirm(String username, String newpwd, String currpwd,
                                      String UserURI, int testtype) throws Exception {
        SessionToken session = TestHelper.createSessionToken(username, currpwd);
        JSONObject body = new JSONObject();
        body.put("currentPassword", currpwd);
        body.put("newPassword", newpwd);
        Response resp = TestHelper.put(UserURI, session, body);
        System.out.println("Edit password body: "+body);
        int responseCode = resp.getStatus();
        System.out.println("Edit password status " +responseCode);
        Results(responseCode, testtype);
        if (testtype == 0) {
            SessionToken newsession = TestHelper.createSessionToken(username, newpwd);
            System.out.println("Creating a new session key with new password: "+newsession);
            TestHelper.deleteSessionToken(newsession);
        } else {
            SessionToken newsession = TestHelper.createSessionToken(username, currpwd);
            System.out.println("Creating a session with unchanged password: "+newsession);
            TestHelper.deleteSessionToken(newsession);
        }
        watch(UserURI, session);
        waitForChanges(session);
    }

    public void Results(int returncode, int testtype)throws Exception {
        if (returncode < 205 && testtype == 0) {
//            System.out.println("Response code is: "+returncode+" - PASS");
        } else if (returncode >= 400 && testtype != 0) {
//            System.out.println("Response code is: "+returncode+" - PASS");
        } else {
            throw new RuntimeException("Response code is: "+returncode+" - FAIL");
        }
    }

} //End


