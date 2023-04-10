package jpabook.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue //AUTO로 GeneratedValue가 설정된다.
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    // 아래 속성은 값 타입으로 대체된다.
    // private String city;
    // private String street;
    // private String zipcode;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //연관관계의 주인은 Order.member 이다.
    private List<Order> orders = new ArrayList<Order>();

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
