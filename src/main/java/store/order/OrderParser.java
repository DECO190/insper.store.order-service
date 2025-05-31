package store.order;

public class OrderParser {

    public static Order to(OrderIn in, String idAccount) {
        return in == null ? null :
            Order.builder()
                .items(in.items().stream().map(ItemParser::to).toList())
                .idAccount(EntityId.builder().id(idAccount).build())
                .build();
    }

    public static OrderOut to(Order a) {
        return a == null ? null :
            OrderOut.builder()
                .id(a.id())
                .date(a.date())
                .items(a.items().stream().map(ItemParser::to).toList())
                .total(a.total())
                .build();
    }

}
