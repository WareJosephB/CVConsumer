package com.qa.CVConsumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.CVConsumer.persistence.domain.CV;
import com.qa.CVConsumer.persistence.domain.Request;
import com.qa.CVConsumer.persistence.domain.Request.requestType;
import com.qa.CVConsumer.persistence.repository.CVRepository;
import com.qa.CVConsumer.util.CVProducer;

@Service
public class CVService {

	@Autowired
	private CVRepository consumerRepo;

	@Autowired
	private CVProducer producer;

	public void setRepo(CVRepository persist) {
		this.consumerRepo = persist;
	}

	private Iterable<CV> getAll() {
		return consumerRepo.findAll();
	}

	private Optional<CV> get(Long id) {
		return consumerRepo.findById(id);
	}

	private CV add(CV cv) {
		return consumerRepo.save(cv);
	}

	private void delete(Long id) {
		consumerRepo.deleteById(id);
	}

	private void update(Long id, CV updatedCV) {
		CV cvToUpdate = consumerRepo.findById(id).get();
		cvToUpdate.setCV(updatedCV.getCV());
		cvToUpdate.setCreator(updatedCV.getCreator());
	}

	private String delete(Request request) {
		Optional<CV> cvToDelete = get(request.getCVID());
		if (!cvToDelete.isPresent()) {
			return "${CVNotFound.message}";
		} else {
			delete(request.getCVID());
			return "${CVDeleted.message}";
		}
	}

	private String update(Request request) {
		Optional<CV> cvToUpdate = get(request.getCVID());
		CV updatedCV = request.getCv();
		if (!cvToUpdate.isPresent()) {
			return "${CVNotFound.message}";
		} else {
			update(request.getCVID(), updatedCV);
			return "${CVUpdated.message}";
		}
	}

	public String parse(Request request) {
		if (request.getType() == requestType.CREATE) {
			return add(request);
		} else if (request.getType() == requestType.DELETE) {
			return delete(request);
		} else if (request.getType() == requestType.READ) {
			return send(get(request.getCVID()));
		} else if (request.getType() == requestType.UPDATE) {
			return update(request);
		} else if (request.getType() == requestType.READALL) {
			return send(getAll());
		}
		return "${MalformedRequest.message}";

	}

	private String add(Request request) {
		if (request.getCv() == null) {
			return "${MalformedRequest.message}";
		} else {
			add(request.getCv());
			return "${CVAdded.message}";
		}
	}

	private String send(Iterable<CV> all) {
		return producer.produce(all);

	}

	public String send(Optional<CV> optional) {
		if (!optional.isPresent()) {
			return "${CVNotFound.message}";
		} else {
			return producer.produce(optional.get());

		}
	}

}