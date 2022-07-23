package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        return repository.findAll(Sort.by("name")).stream().map(CityDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public City findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() ->new NotFoundException(""));
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City entity = fromDTO(dto);
        entity = repository.save(entity);

        return new CityDTO(entity);
    }

    public City delete(Long id) throws DataIntegrityViolationException, NotFoundException {
        City entity = this.findById(id);
        repository.delete(entity);

        return entity;
    }

    private City fromDTO(CityDTO dto) {
        City entity = new City();
        entity.setName(dto.getName());

        return entity;
    }


}
