import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        try {
            shopService.addOrder(List.of("1"));
            shopService.addOrder(List.of("2"));
            shopService.addOrder(List.of("3"));
        }catch (ProductNotAvailableException e){
            System.out.println("There is an error!");
        }

        System.out.println(shopService.getOldestOrderPerStatus());


    }
}
