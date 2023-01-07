package com.example.service.Impl;

import com.example.dao.AppUserDAO;
import com.example.service.UserActivationService;
import com.example.utils.CryptoTool;
import org.springframework.stereotype.Service;

@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserDAO userDAO;
    private final CryptoTool cryptoTool;

    public UserActivationServiceImpl(AppUserDAO userDAO, CryptoTool cryptoTool) {
        this.userDAO = userDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = userDAO.findById(userId);
        if(optional.isPresent()){
            var user = optional.get();
            user.setIsActive(true);
            userDAO.save(user);
            return true;
        }
        return false;
    }
}
