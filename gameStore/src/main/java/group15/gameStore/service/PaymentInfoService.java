package group15.gameStore.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import group15.gameStore.model.Customer;
import group15.gameStore.model.PaymentInfo;
import group15.gameStore.repository.PaymentInfoRepository;
import group15.gameStore.repository.CustomerRepository;


@Service 
public class PaymentInfoService {
    
    @Autowired
    PaymentInfoRepository paymentInfoRepo;

    @Autowired
    CustomerRepository customerRepo;

    /**
     * CreatePaymentInfo: creates new payment information record
     * @param aCardNumber: the card number for payment
     * @param aExpiryDate: the expiry date of the card
     * @param aCvv: the CVV code of the card
     * @param aBillingAddress: the billing address associated with the card
     * @param aCustomer: the customer for whom the payment info is created
     * @return the new PaymentInfo
     * @throws IllegalArgumentException if creation request is invalid or customer is not found
     */
    @Transactional
    public PaymentInfo createPaymentInfo(String aCardNumber, Date aExpiryDate, int aCvv, String aBillingAdress, Customer aCustomer){
        if (aCardNumber == null || aCardNumber.length() != 16 || !aCardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }
        if (aExpiryDate == null || aExpiryDate.before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Invalid expiry date. Must be a future date.");
        }
        if (String.valueOf(aCvv).length() != 3) {
            throw new IllegalArgumentException("Invalid CVV. Must be a 3-digit number.");
        }
        if (aBillingAdress == null || aBillingAdress.isEmpty()) {
            throw new IllegalArgumentException("Billing address cannot be empty.");
        }
        Customer customer = customerRepo.findByUserID(aCustomer.getUserID());
        if (aCustomer == null || customer == null) {
            throw new IllegalArgumentException("Customer must be valid and registered.");
        }
        PaymentInfo paymentInfo = new PaymentInfo(aCardNumber, aExpiryDate, aCvv, aBillingAdress, aCustomer);
        paymentInfoRepo.save(paymentInfo);
        return paymentInfo;
    }

     /**
     * UpdatePaymentInfo: updates an existing payment information record
     * @param paymentInfoId: the ID of the payment information to update
     * @param updatedPaymentInfo: the new payment information to update to
     * @param customer: the customer for whom the payment info is being updated
     * @return the updated PaymentInfo object
     * @throws IllegalArgumentException if update request is invalid
     */
    @Transactional
    public PaymentInfo updatePaymentInfo(int paymentInfoId, PaymentInfo updatedPaymentInfo, Customer customer) {
        PaymentInfo existingPaymentInfo = paymentInfoRepo.findByPaymentInfoID(paymentInfoId);
        if (existingPaymentInfo == null) {
            throw new IllegalArgumentException("Payment info with the specified ID does not exist.");
        }
        if (updatedPaymentInfo == null || customer == null || existingPaymentInfo.getCustomer().getUserID() != customer.getUserID()) {
            throw new IllegalArgumentException("Invalid update request or unauthorized customer.");
        }
        String cardNumber = updatedPaymentInfo.getCardNumber();
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }
        Date expiryDate = updatedPaymentInfo.getExpiryDate();
        if (expiryDate == null || expiryDate.before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Invalid expiry date. Must be a future date.");
        }
        if (updatedPaymentInfo.getCvv() <= 0 || String.valueOf(updatedPaymentInfo.getCvv()).length() != 3) {
            throw new IllegalArgumentException("Invalid CVV. Must be a 3-digit number.");
        }
        if (updatedPaymentInfo.getBillingAddress() == null || updatedPaymentInfo.getBillingAddress().isEmpty()) {
            throw new IllegalArgumentException("Billing address cannot be empty.");
        }
        existingPaymentInfo.setCardNumber(cardNumber);
        existingPaymentInfo.setExpiryDate(expiryDate);
        existingPaymentInfo.setCvv(updatedPaymentInfo.getCvv());
        existingPaymentInfo.setBillingAddress(updatedPaymentInfo.getBillingAddress());
        return paymentInfoRepo.save(existingPaymentInfo);
    }

    /**
     * GetSessionById: get a payment info by its id
     * @param id: id of the payment info to be found
     * @return the payment info with the given id
     * @throws IllegalArgumentException if payment info with given ID is not found
     */
    @Transactional
    public PaymentInfo getPaymentInfoById(int id){
        PaymentInfo paymentInfo = paymentInfoRepo.findByPaymentInfoID(id);
        if (paymentInfo == null){
            throw new IllegalArgumentException("Payment Information not found");
        }
        return paymentInfo;
    }

    /**
     * GetPaymentInfoByCardNumber: get payment info by card number
     * @param cardNumber: card number of payment info to retrieve
     * @return the payment info with the given card number
     * @throws IllegalArgumentException if the payment info with the given card number is not found
     */
    @Transactional
    public PaymentInfo getPaymentInfoByCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }
        
        PaymentInfo paymentInfo = paymentInfoRepo.findByCardNumber(cardNumber);
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment info with the specified card number does not exist.");
        }
        return paymentInfo;
    }

     /**
     * GetAllPaymentInfo: get all payment infos in the system
     * @return list of all the payment infos
     * @throws IllegalArgumentException if no payment info found
     */
    @Transactional
    public List<PaymentInfo> GetAllPaymentInfo(){
        List<PaymentInfo> paymentInfos = paymentInfoRepo.findAll();
        if(paymentInfos.size() == 0){
            throw new IllegalArgumentException("No payment information found in the system");
        }
        return paymentInfos;
    }

     /**
     * DeletePaymentInfo: deletes payment information by card number
     * @param cardNumber: card number of the payment information to delete
     * @param customer: the customer requesting the deletion
     * @throws IllegalArgumentException if payment info not found or unauthorized customer
     */
    @Transactional
    public void deletePaymentInfo(String cardNumber, Customer customer){
        PaymentInfo paymentInfo = paymentInfoRepo.findByCardNumber(cardNumber);
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment info with the specified ID does not exist.");
        }

        if(!paymentInfo.getCustomer().getPhoneNumber().equals(customer.getPhoneNumber())){
            throw new SecurityException("Unauthorized access. Only the owner can delete this payment info.");
        }

        paymentInfoRepo.deleteByCardNumber(cardNumber);
    }

}
