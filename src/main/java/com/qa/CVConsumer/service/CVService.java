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

	private Iterable<CV> getAll() {
		return consumerRepo.findAll();
	}

	private CV add(CV account) {
		return consumerRepo.save(account);
	}

	private void delete(Long id) {
		consumerRepo.deleteById(id);
	}

	private Optional<CV> get(Long id) {
		return consumerRepo.findById(id);
	}
	
	private void update(Request request) {
		get(request.getCVID()).get().setCV(request.getCv().getCV());
	}

	public void parse(Request request) {
		if (request.getType() == requestType.CREATE) {
			add(request.getCv());
		} else if (request.getType() == requestType.DELETE) {
			delete(request.getCVID());
		} else if (request.getType() == requestType.READ) {
			get(request.getCVID());
		} else if (request.getType() == requestType.UPDATE) {
			update(request);
		} else if (request.getType() == requestType.READALL) {
			getAll();
		}
		
	}

}