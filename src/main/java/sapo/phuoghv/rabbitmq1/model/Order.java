package sapo.phuoghv.rabbitmq1.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order implements Serializable {
    private String code;
    private BigDecimal price;
    private ShippingUnit shippingUnit;
    private String customerName;
    private boolean isVIPPriority;
}
