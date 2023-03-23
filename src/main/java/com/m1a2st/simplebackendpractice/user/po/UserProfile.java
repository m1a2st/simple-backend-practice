package com.m1a2st.simplebackendpractice.user.po;

import com.m1a2st.simplebackendpractice.user.enu.UserRole;
import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/18
 * @Version v1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
@EntityListeners(AuditingEntityListener.class)
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @CreatedBy
    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;

}
