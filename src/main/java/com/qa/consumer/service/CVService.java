package com.qa.consumer.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.consumer.persistence.domain.CV;
import com.qa.consumer.persistence.domain.Request;
import com.qa.consumer.persistence.domain.Request.requestType;
import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.util.CVProducer;

@Service
public class CVService {

	@Autowired
	private CVRepository consumerRepo;

	@Autowired
	private CVProducer producer;

	String queuedMessage = "File placed on queue succesfully";

	String malformedMessage = "Queue request malformed";

	String addMessage = "CV added succesfully";

	String notFoundMessage = "CV not found";

	String deletedMessage = "CV deleted succesfully";

	String updatedMessage = "CV updated succesfully";

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

	private void update(CV cvToUpdate, CV updatedCV) {
		cvToUpdate.setCV(updatedCV.getCV());
		cvToUpdate.setCreator(updatedCV.getCreator());
	}

	private String delete(Request request) {
		Optional<CV> cvToDelete = get(request.getcvIDtoActUpon());
		if (!cvToDelete.isPresent()) {
			return notFoundMessage;
		} else {
			delete(request.getcvIDtoActUpon());
			return deletedMessage;
		}
	}

	private String update(Request request) {
		Optional<CV> cvToUpdate = get(request.getcvIDtoActUpon());
		CV updatedCV = request.getCv();
		if (!cvToUpdate.isPresent()) {
			return notFoundMessage;
		} else {
			update(cvToUpdate.get(), updatedCV);
			return updatedMessage;
		}
	}

	public String parse(Request request) {
		if (request.getType() == requestType.CREATE) {
			return add(request);
		} else if (request.getType() == requestType.DELETE) {
			return delete(request);
		} else if (request.getType() == requestType.READ) {
			return send(get(request.getcvIDtoActUpon()));
		} else if (request.getType() == requestType.UPDATE) {
			return update(request);
		} else if (request.getType() == requestType.READALL) {
			return send(getAll());
		}
		return malformedMessage;

	}

	private String add(Request request) {
		if (request.getCv() == null) {
			return malformedMessage;
		} else {
			add(request.getCv());
			return addMessage;
		}
	}

	private String send(Iterable<CV> all) {
		return producer.produce(all);

	}

	public String send(Optional<CV> optional) {
		if (!optional.isPresent()) {
			return notFoundMessage;
		} else {
			return producer.produce(optional.get());

		}
	}

}