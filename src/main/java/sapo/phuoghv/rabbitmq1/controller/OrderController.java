package sapo.phuoghv.rabbitmq1.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sapo.phuoghv.rabbitmq1.model.Order;
import sapo.phuoghv.rabbitmq1.model.ShippingUnit;

import java.text.MessageFormat;

@Controller
@CommonsLog
public class OrderController {

    private final RabbitTemplate rabbitTemplate;

    public OrderController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("order")
    public ResponseEntity<String> checking() {
        for (int i = 0; i < 100; i++) {
            var order = new Order();
            order.setCode("newCode" + i);
            order.setShippingUnit(ShippingUnit.GHTK);
            if (i % 10 == 0) {
                order.setCode("VIPPP" + i);
                order.setVIPPriority(true);
            }
            createOrder(order);
        }
        for (int i = 0; i < 100; i++) {
            var order = new Order();
            order.setCode("newCode" + i);
            order.setShippingUnit(ShippingUnit.SE);
            if (i % 10 == 0) {
                order.setCode("VIPPP" + i);
                order.setVIPPriority(true);
            }
            createOrder(order);
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("order")
    public ResponseEntity<Boolean> createOrder(@RequestBody Order order) {

        if (order.isVIPPriority()) {
            log.info(MessageFormat.format("send order VIPP {0} to rabbit", order.getCode()));
            rabbitTemplate.convertAndSend("create-order-exchange", order.getShippingUnit().getRoutingKey(),order, message -> {
                message.getMessageProperties().setPriority(1);
                return message;
            });
        } else {
            log.info(MessageFormat.format("send order {0} to rabbit", order.getCode()));
            rabbitTemplate.convertAndSend("create-order-exchange",order.getShippingUnit().getRoutingKey(), order);
        }
        return ResponseEntity.ok(true);
    }
}
