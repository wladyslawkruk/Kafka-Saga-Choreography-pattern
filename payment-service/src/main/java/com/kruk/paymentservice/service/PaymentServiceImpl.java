package com.kruk.paymentservice.service;

import com.kruk.paymentservice.domain.OrderStatus;
import com.kruk.paymentservice.domain.ServiceName;
import com.kruk.paymentservice.domain.UserBalance;
import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.PaymentKafkaDto;
import com.kruk.paymentservice.dto.UserKafkaDto;
import com.kruk.paymentservice.exception.UserBalanceNotFoundException;
import com.kruk.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final KafkaService kafkaService;



    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,KafkaService kafkaService) {
        this.paymentRepository = paymentRepository;
        this.kafkaService = kafkaService;
    }
    @Override
    public Optional<UserBalance> topUpBalance(Long amount, String userName) {
        UserBalance resultUserBalance =  paymentRepository.findByName(userName)
                .orElseThrow(() -> new UserBalanceNotFoundException(userName));

        resultUserBalance.setBalance(resultUserBalance.getBalance()+amount);
        paymentRepository.save(resultUserBalance);
        return Optional.empty();
    }
    public String retrieveUserNameFromToken(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String temp= new String(decoder.decode(chunks[1]));
        return org.apache.commons.lang3.StringUtils.chop(temp.substring(8,temp.indexOf(',')));
    }

    @Override
    public void saveToUserBalance(UserKafkaDto userKafkaDto) {
        UserBalance userBalance = new UserBalance(userKafkaDto.getId(),userKafkaDto.getName());
        paymentRepository.save(userBalance);
        logger.info("New User balance created with data obtained from kafka.  UserBalance: "+userBalance.toString());
    }

    @Override
    public void processOrderMessage(OrderKafkaDto orderKafkaDto){
        System.out.println("Entering processOrderMessage method");
        UserBalance ub = paymentRepository.findByName(orderKafkaDto.getOrderedBy())
                .orElseThrow(()->new RuntimeException("user not found"));
        if(ub==null){

            System.out.println("user not found=(");
            OrderKafkaDto fallbackOKD = orderKafkaDto;
            fallbackOKD.setModifiedTime(LocalDateTime.now().toString());
            fallbackOKD.setStatus(OrderStatus.UNEXPECTED_FAILURE);
            fallbackOKD.setServiceName(ServiceName.PAYMENT_SERVICE);
            kafkaService.fallback(fallbackOKD);
        }
        else{
            if(ub.getBalance()>=orderKafkaDto.getTotalCost()){
                System.out.println("seems like enough funds");
                ub.setBalance(ub.getBalance()-orderKafkaDto.getTotalCost());
                PaymentKafkaDto paymentKafkaDto= PaymentKafkaDto.toPKD(orderKafkaDto);
                paymentKafkaDto.setModifiedTime(LocalDateTime.now().toString());
                paymentKafkaDto.setStatus(OrderStatus.PAID);
                paymentKafkaDto.setServiceName(ServiceName.PAYMENT_SERVICE);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                kafkaService.produce(paymentKafkaDto);
                paymentRepository.save(ub);
            }
            else{
                OrderKafkaDto fallbackOKD = orderKafkaDto;
                fallbackOKD.setModifiedTime(LocalDateTime.now().toString());
                fallbackOKD.setStatus(OrderStatus.PAYMENT_FAILED);
                fallbackOKD.setServiceName(ServiceName.PAYMENT_SERVICE);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                kafkaService.fallback(fallbackOKD);
                System.out.println("Insufficient funds");
            }
        }

    }

    @Override
    public void processFallbackMessage(OrderKafkaDto orderKafkaDto) {
        if(orderKafkaDto.getStatus().equals(OrderStatus.DELIVERY_FAILED)||
                orderKafkaDto.getStatus().equals(OrderStatus.INVENTMENT_FAILED)){
            UserBalance ub = paymentRepository.findByName(orderKafkaDto.getOrderedBy()).get();
            ub.setBalance(ub.getBalance()+ orderKafkaDto.getTotalCost());
            paymentRepository.save(ub);
        }
        kafkaService.fallback(orderKafkaDto);
    }


}
