package store.order;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;



@Entity
@Table(name = "order_tb")
@Setter @Accessors(fluent = true)
@NoArgsConstructor
public class OrderModel {

    @Id
    @Column(name = "id_order")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_account")
    private String idAccount;

    @Column(name = "dt_date")
    private Date date;

    @Column(name = "total")
    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemModel> items;

    public OrderModel(Order a) {
        this.id = a.id();
        this.total = a.total() != null ? a.total() : 0.0;
        this.date = a.date() != null ? a.date() : new Date();
        this.idAccount = a.idAccount().id();
    }

    public Order to() {
        return Order.builder()
            .id(this.id)
            .date(this.date)
            .total(total != null ? this.total : 0.0)
            .items(this.items != null ? this.items.stream().map(ItemModel::to).toList() : null)
            .idAccount(EntityId.builder().id(this.idAccount).build())
            .build();
    }

}
