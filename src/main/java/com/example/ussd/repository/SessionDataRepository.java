package com.example.ussd.repository;
import com.example.ussd.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionDataRepository extends JpaRepository<SessionData, String> {
}
