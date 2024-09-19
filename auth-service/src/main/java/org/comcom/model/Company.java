package org.comcom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.comcom.dto.CompanyDataDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "com_gen")
    @SequenceGenerator(name="com_gen", sequenceName = "com_gen_sequence", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "website_url", unique = true)
    private String websiteUrl;

    @Column(name = "video_call_url")
    private String videoCallUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "open_hours")
    private LocalTime openHours;

    @Column(name = "close_hours")
    private LocalTime closeHours;

    @Column(name = "type")
    private String type;

    @Column(name = "video_call_employee")
    private String videoCallEmployee;

    @Column(name = "video_call_leader")
    private String videoCallLeader;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "update_on")
    private LocalDateTime updatedOn;

    @Column(name = "Invoices")
    private String invoices;

    @Column(name = "companyLeader_id")
    private Long companyLeader_id;


    //=========== Foreign Keys ================


    @JoinColumn(name = "companyCategory", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Company_Category companyCategory;

    /**
     * For Creating a company
     * @param companyName
     * @param email
     * @param websiteUrl
     * @param videoCallUrl
     * @param description
     * @param openHours
     * @param type
     * @param videoCallEmployee
     * @param videoCallLeader
     * @param companyCategory
     */
    public Company(String companyName, String email, String websiteUrl, String videoCallUrl, String description, LocalTime openHours, LocalTime closeHours, String type, String videoCallEmployee, String videoCallLeader, Company_Category companyCategory, Long companyLeader_id) {
        this.name = companyName;
        this.email = email;
        this.websiteUrl = websiteUrl;
        this.videoCallUrl = videoCallUrl;
        this.description = description;
        this.openHours = openHours;
        this.closeHours = closeHours;
        this.type = type;
        this.videoCallEmployee = videoCallEmployee;
        this.videoCallLeader = videoCallLeader;
        this.companyCategory = companyCategory;
        this.companyLeader_id = companyLeader_id;
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
    }

    public CompanyDataDto toDto() {
        return CompanyDataDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .websiteUrl(websiteUrl)
                .description(description)
                .type(type)
                .openHours(openHours)
                .closeHours(closeHours)
                .videoCallUrl(videoCallUrl)
                .videoCallLeader(videoCallLeader)
                .videoCallEmployee(videoCallEmployee)
                .categoryName(companyCategory.getName())
                .companyLeaderId((companyLeader_id))
                .build();
    }


}
