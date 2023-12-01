package com.spotit.backend.userAccount.model;

import java.util.List;

import com.spotit.backend.abstraction.model.AbstractEntity;
import com.spotit.backend.course.model.Course;
import com.spotit.backend.education.model.Education;
import com.spotit.backend.experience.model.Experience;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;
import com.spotit.backend.interest.model.Interest;
import com.spotit.backend.project.model.Project;
import com.spotit.backend.social.model.Social;
import com.spotit.backend.softSkill.model.SoftSkill;
import com.spotit.backend.techSkill.model.TechSkill;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAccount extends AbstractEntity {

    @Column(name = "auth0_id", unique = true, nullable = false)
    private String auth0Id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String profilePictureUrl;

    @Column
    private String position;

    @Column
    private String description;

    @Column
    private String cvClause;

    @OneToMany(mappedBy = "userAccount")
    private List<Social> socials;

    @OneToMany(mappedBy = "userAccount")
    private List<Education> educations;

    @OneToMany(mappedBy = "userAccount")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "userAccount")
    private List<TechSkill> techSkills;

    @OneToMany(mappedBy = "userAccount")
    private List<SoftSkill> softSkills;

    @OneToMany(mappedBy = "userAccount")
    private List<ForeignLanguage> foreignLanguages;

    @OneToMany(mappedBy = "userAccount")
    private List<Interest> interests;

    @OneToMany(mappedBy = "userAccount")
    private List<Course> courses;

    @OneToMany(mappedBy = "userAccount")
    private List<Project> projects;

    @Override
    public void update(AbstractEntity otherEntity) {
        if (otherEntity instanceof UserAccount o) {
            if (o.firstName != null) {
                firstName = o.firstName;
            }

            if (o.lastName != null) {
                lastName = o.lastName;
            }

            if (o.email != null) {
                email = o.email;
            }

            if (o.phoneNumber != null) {
                phoneNumber = o.phoneNumber;
            }

            if (o.profilePictureUrl != null) {
                profilePictureUrl = o.profilePictureUrl;
            }

            if (o.position != null) {
                position = o.position;
            }

            if (o.description != null) {
                description = o.description;
            }

            if (o.cvClause != null) {
                cvClause = o.cvClause;
            }
        }
    }
}
