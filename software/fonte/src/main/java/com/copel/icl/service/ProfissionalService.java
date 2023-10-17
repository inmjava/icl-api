package com.copel.icl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.copel.icl.dao.ProfissionalDAOImpl;
import com.copel.icl.dto.EmployeeCastLightDTO;


@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalDAOImpl designacaoTemporariaDAO;


    public ProfissionalService() {
        super();
    }

    public List<EmployeeCastLightDTO> loadEmployees() {
        return this.designacaoTemporariaDAO.loadEmployees();
    }
}
