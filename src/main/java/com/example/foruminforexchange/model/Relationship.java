package com.example.foruminforexchange.model;

import jakarta.persistence.*;


@Entity
@Table(name = "Relationships")
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationship_id;

    @Column(name = "relationship_name", nullable = false)
    private String relationshipName;

    @ManyToOne
    @JoinColumn(name = "source_user_id", referencedColumnName = "user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id", referencedColumnName = "user_id")
    private User targetUser;

    // Constructors, Getters, and Setters
    public Relationship() {
    }

    public Long getRelationshipId() {
        return relationship_id;
    }

    public void setRelationshipId(Long relationship_id) {
        this.relationship_id = relationship_id;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}

