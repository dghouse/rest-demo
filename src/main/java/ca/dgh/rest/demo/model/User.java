package ca.dgh.rest.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Getter
@Setter
@Table(name = "APP_USER")
public class User implements AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="provice_id", updatable = false, insertable = false)
    private Province province;

    @Column(name="provice_id")
    private UUID provinceId;

}
