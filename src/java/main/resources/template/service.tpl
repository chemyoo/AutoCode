package pers.chemyoo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pers.chemyoo.core.mapper.#{T}Mapper;

@Service
public class #{T}Service {

	@Autowired
	private #{T}Mapper mapper;
	
}