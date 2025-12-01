package com.uottawaseg.otams;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;

import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class FirebaseUnitTests {

    private static final String ROOT = "UnitTests";
    private static final String PENDING = ROOT + "/pendingReqs";
    private static final String ACCOUNTS = ROOT + "/accounts";

    // Track created paths for cleanup
    private final java.util.List<String> createdPaths = new java.util.ArrayList<>();

    @Before
    public void setup() throws Exception {
        // Ensure database started
        // Also I was having issues without this so it is necessary
        Database.Database.StartDB();

    }

    @After
    public void cleanup() {
        // Attempt to delete any created child paths (safe because we never touch root "accounts" or "pendingReqs")
        for (var p : createdPaths) {
            try {
                // only delete specific child (path must contain '/')
                Database.Database.Delete(p);
            } catch (Exception ignored) {
            }
        }
    }

    // ------------------------------------------------
    // Test 1 : Database R/W
    // ------------------------------------------------
    @Test
    public void testDatabaseReadWrite() throws Exception {
        final String key = ROOT + "/simpleValue/testKey";
        final String value = "hello-from-unittest";

        // write
        Database.Database.Write(key, value);
        createdPaths.add(key); // mark for cleanup

        // read back
        DataSnapshot snap = Database.Database.Read(key);
        Assert.assertNotNull("Read returned null snapshot", snap);
        Assert.assertTrue("Value should exist", snap.exists());
        String v = snap.getValue(String.class);
        Assert.assertEquals("Written and read values should match", value, v);

        // try remove immediate
        try { Database.Database.Delete(key); } catch (Exception ignored) {}
    }


    // ------------------------------------------------
    // Test 2 : Create Pending Student Account
    // ------------------------------------------------
    @Test
    public void testStudentAccountCreationPendingWrite() throws Exception {
        // create unique username (so you know it reads the name you created)
        final String username = "ut_student_" + System.currentTimeMillis();

        // Build account map matching LoginManager expected account fields
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put(LoginManager.FIRST_NAME, "Unit");
        accountMap.put(LoginManager.LAST_NAME, "Tester");
        accountMap.put(LoginManager.USERNAME, username);
        // store password as the string representation
        accountMap.put(LoginManager.PASSWORD, java.util.Arrays.toString(Database.GetSHA256("password123")));
        accountMap.put(LoginManager.PHONE_NUMBER, "555-0000");
        accountMap.put(LoginManager.EMAIL, "unit@student.test");
        accountMap.put(LoginManager.ROLE, "STUDENT");
        accountMap.put(LoginManager.STUDENT_NUMBER, "99999");

        // Build request node
        Map<String, Object> req = new HashMap<>();
        req.put("account", accountMap);
        req.put("status", "PENDING");
        req.put("type", "StudentAccountCreation");

        String path = PENDING + "/" + username;
        Database.Database.Write(path, req);
        createdPaths.add(path);

        // read back and assert
        DataSnapshot ds = Database.Database.Read(path);
        Assert.assertNotNull("Pending snapshot null", ds);
        Assert.assertTrue("Pending should exist", ds.exists());
        String status = ds.child("status").getValue(String.class);
        Assert.assertEquals("PENDING", status);
        String readUsername = ds.child("account").child(LoginManager.USERNAME).getValue(String.class);
        Assert.assertEquals(username, readUsername);
    }


    // ------------------------------------------------
    // Test 3 : Create Pending Tutor Account
    // ------------------------------------------------
    @Test
    public void testTutorAccountCreationPendingWrite() throws Exception {
        // create unique username (so you know it reads the name you created)
        final String username = "ut_tutor_" + System.currentTimeMillis();

        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put(LoginManager.FIRST_NAME, "Tutor");
        accountMap.put(LoginManager.LAST_NAME, "Tester");
        accountMap.put(LoginManager.USERNAME, username);
        accountMap.put(LoginManager.PASSWORD, java.util.Arrays.toString(Database.GetSHA256("tutorpass")));
        accountMap.put(LoginManager.PHONE_NUMBER, "555-1111");
        accountMap.put(LoginManager.EMAIL, "unit@tutor.test");
        accountMap.put(LoginManager.ROLE, "TUTOR");
        // tutor-specific fields
        accountMap.put(LoginManager.FIELD_OF_STUDY, Field.ENGINEERING.toString());
        accountMap.put(LoginManager.HIGHEST_DEGREE, Degree.BACHELORS.toString());
        accountMap.put("averageRating", 0f);
        accountMap.put("totalSessions", 0);

        Map<String, Object> req = new HashMap<>();
        req.put("account", accountMap);
        req.put("status", "PENDING");
        req.put("type", "TutorAccountCreation");

        String path = PENDING + "/" + username;
        Database.Database.Write(path, req);
        createdPaths.add(path);

        DataSnapshot ds = Database.Database.Read(path);
        Assert.assertNotNull(ds);
        Assert.assertTrue(ds.exists());
        String role = ds.child("account").child(LoginManager.ROLE).getValue(String.class);
        Assert.assertEquals("TUTOR", role);
        String degree = ds.child("account").child(LoginManager.HIGHEST_DEGREE).getValue(String.class);
        Assert.assertEquals(Degree.BACHELORS.toString(), degree);
    }

    // ------------------------------------------------
    // Test 4 : Accepting Pending Request Moves to Accounts
    // ------------------------------------------------
    @Test
    public void testAcceptAccountCreationMovesToAccounts() throws Exception {
        final String username = "ut_accept_" + System.currentTimeMillis();

        // Create pending request map
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put(LoginManager.FIRST_NAME, "Accept");
        accountMap.put(LoginManager.LAST_NAME, "Tester");
        accountMap.put(LoginManager.USERNAME, username);
        accountMap.put(LoginManager.PASSWORD, java.util.Arrays.toString(Database.GetSHA256("acceptpass")));
        accountMap.put(LoginManager.PHONE_NUMBER, "555-2222");
        accountMap.put(LoginManager.EMAIL, "accept@test");
        accountMap.put(LoginManager.ROLE, "STUDENT");
        accountMap.put(LoginManager.STUDENT_NUMBER, "424242");

        Map<String, Object> req = new HashMap<>();
        req.put("account", accountMap);
        req.put("status", "PENDING");
        req.put("type", "StudentAccountCreation");

        String pendingPath = PENDING + "/" + username;
        String accountPath = ACCOUNTS + "/" + username;

        // Write pending
        Database.Database.Write(pendingPath, req);
        createdPaths.add(pendingPath);

        // Now simulate admin accepting: write account under accounts and delete pending
        // Create account node
        Map<String, Object> accountNode = new HashMap<>(accountMap);
        accountNode.put("name", accountMap.get(LoginManager.FIRST_NAME) + " " + accountMap.get(LoginManager.LAST_NAME));
        Database.Database.Write(accountPath, accountNode);
        createdPaths.add(accountPath);

        // delete pending
        try {
            Database.Database.Delete(pendingPath);
            createdPaths.remove(pendingPath); // it was deleted
        } catch (Exception e) {
        }

        // Verify pending removed
        DataSnapshot pendingSnap = Database.Database.Read(pendingPath);
        Assert.assertTrue("Pending should not exist after accept", pendingSnap == null || !pendingSnap.exists());

        // Verify account exists
        DataSnapshot accSnap = Database.Database.Read(accountPath);
        Assert.assertNotNull(accSnap);
        Assert.assertTrue("Accepted account should exist in UnitTests/accounts", accSnap.exists());
        String storedUsername = accSnap.child(LoginManager.USERNAME).getValue(String.class);
        Assert.assertEquals(username, storedUsername);
    }
}
