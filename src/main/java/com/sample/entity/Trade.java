package com.sample.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "trade")
@Data
public class Trade implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "exchange", nullable = false)
    private String exchange;

    @Column(name = "ticker", nullable = false)
    private String ticker;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "date", nullable = false)
    private OffsetDateTime date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "sent", nullable = false)
    private boolean sent;
}