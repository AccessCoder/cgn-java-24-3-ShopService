import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) throws ProductNotAvailableException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId)
                    .orElseThrow(ProductNotAvailableException::new);
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }
    public List<Order> findAllOrders(OrderStatus status) {
        return orderRepo.findAllOrders(status);
    }
    public void updateOrder(String orderId, OrderStatus newStatus) {
        Order oldOrder = orderRepo.getOrderById(orderId);
        orderRepo.removeOrder(orderId);
        Order newOrder = oldOrder.withStatus(newStatus);
        orderRepo.addOrder(newOrder);
    }

    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        List<Order> orders = orderRepo.getOrders();
        Map<OrderStatus, Order> oldestOrdersPerStatus = new HashMap<>();

        for (Order order : orders) {
            OrderStatus status = order.status();
            Order currentOldest = oldestOrdersPerStatus.get(status);

            if (currentOldest == null || order.orderTime().isBefore(currentOldest.orderTime())) {
                oldestOrdersPerStatus.put(status, order);
            }
        }

        return oldestOrdersPerStatus;
    }

}
