import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() throws ProductNotAvailableException {
        //GIVEN
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN

        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);
        List<String> productsIds = List.of("1", "42");

        //WHEN
        try {
            shopService.addOrder(productsIds);
            fail("Expected ProductNotAvailableException not thrown, even though product 2 was ordered, that does not exist.");
        } catch (ProductNotAvailableException e) {
        }
    }

    @Test
    void updateOrder() throws ProductNotAvailableException {
        //GIVEN
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);

        // WHEN
        shopService.updateOrder(order.id(), OrderStatus.COMPLETED);

        // THEN
        List<Order> processingOrders = shopService.findAllOrders(OrderStatus.PROCESSING);
        assertEquals(0, processingOrders.size());
        List<Order> completedOrders = shopService.findAllOrders(OrderStatus.COMPLETED);
        assertEquals(1, completedOrders.size());
    }

    @Test
    void getOldestOrderPerStatus() throws ProductNotAvailableException {
        //GIVEN
        Instant testTime = Instant.now();
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        //Orders "am Shopservice vorbeimogeln", damit sie keine random ID erhalten
        orderRepo.addOrder(new Order("1", List.of(new Product("1", "Apfel")),OrderStatus.IN_DELIVERY, testTime));
        orderRepo.addOrder(new Order("2", List.of(new Product("1", "Apfel")),OrderStatus.IN_DELIVERY, Instant.now()));
        orderRepo.addOrder(new Order("3", List.of(new Product("1", "Apfel")),OrderStatus.PROCESSING, testTime));
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        Map<OrderStatus, Order> expected = Map.of(OrderStatus.IN_DELIVERY, new Order("1", List.of(new Product("1", "Apfel")),OrderStatus.IN_DELIVERY, testTime),
                OrderStatus.PROCESSING, new Order("3", List.of(new Product("1", "Apfel")),OrderStatus.PROCESSING, testTime));

        // WHEN
        Map<OrderStatus, Order> actual = shopService.getOldestOrderPerStatus();

        // THEN
        assertEquals(expected, actual);
    }
}