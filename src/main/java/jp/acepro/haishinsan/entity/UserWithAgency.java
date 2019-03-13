/*
 * Copyright 2018 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import jp.acepro.haishinsan.db.entity.BaseEntity;
import lombok.Data;

/**
 * 
 */
@Entity
@Data
public class UserWithAgency extends BaseEntity {

    /** ユーザーID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;
    
    /** ユーザー名 */
    @Column(name = "user_name")
    String userName;
    
    /** 店舗ID */
    @Column(name = "shop_id")
    Long shopId;
    
    /** 店舗名 */
    @Column(name = "shop_name")
    String shopName;
    
    /** 法人ID */
    @Column(name = "corporation_id")
    Long corporationId;
    
    /** 法人名 */
    @Column(name = "corporation_name")
    String corporationName;

    /** 代理店ID */
    @Column(name = "agency_id")
    Long agencyId;
    
    /** 代理店名 */
    @Column(name = "agency_name")
    String agencyName;

    /** メールアドレス */
    @Column(name = "email")
    String email;
    
    /** パスワード */
    @Column(name = "password")
    String password;
    
    /** ロールID */
    @Column(name = "role_id")
    Long roleId;
    
}