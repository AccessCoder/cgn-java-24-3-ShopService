import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderListRepoTest {

    private static final Instant TEST_TIME_OF_ORDER = Instant.now();

    @Test
    void getOrders() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER);
        repo.addOrder(newOrder);

        //WHEN
        List<Order> actual = repo.getOrders();

        //THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel");
        expected.add(new Order("1", List.of(product1), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER));

        assertEquals(expected, actual);
    }

    @Test
    void getOrderById() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER);
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER);

        assertEquals(expected, actual);
    }

    @Test
    void addOrder() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER);

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER);
        assertEquals(expected, actual);
        assertEquals(expected, repo.getOrderById("1"));
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }

    @Test
    void findAllOrders() {
        // GIVEN
        OrderListRepo repo = new OrderListRepo();
        repo.addOrder(new Order("1", List.of(), OrderStatus.PROCESSING, TEST_TIME_OF_ORDER));
        Order orderA = new Order("2", List.of(), OrderStatus.IN_DELIVERY, TEST_TIME_OF_ORDER);
        repo.addOrder(orderA);
        Order orderB = new Order("3", List.of(), OrderStatus.IN_DELIVERY, TEST_TIME_OF_ORDER);
        repo.addOrder(orderB);
        repo.addOrder(new Order("4", List.of(), OrderStatus.COMPLETED, TEST_TIME_OF_ORDER));

        // WHEN
        List<Order> actual = repo.findAllOrders(OrderStatus.IN_DELIVERY);

        // THEN
        assertEquals(List.of(orderA, orderB), actual);
    }
}