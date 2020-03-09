package com.arsalaan.business;

import com.arsalaan.data.entity.Status;
import com.arsalaan.data.entity.Sushi;
import com.arsalaan.data.entity.SushiOrder;
import com.arsalaan.data.repository.StatusRepo;
import com.arsalaan.data.repository.SushiOrderRepo;
import com.arsalaan.data.repository.SushiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private StatusRepo statusRepo;
    private SushiOrderRepo sushiOrderRepo;
    private SushiRepo sushiRepo;
    private static long start = 0;
    private static long end = 0;

    @Autowired
    public OrderService(StatusRepo statusRepo, SushiOrderRepo sushiOrderRepo, SushiRepo sushiRepo) {
        this.statusRepo = statusRepo;
        this.sushiOrderRepo = sushiOrderRepo;
        this.sushiRepo = sushiRepo;
        //this.orderRepo = orderRepo;
    }
    public OrderService() {
    }

    public SushiOrder placeOrder(String sushi_name){
        Sushi sushi = this.sushiRepo.findByName(sushi_name);
        Status status = this.statusRepo.findByName("created");
        SushiOrder newOrder = new SushiOrder();
        newOrder.setSushi_id(sushi.getId());
        newOrder.setStatus_id(status.getId());
        newOrder.setCreatedAt();
        newOrder.setTime_to_prepare(sushi.getTime_to_make());
        SushiOrder placedOrder = this.sushiOrderRepo.save(newOrder);
        System.out.println("New order"+placedOrder.getId()+" created.");
        return placedOrder;
    }

    @Async("threadPoolTaskExecutor")
    public void chefIsWorking() {
        System.out.println("Chef is Working");
        int orderId = 0;
            try {
                while(true) {
                    Map<Integer, OrderPlacement> allOrdersMap = this.getDetails();
                    for (Integer id : allOrdersMap.keySet()) {
                        if (allOrdersMap.get(id).getStatus_id() == 1 || allOrdersMap.get(id).getStatus_id() == 2) {
                            orderId = allOrdersMap.get(id).getOrder_id();
                            this.orderUpdate(Integer.toString(orderId), "in-progress");
                            System.out.println("Order : " + orderId + " is in-progress and will take time(sec) :" + allOrdersMap.get(id).getTime_to_make());
                            start = System.currentTimeMillis();
                            Thread.sleep(allOrdersMap.get(id).getTime_to_make() * 1000);
                            this.orderUpdate(Integer.toString(orderId), "finished");
                            System.out.println("order :" + orderId + " is finished");
                            break;
                        }

                    }
                }
            }
            catch(InterruptedException e){}
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    public SushiOrder orderUpdate(String order_id, String statusValue){
        int order = Integer.parseInt(order_id);
        int sec = 0;
        SushiOrder placed_Order = this.sushiOrderRepo.findById(order);
        Status status = this.statusRepo.findByName(statusValue);
        SushiOrder placedOrder;
        if(placed_Order.getStatus_id()==2){
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for(Thread t : threadSet){
                if(t.getName().equals("Async-1")) {
                    t.interrupt();
                    end = System.currentTimeMillis();
                    sec = (int) (end - start) / 1000;
                    System.out.println("Chef is leaving the " + statusValue + " order and start preparing the next.");
                }
            }
        }
        placedOrder = null;
        if(placed_Order!=null && (placed_Order.getStatus_id()!=5 || placed_Order.getStatus_id()!=4)){

            placed_Order.setStatus_id(status.getId());
            if(placed_Order.getTime_to_prepare()-sec > 0)
                placed_Order.setTime_to_prepare(placed_Order.getTime_to_prepare()-sec);
            else
                placed_Order.setTime_to_prepare(placed_Order.getTime_to_prepare());
            placedOrder = this.sushiOrderRepo.save(placed_Order);
        }
        return placedOrder;
    }

    public SushiOrder resumeOrder(String order_id){
        int order = Integer.parseInt(order_id);
        SushiOrder placed_Order = this.sushiOrderRepo.findById(order);
        SushiOrder placedOrder;
        placedOrder = null;
        if(placed_Order!=null && placed_Order.getStatus_id()==3){
            placed_Order.setStatus_id(2);
            placedOrder = this.sushiOrderRepo.save(placed_Order);
        }
        return placedOrder;
    }

    public Map<Integer, Sushi> getSushiName(){
        Iterable<Sushi> allSushi = this.sushiRepo.findAll();
        Map<Integer, Sushi> allSushiMap = new HashMap<>();
        allSushi.forEach(s->{
            Sushi sushi = new Sushi();
            sushi.setId(s.getId());
            sushi.setName(s.getName());
            sushi.setTime_to_make(s.getTime_to_make());
            allSushiMap.put(sushi.getId(), sushi);
        });
        return allSushiMap;
    }
    public Map<Integer, Status> getStatusName(){
        Iterable<Status> allStatus = this.statusRepo.findAll();
        Map<Integer, Status> allStatusMap = new HashMap<>();
        allStatus.forEach(s->{
            Status status = new Status();
            status.setId(s.getId());
            status.setName(s.getName());
            allStatusMap.put(status.getId(), status);
        });
        return allStatusMap;
    }

    public Map<Integer, OrderPlacement> getDetails(){
        Iterable<SushiOrder> allOrders = this.sushiOrderRepo.findAll();
        Map<Integer, OrderPlacement> allOrdersMap = new HashMap<>();
        Map<Integer, Sushi> allSushiMap = getSushiName();
        Map<Integer, Status> allStatusMap = getStatusName();
        allOrders.forEach(order->{
            OrderPlacement orderPlacement = new OrderPlacement();
            orderPlacement.setOrder_id(order.getId());
            orderPlacement.setSushi_id(order.getSushi_id());
            orderPlacement.setStatus_id(order.getStatus_id());
            orderPlacement.setCreatedAt(order.getCreatedAt());
            orderPlacement.setTime_to_make(order.getTime_to_prepare());
            orderPlacement.setStatus_name(allStatusMap.get(order.getStatus_id()).getName());
            orderPlacement.setSushi_name(allSushiMap.get(order.getSushi_id()).getName());
            allOrdersMap.put(order.getId(), orderPlacement);
        });
        return allOrdersMap;
    }

    public List<OrderPlacement> getAllOrders(){
        Map<Integer, OrderPlacement> allOrdersMap = this.getDetails();
        List<OrderPlacement> orderPlacements = new ArrayList<>();
        for(Integer id:allOrdersMap.keySet()){
            orderPlacements.add(allOrdersMap.get(id));
        }
        return orderPlacements;
    }

}
