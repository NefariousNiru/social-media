package com.nefarious.socialnetwork.repository;

import com.nefarious.socialnetwork.entity.OtpEntry;
import org.springframework.data.repository.CrudRepository;

public interface OtpRepository extends CrudRepository<OtpEntry, String> {
}
