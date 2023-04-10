package jpabook.model.entity;

import jpabook.model.entity.item.Item;

import javax.persistence.*;

@Entity
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY) //연관관계의 주인
    @JoinColumn(name = "ITEM_ID")
    private Item item; //주문 상품

    @ManyToOne(fetch = FetchType.LAZY) //연관관계의 주인
    @JoinColumn(name = "ORDER_ID")
    private Order order; //주문

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
