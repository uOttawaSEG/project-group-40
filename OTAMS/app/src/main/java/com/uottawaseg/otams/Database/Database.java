package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Requests.AccountCreationRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Database implements Runnable {
    public final static Database Database = new Database();
    private final Thread currentThread;
    private boolean _alreadySetup = false;
    private DatabaseReference db;

    private Database() {
        currentThread = new Thread(this);
        // Sets it to a daemon so it runs in the background.
        currentThread.setDaemon(true);
        currentThread.run();

    }
    private void OnCancelled(DatabaseError e) {
        System.out.println("Firebase error: " + e.getMessage());
    }

    private void onDataChange(DataSnapshot snap) {
        var value = snap.getValue(String.class);
        System.out.println("Firebase read success. Value = " + value);
    }
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
    public DatabaseReference GetDB() {
        return db;
    }

    public void Write(String path, Object val) {
        db.child(path).setValue(val);
    }

    public void WriteRequest(String path, AccountCreationRequest req) {
        db.child(path).setValue(req);
    }

    public void WriteAccount(String path, Account acc) {
        db.child(path).setValue(acc);
    }

    // This needs to be the exact path
    public void Delete(String path) {
        if(!path.contains("/"))
            throw new IllegalArgumentException("Please don't delete an entire section of the DB");

        if(!canDeletePath(path))
            throw new IllegalArgumentException("Invalid delete path");
        db.child(path).removeValue();
    }
    // This checks if it's a valid deletion path,
    // We don't want to delete all the accounts because somebody screwed up
    // Same for pending requests.
    private boolean canDeletePath(String path) {
        // !(all the paths we don't want to delete)
        return !(path.toUpperCase() == LoginManager.ACCOUNTS.toUpperCase()
                || path.toUpperCase() == AccountCreationManager.GetRequestDir().toUpperCase());
    }

    public DataSnapshot Read(String path) {
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

    // If this throws, just let it.
    public static byte[] GetSHA256(String str) throws RuntimeException {
        // Not crazy high security but good enough
        // Also I know this can throw, just don't worry about it okay
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            // This is a sebastien issue.
            throw new RuntimeException("Tell sebastien to fix his database SHA256");
        }
    }

    @Override
    public void run() {
        StartDB();
    }
}
