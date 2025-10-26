package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Requests.AccountCreationRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;


public class Database {
    public final static Database Database = new Database();
    private Thread currentThread;
    private boolean _alreadySetup = false;
    private DatabaseReference db;

    private Database() {
        currentThread = new Thread(() -> {
            StartDB();
        });
        // Sets it to a daemon so it runs in the background.
        currentThread.start();
    }
    public static String GetMailAPIKey() {
        if(!Database._alreadySetup) {
           Database.StartDB();
        }

        return (String) (Database.Read("mailAPIkey").getValue());
    }
    // Callbacks
    private void OnCancelled(DatabaseError e) {
        System.out.println("Firebase error: " + e.getMessage());
    }

    private void onDataChange(DataSnapshot snap) {
        var value = snap.getValue(String.class);
        System.out.println("Firebase read success. Value = " + value);
    }

    /**
     * @return Whether or not we were able to start the database, i.e., if it was already started or not.
     */
    // This will get called whenever we need to access the DB,
    // It only actually needs to be called once but we're doing it for redundancy.
    public boolean StartDB() {
        if (_alreadySetup) {
            return false;
        }
        db = FirebaseDatabase.getInstance().getReference();
        return true;
    }

    public boolean IsDbStarted() {
        return _alreadySetup;
    }

    /**
     * @return The database reference
     */
    public DatabaseReference GetDB() {
        return db;
    }

    /**
     * @DEPRECATED Shouldn't be used if possible, prefer WriteRequest or WriteAccount
     * @param path The database path to write to
     * @param val The value to write
     */
    public void Write(String path, Object val) {
        db.child(path).setValue(val);
    }

    /**
     * @param path The database path to write to
     * @param req The AccountCreationRequest to be written
     */
    public void WriteRequest(String path, AccountCreationRequest req) {
        db.child(path).setValue(req);
    }

    /**
     * @param path The path to write to
     * @param acc The account whose information needs to be written
     */
    public void WriteAccount(String path, Account acc) {
        db.child(path).setValue(acc);
    }

    /**
     * @param path The path to delete, needs to be exact.
     */
    // This needs to be the exact path
    public void Delete(String path) {
        if(!path.contains("/"))
            throw new IllegalArgumentException("Please don't delete an entire section of the DB");

        if(!canDeletePath(path))
            throw new IllegalArgumentException("Invalid delete path");
        db.child(path).removeValue();
    }

    /**
     * @param path The path to be deleted
     * @return Whether or not we can delete the path.
     */
    // This checks if it's a valid deletion path,
    // We don't want to delete all the accounts because somebody screwed up
    // Same for pending requests.
    private boolean canDeletePath(String path) {
        // !(all the paths we don't want to delete)
        return !(path.toUpperCase() == LoginManager.ACCOUNTS.toUpperCase()
                || path.toUpperCase() == AccountCreationManager.GetRequestDir().toUpperCase());
    }

    /**
     * @param path Path to read
     * @return The data snapshot of what's read at the given path
     */
    public synchronized DataSnapshot Read(String path) {
        var query = db.child(path);
        var data = query.get();
        while(!(data.isComplete() || data.isCanceled() || data.isSuccessful())) {
            try{
                Thread.sleep(1);
            } catch (InterruptedException ignored) {

            }
        }
        return data.getResult();
    }

    /**
     * @param str The string to transform into a SHA256 hash
     * @return The SHA256 Byte[] of the given input string
     * @throws RuntimeException It could throw an exception if it doesn't recognize the algorithm, if it throws, there's a problem.
     */
    // If this throws, just let it.
    // SHA256 for Password (used to reset passwords)
    // [-25, -49, 62, -12, -15, 124, 57, -103, -87, 79, 44, 111, 97, 46, -118, -120, -114, 91, 16, 38, -121, -114, 78, 25, 57, -117, 35, -67, 56, -20, 34, 26]
    public static byte[] GetSHA256(String str) throws RuntimeException {
        // Not crazy high security but good enough
        // Also I know this can throw, just don't worry about it, it'll be okay

        // Could probably easily make this a one line, but that's kinda hard to follow
        //  return MessageDigest.getInstance("SHA-256").digest(str.getBytes());
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            // This is a sebastien issue.
            throw new RuntimeException("Tell sebastien to fix his database SHA256");
        }
    }

}
