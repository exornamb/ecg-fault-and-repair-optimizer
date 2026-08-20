package com.g15.dsa.dao;

import com.g15.dsa.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * AuditEventDAO - Logs important system actions (dispatches, deletions,
 * status changes) into the audit_events table so there's a record of
 * what happened and when.
 */
public class AuditEventDAO {

    public boolean logEvent(String eventType, String relatedEntityType, String relatedEntityId, String description) {
        String sql = "INSERT INTO audit_events (event_type, related_entity_type, related_entity_id, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventType);
            ps.setString(2, relatedEntityType);
            ps.setString(3, relatedEntityId);
            ps.setString(4, description);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("AuditEventDAO.logEvent failed: " + e.getMessage());
            return false;
        }
    }
}
