package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository repository;
    @Autowired
    private CityService cityService;

    @Transactional(readOnly = true)
    public List<EventDTO> findAll() {
        return repository.findAll(Sort.by("name")).stream().map(EventDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(""));
    }

    @Transactional
    public EventDTO insert(EventDTO dto) {
        Event entity = fromDTO(dto);
        entity = repository.save(entity);

        return new EventDTO(entity);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto) throws NotFoundException{
        Event entity = this.findById(id);

        return new EventDTO(fromDTO(entity,dto));
    }

    public Event delete(Long id) throws DataIntegrityViolationException, NotFoundException {
        Event entity = this.findById(id);
        repository.delete(entity);

        return entity;
    }

    private Event fromDTO(EventDTO dto) {
        Event entity = new Event();
        fromDTO(entity, dto);

        return entity;
    }

    private Event fromDTO(Event entity, EventDTO dto) {
        try {
            entity.setName(dto.getName());
            entity.setDate(dto.getDate());
            entity.setCity(cityService.findById(dto.getCityId()));
            entity.setUrl(dto.getUrl());

            return entity;
        } catch (NotFoundException e) {
            return null;
        }
    }

}
