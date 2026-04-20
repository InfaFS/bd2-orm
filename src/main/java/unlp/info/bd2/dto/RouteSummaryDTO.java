package unlp.info.bd2.dto;

public class RouteSummaryDTO {

    private String routeName;
    private long purchaseCount;
    private double averagePrice;

    public RouteSummaryDTO(String routeName, long purchaseCount, double averagePrice) {
        this.routeName = routeName;
        this.purchaseCount = purchaseCount;
        this.averagePrice = averagePrice;
    }

    public String getRouteName() { return routeName; }
    public long getPurchaseCount() { return purchaseCount; }
    public double getAveragePrice() { return averagePrice; }
}
