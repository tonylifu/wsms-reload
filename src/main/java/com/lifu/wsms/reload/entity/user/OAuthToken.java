package com.lifu.wsms.reload.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "oauth_tokens")
@Data
public class OAuthToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oauth_tokens_sequence")
    @SequenceGenerator(name = "oauth_tokens_sequence", sequenceName = "oauth_tokens_id_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
