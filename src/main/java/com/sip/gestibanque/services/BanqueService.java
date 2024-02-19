package com.sip.gestibanque.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sip.gestibanque.entities.Banque;
import com.sip.gestibanque.repositories.BanqueRepository;

@Service
public class BanqueService {
  
  @Autowired
  BanqueRepository banqueRepository;
  
  public List<Banque> banques() {
    return (List<Banque>) banqueRepository.findAll();
  }
  
  public Banque saveBanque(Banque banque) {
    return banqueRepository.save(banque);
  }
  
  public void deleteBanque(int idBanque) {
    banqueRepository.deleteById(idBanque);
  }

}
