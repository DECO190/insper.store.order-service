package store.order;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderResource implements OrderController {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<OrderOut> create(OrderIn orderIn, String idAccount) {
        OrderOut created = orderService.create(orderIn, idAccount);

        return ResponseEntity.ok().body(created);
    }

    @Override
    public ResponseEntity<List<OrderOut>> findAll(String idAccount) {
        return ResponseEntity.ok().body(
            orderService.findAll(idAccount)
                .stream()
                .map(OrderParser::to)
                .toList()
        );
    }

   
}
