package com.m1a2st.simplebackendpractice.user.po;

import com.m1a2st.simplebackendpractice.user.enu.UserLoginStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document
public class UserLoginDocument {

    @Id
    private String id;

    @Indexed
    @Field("username")
    private String username;

    @Field("userLoginStatus")
    private UserLoginStatus userLoginStatus;

    @Field("loginTime")
    private Date loginTime;

    @CreatedBy
    @Field("createBy")
    private String createBy;

    @CreatedDate
    @Field("createDate")
    private Date createDate;

    @LastModifiedDate
    @Field("lastModifiedDate")
    private Date lastModifiedDate;
}
