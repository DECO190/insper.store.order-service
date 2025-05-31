package store.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "order_item")
@Setter @Accessors(fluent = true)
@NoArgsConstructor
public class ItemModel {

    @Id
    @Column(name = "id_item")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "total")
    private Double total;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "id_product")
    private String idProduct;

    @Column(name = "id_order")
    private String idOrder;

    // ref: https://stackoverflow.com/questions/37542208/what-is-joincolumn-and-how-it-is-used-in-hibernate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", insertable=false, updatable=false)
    private OrderModel order;


    public ItemModel(Item a, String idOrder) {
        this.total = a.total();
        this.quantity = a.quantity();
        this.idProduct = a.product().id();
        this.idOrder = idOrder;
    }

    public Item to() {
        return Item.builder()
            .id(id)
            .total(total)
            .quantity(quantity)
            .product(EntityId.builder().id(idProduct).build())
            .order(EntityId.builder().id(idOrder).build())
            .build();
    }
}
