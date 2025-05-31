package store.order;

public class ItemParser {

    public static Item to(ItemIn in) {
        return in == null ? null :
            Item.builder()
                .id(in.idProduct())
                .quantity(in.quantity())
                .product(EntityId.builder().id(in.idProduct()).build())
                .build();
    }

    public static ItemOut to(Item a) {
        return a == null ? null :
            ItemOut.builder()
                .id(a.id())
                .product(a.product())
                .quantity(a.quantity())
                .total(a.total())
                .build();
    }

}
