package com.c9.financeapi.controller;

import com.c9.financeapi.model.Stock;
import com.c9.financeapi.model.User;
import com.c9.financeapi.repo.StockRepository;
import com.c9.financeapi.repo.UserRepository;
import com.c9.financeapi.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    private final ProducerService producerService;

    public UsersController(ProducerService producerService) {
        this.producerService = producerService;
    }



    @PostMapping(value = "/signup")
    public void createUser(@RequestBody User user){
        producerService.sendMessage("User '" + user.getUsername() + "' created an account with a starting balance of $" + user.getBalance());
        userRepository.save(user);
    }

    @GetMapping(value = "/login")
    public Iterable<User> getAllUsers(){

        return userRepository.findAll();
    }

    @GetMapping(value = "/trading/{id}")
    public User getUserById(@PathVariable Long id){

        return userRepository.findById(id).get();
    }


    @PostMapping(value = "/trading/{id}")
    public void buyStock(@PathVariable Long id, @RequestBody Stock stock){
        User user = userRepository.findById(id).get();
        producerService.sendMessage("User '" + user.getUsername() + "' purchased " + stock.getQuantity() + " stocks of " + stock.getName() + " at $" + stock.getPriceAtTransaction() + " each");
        Double userBalance = user.getBalance();
        userBalance -= (stock.getPriceAtTransaction() * stock.getQuantity());
        user.setBalance(userBalance);

        stock.setUser(user);

        stockRepository.save(stock);

    }

    @PutMapping(value = "/trading/{id}")
    public void sellStock(@PathVariable Long id, @RequestBody Stock stock){
        User user = userRepository.findById(id).get();
        producerService.sendMessage("User '" + user.getUsername() + "' sold " + stock.getQuantity() + " stocks of " + stock.getName() + " at $" + stock.getPriceAtTransaction() + " each");
        Double userBalance = user.getBalance();
        userBalance += (stock.getPriceAtTransaction() * stock.getQuantity());
        user.setBalance(userBalance);

        List<Stock> userStocks = user.getStocks();
        for(int i = 0; i < userStocks.size(); i++){
            if(userStocks.get(i).getName().equals(stock.getName())){
                Stock stockToChange = userStocks.get(i);
                stockToChange.setQuantity(stockToChange.getQuantity() - stock.getQuantity());
                userStocks.set(i, stockToChange);
                break;
            }
        }

        user.setStocks(userStocks);
        userRepository.save(user);
    }

}
