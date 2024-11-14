package group15.gameStore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import group15.gameStore.exception.GameStoreException;
import group15.gameStore.model.Customer;
import group15.gameStore.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers_Success() {
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);

        String name2 = "Joe Smith";
        String password2 = "joesmith1234";
        String email2 = "joe@gmail.com";
        String address2 = "432 Sesame St";
        String phoneNumber2 = "123-456-7891";
        Customer c2 = new Customer(email2, name2, password2, address2, phoneNumber2);

        List<Customer> customers = new ArrayList<>();
        customers.add(c1);
        customers.add(c2);

        when(customerRepo.findAll()).thenReturn(customers);
        List<Customer> allCustomers = customerService.findAllCustomers();

        assertNotNull(allCustomers);
        assertFalse(allCustomers.isEmpty());
        assertEquals(2, allCustomers.size());
        assertEquals(c1, allCustomers.get(0));
        assertEquals(c2, allCustomers.get(1));
    }

    @Test
    public void testGetAllCustomers_EmptyList() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepo.findAll()).thenReturn(customers);

        GameStoreException e = assertThrows(GameStoreException.class, () -> customerService.findAllCustomers());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("There are no customers in the system", e.getMessage());
    }

    @Test
    public void testGetCustomerByID_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findById(c1.getUserID())).thenReturn(java.util.Optional.of(c1));

        // Act
        Customer result = customerService.getCustomerByID(c1.getUserID());

        // Assert
        assertNotNull(result);
        assertEquals(c1, result);
    }

    @Test
    public void testGetCustomerByID_InvalidId() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findById(c1.getUserID())).thenReturn(java.util.Optional.of(c1));

        // Act
        GameStoreException exception = assertThrows(GameStoreException.class, () -> {
            customerService.getCustomerByID(2);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no customer with ID 2", exception.getMessage());
    }

    @Test
    public void testGetCustomerByEmail_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.getCustomerByEmail(email);

        // Assert
        assertEquals(c1, result);
    }

    
    
    @Test
    public void testGetCustomerByEmail_InvalidEmail() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        GameStoreException exception = assertThrows(GameStoreException.class, () -> {
            customerService.getCustomerByEmail("notvalidemail@fmail.com");
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    // Test update Customer Username

    @Test
    public void testUpdateCustomerUsername_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.updateCustomerUsername(c1, "newUsername");

        // Assert
        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }
   
   
    //Test update Customer password
    @Test
    public void testUpdateCustomerPassword_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.updateCustomerPassword(c1, "newPassword");

        // Assert
        assertEquals("newPassword", result.getPassword());
    }

    // Test update Customer email
    @Test
    public void testUpdateCustomerEmail_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.updateCustomerEmail(c1, "newemail");

        // Assert
        assertEquals("newemail", result.getEmail());
    }

    // Test update Customer address
    @Test
    public void testUpdateCustomerAddress_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.updateCustomerAddress(c1, "newaddress");

        // Assert

        assertEquals("newaddress", result.getAddress());
    }

    // Test update Customer phone number
    @Test
    public void testUpdateCustomerPhoneNumber_Success() {
        // Arrange
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        when(customerRepo.findByEmail(email)).thenReturn(c1);

        // Act
        Customer result = customerService.updateCustomerPhoneNumber(c1, "newPhoneNumber");

        // Assert
        assertEquals("newPhoneNumber", result.getPhoneNumber());
    }
       

    @Test
    public void testCreateCustomer_Success() {
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);

        when(customerRepo.save(c1)).thenReturn(c1);
        Customer createdCustomer = customerService.createCustomer(name, password, email, address, phoneNumber);

        assertNotNull(createdCustomer);
        assertEquals(c1, createdCustomer);
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomer_EmailTaken() {
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);

        when(customerRepo.findByEmail(email)).thenReturn(c1);

        GameStoreException e = assertThrows(GameStoreException.class, () -> customerService.createCustomer(name, password, email, address, phoneNumber));
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("This email is already in use.", e.getMessage());
    }

    @Test
    public void testCreateCustomer_EmptyName() {
        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.createCustomer("", "password123", "email@example.com", "123 Main St", "123-456-7890"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Name field cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomer_EmptyPassword() {
        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.createCustomer("username", "", "email@example.com", "123 Main St", "123-456-7890"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Password field cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomer_EmptyEmail() {
        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.createCustomer("username", "password123", "", "123 Main St", "123-456-7890"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email field cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomer_EmptyAddress() {
        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.createCustomer("username", "password123", "email@example.com", "", "123-456-7890"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Address field cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomerEmpty_PhoneNumber() {
        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.createCustomer("username", "password123", "email@example.com", "123 Main St", ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Phone number field cannot be empty.", exception.getMessage());
    }
   
    @Test
    public void testUpdateCustomer_EmptyName() {
        Customer customer = new Customer("oldName", "password123", "email@example.com", "123 Main St", "123-456-7890");

        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.updateCustomerUsername(customer, ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New username cannot be empty", exception.getMessage());
    }

    @Test
    public void testUpdateCustomer_EmptyPassword() {
        Customer customer = new Customer("username", "oldPassword", "email@example.com", "123 Main St", "123-456-7890");

        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.updateCustomerPassword(customer, ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New password cannot be empty", exception.getMessage());
    }

    @Test
    public void testUpdateCustomer_EmptyEmail() {
        Customer customer = new Customer("username", "password123", "oldEmail@example.com", "123 Main St", "123-456-7890");

        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.updateCustomerEmail(customer, ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New email cannot be empty", exception.getMessage());
    }

    @Test
    public void testUpdateCustomer_EmptyAddress() {
        Customer customer = new Customer("username", "password123", "email@example.com", "oldAddress", "123-456-7890");

        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.updateCustomerAddress(customer, ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New address cannot be empty", exception.getMessage());
    }

    @Test
    public void testUpdateCustomer_EmptyPhoneNumber() {
        Customer customer = new Customer("username", "password123", "email@example.com", "123 Main St", "oldPhone");

        GameStoreException exception = assertThrows(GameStoreException.class, 
            () -> customerService.updateCustomerPhoneNumber(customer, ""));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("New phone number cannot be empty", exception.getMessage());
    }

    @Test
    public void testDeleteCustomer_Success() {
        int customerId = 1;
        String name = "Dana White";
        String password = "password1234";
        String email = "dana@gmail.com";
        String address = "1234 Main St";
        String phoneNumber = "123-456-7890";
        Customer c1 = new Customer(name, password, email, address, phoneNumber);
        c1.setUserID(customerId);

        when(customerRepo.findByUserID(customerId)).thenReturn(c1);

        customerService.deleteCustomer(c1);

        verify(customerRepo, times(1)).delete(c1);
    }

    @Test
    public void testDeleteCustomerNotFound() {

        when(customerRepo.findByUserID(1)).thenReturn(null);

        GameStoreException e = assertThrows(GameStoreException.class, () -> customerService.deleteCustomer(new Customer()));
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("The customer to delete does not exist", e.getMessage());
    }
}
