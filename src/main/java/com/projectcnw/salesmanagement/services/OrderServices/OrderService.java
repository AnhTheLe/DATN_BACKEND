package com.projectcnw.salesmanagement.services.OrderServices;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.orderDtos.*;
import com.projectcnw.salesmanagement.dto.orderDtos.createOrder.CreateOrderDto;
import com.projectcnw.salesmanagement.dto.orderDtos.createOrder.OrderVariant;
import com.projectcnw.salesmanagement.exceptions.BadRequestException;
import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.models.Auth.UserEntity;
import com.projectcnw.salesmanagement.models.*;
import com.projectcnw.salesmanagement.models.Products.Variant;
import com.projectcnw.salesmanagement.models.enums.OrderType;
import com.projectcnw.salesmanagement.models.enums.PaymentStatus;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import com.projectcnw.salesmanagement.repositories.OrderRepositories.NonJPARepository.impl.NonJpaOrderRepositoryImpl;
import com.projectcnw.salesmanagement.repositories.OrderRepositories.OrderLineRepository;
import com.projectcnw.salesmanagement.repositories.OrderRepositories.OrderRepository;
import com.projectcnw.salesmanagement.repositories.PaymentRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.VariantRepository;
import com.projectcnw.salesmanagement.repositories.SaleChannelRepository.SalesChannelRepository;
import com.projectcnw.salesmanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderLineRepository orderLineRepository;

    private final CustomerRepository customerRepository;

    private final PaymentRepository paymentRepository;

    private final VariantRepository variantRepository;

    private final UserRepository userRepository;

    private final SalesChannelRepository salesChannelRepository;

    private final ModelMapper modelMapper;

    private final NonJpaOrderRepositoryImpl nonJpaVariantRepository;

    public long countTotalOrders() {
        return orderRepository.count();
    }

    public List<OrderLine> statisticalByTime(java.util.Date startDate, java.util.Date endDate) {
        List<OrderLine> result = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            java.util.Date currentDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Tăng ngày lên 1 để lấy ngày tiếp theo
            java.util.Date nextDate = calendar.getTime();
            List<OrderLine> statisticalList = orderRepository.statisticalOrderByTime(currentDate, nextDate);
            result.addAll(statisticalList);
        }

        return result;
    }

    public List<OrderStatistical> statisticalListByTime(java.util.Date startDate, java.util.Date endDate) {
        List<OrderStatistical> result = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            java.util.Date currentDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Tăng ngày lên 1 để lấy ngày tiếp theo
            java.util.Date nextDate = calendar.getTime();
            OrderStatistical statistical = orderRepository.statisticalByTime(currentDate, nextDate);
            result.add(statistical);
        }

        return result;
    }

    public List<TopOrder> topOrder(java.util.Date startDate, java.util.Date endDate, String type) {
        List<TopOrder> result = new ArrayList<>();
        List<Object[]> listVariant = orderRepository.topProductByRevenue(startDate, endDate);
        if ("revenue".equals(type)) {
            listVariant = orderRepository.topProductByRevenue(startDate, endDate);
            System.out.println("revenue");
        } else if ("quantity".equals(type)) {
            listVariant = orderRepository.topProductByQuantity(startDate, endDate);
            System.out.println("quantity");
        } else if ("order".equals(type)) {
            listVariant = orderRepository.topProductByOrder(startDate, endDate);
            System.out.println("order");
        }

        for (Object[] data : listVariant) {
            int variantId = (int) data[0];
            Variant variant = null;
            variant = variantRepository.findById((Integer) variantId)
                    .orElseThrow(() -> new NotFoundException("variant " + variantId + " not found"));
            BigDecimal value = BigDecimal.valueOf(((Number) data[1]).doubleValue());
            TopOrder topOrder = new TopOrder(variant, value);
            result.add(topOrder);
        }

        return result;
    }

    public List<TopCustomer> topCustomer(java.util.Date startDate, java.util.Date endDate, String type) {
        List<TopCustomer> result = new ArrayList<>();
        List<Object[]> listCustomer = orderRepository.topCustomerByRevenue(startDate, endDate);
        if ("revenue".equals(type)) {
            listCustomer = orderRepository.topCustomerByRevenue(startDate, endDate);
            System.out.println("revenue");
//        } else if("quantity".equals(type)) {
//            listVariant = orderRepository.topCustomerByQuantity(startDate, endDate);
//            System.out.println("quantity");
        } else if ("order".equals(type)) {
            listCustomer = orderRepository.topCustomerByOrder(startDate, endDate);
            System.out.println("order");
        }

        for (Object[] data : listCustomer) {
            int customerId = (int) data[0];
            Customer customer = null;
            customer = customerRepository.findById((Integer) customerId)
                    .orElseThrow(() -> new NotFoundException("customer " + customerId + " not found"));
            BigDecimal value = BigDecimal.valueOf(((Number) data[1]).doubleValue());
            TopCustomer topCustomer = new TopCustomer(customer, value);
            result.add(topCustomer);
        }

        return result;
    }

    public int countOrderFilter(String search, String start_date, String end_date, String channels, String status) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        List<String> channelList = channels == null || channels.isEmpty() ? null : Arrays.stream(channels.split(",")).toList();
        List<String> statusList = status == null || status.isEmpty() ? null : Arrays.stream(status.split(",")).toList();
        if (start_date != null && !start_date.isEmpty()) {
            try {
                startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }

        if (end_date != null && !end_date.isEmpty()) {
            try {
                endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
                ;
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }
        return nonJpaVariantRepository.countOrder(search, startDate, endDate, channelList, statusList);
    }

    public List<OrderListItemDto> getOrderList(int page, int size, String search, String start_date, String end_date, String channels, String status) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (start_date != null && !start_date.isEmpty()) {
            try {
                startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }

        if (end_date != null && !end_date.isEmpty()) {
            try {
                endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
                ;
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }
        List<String> channelList = channels == null || channels.isEmpty() ? null : Arrays.stream(channels.split(",")).toList();
        List<String> statusList = status == null || status.isEmpty() ? null : Arrays.stream(status.split(",")).toList();
        List<Order> orderListPage = nonJpaVariantRepository.getOrderFilter(page, size, search, startDate, endDate, channelList, statusList);

        List<OrderListItemDto> orderListItemDtos = new ArrayList<>();
        for (Order order : orderListPage) {
            OrderListItemDto orderListItemDto = new OrderListItemDto();
            Payment payment = paymentRepository.findPaymentByOrderIdAndOrderType(order.getId(), OrderType.ORDER);
            orderListItemDto.setOrderId(order.getId());
            orderListItemDto.setCustomerName(order.getCustomerName() != null ? order.getCustomerName() : order.getCustomer().getName());
            orderListItemDto.setPhone(order.getPhone() != null ? order.getPhone() : order.getCustomer().getPhone());
            orderListItemDto.setCreatedAt(order.getCreatedAt());
            orderListItemDto.setSalesChannelName(order.getSalesChannel().getName());
            orderListItemDto.setAmount(payment == null ? 0 : payment.getAmount());
            orderListItemDto.setPaymentStatus(payment == null ? null : payment.getPaymentStatus());
            orderListItemDtos.add(orderListItemDto);
        }

        return orderListItemDtos;
    }

//    public List<OrderListByCustomer> getOrderListByCustomerId(int customerId) {
//        Customer existingCustomer = customerRepository.findById(customerId).orElse(null);
//
//        if (existingCustomer == null) {
//            // Khách hàng không tồn tại, bạn có thể ném một ngoại lệ hoặc trả về null
//            throw new BadRequestException("Khách hàng không tồn tại.");
//        }
//        List<Order> orderList = orderRepository.findAllOrderByCustomer(customerId);
//        List<OrderListByCustomer> orderListByCustomer = new ArrayList<>();
//
//        for (Order order : orderList) {
//            OrderListByCustomer orderDto = new OrderListByCustomer(order, paymentRepository.findPaymentByOrderId(order.getId()));
//            orderListByCustomer.add(orderDto);
//        }
//
//        return orderListByCustomer;
//    }

//    public List<OrderListByCustomer> getOrderListByCustomerId(int customerId) {
//        Customer existingCustomer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại."));
//
//        List<Order> orderList = orderRepository.findAllOrderByCustomer(customerId);
//
//        List<OrderListByCustomer> orderListByCustomer = orderList.stream()
//                .map(order -> new OrderListByCustomer(order, paymentRepository.findPaymentByOrder(order.getId())))
//                .collect(Collectors.toList());
//
//        return orderListByCustomer;
//    }

    public List<OrderListByCustomerDto> getOrderListByCustomerId(int customerId) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại."));

        List<Order> orderList = orderRepository.findAllOrderByCustomer(customerId);

        List<OrderListByCustomerDto> orderListByCustomer = orderList.stream()
                .map(order -> {
                    Payment payment = paymentRepository.findPaymentByOrder(order.getId());
                    List<OrderLineDTO> orderLines = order.getOrderLineList().stream()
                            .map(orderLine -> new OrderLineDTO(orderLine.getQuantity(), orderLine.getReturnQuantity(), orderLine.getPrice(), orderLine.getVariant()))
                            .collect(Collectors.toList());
                    return OrderListByCustomerDto.builder()
                            .orderId(order.getId())
                            .staffFullName(order.getUserEntity().getFullName())
                            .discount(order.getDiscount())
                            .orderLines(orderLines)
                            .payment(payment)
                            .address(order.getAddress())
                            .phone(order.getPhone())
                            .customerName(order.getCustomerName())
                            .build();
                })
                .collect(Collectors.toList());

        return orderListByCustomer;
    }

    public OrderDetailInfo getOrderDetailInfo(int id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("order not found");
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderRepository.getOrderDetailInfo(id), OrderDetailInfo.class);
    }

    public List<OrderLine> getAllOrderLines(int id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("order not found");
        }

        return orderLineRepository.findAllByOrderId(id);
    }

    @Transactional
    public void createOrder(CreateOrderDto createOrderDto, String staffPhone) {

        Optional<Customer> customer;
        if (createOrderDto.getCustomerId() != null) {
            customer = Optional.ofNullable(customerRepository.findById(createOrderDto.getCustomerId()).orElseThrow(() -> new NotFoundException("customer " + createOrderDto.getCustomerId() + " not found")));
        } else {
            customer = customerRepository.findByPhone("-1");
        }

        UserEntity user = userRepository.findByPhone(staffPhone).orElseThrow(() -> new NotFoundException("user's phone " + staffPhone + " not found"));

        Optional<SalesChannel> salesChannelPOS = salesChannelRepository.findSalesChannelByCode("POS");

        Order order = Order.builder()
                .discount(createOrderDto.getDiscount())
                .userEntity(user)
                .customer(customer.orElse(null))
                .customerName(createOrderDto.getCustomerName())
                .address(createOrderDto.getAddress())
                .phone(createOrderDto.getPhone())
                .salesChannel(salesChannelPOS.orElse(null))
                .build();

        List<OrderVariant> orderVariantList = createOrderDto.getOrderVariantList();
        List<OrderLine> orderLineList = new ArrayList<>();
        List<Variant> updatedVariantList = new ArrayList<>();
        int amount = 0;

        for (OrderVariant orderVariant : orderVariantList) {
            Variant variant = variantRepository.findById(orderVariant.getVariantId());
            if (variant == null) {
                throw new NotFoundException("variant " + orderVariant.getVariantId() + " not found");
            }

            if (variant.getQuantity() < orderVariant.getQuantity()) {
                throw new BadRequestException("insufficient stock for order variant " + orderVariant.getVariantId());
            }

            amount += orderVariant.getQuantity() * variant.getRetailPrice();
            variant.setQuantity(variant.getQuantity() - orderVariant.getQuantity());
            updatedVariantList.add(variant);
            orderLineList.add(OrderLine.builder()
                    .quantity(orderVariant.getQuantity())
                    .variant(variant)
                    .order(order)
                    .price(variant.getRetailPrice())
                    .build());
        }

//        order.setOrderLineList(orderLineList);
        variantRepository.saveAll(updatedVariantList);
        Order savedOrder = orderRepository.save(order);
        orderLineRepository.saveAll(orderLineList);

        Payment payment = Payment.builder()
                .orderId(savedOrder.getId())
                .orderType(OrderType.ORDER)
                .amount(amount - createOrderDto.getDiscount())
                .paymentMethod(createOrderDto.getPaymentMethod())
                .payDate(new Date(new java.util.Date().getTime()))
                .paymentStatus(PaymentStatus.COMPLETE)
                .build();
        paymentRepository.save(payment);
    }

    @Transactional
    public void createOrderWebPage(CreateOrderDto createOrderDto, String staffPhone) {

        Optional<Customer> customer;
        if (createOrderDto.getCustomerId() != null) {
            customer = Optional.ofNullable(customerRepository.findById(createOrderDto.getCustomerId()).orElseThrow(() -> new NotFoundException("customer " + createOrderDto.getCustomerId() + " not found")));
        } else {
            customer = customerRepository.findByPhone("-1");
        }

        Optional<SalesChannel> salesChannelPOS = salesChannelRepository.findSalesChannelByCode("WEB");

        Order order = Order.builder()
                .discount(createOrderDto.getDiscount())
                .customer(customer.orElse(null))
                .customerName(createOrderDto.getCustomerName())
                .address(createOrderDto.getAddress())
                .phone(createOrderDto.getPhone())
                .salesChannel(salesChannelPOS.orElse(null))
                .build();

        List<OrderVariant> orderVariantList = createOrderDto.getOrderVariantList();
        List<OrderLine> orderLineList = new ArrayList<>();
        List<Variant> updatedVariantList = new ArrayList<>();
        int amount = 0;


        for (OrderVariant orderVariant : orderVariantList) {
            Variant variant = variantRepository.findById(orderVariant.getVariantId());
            if (variant == null) {
                throw new NotFoundException("variant " + orderVariant.getVariantId() + " not found");
            }

            if (variant.getQuantity() < orderVariant.getQuantity()) {
                throw new BadRequestException("insufficient stock for order variant " + orderVariant.getVariantId());
            }

            amount += orderVariant.getQuantity() * (variant.getRetailPrice() - orderVariant.getDiscountPerItem());
            variant.setQuantity(variant.getQuantity() - orderVariant.getQuantity());
            updatedVariantList.add(variant);
            orderLineList.add(OrderLine.builder()
                    .quantity(orderVariant.getQuantity())
                    .variant(variant)
                    .order(order)
                    .price(variant.getRetailPrice() - orderVariant.getDiscountPerItem())
                    .build());
        }

//        order.setOrderLineList(orderLineList);
        variantRepository.saveAll(updatedVariantList);
        Order savedOrder = orderRepository.save(order);
        orderLineRepository.saveAll(orderLineList);

        Payment payment = Payment.builder()
                .orderId(savedOrder.getId())
                .orderType(OrderType.ORDER)
                .amount(amount - createOrderDto.getDiscount() + createOrderDto.getShippingFee())
                .paymentMethod(createOrderDto.getPaymentMethod())
                .payDate(new Date(new java.util.Date().getTime()))
                .paymentStatus(PaymentStatus.COMPLETE)
                .build();
        paymentRepository.save(payment);
    }

    public ResponseEntity<ResponseObject> getAllOrderByCustomerId(int customerId) {
        List<Order> orderList = orderRepository.getAllByCustomer_Id(customerId);
        List<OrderListByCustomerDto> orderListByCustomer = orderList.stream()
                .map(order -> {
                    Payment payment = paymentRepository.findPaymentByOrder(order.getId());
                    List<OrderLineDTO> orderLines = order.getOrderLineList().stream()
                            .map(orderLine -> new OrderLineDTO(orderLine.getQuantity(), orderLine.getReturnQuantity(), orderLine.getPrice(), orderLine.getVariant()))
                            .collect(Collectors.toList());
                    return OrderListByCustomerDto.builder()
                            .orderId(order.getId())
                            .discount(order.getDiscount())
                            .orderLines(orderLines)
                            .payment(payment)
                            .address(order.getAddress())
                            .phone(order.getPhone())
                            .customerName(order.getCustomerName())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("success")
                .data(orderListByCustomer)
                .build());
    }

    public ResponseEntity<ResponseObject> getOrderDetail(int id) {
        Optional<Order> orderDetail = orderRepository.findById(id);
        if (orderDetail.isEmpty()) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("order not found")
                    .responseCode(404)
                    .build());
        }

        OrderListByCustomerDto orderDetailInfo = OrderListByCustomerDto.builder()
                .orderId(orderDetail.get().getId())
                .discount(orderDetail.get().getDiscount())
                .orderLines(orderDetail.get().getOrderLineList().stream()
                        .map(orderLine -> OrderLineDTO.builder()
                                .quantity(orderLine.getQuantity())
                                .returnQuantity(orderLine.getReturnQuantity())
                                .price(orderLine.getPrice())
                                .variant(orderLine.getVariant())
                                .build())
                        .collect(Collectors.toList()))
                .payment(paymentRepository.findPaymentByOrder(orderDetail.get().getId()))
                .address(orderDetail.get().getAddress())
                .phone(orderDetail.get().getPhone())
                .customerName(orderDetail.get().getCustomerName())
                .build();

        return ResponseEntity.ok(ResponseObject.builder()
                .data(orderDetailInfo)
                .message("success")
                .responseCode(200)
                .build());
    }

}

