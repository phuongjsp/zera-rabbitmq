package sapo.phuoghv.rabbitmq1.model;

import lombok.Getter;

@Getter
public enum ShippingUnit {
    GHTK("GHTK", "ghtk"),
    SE("SE", "se");

    private final String name;
    private final String routingKey;

    ShippingUnit(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }
}
