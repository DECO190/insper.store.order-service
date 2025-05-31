package store.order;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import store.product.ProductController;
import store.product.ProductOut;

@Service
public class OrderService {

    @Autowired
    private ProductController productController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public OrderOut create(OrderIn orderIn, String idAccount) {
        Order order = OrderParser.to(orderIn, idAccount);

        if (order == null || order.items() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order data");
        }

        if (order.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must contain at least one item");
        }


        List<Item> orderItems = order.items();

        List<ItemOut> items = new ArrayList<>();

        Double total = 0.0;

        for (Item it : orderItems) {
            ProductOut productOut = null;

            try {
                ResponseEntity<ProductOut> response = productController.getProductById(it.product().id());
                productOut = response.getBody();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product ID for item: " + it.id(), e);
            }

            if (productOut == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + it.product().id());
            }

            if (it.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity for product: " + it.product().id());
            }

            it.total(productOut.price() * it.quantity());

            items.add(ItemOut.builder()
                .id(it.id())
                .product(EntityId.builder().id(it.product().id()).build())
                .quantity(it.quantity())
                .total(productOut.price() * it.quantity())
                .build());

            total += productOut.price() * it.quantity();
        }

        order.total(total);

        Order savedOrder = orderRepository.save(new OrderModel(order)).to();

        if (savedOrder == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save order");
        }

        for (Item it : orderItems) {
            itemRepository.save(new ItemModel(it, String.valueOf(savedOrder.id())));
        }

        OrderOut orderOut = OrderOut.builder()
            .id(savedOrder.id())
            .date(savedOrder.date())
            .items(items)
            .total(total)
            .build();

        return orderOut;
    }

    public List<Order> findAll(String idAccount) {
        if (idAccount == null || idAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account ID");
        }
        return StreamSupport
            .stream(orderRepository.findAllByIdAccount(idAccount).spliterator(), false)
            .map(OrderModel::to)
            .toList();
    }

    public Order findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
        }

        OrderModel prod = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return prod.to();
    }
}
