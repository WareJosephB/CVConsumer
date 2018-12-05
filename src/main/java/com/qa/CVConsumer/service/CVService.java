package com.qa.CVConsumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.CVConsumer.persistence.domain.CV;
import com.qa.CVConsumer.persistence.domain.Request;
import com.qa.CVConsumer.persistence.domain.Request.requestType;
import com.qa.CVConsumer.persistence.repository.CVRepository;

@Service
public class CVService {

	@Autowired
	private CVRepository consumerRepo;

	public void setRepo(CVRepository persist) {
		this.consumerRepo = persist;
	}

	public Iterable<CV> getAll() {
		return consumerRepo.findAll();
	}

	public CV add(CV account) {
		return consumerRepo.save(account);
	}

	public void delete(Long id) {
		consumerRepo.deleteById(id);
	}

	public Optional<CV> get(Long id) {
		return consumerRepo.findById(id);
	}

	public void parse(Request request) {
		if (request.getType() == requestType.CREATE) {
			add(request.getCv());
		} else if (request.getType() == requestType.DELETE) {
			delete(request.getCVID());
		} else if (request.getType() == requestType.READ) {
			get(request.getCVID());
		}
		
	}

}