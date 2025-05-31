package store.order;


import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Data @Accessors(fluent = true)
@Setter()
public class Item {
    private String id;
    private Double total;
    private Integer quantity;   
    private EntityId product;
    private EntityId order;
}
