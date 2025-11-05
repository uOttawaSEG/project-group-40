// If your project already has a Request interface, delete this placeholder and import yours instead.
package com.example.tutoring;


import java.time.LocalDate;
import java.time.OffsetTime;


interface Request {
Student requester();
Tutor approver();
LocalDate date();
OffsetTime start();
OffsetTime end();
}
