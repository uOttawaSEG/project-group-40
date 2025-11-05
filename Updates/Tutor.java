package com.example.tutoring;


import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
* Tutor with availability windows and inbound requests.
*/
public class Tutor {
// ... existing fields ...


private final List<Availability> _availabilities;
private final List<SessionRequest> _requests;


// --- Baseline constructor: ensure non-null lists ---
public Tutor(/* your existing ctor params here */) {
// ... your existing initialization ...
this._availabilities = new ArrayList<>();
this._requests = new ArrayList<>();
}


// --- Overload to allow constructing with availabilities (e.g., from DB) ---
public Tutor(/* your existing ctor params here, e.g., id, name, etc. */
List<Availability> availabilities,
List<SessionRequest> requests
) {
// ... your existing initialization ...
this._availabilities = (availabilities == null) ? new ArrayList<>() : new ArrayList<>(availabilities);
this._requests = (requests == null) ? new ArrayList<>() : new ArrayList<>(requests);
}

// --- Read-only view of availabilities ---
public ImmutableList<Availability> GetAvailabilities() {
return ImmutableList.copyOf(_availabilities);
}


// --- Mutators for Availability list ---
public void AddAvailability(Availability availability) {
Objects.requireNonNull(availability, "availability cannot be null");
_availabilities.add(availability);
updateAvailabilityInDb();
}


public void DeleteAvailability(Availability availability) {
if (availability == null) return; // nothing to do
_availabilities.remove(availability); // no error if missing
updateAvailabilityInDb();
}


public void DeleteAvailability(int index) {
if (index < 0 || index >= _availabilities.size()) {
return; // swallow invalid index per requirements (no throw)
}
_availabilities.remove(index);
updateAvailabilityInDb();
}


// --- (Optional) accessor for requests, if needed by callers ---
public ImmutableList<SessionRequest> getRequests() {
return ImmutableList.copyOf(_requests);
}


// --- Placeholder for persistence side-effects ---
private void updateAvailabilityInDb() {
// TODO: implement actual DB update logic.
// This method is intentionally left as a stub since persistence is out of scope here.
}
}
