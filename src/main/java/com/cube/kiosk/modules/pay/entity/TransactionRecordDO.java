package com.cube.kiosk.modules.pay.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION_RECORD")
@Data
public class TransactionRecordDO {

    @Id
    private String id;

    @Column(name = "T_NAME")
    private String name;
}
