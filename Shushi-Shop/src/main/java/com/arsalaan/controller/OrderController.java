    package com.arsalaan.controller;

    import com.arsalaan.business.OrderPlacement;
    import com.arsalaan.business.OrderService;
    import com.arsalaan.data.entity.SushiOrder;
    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @RestController
    @RequestMapping(value="/api")
    public class OrderController {

        @Autowired
        private OrderService orderService;

        @RequestMapping(method= RequestMethod.POST, value="/orders/")
        public ResponseEntity<JsonNode> placeOrder(@RequestBody String sushiName) throws JsonProcessingException {

            ObjectMapper mapper = new ObjectMapper();
            OrderPlacement obj = mapper.readValue(sushiName, OrderPlacement.class);
            SushiOrder order = this.orderService.placeOrder(obj.getSushi_name());

            this.orderService.chefIsWorking();
            String str = "";
            if(order.getId()!=0){
                str = "{  \"order\": {  \"id\": "+order.getId()+", \"statusId\":" +order.getStatus_id()+", \"sushiId\":" +order.getSushi_id()+", \"createdAT\":" + order.getCreatedAt().getTime()  + "},\"code\": 0, \"msg\": \"Order submitted\" }";
            }
            JsonNode actualObj = mapper.readTree(str);
            return ResponseEntity.ok()
                    .header("code", "200")
                    .body(actualObj);

        }

        @RequestMapping(method= RequestMethod.PUT, value="/orders/cancel/{order_id}")
        public ResponseEntity<JsonNode> cancelOrder(@PathVariable(value="order_id")String order_id) throws JsonProcessingException {
            SushiOrder response = this.orderService.orderUpdate(order_id, "cancelled");
            String str = "";
            if (response!=null)
                str = "{ \"code\": 0, \"msg\": \"Order Cancelled\" }";
            else
                str = "{ \"code\": 404, \"msg\": \"Order not found\" }";
            ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(str);

            return ResponseEntity.ok()
                    .header("code", "200")
                    .body(actualObj);
        }

        @RequestMapping(method= RequestMethod.PUT, value="/orders/pause/{order_id}")
        public ResponseEntity<JsonNode> pauseOrder(@PathVariable(value="order_id")String order_id) throws JsonProcessingException {
            SushiOrder response = this.orderService.orderUpdate(order_id, "paused");

            String str = "";
            if (response!=null)
                str = "{ \"code\": 0, \"msg\": \"Order Paused\" }";
            else
                str = "{ \"code\": 404, \"msg\": \"Order not found\" }";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(str);

            return ResponseEntity.ok()
                    .header("code", "200")
                    .body(actualObj);
        }

        @RequestMapping(method= RequestMethod.PUT, value="/orders/resume/{order_id}")
        public ResponseEntity<JsonNode> resumeOrder(@PathVariable(value="order_id")String order_id) throws JsonProcessingException {
            SushiOrder response = this.orderService.resumeOrder(order_id);

            String str = "";
            if (response!=null) {
                this.orderService.chefIsWorking();
                str = "{ \"code\": 0, \"msg\": \"Order Resumed\" }";
            }
            else
                str = "{ \"code\": 404, \"msg\": \"Order not found\" }";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(str);

            return ResponseEntity.ok()
                    .header("code", "200")
                    .body(actualObj);
        }

        @RequestMapping(method= RequestMethod.GET, value="/orders/status")
        public List<OrderPlacement> getAllOrdersStatus(){
            return this.orderService.getAllOrders();
        }


    }
